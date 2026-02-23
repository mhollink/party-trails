package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.data.TreasureTrail;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.ClueContext;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@Getter
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TrailRuntime
{
	private final EventBus bus;
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
		this.context.getProgress().reset();
		trail.getStep(this.context.getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::startStep, this::resetOnStepNotFound);
	}

	public void startStep(TrailStep step)
	{
		log.info("Starting new trail step (type={})", step.type().name());
		this.context.getProgress().getStepState().clear();;
		this.currentStep = step;
		step.onActivate(context);
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
		this.bus.unregister(this::onEvent);
	}

	private void onEvent(ClueEvent event)
	{
		if (currentStep == null)
		{
			return;
		}

		context.setLastEvent(event);
		if (currentStep.isComplete(event))
		{
			advanceToNextStep();
		}
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
		this.reset();
	}
}
