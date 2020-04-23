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
    private String title = "Graphics";
    private int width;
    private int height;

    private JFrame frame;
    private BufferedImage image;
    private int[] pixels;
    private int scale;

    private Thread thread;
    private boolean running = false;
    private int fps = 60;
    private int ups = 30;

    //private Sprite s;
    private Sprite ball;
    //private Sprite paddle;
    private Paddle paddle;
    //private Sprite square2;

    private double t=0;
    private int xBall = 0;
    private int yBall = 0;
    private double vxBall = 0;
    private double vyBall = 0;

    private int timeSinceBounce = 0;
    private double acceleration = 1;
    private double vyBallStart = 0;

    public Graphics(int w, int h, int scale) {
        this.width = w;
        this.height = h;
        this.scale = scale;
        //image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        Dimension size = new Dimension(scale*width, scale*height);
        setPreferredSize(size);
        frame = new JFrame();
        frame.setTitle(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.addKeyListener(new MyKeyListener());
        this.addMouseListener(new MyMouseListener());
        this.requestFocus();

        ball = new Sprite(50, 50);
        this.xBall = width/2 - (ball.getWidth()/2);
        this.vxBall = 3*Math.random();
        if (Math.random() < 0.5) {
            this.vxBall *= -1;
        }

        paddle = new Paddle(100, height-10, 70, 10, 0xFFFFFFFF);
    }

    private void draw() {
        Arrays.fill(pixels, 0xFF000000);
        paddle.draw(pixels, width);
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
        paddle.update(width, height);

        //change y-velocity with acceleration and time
        vyBall = vyBallStart + (acceleration* timeSinceBounce);
        timeSinceBounce++;

        // The moving magenta square
        if (xBall + vxBall < 0 || xBall + vxBall > width - ball.getWidth())
            vxBall = 0;
        if (yBall + vyBall < 0 || yBall + vyBall > height - ball.getHeight())
            vyBall = 0;

        xBall += vxBall;
        yBall += vyBall;

        for (int i = 0; i < ball.getHeight() ; i++) {
            for (int j = 0; j < ball.getWidth() ; j++) {
                pixels[(yBall +i)*width + xBall +j] = ball.getPixels()[i* ball.getWidth()+j];
            }
        }

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

    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            vyBall = -10;
            vyBallStart = vyBall;
            timeSinceBounce = 0;
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    }
}