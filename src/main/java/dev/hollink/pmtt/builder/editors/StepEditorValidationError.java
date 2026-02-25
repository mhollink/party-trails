package dev.hollink.pmtt.builder.editors;

public record StepEditorValidationError(int stepIndex, String field, String errorMessage)
{
}
