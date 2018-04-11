package com.pyesmeadow.george.recursion.util;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

public class GSONColorSerializer implements JsonSerializer<Color>, JsonDeserializer<Color> {

	@Override
	public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return Color.decode(json.getAsJsonPrimitive().getAsString());
	}

	@Override
	public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context)
	{
		return new JsonPrimitive("0x" + String.valueOf(src.getRGB()));
	}
}
