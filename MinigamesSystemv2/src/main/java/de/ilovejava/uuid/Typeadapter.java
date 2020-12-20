package de.ilovejava.uuid;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class Typeadapter extends TypeAdapter<UUID>{
	
	@Override
	public void write(JsonWriter paramJsonWriter, UUID paramUUID) throws IOException
	  {
	    paramJsonWriter.value(fromUUID(paramUUID));
	  }

	  @Override
	public UUID read(JsonReader paramJsonReader) throws IOException {
	    return fromString(paramJsonReader.nextString());
	  }

	  public static String fromUUID(UUID paramUUID) {
	    return paramUUID.toString().replace("-", "");
	  }

	  public static UUID fromString(String paramString) {
	    return UUID.fromString(paramString.replaceFirst(
	      "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
	  }
}
