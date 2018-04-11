package com.pyesmeadow.george.recursion.theme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.pyesmeadow.george.recursion.TextureManager;
import com.pyesmeadow.george.recursion.util.FileUtils;
import com.pyesmeadow.george.recursion.util.GSONColorSerializer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class ThemeLoader {

	public static Theme loadDefaultTheme() throws IOException
	{
		// Load textures
		Map<Theme.ThemeTextureType, BufferedImage> textures = new HashMap<>();

		// Attempt to load each texture
		try
		{
			for (Theme.ThemeTextureType themeTextureType : Theme.ThemeTextureType.values())
			{
				textures.put(themeTextureType,
						TextureManager.loadImage("themes/default/textures/" + themeTextureType.getPath() + ".png"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IOException("Could not load textures for the default theme");
		}

		/* Load theme info json file */
		Theme theme;

		try (InputStream themeInfoInputStream = ThemeLoader.class.getClassLoader().getResourceAsStream(
				"themes/default/theme_info.json");
			 BufferedReader themeInfoReader = new BufferedReader(new InputStreamReader(themeInfoInputStream)))
		{
			/* Setup GSON deserialiser */
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(Color.class, new GSONColorSerializer());
			Gson gson = gsonBuilder.create();

			theme = gson.fromJson(themeInfoReader, Theme.class);
		}

		theme.setTextures(textures);

		return theme;
	}

	public static List<Theme> loadCustomThemes() throws IOException
	{
		// Load custom themes
		List<Theme> themes = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(FileUtils.getThemesFolder()))
		{
			for (Path themeDirectory : directoryStream)
			{
				try
				{
					Theme theme = loadTheme(themeDirectory);

					if (!theme.validate()) throw new IOException("This theme has not been set up correctly");

					themes.add(theme);
				}
				catch (Exception e)
				{
					System.err.printf("Could not load theme at %s%n", themeDirectory);
					e.printStackTrace();
				}
			}
		}

		// Print number and names of themes
		System.out.printf("Found %d custom themes:%n", themes.size());
		themes.forEach(theme -> System.out.println("\t" + theme.name));

		return themes;
	}

	/**
	 * Loads a theme from a directory
	 *
	 * @param themePath the base directory of the theme
	 * @return a loaded theme
	 * @throws IOException              if there was an error loading the theme
	 * @throws IllegalArgumentException if {@code themePath} is non-existent or is a file
	 */
	static Theme loadTheme(Path themePath) throws IOException, IllegalArgumentException
	{
		if (!Files.exists(themePath)) throw new IllegalArgumentException("The file doesn't exist");
		if (!Files.isDirectory(themePath)) throw new IllegalArgumentException("The path does not point to a directory");

		/* Load textures */
		Map<Theme.ThemeTextureType, BufferedImage> textures = new HashMap<>();

		Path texturesPath = themePath.resolve("textures");

		// Attempt to load each texture
		try
		{
			for (Theme.ThemeTextureType themeTextureType : Theme.ThemeTextureType.values())
			{
				textures.put(themeTextureType,
						TextureManager.loadImageFromAbsolutePath(texturesPath.resolve(themeTextureType.getPath() + ".png")));
			}
		}
		catch (IOException e)
		{
			throw new IOException(new Formatter().format("Could not load textures for theme at %s", themePath).toString());
		}

		/* Load theme info json file */
		Theme theme;

		Path themeInfoPath = themePath.resolve("theme_info.json");
		try (BufferedReader themeInfoReader = Files.newBufferedReader(themeInfoPath))
		{
			/* Setup GSON deserialiser */
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(Color.class, new GSONColorSerializer());
			Gson gson = gsonBuilder.create();

			theme = gson.fromJson(themeInfoReader, Theme.class);
		}
		catch (JsonParseException e)
		{
			throw new IOException(new Formatter().format("Could not load json for theme at %s", themePath).toString());
		}

		theme.setTextures(textures);

		return theme;
	}
}
