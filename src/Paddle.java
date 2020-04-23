import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Paddle {
    private int xDirection;
    private int yDirection;
    private int[] pixels;
    private Rectangle boundingBox;
    private int width;
    private int height;

    public Paddle (int x, int y, int width, int height, int col) {
        this.width = width;
        this.height = height;
        boundingBox = new Rectangle(x, y, width, height);
        pixels = new int[width * height];
        Arrays.fill(pixels, col);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') {
            setXDirection(-1);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') {
            setXDirection(1);
        }
    }

    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a'){
            setXDirection(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd'){
            setXDirection(0);
        }
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void update(int width, int height){
        boundingBox.x += xDirection;
        if(boundingBox.x <= 0) {
            boundingBox.x = 0;
        }
        if(boundingBox.x >= width-boundingBox.width) {
            boundingBox.x = width-boundingBox.width;
        }
    }

    public void draw(int[] Screen, int screenWidth){
        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                Screen[(boundingBox.y+i)*screenWidth + boundingBox.x+j] = pixels[i*width+j];
            }
        }
    }

    public void setXDirection(int xdir) {
        xDirection = xdir;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
