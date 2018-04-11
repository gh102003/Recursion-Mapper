package com.pyesmeadow.george.recursion.network;

import com.pyesmeadow.george.recursion.*;
import com.pyesmeadow.george.recursion.theme.Theme;
import com.pyesmeadow.george.recursion.util.RenderUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Connection implements ITraversable, Serializable {

	private static final long serialVersionUID = 1L;
	public final Node node1, node2;
	public final char id;
	private final Network network;
	protected boolean selected = false;
	private int weight;

	public Connection(Node node1, Node node2, char id, int weight, Network network)
	{
		this.node1 = node1;
		this.node2 = node2;
		this.id = id;
		this.weight = weight;
		this.network = network;

		node1.addConnection(this);
		node2.addConnection(this);
	}

	public Connection(Node node1, Node node2, char id, Network network)
	{
		this(node1, node2, id, new Random().nextInt(1000), network);
	}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		network.markDirty(true);
		this.weight = weight;
	}

	public void tick()
	{

	}

	public void render(Graphics2D g, Renderer.RenderMode renderMode)
	{
		int width = 3;

		if (renderMode == Renderer.RenderMode.TEXTURED)
		{
			//  Render texture, calculate width
			g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 50));

			// Carriageway 1 (left)
//			BufferedImage laneTexture = TextureManager.TEXTURES.get("road");
//			BufferedImage edgeLaneTexture = TextureManager.TEXTURES.get("road_edge");
//			BufferedImage borderTexture = TextureManager.TEXTURES.get("road_border");
			BufferedImage laneTexture = Main.main.themeManager.getTexture(Theme.ThemeTextureType.ROAD_LANE);
			BufferedImage edgeLaneTexture = Main.main.themeManager.getTexture(Theme.ThemeTextureType.ROAD_EDGE);
			BufferedImage borderTexture = Main.main.themeManager.getTexture(Theme.ThemeTextureType.ROAD_BORDER);

			int laneCount = weight / 200;
			int carriagewayWidth = 0;

			// Create list of images to be a part of the carriageway
			List<BufferedImage> carriagewayComponents = new ArrayList<>();
			carriagewayComponents.add(borderTexture);
			carriagewayWidth += borderTexture.getWidth();
			if (laneCount > 0)
			{
				carriagewayComponents.add(edgeLaneTexture);
				carriagewayWidth += edgeLaneTexture.getWidth();
			}
			for (int i = 0; i < laneCount - 1; i++)
			{
				carriagewayComponents.add(laneTexture);
				carriagewayWidth += laneTexture.getWidth();
			}

			BufferedImage carriageway1 = new BufferedImage(carriagewayWidth, laneTexture.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D carriageway1Graphics = carriageway1.createGraphics();
			carriageway1Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			try
			{
				int currentWidth = 0;
				for (BufferedImage image : carriagewayComponents)
				{
					carriageway1Graphics.drawImage(image, currentWidth, 0, null);
					currentWidth += image.getWidth();
				}
			}
			finally
			{
				carriageway1Graphics.dispose();
			}

			// Full road
			BufferedImage medianTexture = Main.main.themeManager.getTexture(Theme.ThemeTextureType.ROAD_MEDIAN);
			width = carriagewayWidth * 2 + medianTexture.getWidth();

			int length = (int) Math.sqrt(Math.pow(node2.getX() - node1.getX(), 2) + Math.pow(node2.getY() - node1.getY(), 2));

			BufferedImage road = new BufferedImage(width, length, BufferedImage.TYPE_INT_ARGB);
			Graphics2D roadGraphics = road.createGraphics();
			roadGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			try
			{
				TexturePaint carriageway1Paint = new TexturePaint(carriageway1,
						new Rectangle(carriagewayWidth, carriageway1.getHeight()));

				AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
				flip.translate(-carriageway1.getWidth(), 0);
				AffineTransformOp flipOp = new AffineTransformOp(flip, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				BufferedImage carriageway2 = flipOp.filter(carriageway1, null);
				TexturePaint carriageway2Paint = new TexturePaint(carriageway2,
						new Rectangle(carriageway2.getWidth() + medianTexture.getWidth(),
								carriageway2.getHeight(),
								carriageway2.getWidth(),
								carriageway2.getHeight()));

				roadGraphics.setPaint(carriageway1Paint);
				roadGraphics.fillRect(0, 0, carriagewayWidth, length);
				roadGraphics.drawImage(medianTexture, carriagewayWidth, 0, medianTexture.getWidth(), length, null);
				roadGraphics.setPaint(carriageway2Paint);
				roadGraphics.fillRect(carriageway1.getWidth() + medianTexture.getWidth(), 0, carriageway2.getWidth(), length);
			}
			finally
			{
				roadGraphics.dispose();
			}

			// Rotate road

			double angle = Math.atan2((node2.getY() - node1.getY()), (double) node2.getX() - node1.getX());
			BufferedImage roadRotated = RenderUtils.rotateBy(road, Math.toDegrees(angle) + 270);

			g.drawImage(roadRotated,
					Math.min(node1.getX(), node2.getX()) - (road.getWidth() / 2),
					Math.min(node1.getY(), node2.getY()) - (road.getWidth() / 2),
					null);
		}
		else if (renderMode == Renderer.RenderMode.DYNAMIC)
		{
			width = weight / 50 + 1;
		}

		// Set width
		g.setStroke(new BasicStroke(width));

		if (renderMode != Renderer.RenderMode.TEXTURED)
		{
			// Render line
			g.setColor(new Color(220, 0, 0));
			g.drawLine(node1.getX(), node1.getY(), node2.getX(), node2.getY());
		}

		// Selection
		if (selected)
		{
			g.setColor(new Color(255, 255, 255, 180));
			g.drawLine(node1.getX(), node1.getY(), node2.getX(), node2.getY());
		}

		// Text
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(id), (node1.getX() + node2.getX()) / 2, (node1.getY() + node2.getY()) / 2);

		MouseInput.InteractionMode interactionMode = Main.main.mouseInputListener.interactionMode;
		if (interactionMode == MouseInput.InteractionMode.EDIT || interactionMode == MouseInput.InteractionMode.PATHFIND)
		{
			g.drawString("Weight: " + String.valueOf(weight),
					(node1.getX() + node2.getX()) / 2,
					(node1.getY() + node2.getY()) / 2 + 12);
		}
	}

	public boolean containsPoint(int pointX, int pointY)
	{
		// Calculate angle (i.e. ratio between delta x and delta y) from node 1
		int node1DeltaX = pointX - node1.getX();
		int node1DeltaY = pointY - node1.getY();

		double angleFromNode1 = (node1DeltaX + 0.001) / (node1DeltaY + 0.001);

		// Calculate angle (i.e. ratio between delta x and delta y) to node 2
		int node2DeltaX = node2.getX() - pointX;
		int node2DeltaY = node2.getY() - pointY;

		if (node1DeltaX > 0 && node2DeltaX < 0 || node1DeltaX < 0 && node2DeltaX > 0 || node1DeltaY > 0 && node2DeltaY < 0 || node1DeltaY < 0 && node2DeltaY > 0)
		{
			return false;
		}

		double angleToNode2 = (node2DeltaX + 0.001) / (node2DeltaY + 0.001);

		// If the angles are similar, return true
		if (angleFromNode1 - angleToNode2 > -1 && angleFromNode1 - angleToNode2 < 1)
		{
			return true;
		}

		return false;
	}

	/**
	 * Follows this connection to the node at the other end
	 *
	 * @param node a node at one end of this connection
	 * @return the other node
	 * @throws IllegalArgumentException if <code>node</code> is not part of this connection
	 */
	public Node follow(Node node)
	{
		if (node.equals(node1))
		{
			return node2;
		}
		else if (node.equals(node2))
		{
			return node1;
		}
		else
		{
			throw new IllegalArgumentException("The node must be at one end of this connection");
		}
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Connection)
		{
			Connection path = (Connection) obj;
			if (path.node1 == node1 && path.node2 == node2)
			{
				return true;
			}

			if (path.node1 == node2 && path.node2 == node1)
			{
				return true;
			}

			return false;
		}

		return super.equals(obj);
	}
}
