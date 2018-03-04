package com.pyesmeadow.george.recursion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {

	public static final Map<String, BufferedImage> TEXTURES = new HashMap<>();

	public static void reloadTextures()
	{
		TEXTURES.put("road", loadImage("textures/road/road.png"));
		TEXTURES.put("road_edge", loadImage("textures/road/road_edge.png"));
		TEXTURES.put("road_median", loadImage("textures/road/median.png"));
		TEXTURES.put("road_border", loadImage("textures/road/border.png"));
	}

	private static BufferedImage loadImage(String fileName)
	{
		BufferedImage img;
		try
		{
			img = ImageIO.read(TextureManager.class.getClassLoader().getResourceAsStream(fileName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return img;
	}
}
