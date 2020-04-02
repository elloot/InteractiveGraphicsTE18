import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * This is a class
 * Created 2020-03-26
 *
 * @author Magnus Silverdal
 */
public class Sprite {
    private int width;
    private int height;
    private int[] pixels;

    public Sprite(int w, int h) {
        this.width = w;
        this.height = h;
        pixels = new int[w*h];
        for (int i = 0 ; i < pixels.length ; i++) {
            pixels[i] = 0xFFFFFF;
        }
    }

    public Sprite(int w, int h, int col) {
        this.width = w;
        this.height = h;
        pixels = new int[w*h];
        for (int i = 0 ; i < pixels.length ; i++) {
            pixels[i] = col;
        }
    }

    public Sprite(String path) {
        BufferedImage image = null;
        try {
            BufferedImage rawImage = ImageIO.read(new File(path));
            // Since the type of image is unknown it must be copied into an INT_RGB
            image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(rawImage, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public Sprite(int r) {
        this.height = r;
        this.width = r;
        pixels = new int[height*width];

        int thisIsATest = 0;

        int[] xCoordinates = new int[width];
        int[] yCoordinates = new int[height];

        for (int i = 0; i < xCoordinates.length; i ++) {
            if (i < width/2) {
                xCoordinates[i] = ((xCoordinates.length/2 - i) * -1);
                yCoordinates[i] = ((yCoordinates.length/2 - i) * -1);
            } else {
                xCoordinates[i] = i - ((width/2)-1);
                yCoordinates[i] = i - ((height/2)-1);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int k = 0; k < 360; k++){
                    if (Math.signum(xCoordinates[x]) == -1 && Math.signum(yCoordinates[y]) == 1) {
                        if (xCoordinates[x] >= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[y] <= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00FFFFFF;
                        }
                    } else if (Math.signum(xCoordinates[x]) == 1 && Math.signum(yCoordinates[y]) == 1) {
                        if (xCoordinates[x] <= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[y] <= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00FFFFFF;
                        }
                    } else if (Math.signum(xCoordinates[x]) == -1 && Math.signum(yCoordinates[y]) == -1) {
                        if (xCoordinates[x] >= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[y] >= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00FFFFFF;
                        }
                    } else if (Math.signum(xCoordinates[x]) == 1 && Math.signum(yCoordinates[y]) == -1) {
                        if (xCoordinates[x] <= (Math.cos(Math.toRadians(k))*(width/2)) && yCoordinates[y] >= (Math.sin(Math.toRadians(k))*(height/2))) {
                            pixels[thisIsATest] = 0xFFFF00FF;
                            break;
                        } else {
                            pixels[thisIsATest] = 0x00FFFFFF;
                        }
                    }
                }
                thisIsATest++;
            }
        }
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

    public void setColor(int color) {
        for (int i = 0 ; i < pixels.length ; i++) {
            pixels[i] = color;
        }
    }
}