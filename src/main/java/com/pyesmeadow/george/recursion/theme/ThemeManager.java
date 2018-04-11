package com.pyesmeadow.george.recursion.theme;

import java.awt.image.BufferedImage;
import java.util.List;

public class ThemeManager {

	private final List<Theme> loadedThemes;
	private Theme currentTheme;
	public ThemeManager(List<Theme> loadedThemes)
	{
		this.loadedThemes = loadedThemes;
		this.setCurrentTheme(loadedThemes.get(0));
	}

	public List<Theme> getLoadedThemes()
	{
		return loadedThemes;
	}

	public BufferedImage getTexture(Theme.ThemeTextureType themeTextureType)
	{
		Theme theme = getCurrentTheme();
		return theme.getTexture(themeTextureType);
	}

	public Theme getCurrentTheme()
	{
		return currentTheme;
	}

	public void setCurrentTheme(Theme currentTheme)
	{
		this.currentTheme = currentTheme;
	}
}
