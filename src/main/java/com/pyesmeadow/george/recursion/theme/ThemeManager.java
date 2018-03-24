package com.pyesmeadow.george.recursion.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

	private List<Theme> loadedThemes = new ArrayList<>();
	private Theme currentTheme;

	public ThemeManager()
	{
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
