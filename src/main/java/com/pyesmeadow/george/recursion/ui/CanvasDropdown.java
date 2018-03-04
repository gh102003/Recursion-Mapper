package com.pyesmeadow.george.recursion.ui;

import com.pyesmeadow.george.recursion.Renderer;

import java.awt.*;
import java.util.function.Consumer;

public class CanvasDropdown<E extends Enum<E>> {

	private final int DROPDOWN_CHOICE_HEIGHT;
	private final int DROPDOWN_CHOICE_PADDING;
	private final int x, y, width, height;
	private final Enum<E>[] values;
	private final String prefix;
	private boolean hover;
	private boolean dropdownOpen;
	private int selectedIndex = 0;
	private Consumer<Enum<E>> choiceChangeListener;

	public CanvasDropdown(int x, int y, int width, int height, final int dropdownChoiceHeight , String prefix, Enum<E>[] values, Consumer<Enum<E>> onChoiceChange)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.values = values;
		this.prefix = prefix;
		this.DROPDOWN_CHOICE_HEIGHT = dropdownChoiceHeight;
		this.choiceChangeListener = onChoiceChange;

		DROPDOWN_CHOICE_PADDING = dropdownChoiceHeight / 5;
	}

	public void setDropdownOpen(boolean dropdownOpen)
	{
		this.dropdownOpen = dropdownOpen;
	}

	public void setHover(boolean hover)
	{
		this.hover = hover;
	}

	public Enum<E> getSelectedValue()
	{
		return values[selectedIndex];
	}

	public void render(Graphics2D g)
	{
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRoundRect(x, y, width, height, 2, 2);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		g.drawString(prefix + ": " + getSelectedValue(), x + DROPDOWN_CHOICE_PADDING, y + height - DROPDOWN_CHOICE_PADDING);

		if (hover)
		{
			g.setColor(new Color(255, 255, 255, 80));
			g.fillRoundRect(x, y, width, height, 2, 2);
		}

		if (dropdownOpen)
		{
			Color evenColor = new Color(50, 50, 50, 230);
			Color oddColor = new Color(20, 20, 20, 230);

			for (int i = 0; i < values.length; i++)
			{
				if (i % 2 == 0)
				{
					g.setColor(evenColor);
				}
				else
				{
					g.setColor(oddColor);
				}

				int yPos = y + height + i * DROPDOWN_CHOICE_HEIGHT;
				g.fillRect(x, yPos, width, DROPDOWN_CHOICE_HEIGHT);
				g.setColor(Color.WHITE);
				g.drawString(values[i].toString(), x + DROPDOWN_CHOICE_PADDING, yPos + DROPDOWN_CHOICE_HEIGHT - DROPDOWN_CHOICE_PADDING);
			}
		}
	}

	public synchronized void click(int mouseX, int mouseY)
	{
		if (!dropdownOpen)
		{
			dropdownOpen = true;
		}
		else
		{
			// If the dropdown was clicked
			if (mouseY > y + height)
			{
				selectedIndex = (mouseY - y - height) / DROPDOWN_CHOICE_HEIGHT;

				choiceChangeListener.accept(values[selectedIndex]);
			}

			dropdownOpen = false;
		}
	}

	public boolean containsPoint(int pointX, int pointY)
	{
		int y2;

		if (!dropdownOpen)
		{
			y2 = y + height;
		}
		else
		{
			y2 = y + height + DROPDOWN_CHOICE_HEIGHT * values.length;
		}

		return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y2;
	}
}
