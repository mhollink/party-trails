package dev.hollink.pmtt.builder.editors;

import lombok.Value;

@Value
public class StepEditorValidationError
{
	int stepIndex;
	String field;
	String errorMessage;
}
