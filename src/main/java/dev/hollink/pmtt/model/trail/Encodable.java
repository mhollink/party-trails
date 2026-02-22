package dev.hollink.pmtt.model.trail;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Encodable {
    byte typeId();

    void encode(DataOutput out) throws IOException;

    Encodable decode(DataInput in) throws IOException;
}
