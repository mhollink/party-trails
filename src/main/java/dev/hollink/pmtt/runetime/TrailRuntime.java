package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.TreasureTrailConfig;
import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.ClueContext;
import dev.hollink.pmtt.data.trail.TrailProgress;
import dev.hollink.pmtt.encoding.TrailEncoder;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@Getter
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailRuntime
{
	private final EventBus bus;
	private final ConfigManager configManager;
	private final ClueContext context;

	private TreasureTrail trail;
	private TrailStep currentStep;

	public void renderCurrentStep(Graphics2D graphics, PanelComponent panel)
	{
		if (currentStep == null)
		{
			return;
		}
		currentStep.drawOverlay(panel, graphics);
	}

	public void startTrail(TreasureTrail trail)
	{
		log.info("Starting new trail (length={})", trail.getMetadata().stepCount());
		bus.register(this::onEvent);
		this.trail = trail;
		this.context.getProgress().start(trail.getMetadata().trailId());
		trail.getStep(this.context.getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::startStep, this::resetOnStepNotFound);
		saveProgress();
	}

	private void startStep(TrailStep step)
	{
		log.info("Starting new trail step (type={})", step.type().name());
		this.context.getProgress().getStepState().clear();;
		this.currentStep = step;
		step.onActivate(context);
	}

	public void resumeTrail(TreasureTrail trail, TrailProgress progress)
	{
		log.info("Resuming trail (length={}, currentStep={})", trail.getMetadata().stepCount(), progress.getCurrentStepIndex());
		bus.register(this::onEvent);
		this.trail = trail;
		trail.getStep(this.context.getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::resumeStep, this::resetOnStepNotFound);
	}

	private void resumeStep(TrailStep step)
	{
		log.info("Resuming trail step (type={})", step.type().name());
		this.context.getProgress().getStepState().clear();;
		this.currentStep = step;
	}

	private void resetOnStepNotFound()
	{
		log.error("Unable to start trail, no step found at index {}", this.getContext().getProgress().getCurrentStepIndex());
		this.reset();
	}

	public void reset()
	{
		log.info("Resetting active treasure trail");
		this.trail = null;
		this.currentStep = null;
		this.context.getProgress().reset();
		this.bus.unregister(this::onEvent);
	}

	private void onEvent(ClueEvent event)
	{
		if (currentStep == null || !currentStep.handlesEvent(event))
		{
			return;
		}

		context.setLastEvent(event);
		if (currentStep.isComplete(context, event))
		{
			log.info("Completed trail step!");
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
		log.info("Completing trail...");
		this.trail = null;
		this.currentStep = null;
		this.context.getProgress().setCompleted(true);
		this.bus.unregister(this::onEvent);
	}

	private void saveProgress() {
		try {
			TrailProgress progress = context.getProgress();
			String encoded = TrailEncoder.encodeProgress(progress);
			configManager.setConfiguration(
				TreasureTrailConfig.CONFIG_GROUP,
				TreasureTrailConfig.TREASURE_TRAIL_PROGRESS,
				encoded);
			log.info("Saved treasure trail progress to config file. {}", progress.toString());
		} catch (IOException e) {
			log.error("Error while saving progress!", e);
		}
	}
}
