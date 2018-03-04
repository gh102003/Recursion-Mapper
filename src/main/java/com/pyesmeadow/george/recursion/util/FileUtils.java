package com.pyesmeadow.george.recursion.util;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

	public static Path getDataFolder() throws IOException
	{
		Path userFolder = Paths.get(System.getProperty("user.home"));
		Path dataFolder = userFolder.resolve("RecursionMapper");

		// Create directory if necessary
		if (!Files.exists(dataFolder) || !Files.isDirectory(dataFolder))
		{
			Files.createDirectories(dataFolder);
		}

		return dataFolder;
	}

	public static Path getThemesFolder() throws IOException
	{
		Path dataFolder = getDataFolder();
		Path themesFolder = dataFolder.resolve("themes");

		// Create directory if necessary
		if (!Files.exists(themesFolder) || !Files.isDirectory(themesFolder))
		{
			Files.createDirectories(themesFolder);
		}

		return themesFolder;
	}

	public static void openDirectoryInExplorer(Path filePath)
	{
		try
		{
			if (Desktop.isDesktopSupported())
			{
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(filePath.toUri());
			}
			else
			{
				throw new UnsupportedOperationException("Desktop is not supported");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
