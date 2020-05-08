import java.awt.*;
import java.util.Random;

public class Ball {
    private int xDirection, yDirection, yDirectionStart;
    private int[] pixels;
    private Rectangle boundingBox;
    private int width, height;
    private int timeSinceBounce, acceleration;
    public boolean stopGame;

    public Ball (int x, int y, int width, int height, int col) {
        this.width = width;
        this.height = height;

        pixels = new int[height*width];

        int[] xCoordinates = new int[width];
        int[] yCoordinates = new int[height];

        makeCoordinateSystem(width, height, xCoordinates, yCoordinates);

        circlifySprite(width, height, xCoordinates, yCoordinates);

        boundingBox = new Rectangle(x, y, width, height);

        Random r = new Random();
        int rDir = r.nextInt(1);
        if (rDir == 0)
            rDir--;
        setXDirection(rDir);

        timeSinceBounce = 0;
        acceleration = 1;
        yDirectionStart = 1;

        stopGame = false;
    }

    public void draw(int[] Screen, int screenWidth) {
        for (int i = 0 ; i < height ; i++) {
            for (int j = 0 ; j < width ; j++) {
                Screen[(boundingBox.y+i)*screenWidth + boundingBox.x+j] = pixels[i*width+j];
            }
        }
    }

    public void collision(Rectangle r){
        if(boundingBox.intersects(r)) {
            System.out.println(r.y + "   ,   " + (boundingBox.y+boundingBox.height));
            if (getXDirection() > 0 && Math.abs(r.x - (boundingBox.x + boundingBox.width)) <= getXDirection()) {
                setXDirection(-1);
            } else if (getXDirection() < 0 && Math.abs(r.x + r.width - boundingBox.x) <= -getXDirection()) {
                setXDirection(+1);
            } else if (getYDirection() > 0 && Math.abs(r.y - (boundingBox.y + boundingBox.height)) <= getYDirection()) {
                setYDirection(-10);
            } else if (getYDirection() < 0 && Math.abs(r.y + r.height - boundingBox.y) <= -getYDirection()) {
                setYDirection(1);
            }
            timeSinceBounce = 0;
            yDirectionStart = getYDirection();
        }
    }

    public void move(Rectangle r) {
        if (shouldBounce(r)) {
            if (isColliding(r)) {
                boundingBox.y = r.y - boundingBox.height;
                timeSinceBounce = 0;
                setYDirection(-10);
                yDirectionStart = getYDirection();
            } else {
                stopGame();
            }
        }
        yDirection = yDirectionStart + (acceleration * timeSinceBounce);
        //System.out.println(getYDirection());
        //boundingBox.x += xDirection;
        boundingBox.y += yDirection;
        //System.out.println();
        //Bounce the ball when edge is detected
        if (boundingBox.x <= 0) {
            setXDirection(+1);
        }
        if (boundingBox.x >= 385) {
            setXDirection(-1);
        }
        if (boundingBox.y <= 0) setYDirection(+10);
        if (boundingBox.y >= 285) setYDirection(-10);
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

    public void update(Rectangle r) {
        collision(r);
        timeSinceBounce++;
        move(r);
        collision(r);
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

    private void circlifySprite(int width, int height, int[] xCoordinates, int[] yCoordinates) {
        int thisIsATest = 0;
        for (int pixelsY = 0; pixelsY < height; pixelsY++) {
            for (int pixelsX = 0; pixelsX < width; pixelsX++) {
                for (int k = 0; k < 360; k++){
                    if (Math.signum(xCoordinates[pixelsX]) == -1 && Math.signum(yCoordinates[pixelsY]) == 1) {
                        if (xCoordinates[pixelsX] >= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[pixelsY] <= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00000000;
                        }
                    } else if (Math.signum(xCoordinates[pixelsX]) == 1 && Math.signum(yCoordinates[pixelsY]) == 1) {
                        if (xCoordinates[pixelsX] <= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[pixelsY] <= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00000000;
                        }
                    } else if (Math.signum(xCoordinates[pixelsX]) == -1 && Math.signum(yCoordinates[pixelsY]) == -1) {
                        if (xCoordinates[pixelsX] >= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[pixelsY] >= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00000000;
                        }
                    } else if (Math.signum(xCoordinates[pixelsX]) == 1 && Math.signum(yCoordinates[pixelsY]) == -1) {
                        if (xCoordinates[pixelsX] <= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[pixelsY] >= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00000000;
                        }
                    }
                }
                thisIsATest++;
            }
        }
    }

    private void makeCoordinateSystem(int width, int height, int[] xCoordinates, int[] yCoordinates) {
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
