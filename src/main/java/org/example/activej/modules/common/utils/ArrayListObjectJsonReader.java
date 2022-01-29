package org.example.activej.modules.common.utils;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.ObjectConverter;

import java.io.IOException;
import java.util.ArrayList;

public abstract class ArrayListObjectJsonReader {
    public static final JsonReader.ReadObject<ArrayList<Object>> JSON_READER = reader -> {
        reader.getNextToken();
        return ObjectConverter.deserializeList(reader);
    };
    public static final JsonWriter.WriteObject<Object> OBJECT_WRITER = (writer, value) -> {
        try {
            ObjectConverter.serializeObject(value, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
    public static final JsonWriter.WriteObject<ArrayList<Object>> JSON_WRITER = (writer, value) -> writer.serialize(value, OBJECT_WRITER);
}
