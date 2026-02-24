package dev.hollink.pmtt.data.trail;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TrailProgress
{
	private String trailId = null;
	private int currentStepIndex = 0;
	private boolean completed = false;
	private Map<String, String> stepState = Maps.newHashMap();

	public void start(String trailId)
	{
		this.trailId = trailId;
		this.currentStepIndex = 0;
		completed = false;
		this.stepState.clear();
	}

	public void reset()
	{
		this.start(null);
	}

	public void storeInt(String key, int value)
	{
		stepState.put(key, Integer.toString(value));
	}

	public int getStoredInt(String key)
	{
		return Integer.parseInt(stepState.getOrDefault(key, String.valueOf(0)));
	}
}
