package dev.hollink.partytrails.runetime;

import dev.hollink.partytrails.PartyTrailsConfig;
import dev.hollink.partytrails.codec.TrailProgressCodec;
import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.data.trail.TrailProgress;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;

@Slf4j
@Getter
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailRuntime
{
	private final TrailEventBus bus;
	private final ConfigManager configManager;
	private final TrailContext context;
	private final TrailProgressCodec progressCodec;

	private TreasureTrail trail;
	private TrailStep currentStep;

	public void renderCurrentStep(Graphics2D graphics, PanelComponent panel)
	{
		if (currentStep == null)
		{
			return;
		}

		currentStep.drawOverlay(panel, graphics);

		panel.getChildren().add(LineComponent.builder().build());
		String stepCounter = String.format("Step %d of %d", context.getProgress().getCurrentStepIndex() + 1, trail.getSteps().size());
		panel.getChildren().add(TitleComponent.builder().text(stepCounter).color(TITLED_CONTENT_COLOR).build());
	}

	public void startTrail(TreasureTrail trail)
	{
		log.debug("Starting new trail (length={})", trail.getMetadata().getStepCount());
		bus.register(this::onEvent);
		this.trail = trail;
		this.context.getProgress().start(trail.getMetadata().getTrailId());
		trail.getStep(this.context.getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::startStep, this::resetOnStepNotFound);
		saveProgress();
	}

	private void startStep(TrailStep step)
	{
		log.debug("Starting new trail step (type={})", step.getStepType().name());
		this.context.getProgress().getStepState().clear();
		this.currentStep = step;
		step.onActivate(context);
	}

	public void resumeTrail(TreasureTrail trail, TrailProgress progress)
	{
		log.debug("Resuming trail (length={}, currentStep={})", trail.getMetadata().getStepCount(), progress.getCurrentStepIndex());
		bus.register(this::onEvent);
		this.trail = trail;
		trail.getStep(this.context.getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::resumeStep, this::resetOnStepNotFound);
	}

	private void resumeStep(TrailStep step)
	{
		log.debug("Resuming trail step (type={})", step.getStepType().name());
		this.context.getProgress().getStepState().clear();
		this.currentStep = step;
	}

	private void resetOnStepNotFound()
	{
		log.error("Unable to start trail, no step found at index {}", this.getContext().getProgress().getCurrentStepIndex());
		this.reset();
	}

	public void reset()
	{
		log.debug("Resetting active treasure trail");
		this.trail = null;
		this.currentStep = null;
		this.context.getProgress().reset();
		this.bus.unregister(this::onEvent);
	}

	private void onEvent(TrailEvent event)
	{
		if (currentStep == null || !currentStep.handlesEvent(event))
		{
			return;
		}

		context.setLastEvent(event);
		if (currentStep.isComplete(context, event))
		{
			log.debug("Completed trail step!");
			advanceToNextStep();
		}

		saveProgress();
	}

	private void advanceToNextStep()
	{
		if (trail == null)
		{
			return;
		}
		int nextStepIndex = this.context.getProgress().getCurrentStepIndex() + 1;
		this.context.getProgress().setCurrentStepIndex(nextStepIndex);
		trail.getStep(nextStepIndex).ifPresentOrElse(this::startStep, this::completeTrail);
	}

	private void completeTrail()
	{
		log.debug("Completing trail...");
		this.currentStep = null;
		this.context.getProgress().setCompleted(true);
		this.bus.unregister(this::onEvent);
	}

	private void saveProgress()
	{
		try
		{
			TrailProgress progress = context.getProgress();
			String encoded = progressCodec.encodeToString(progress);
			configManager.setConfiguration(
				PartyTrailsConfig.CONFIG_GROUP,
				PartyTrailsConfig.TREASURE_TRAIL_PROGRESS,
				encoded);
			log.debug("Saved treasure trail progress to config file. {}", progress.toString());
		}
		catch (IOException e)
		{
			log.error("Error while saving progress!", e);
		}
	}
}
