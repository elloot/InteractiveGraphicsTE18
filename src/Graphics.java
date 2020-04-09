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
    private Sprite square1;
    //private Sprite square2;

    private double t=0;
    private int xSquare1 = 0;
    private int ySquare1 = 0;
    private double vxSquare1 = 0;
    private double vySquare1 = 0;

    private int timeSinceBounce = 0;
    private double acceleration = 1;
    private double vySquare1Start = 0;

    private int xSquare2 = 100;
    private int ySquare2 = 100;

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
        this.addMouseMotionListener(new MySecondMouseListener());
        this.requestFocus();

        //s = new Sprite("sprite.png");
        //square1 = new Sprite(16,16,0xFF00FF);
        square1 = new Sprite(50, 50);
        this.xSquare1 = width/2 - (square1.getWidth()/2);
        this.vxSquare1 = 3*Math.random();
        if (Math.random() < 0.5) {
            this.vxSquare1 *= -1;
        }
        //square2 = new Sprite(32,8,0x00FF00);
    }

    private void draw() {
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
        Arrays.fill(pixels, 0xFF000000);
        // The mario sprite

        /* Parametric curve (a circle) see https://en.wikipedia.org/wiki/Parametric_equation
           t controls the coordinates as (x(t),y(t)). Here t is increased by 2 degrees (pi/180 rad)
           each timestep.
        */

        //int x = (int)(width/2+(width/2-s.getWidth())*Math.sin(t));
        //int y = (int)(height/2+(height/2-s.getHeight())*Math.cos(t));

        /*for (int i = 0 ; i < s.getHeight() ; i++) {
            for (int j = 0 ; j < s.getWidth() ; j++) {
                pixels[(y+i)*width + x+j] = s.getPixels()[i*s.getWidth()+j];
            }
        }*/

        //change y-velocity with acceleration and time
        vySquare1 = vySquare1Start + (acceleration* timeSinceBounce);
        timeSinceBounce++;

        // The moving magenta square
        if (xSquare1 + vxSquare1 < 0 || xSquare1 + vxSquare1 > width - square1.getWidth())
            vxSquare1 = 0;
        if (ySquare1 + vySquare1 < 0 || ySquare1 + vySquare1 > height - square1.getHeight())
            vySquare1 = 0;

        xSquare1 += vxSquare1;
        ySquare1 += vySquare1;

        for (int i = 0 ; i < square1.getHeight() ; i++) {
            for (int j = 0 ; j < square1.getWidth() ; j++) {
                pixels[(ySquare1+i)*width + xSquare1+j] = square1.getPixels()[i*square1.getWidth()+j];
            }
        }

        // The mouse-controlled square
        /*for (int i = 0 ; i < square2.getHeight() ; i++) {
            for (int j = 0 ; j < square2.getWidth() ; j++) {
                pixels[(ySquare2+i)*width + xSquare2+j] = square2.getPixels()[i*square2.getWidth()+j];
            }
        }*/

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
            /*if (keyEvent.getKeyChar()=='a') {
                vxSquare1 = -5;
            } else if (keyEvent.getKeyChar()=='d') {
                vxSquare1 = 5;
            } else if (keyEvent.getKeyChar()=='w') {
                vySquare1 = -5;
            } else if (keyEvent.getKeyChar()=='s') {
                vySquare1 = 5;
            }*/
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            /*if (keyEvent.getKeyChar()=='a' || keyEvent.getKeyChar()=='d') {
                vxSquare1 = 0;
            } else if (keyEvent.getKeyChar()=='w' || keyEvent.getKeyChar()=='s') {
                vySquare1 = 0;
            }*/
        }
    }

    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            vySquare1 = -10;
            vySquare1Start = vySquare1;
            timeSinceBounce = 0;
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            /*square2.setColor(0x00FF00);*/
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            /*square2.setColor(0xFF0000);*/
        }
    }

    private class MySecondMouseListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            /*if (e.getX() <= width*scale && e.getY() <= height*scale) {
                xSquare1 = e.getX()/scale-(square1.getWidth()/2);
                ySquare1 = e.getY()/scale-(square1.getHeight()/2);
            }*/
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}