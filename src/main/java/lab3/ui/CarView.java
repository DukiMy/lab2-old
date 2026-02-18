package lab3.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.CYAN;
import static java.awt.Color.RED;
import static java.awt.Color.GREEN;

public final class CarView extends JFrame {

  private static final Dimension FRAME_DIM = new Dimension(800, 800);
  private static final Dimension D_PANEL_DIM = new Dimension((int) FRAME_DIM.getWidth(), (int) FRAME_DIM.getHeight() - 240);
  private static final Dimension RIGHT_C_PANEL_DIM = new Dimension((int) (FRAME_DIM.getWidth() / 2) + 4, 200);
  private static final Dimension LEFT_C_PANEL_DIM = new Dimension((int) (FRAME_DIM.getWidth() / 5) - 15, 200);

  final CarController carC;

  final DrawPanel drawPanel = new DrawPanel(D_PANEL_DIM);

  final JPanel controlPanel = new JPanel();

  final JLabel gasLabel = new JLabel("Amount of gas");
  final JPanel gasPanel = new JPanel();

  final JButton gasButton = new JButton("Gas");
  final JButton brakeButton = new JButton("Brake");
  final JButton turboOnButton = new JButton("Saab Turbo on");
  final JButton turboOffButton = new JButton("Saab Turbo off");
  final JButton liftBedButton = new JButton("Scania Lift Bed");
  final JButton lowerBedButton = new JButton("Lower Lift Bed");
  final JButton startButton = new JButton("Start all cars");
  final JButton stopButton = new JButton("Stop all cars");
  final JSpinner gasSpinner = new JSpinner(new SpinnerNumberModel(
    /* Startvärde */ 0,
    /* Minsta värde */0,
    /* Högsta värde */100,
    /* Steglängd */ 1
  ));


  int gasAmount = 0;

  public CarView(String framename, CarController cc) {
    this.carC = cc;
    initComponents(framename);
  }

  private void initComponents(final String title) {

    setTitle(title);
    setPreferredSize(FRAME_DIM);
    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    add(drawPanel);
    gasSpinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        gasAmount = (int) ((JSpinner) e.getSource()).getValue();
      }
    });

    gasPanel.setLayout(new GridLayout(2, 1));
    gasPanel.add(gasLabel, BorderLayout.PAGE_START);
    gasPanel.add(gasSpinner, BorderLayout.PAGE_END);
    add(gasPanel);

    controlPanel.setLayout(new GridLayout(2, 4));
    controlPanel.add(gasButton, 0);
    controlPanel.add(turboOnButton, 1);
    controlPanel.add(liftBedButton, 2);
    controlPanel.add(brakeButton, 3);
    controlPanel.add(turboOffButton, 4);
    controlPanel.add(lowerBedButton, 5);
    controlPanel.setPreferredSize(RIGHT_C_PANEL_DIM);
    controlPanel.setBackground(CYAN);
    add(controlPanel);

    startButton.setBackground(BLUE);
    startButton.setForeground(GREEN);
    startButton.setPreferredSize(LEFT_C_PANEL_DIM);
    add(startButton);

    stopButton.setBackground(RED);
    stopButton.setForeground(BLACK);
    stopButton.setPreferredSize(LEFT_C_PANEL_DIM);
    add(stopButton);

    gasButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.gas(gasAmount); }
    });

    brakeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.brake(gasAmount); }
    });

    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.startAll(); }
    });

    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.stopAll(); }
    });

    turboOnButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.saabTurboOn(); }
    });

    turboOffButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.saabTurboOff(); }
    });

    liftBedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.scaniaLiftBed(); }
    });

    lowerBedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { carC.scaniaLowerBed(); }
    });

    pack();

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(
      dim.width / 2 - getSize().width / 2,
      dim.height / 2 - getSize().height / 2
    );

    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
