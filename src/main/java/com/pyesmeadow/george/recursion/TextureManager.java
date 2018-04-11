package com.pyesmeadow.george.recursion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {

	public static final Map<String, BufferedImage> TEXTURES = new HashMap<>();

	public static void reloadTextures()
	{
		try
		{
			TEXTURES.put("road", loadImage("textures/road/road.png"));
			TEXTURES.put("road_edge", loadImage("textures/road/road_edge.png"));
			TEXTURES.put("road_median", loadImage("textures/road/median.png"));
			TEXTURES.put("road_border", loadImage("textures/road/border.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static BufferedImage loadImage(String filePath) throws IOException
	{

		return ImageIO.read(TextureManager.class.getClassLoader().getResourceAsStream(filePath));
	}

	public static BufferedImage loadImageFromAbsolutePath(Path filePath) throws IOException
	{
		return ImageIO.read(filePath.toFile());
	}
}
