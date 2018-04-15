package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.io.NetworkManager;
import com.pyesmeadow.george.recursion.theme.ThemeManager;
import com.pyesmeadow.george.recursion.ui.CanvasButton;
import com.pyesmeadow.george.recursion.ui.CanvasDropdown;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.List;

public class Renderer {

	private final Main canvas;
	private final ThemeManager themeManager;
	private final NetworkManager networkManager;
	private final List<CanvasButton> buttonList;
	private final List<CanvasDropdown> dropdownList;

	RenderMode renderMode = RenderMode.STATIC;

	Renderer(Main canvas,
			 ThemeManager themeManager,
			 NetworkManager networkManager,
			 List<CanvasButton> buttonList,
			 List<CanvasDropdown> dropdownList)
	{
		this.canvas = canvas;
		this.themeManager = themeManager;
		this.networkManager = networkManager;
		this.buttonList = buttonList;
		this.dropdownList = dropdownList;
	}

	void render()
	{
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setBackground(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		g.setColor(themeManager.getCurrentTheme().backgroundColour);
		g.setStroke(new BasicStroke(1));
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		networkManager.network.render(g, renderMode, canvas.mouseInputListener.interactionMode, themeManager);

		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			buttonList.get(i).render(g);
		}

		for (int i = dropdownList.size() - 1; i >= 0; i--)
		{
			dropdownList.get(i).render(g);
		}

		g.dispose();
		bs.show();
	}

	public enum RenderMode {
		STATIC("Static"), DYNAMIC("Dynamic"), TEXTURED("Textured");

		final String name;

		RenderMode(String name)
		{
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
