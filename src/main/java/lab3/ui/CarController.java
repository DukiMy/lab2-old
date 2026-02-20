package lab3.ui;

import lab3.Vehicle;
import lab3.Volvo240;
import lab3.Saab95;
import lab3.Scania;
import lab3.Garage;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CarController {

  private static final int DELAY_MS = 50; // 20 Hz
  private final Timer timer = new Timer(DELAY_MS, new TimerListener());

  CarView frame;

  private final List<Vehicle> cars = new ArrayList<>();

  private final Volvo240 volvo = new Volvo240();
  private final Saab95 saab = new Saab95();
  private final Scania scania = new Scania();

  private final Garage<Volvo240> volvoWorkshop =
    new Garage<>(Volvo240.class, new Point2D.Double(0, 800 - 340), 10);

  private class TimerListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent e) {

      for (int i = 0; i < cars.size(); i++) {
        Vehicle car = cars.get(i);

        car.move();

        bounceIfNeeded(car);

        if (car instanceof Volvo240) {
          if (hitsWorkshop(car)) {
            boolean loaded = volvoWorkshop.load();
            if (loaded) {
              frame.drawPanel.removeVehicle(car);
              cars.remove(i);
              i--;
              continue;
            }
          }
        }
      }

      frame.drawPanel.repaint();
    }
  }

  // TODO: Flyttas till domänmodell
  private boolean hitsWorkshop(final Vehicle car) {
    double dx = car.getX() - volvoWorkshop.getX();
    double dy = car.getY() - volvoWorkshop.getY();
    return (dx*dx + dy*dy) <= 10.0*10.0; // samma som LOAD_RADIUS
  }

  // TODO: Flyttas till domänmodell
  private void bounceIfNeeded(final Vehicle car) {
    BufferedImage img = frame.drawPanel.getImage(car);
    if (img == null) return;

    int panelW = frame.drawPanel.getWidth();
    int panelH = frame.drawPanel.getHeight();

    double x = car.getX();
    double y = car.getY();
    double w = img.getWidth();
    double h = img.getHeight();

    boolean hitWall =
      (x < 0) || (y < 0) ||
      (x + w > panelW) || (y + h > panelH);

    if (!hitWall) return;

    car.stopEngine();

    // clampa in så bilen inte fastnar "utanför"
    double cx = clamp(x, 0, Math.max(0, panelW - w));
    double cy = clamp(y, 0, Math.max(0, panelH - h));
    car.mutatePoint(cx, cy);

    car.invertDirection();
    car.startEngine();
  }

  // TODO: Flyttas till domänmodell
  private static double clamp(final double v, final double min, final double max) {
    if (v < min) return min;
    if (v > max) return max;
    return v;
  }

  void gas(int amount) {
    double gas = amount / 100.0d;
    for (Vehicle car : cars) car.gas(gas);
  }

  void brake(final int amount) {
    double brake = amount / 100.0d;
    for (Vehicle car : cars) car.brake(brake);
  }

  void startAll() { for (Vehicle car : cars) car.startEngine(); }

  void stopAll() { for (Vehicle car : cars) car.stopEngine(); }

  void saabTurboOn() { saab.setTurbo(true); }

  void saabTurboOff() { saab.setTurbo(false); }

  void scaniaLiftBed() { scania.setTipBedAngle((byte) 1); }

  void scaniaLowerBed() { scania.setTipBedAngle((byte) 0); }

  public static void main() {
    CarController cc = new CarController();

    cc.volvo.mutatePoint(0, 0);
    cc.saab.mutatePoint(0, 100);
    cc.scania.mutatePoint(0, 200);

    cc.cars.add(cc.volvo);
    cc.cars.add(cc.saab);
    cc.cars.add(cc.scania);

    cc.frame = new CarView("CarSim 1.0", cc);

    try {
      BufferedImage volvoImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Volvo240.jpg")
      );
      BufferedImage saabImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Saab95.jpg")
      );
      BufferedImage scaniaImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Scania.jpg")
      );

      cc.frame.drawPanel.addVehicle(cc.volvo, volvoImg);
      cc.frame.drawPanel.addVehicle(cc.saab, saabImg);
      cc.frame.drawPanel.addVehicle(cc.scania, scaniaImg);

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    cc.timer.start();
  }
}
