package dev.hollink.partytrails.engine.builder.editors;

import lombok.Value;

@Value
public class StepEditorValidationError
{
	int stepIndex;
	String field;
	String errorMessage;
}
