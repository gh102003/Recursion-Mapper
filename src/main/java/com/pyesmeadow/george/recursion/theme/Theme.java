package com.pyesmeadow.george.recursion.theme;

import java.awt.image.BufferedImage;
import java.util.*;

public class Theme {

	public final String name;
	private final Map<ThemeTextureType, BufferedImage> textures;

	public Theme(String name, Map<ThemeTextureType, BufferedImage> textures)
	{
		this.name = name;
		this.textures = textures;

		if (!validate()) throw new IllegalArgumentException("Textures for this theme have not been set up correctly");
	}

	public BufferedImage getTexture(ThemeTextureType textureType)
	{
		return textures.get(textureType);
	}

	private boolean validate()
	{
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
		ROAD_BORDER, ROAD_MEDIAN, ROAD_EDGE, ROAD_LANE;
	}
}
