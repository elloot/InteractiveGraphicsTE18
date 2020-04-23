import java.awt.*;
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
