import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * This is a class
 * Created 2020-03-25
 *
 * @author Magnus Silverdal
 */
public class Graphics extends Canvas implements Runnable {
    private final int width;
    private final int height;

    private final BufferedImage image;
    private final int[] pixels;
    private int scale;

    private Thread thread;
    private boolean running = false;
    private final int fps = 60;
    private final int ups = 30;

    private final Ball ball;
    private final Paddle paddle;

    public Graphics(int w, int h, int scale) {
        this.width = w;
        this.height = h;
        this.scale = scale;

        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        Dimension size = new Dimension(scale*width, scale*height);
        setPreferredSize(size);

        JFrame frame = new JFrame();

        String title = "Graphics";
        frame.setTitle(title);

        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.addKeyListener(new MyKeyListener());
        this.requestFocus();

        ball = new Ball(this.width/2-25, 0, 24, 24, 0xFFFFFFFF);

        paddle = new Paddle(this.width/2-35, height-10, 70, 10, 0xFFFFFFFF);
    }

    private void draw() {
        Arrays.fill(pixels, 0xFF000000);

        paddle.draw(pixels, width);
        ball.draw(pixels, width);

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        java.awt.Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private void update() {
        ball.update(paddle.getBoundingBox(), width, height);
        if (ball.stopGame) {
            stop();
        }
        paddle.update(width, height);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double frameUpdateinteval = 1000000000.0 / fps;
        double stateUpdateinteval = 1000000000.0 / ups;
        double deltaFrame = 0;
        double deltaUpdate = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            deltaFrame += (now - lastTime) / frameUpdateinteval;
            deltaUpdate += (now - lastTime) / stateUpdateinteval;
            lastTime = now;

            while (deltaUpdate >= 1) {
                update();
                deltaUpdate--;
            }

            while (deltaFrame >= 1) {
                draw();
                deltaFrame--;
            }
        }
        stop();
    }

    private class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            paddle.keyPressed(keyEvent);
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            paddle.keyReleased(keyEvent);
        }
    }
}