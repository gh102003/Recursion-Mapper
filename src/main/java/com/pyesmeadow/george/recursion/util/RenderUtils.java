package com.pyesmeadow.george.recursion.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RenderUtils {

	public static BufferedImage rotateBy(Image image, double degrees)
	{
		// The size of the original image
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		// The angle of the rotation in radians
		double rads = Math.toRadians(degrees);
		// Calculate the amount of space the image will need in
		// order not be clipped when it's rotated
		double sin = Math.abs(Math.sin(rads));
		double cos = Math.abs(Math.cos(rads));
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		// A new image, into which the original can be painted
		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// The transformation which will be used to actually rotate the image
		// The translation, actually makes sure that the image is positioned onto
		// the viewable area of the image
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		// And we rotate about the center of the image...
		int x = w / 2;
		int y = h / 2;
		at.rotate(rads, x, y);
		g2d.setTransform(at);
		// And we paint the original image onto the new image
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		return rotated;
	}
}
