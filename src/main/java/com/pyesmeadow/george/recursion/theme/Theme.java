package com.pyesmeadow.george.recursion.theme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Theme {

	public final String name;
	public final Color nodeColour;
	public final Color backgroundColour;
	private transient Map<ThemeTextureType, BufferedImage> textures;

	public Theme(String name, Color nodeColour, Color backgroundColour, Map<ThemeTextureType, BufferedImage> textures)
	{
		this.name = name;
		this.nodeColour = nodeColour;
		this.backgroundColour = backgroundColour;
		this.textures = textures;

		if (!validate()) throw new IllegalArgumentException("This theme has not been set up correctly");
	}

	public void setTextures(Map<ThemeTextureType, BufferedImage> textures)
	{
		this.textures = textures;
	}

	public BufferedImage getTexture(ThemeTextureType textureType)
	{
		return textures.get(textureType);
	}

	public boolean validate()
	{
		// Check name and colours are not null
		if (name == null || nodeColour == null || backgroundColour == null) return false;

		// Check all textures exist
		if (!new HashSet<>(Arrays.asList(ThemeTextureType.values())).equals(textures.keySet()))
		{
			return false;
		}

		// Check heights are the same for all textures
		List<BufferedImage> textureList = new ArrayList<>(textures.values());
		for (int i = 0; i < textureList.size() - 1; i++)
		{
			BufferedImage current = textureList.get(i);
			BufferedImage next = textureList.get(i + 1);

			if (current.getHeight() != next.getHeight())
			{
				return false;
			}
		}

		return true;
	}

	public enum ThemeTextureType {
		ROAD_BORDER("road/border"), ROAD_MEDIAN("road/median"), ROAD_EDGE("road/edge"), ROAD_LANE("road/lane");

		private String path;

		ThemeTextureType(String pathToFile)
		{
			this.path = pathToFile;
		}

		public String getPath()
		{
			return path;
		}
	}
}
