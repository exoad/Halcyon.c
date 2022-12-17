package com.test;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.Timer;

public class FadingPanel extends JComponent implements MouseListener, ActionListener {

    // The current alpha level of the panel (0.0f is completely transparent, 1.0f is completely opaque)
    private float alpha = 1.0f;

    // The amount to subtract from the alpha level each time the timer fires
    private float fadeStep;

    // The initial delay before the fading begins
    private long initialDelay;

    // The timer responsible for fading the panel
    private Timer timer;

    public FadingPanel(float fadeStep, long initialDelay) {
        this.fadeStep = fadeStep;
        this.initialDelay = initialDelay;

        // Add a mouse listener to the panel to pause the fading when the mouse enters and resume it when the mouse exits
        addMouseListener(this);

        // Turn off double buffering for the panel
        setDoubleBuffered(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the alpha level for the panel
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alphaComposite);
    }

    public void startFading() {
        // Create a new timer to fade the panel
        timer = new Timer(50, this); // The delay between timer fires is 50 milliseconds

        // Schedule the timer to fire repeatedly with the specified delay
        timer.setInitialDelay((int) initialDelay);
        timer.start();
    }

    // Pause the fading when the mouse enters the panel
    @Override
    public void mouseEntered(MouseEvent e) {
        timer.stop();
    }

    // Resume the fading when the mouse exits the panel
    @Override
    public void mouseExited(MouseEvent e) {
        timer.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Decrease the alpha level of the panel by the fade step
        alpha -= fadeStep;

        // Repaint the panel with the new alpha level
        repaint();

        // If the alpha level has reached 0, stop the timer and dispose of the panel
        if (alpha <= 0) {
            timer.stop();
            setVisible(false);
        }
    }
}

