package com.pyesmeadow.george.recursion;

import javax.swing.*;
import java.awt.*;

public class Window {

	public Window(int width, int height, String title, Main main)
	{
		JFrame frame = new JFrame(title);

		frame.setResizable(false);

		Dimension windowSize = new Dimension(width, height);

		frame.getContentPane().setPreferredSize(windowSize);
		frame.getContentPane().setMaximumSize(windowSize);
		frame.getContentPane().setMinimumSize(windowSize);

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(main);
		frame.setVisible(true);
		main.start();
	}
}
