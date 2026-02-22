package dev.hollink.pmtt.model.steps;

import dev.hollink.pmtt.model.StepTypes;
import dev.hollink.pmtt.model.trail.ClueContext;
import dev.hollink.pmtt.model.events.ClueEvent;
import dev.hollink.pmtt.model.trail.Encodable;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


@Slf4j
public class SkillStep implements TrailStep  {

    @Override
    public byte typeId() {
        return StepTypes.SKILL_STEP;
    }

    @Override
    public void drawOverlay(PanelComponent panel, Graphics2D graphics) {

    }

    @Override
    public void onActivate(ClueContext context) {

    }

    @Override
    public boolean isComplete(ClueEvent event) {
        return false;
    }

    @Override
    public void encode(DataOutput out) throws IOException {

    }

    @Override
    public Encodable decode(DataInput in) throws IOException {
        return null;
    }

}
