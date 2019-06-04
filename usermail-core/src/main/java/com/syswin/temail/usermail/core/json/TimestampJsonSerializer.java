package com.syswin.temail.usermail.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;

public class TimestampJsonSerializer extends JsonSerializer<java.sql.Timestamp> {

  @Override
  public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
    jgen.writeNumber(value.getTime());
  }
}
