package dev.hollink.pmtt.model;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class TreasureTrail {
    private final int version;
    private final String trailId;
    private final String trailName;
    private final String author;
    @Getter(AccessLevel.NONE)
    private final List<TrailStep> steps;

    public TreasureTrail(int version, String trailId, String trailName, String author, List<TrailStep> steps) {
        this.version = version;
        this.trailId = trailId;
        this.trailName = trailName;
        this.author = author;
        this.steps = steps;
    }

    public Optional<TrailStep> getStep(int index) {
        try {
           return Optional.ofNullable(steps.get(index));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public int getStepCount() {
        return steps.size();
    }
}
