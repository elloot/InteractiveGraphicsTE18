import java.awt.*;
import java.util.Random;

public class Ball {
    private int xDirection, yDirection, yDirectionStart;
    private final int[] pixels;
    private final Rectangle boundingBox;
    private final int width;
    private final int height;
    private int timeSinceBounce;
    private final int acceleration;
    public boolean stopGame;
    private final int[] xCoordinates;
    private final int[] yCoordinates;

    public Ball (int x, int y, int width, int height, int col) {
        this.width = width;
        this.height = height;

        pixels = new int[height*width];

        this.xCoordinates = new int [width];
        this.yCoordinates = new int[height];

        makeCoordinateSystem();
        makeCircle();

        boundingBox = new Rectangle(x, y, width, height);

        Random r = new Random();
        int rDir = r.nextInt(1);
        if (rDir == 0) rDir--;
        setXDirection(rDir);

        timeSinceBounce = 0;
        acceleration = 1;
        yDirectionStart = 1;

        stopGame = false;
    }

    public void draw(int[] Screen, int screenWidth) {
        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                //if pixel isn't transparent, draw it, else don't draw it
                if ((pixels[i*width+j]>>24) != 0x00) {
                    Screen[(boundingBox.y + i) * screenWidth + boundingBox.x + j] = pixels[i * width + j];
                }
            }
        }
    }

    public void move(Rectangle r, int screenWidth, int screenHeight) {
        if (shouldBounce(r)) {
            if (isColliding(r)) {
                setXDirection(paddleXBounce(r));

                boundingBox.y = r.y - boundingBox.height;
                timeSinceBounce = 0;
                setYDirection(-18);
                yDirectionStart = getYDirection();
            } else {
                stopGame();
            }
        }
        yDirection = yDirectionStart + (acceleration * timeSinceBounce);

        if (boundingBox.x + xDirection <= 0) {
            boundingBox.x = 0;
            setXDirection((int)(-0.6*xDirection));
        }
        if (boundingBox.x + boundingBox.width + xDirection >= screenWidth) {
            boundingBox.x = screenWidth - boundingBox.width;
            setXDirection((int)(-0.6*xDirection));
        }

        boundingBox.x += xDirection;
        boundingBox.y += yDirection;
    }

    private int paddleXBounce(Rectangle r) {
        // returns an x-velocity depending on how the ball hits the paddle
        return (boundingBox.x + boundingBox.width/2 - r.x - r.width/2)/(boundingBox.width/16);
    }

    private boolean isColliding(Rectangle r) {
        return boundingBox.x + boundingBox.width >= r.x && boundingBox.x <= r.x + r.width;
    }

    private boolean shouldBounce(Rectangle r) {
        return boundingBox.y + boundingBox.height + yDirection >= r.y;
    }

    private void stopGame() {
        stopGame = true;
    }

    public void update(Rectangle r, int screenWidth, int screenHeight) {
        timeSinceBounce++;
        move(r, screenWidth, screenHeight);
    }

    public void setXDirection(int xDir) {
        xDirection = xDir;
    }

    public void setYDirection(int yDir) {
        yDirection = yDir;
    }

    public int getXDirection() {
        return xDirection;
    }

    public int getYDirection() {
        return  yDirection;
    }

    private void makeCircle() {
        int radius = width/2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (xCoordinates[x]*xCoordinates[x] + yCoordinates[y]*yCoordinates[y] <= radius*radius) {
                    pixels[y*width + x] = 0xFFFF00FF;
                } else {
                    pixels[y*width + x] = 0x00000000;
                }
            }
        }
    }

    private void makeCoordinateSystem() {
        for (int i = 0; i < xCoordinates.length; i ++) {
            if (i < width/2) {
                xCoordinates[i] = ((xCoordinates.length/2 - i) * -1);
                yCoordinates[i] = ((yCoordinates.length/2 - i) * -1);
            } else {
                xCoordinates[i] = i - ((width/2)-1);
                yCoordinates[i] = i - ((height/2)-1);
            }
        }
    }
}
