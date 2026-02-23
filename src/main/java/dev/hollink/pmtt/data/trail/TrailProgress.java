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
	private int currentStepIndex = 0;
	private Map<String, Object> stepState = Maps.newHashMap();

	public void reset()
	{
		currentStepIndex = 0;
		stepState.clear();
	}
}
