package dev.hollink.partytrails.engine.builder;

import dev.hollink.partytrails.PartyTrailsConfig;
import dev.hollink.partytrails.data.steps.CoordsStep;
import dev.hollink.partytrails.data.steps.EmoteStep;
import dev.hollink.partytrails.data.steps.InteractionStep;
import dev.hollink.partytrails.data.steps.SkillStep;
import dev.hollink.partytrails.data.steps.TrailStep;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
@Singleton
public final class TrailBuilderOverlay extends Overlay
{

	private final Client client;
	private final PartyTrailsConfig config;
	private final TrailBuilderPanel builderPanel;

	@Inject
	public TrailBuilderOverlay(Client client, PartyTrailsConfig config, TrailBuilderPanel builderPanel)
	{
		this.client = client;
		this.config = config;
		this.builderPanel = builderPanel;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics2D)
	{
		if (!config.showBuilderOverlay())
		{
			return null;
		}


		List<StepEditorPanel> steps = List.copyOf(builderPanel.getEditors());
		for (int i = 0; i < steps.size(); i++)
		{
			renderStepOverlay(steps.get(i).toTrailStep(), graphics2D, i);
		}

		return null;
	}

	private void renderStepOverlay(TrailStep configuredStep, Graphics2D graphics2D, int stepNumber)
	{
		if (configuredStep instanceof EmoteStep)
		{
			renderStepOverlay((EmoteStep) configuredStep, stepNumber, graphics2D);
		}
		else if (configuredStep instanceof InteractionStep)
		{
			renderStepOverlay((InteractionStep) configuredStep, stepNumber, graphics2D);

		}
		else if (configuredStep instanceof CoordsStep)
		{
			renderStepOverlay((CoordsStep) configuredStep, stepNumber, graphics2D);
		}
		else if (configuredStep instanceof SkillStep)
		{
			renderStepOverlay((SkillStep) configuredStep, stepNumber, graphics2D);

		}

	}

	private void renderStepOverlay(InteractionStep step, int stepNumber, Graphics2D graphics2D)
	{
		if (step.getTarget() == null || step.getTarget().getLocation() == null)
		{
			return;
		}
		if (step.getTarget().getLocation().getPlane() != client.getLocalPlayer().getWorldLocation().getPlane())
		{
			return;
		}

		renderTile(graphics2D, step.getTarget().getLocation(), String.format("Step %s", stepNumber));
	}

	private void renderStepOverlay(SkillStep step, int stepNumber, Graphics2D graphics2D)
	{
		if (step.getArea() == null)
		{
			return;
		}
		if (step.getArea().getPlane() != client.getLocalPlayer().getWorldLocation().getPlane())
		{
			return;
		}

		renderWorldAreaOutline(graphics2D, step.getArea(), String.format("Step %s", stepNumber));
	}

	private void renderStepOverlay(CoordsStep step, int stepNumber, Graphics2D graphics2D)
	{
		if (step.getTargetLocation() == null)
		{
			return;
		}
		if (step.getTargetLocation().getPlane() != client.getLocalPlayer().getWorldLocation().getPlane())
		{
			return;
		}

		renderTile(graphics2D, step.getTargetLocation(), String.format("Step %s", stepNumber));
	}

	private void renderStepOverlay(EmoteStep step, int stepNumber, Graphics2D graphics2D)
	{
		if (step.getTargetLocation() == null)
		{
			return;
		}
		if (step.getTargetLocation().getPlane() != client.getLocalPlayer().getWorldLocation().getPlane())
		{
			return;
		}

		renderTile(graphics2D, step.getTargetLocation(), String.format("Step %s", stepNumber));
	}


	private void renderTile(Graphics2D graphics, WorldPoint worldPoint, String text)
	{
		LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
		if (localPoint == null)
		{
			return;
		}

		Polygon poly = Perspective.getCanvasTilePoly(client, localPoint);
		if (poly == null)
		{
			return;
		}

		graphics.setColor(config.builderOverlayColor());
		graphics.drawPolygon(poly);

		drawText(graphics, text, localPoint);
	}

	private void renderWorldAreaOutline(Graphics2D graphics, WorldArea area, String text)
	{
		int x1 = area.getX();
		int y1 = area.getY();
		int x2 = x1 + area.getWidth();
		int y2 = y1 + area.getHeight();
		int plane = area.getPlane();

		WorldPoint sw = new WorldPoint(x1, y1, plane);
		WorldPoint nw = new WorldPoint(x1, y2, plane);
		WorldPoint ne = new WorldPoint(x2, y2, plane);
		WorldPoint se = new WorldPoint(x2, y1, plane);

		LocalPoint lsw = LocalPoint.fromWorld(client, sw);
		LocalPoint lnw = LocalPoint.fromWorld(client, nw);
		LocalPoint lne = LocalPoint.fromWorld(client, ne);
		LocalPoint lse = LocalPoint.fromWorld(client, se);

		if (lsw == null || lnw == null || lne == null || lse == null)
		{
			return;
		}

		Polygon poly = new Polygon();

		addPoint(poly, lsw);
		addPoint(poly, lnw);
		addPoint(poly, lne);
		addPoint(poly, lse);

		graphics.setColor(config.builderOverlayColor());
		graphics.setStroke(new BasicStroke(2));
		graphics.drawPolygon(poly);
		drawText(graphics, text, lsw);
	}

	private void addPoint(Polygon poly, LocalPoint lp)
	{
		Point p = Perspective.localToCanvas(client, lp, client.getWorldView(lp.getWorldView()).getPlane());
		if (p != null)
		{
			poly.addPoint(p.getX(), p.getY());
		}
	}

	private void drawText(Graphics2D graphics, String text, LocalPoint localPoint)
	{
		Point canvasTextLocation = Perspective.getCanvasTextLocation(client, graphics, localPoint, text, 0);
		if (canvasTextLocation != null)
		{
			graphics.setColor(config.builderOverlayColor());
			graphics.drawString(text, canvasTextLocation.getX(), canvasTextLocation.getY());
		}
	}
}
