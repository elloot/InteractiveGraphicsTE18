import java.awt.*;

public class Ball {
    private int xDirection, yDirection;
    private int[] pixels;
    private Rectangle boundingBox;
    private int width, height;

    public Ball (int x, int y, int width, int height, int col) {
        this.width = width;
        this.height = height;

        pixels = new int[height*width];

        int[] xCoordinates = new int[width];
        int[] yCoordinates = new int[height];

        makeCoordinateSystem(width, height, xCoordinates, yCoordinates);

        circlifySprite(width, height, xCoordinates, yCoordinates);
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
