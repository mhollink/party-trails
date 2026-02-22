package dev.hollink.pmtt.model.trail;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TrailProgress {
    private int currentStepIndex = 0;
    private Map<String, Object> stepState = Maps.newHashMap();

    public void reset() {
        currentStepIndex = 0;
        stepState.clear();
    }
}
