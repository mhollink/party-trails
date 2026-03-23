package dev.hollink.partytrails.engine.hunter;

import dev.hollink.partytrails.data.TreasureTrail;
import dev.hollink.partytrails.data.steps.TrailStep;
import dev.hollink.partytrails.data.trail.TrailContext;
import dev.hollink.partytrails.events.Subscription;
import dev.hollink.partytrails.events.TrailEventBus;
import dev.hollink.partytrails.events.events.TrailCompletedEvent;
import dev.hollink.partytrails.events.events.TrailStepCompletedEvent;
import dev.hollink.partytrails.engine.hunter.handlers.StepHandler;
import dev.hollink.partytrails.engine.hunter.handlers.StepHandlerFactory;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TrailHunterEngine
{
	private final TrailEventBus events;
	private final StepHandlerFactory stepHandlerFactory;

	private TreasureTrail trail;
	private TrailContext context;
	private Subscription subscription;

	private int currentIndex;
	private StepHandler<?> activeHandler;

	public void start(TreasureTrail trail, TrailContext context)
	{
		this.trail = trail;
		this.context = context;
		this.currentIndex = 0;

		subscription = events.register(TrailStepCompletedEvent.class, this::onStepCompleted);

		log.debug("Starting {} trail", trail.getMetadata().getTrailName());
		activateCurrentStep();
	}

	private void activateCurrentStep()
	{
		if (currentIndex >= trail.getSteps().size())
		{
			log.debug("Trail completed");
			events.publish(new TrailCompletedEvent(trail, context));
			return;
		}

		TrailStep step = getCurrentStep();

		activeHandler = stepHandlerFactory.createHandler(step, context);
		log.debug("Activated step {}", currentIndex);
	}

	public TrailStep getCurrentStep()
	{
		if (trail == null)
		{
			return null;
		}
		if (trail.getSteps().size() <= currentIndex)
		{
			return null;
		}

		return trail.getSteps().get(currentIndex);
	}

	private void onStepCompleted(TrailStepCompletedEvent event)
	{
		log.debug("Step {} completed", currentIndex);

		deactivateCurrentHandler();

		currentIndex++;
		activateCurrentStep();
	}

	private void deactivateCurrentHandler()
	{
		if (activeHandler != null)
		{
			activeHandler.onDeactivate(); // recommend adding this
			activeHandler = null;
		}
	}

	public void reset()
	{
		deactivateCurrentHandler();

		trail = null;
		context = null;
		currentIndex = 0;

		if (subscription != null) {
			subscription.unsubscribe();
			subscription = null;
		}
	}
}
