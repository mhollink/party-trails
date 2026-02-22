package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.model.TreasureTrail;
import dev.hollink.pmtt.model.events.ClueEvent;
import dev.hollink.pmtt.model.steps.TrailStep;
import dev.hollink.pmtt.model.trail.ClueContext;
import java.awt.Graphics2D;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Slf4j
@Getter
public class TrailRuntime
{

	private final EventBus bus;
	private final ClueContext context;

	private TreasureTrail trail;
	private TrailStep currentStep;

	public TrailRuntime(EventBus bus, ClueContext context)
	{
		this.bus = bus;
		this.context = context;
		bus.register(this::onEvent);
	}

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
		log.info("Starting new trail (length={})", trail.getStepCount());
		this.trail = trail;
		this.getContext().getProgress().reset();
		trail.getStep(this.getContext().getProgress().getCurrentStepIndex())
			.ifPresentOrElse(this::startStep, this::resetOnStepNotFound);
	}

	public void startStep(TrailStep step)
	{
		log.info("Starting new trail step (type={})", step.typeId());
		this.currentStep = step;
		step.onActivate(context);
	}

	private void resetOnStepNotFound()
	{
		log.error("Unable to start trail, no step found at index {}", this.getContext()
			.getProgress()
			.getCurrentStepIndex());
		this.reset();
	}

	public void reset()
	{
		log.info("Resetting active treasure trail");
		this.trail = null;
		this.currentStep = null;
	}

	private void onEvent(ClueEvent event)
	{
		if (currentStep == null || !currentStep.isComplete(event))
		{
			return;
		}
		advanceToNextStep();
	}

	private void advanceToNextStep()
	{
		if (trail == null)
		{
			return;
		}
		int nextStepIndex = this.context.getProgress()
			.getCurrentStepIndex() + 1;
		this.context.getProgress().setCurrentStepIndex(nextStepIndex);
		trail.getStep(nextStepIndex)
			.ifPresentOrElse(this::startStep, this::completeTrail);
	}

	private void completeTrail()
	{
		log.info("Completing trail...");
		this.reset();
	}
}
