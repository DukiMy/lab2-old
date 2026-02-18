package lab3.ui;

import lab3.Vehicle;

import javax.imageio.ImageIO;
import java.io.IOException;

import javax.swing.JPanel;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.Color.GREEN;

public final class DrawPanel extends JPanel {

  private final Map<Vehicle, BufferedImage> sprites = new LinkedHashMap<>();

  private BufferedImage workshopImage;
  private final Point workshopPoint = new Point(0, 800 - 340);

  public DrawPanel(Dimension dim) {
    setDoubleBuffered(true);
    setPreferredSize(dim);
    setBackground(GREEN);

    try {
      workshopImage = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/VolvoBrand.jpg")
      );
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void addVehicle(Vehicle v, BufferedImage img) {
    sprites.put(v, img);
  }

  public void removeVehicle(Vehicle v) {
    sprites.remove(v);
  }

  public BufferedImage getImage(Vehicle v) {
    return sprites.get(v);
  }

  public Rectangle2D getWorkshopRect() {
    if (workshopImage == null) return new Rectangle2D.Double(workshopPoint.x, workshopPoint.y, 0, 0);
    return new Rectangle2D.Double(workshopPoint.x, workshopPoint.y, workshopImage.getWidth(), workshopImage.getHeight());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (workshopImage != null) {
      g.drawImage(workshopImage, workshopPoint.x, workshopPoint.y, null);
    }

    for (Map.Entry<Vehicle, BufferedImage> e : sprites.entrySet()) {
      Vehicle v = e.getKey();
      BufferedImage img = e.getValue();
      if (img == null) continue;

      int x = (int) Math.round(v.getX());
      int y = (int) Math.round(v.getY());
      g.drawImage(img, x, y, null);
    }
  }
}
