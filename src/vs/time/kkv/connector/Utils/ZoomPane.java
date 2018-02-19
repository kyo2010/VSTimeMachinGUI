/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vs.time.kkv.connector.Utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

public class ZoomPane extends JPanel implements MouseWheelListener, Scrollable {

  private int width, height, screenWidth, screenHeight, tileSize;
  private Color[] colorMap;
  private double scale = 1.0;

// Zooming
  AffineTransform at;

  class MouseTest extends MouseAdapter {

    public void mousePressed(MouseEvent e) {
      try {
        int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
        int newY = (int) at.inverseTransform(e.getPoint(), null).getY();
        colorMap[(newX / tileSize) + ((newY / tileSize) * width)] = getRandomColor();

      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
      repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      super.mouseDragged(e);
      try {
        System.out.println("Dragging");
        int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
        int newY = (int) at.inverseTransform(e.getPoint(), null).getY();
        colorMap[(newX / tileSize) + ((newY / tileSize) * width)] = getRandomColor();

      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
      repaint();
    }
  }

  public ZoomPane(int width, int height, int tileSize) {
    super();
    this.width = width;
    this.height = height;
    this.screenWidth = width * tileSize;
    this.screenHeight = height * tileSize;
    this.tileSize = tileSize;
    this.colorMap = new Color[width * height];
    addMouseWheelListener(this);
    addMouseListener(new MouseTest());
    addMouseMotionListener(new MouseTest());
  }

  public void paintComponent(Graphics g) {
    super.paintComponents(g);
    final Graphics2D g2d = (Graphics2D) g.create();

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Scale
    at = null;
    at = g2d.getTransform();
    // Translate code here?

    at.scale(scale, scale);

    g2d.setTransform(at);
    final Rectangle clip = g2d.getClipBounds();

    g2d.setColor(Color.DARK_GRAY);
    g2d.fill(clip);

    int topX = clip.x / tileSize;
    int topY = clip.y / tileSize;
    int bottomX = clip.x + clip.width / tileSize + 1 + (int) (tileSize * scale);
    int bottomY = clip.y + clip.height / tileSize + 1;

    // Draw colors
    for (int y = topY; y < bottomY; y++) {
      for (int x = topX; x < bottomX; x++) {
        Rectangle r = new Rectangle(width, height);
        if (r.contains(x, y) && colorMap[x + y * width] != null) {
          g2d.setColor(colorMap[x + y * width]);
          g2d.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
        }
      }
    }
    g2d.dispose();
  }

  private Color getRandomColor() {
    Random rand = new Random();
    return new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension((int) (screenWidth), (int) (screenHeight));
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    double delta = 0.05 * e.getPreciseWheelRotation();
    if (scale + delta > 0) {
      scale += delta;
    }
    revalidate();
    repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame("Zoom test");
        // Frame settings
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane pane = new JScrollPane(new ZoomPane(50, 50, 32));
        frame.add(pane);
        frame.pack();
      }
    });

  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    repaint();
    return null;
  }

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    repaint();
    return 0;
  }

  @Override
  public boolean getScrollableTracksViewportHeight() {
    repaint();
    return false;
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    repaint();
    return false;
  }

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    repaint();
    return 0;
  }
}
