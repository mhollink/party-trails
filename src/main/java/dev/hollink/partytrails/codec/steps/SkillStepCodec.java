package dev.hollink.partytrails.codec.steps;

import dev.hollink.partytrails.data.steps.SkillStep;
import dev.hollink.partytrails.codec.Codec;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;

public class SkillStepCodec implements Codec<SkillStep>
{
	@Override
	public void encode(SkillStep step, DataOutput out) throws IOException
	{
		writeString(out, step.getHint());
		out.writeInt(step.getSkill().ordinal());
		out.writeInt(step.getExpRequired());
		out.writeInt(step.getArea().getX());
		out.writeInt(step.getArea().getY());
		out.writeInt(step.getArea().getWidth());
		out.writeInt(step.getArea().getHeight());
		out.writeInt(step.getArea().getPlane());
	}

	@Override
	public SkillStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		Skill skill = Skill.values()[in.readInt()];
		int exp = in.readInt();
		int x = in.readInt();
		int y = in.readInt();
		int w = in.readInt();
		int h = in.readInt();
		int plane = in.readInt();

		return new SkillStep(hint, skill, exp, new WorldArea(x, y, w, h, plane));
	}
}
