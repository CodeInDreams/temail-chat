package com.syswin.temail.usermail.core.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.sql.Timestamp;

public class GsonTimestampJsonSerializer implements JsonSerializer<Timestamp> {

  @Override
  public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(timestamp.getTime());
  }
}
