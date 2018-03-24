package com.pyesmeadow.george.recursion.theme;

import com.pyesmeadow.george.recursion.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ThemeLoader {

	public static List<Theme> loadCustomThemes() throws IOException
	{
		List<Theme> themes = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(FileUtils.getThemesFolder()))
		{
			for (Path themeDirectory : directoryStream)
			{
				themes.add(loadTheme(themeDirectory));
			}
		}
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

		Path themeInfoPath = themePath.resolve("theme_info.json");

		try (InputStream themeInfoInputStream = Files.newInputStream(themeInfoPath))
		{

		}
	}
}
