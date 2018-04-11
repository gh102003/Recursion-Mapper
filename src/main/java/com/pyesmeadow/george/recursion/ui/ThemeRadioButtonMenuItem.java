package com.pyesmeadow.george.recursion.ui;

import com.pyesmeadow.george.recursion.theme.Theme;
import com.pyesmeadow.george.recursion.theme.ThemeManager;

import javax.swing.*;

/**
 * A {@link JRadioButtonMenuItem} that stores a theme and sets the current theme when selected
 */
class ThemeRadioButtonMenuItem extends JRadioButtonMenuItem {

	private Theme theme;

	/**
	 * @param theme the theme to change to when selected. Also used to set text
	 * @param themeManager the themeManager to change the selected theme of when selected
	 */
	ThemeRadioButtonMenuItem(Theme theme, ThemeManager themeManager)
	{
		super(theme.name);

		this.theme = theme;
		this.addActionListener(action -> themeManager.setCurrentTheme(theme));
	}
}
