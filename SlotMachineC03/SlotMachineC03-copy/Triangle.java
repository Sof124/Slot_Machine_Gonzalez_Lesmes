import java.awt.Shape;
import java.awt.Polygon;


/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0  (15 July 2000)
 */

public class Triangle extends Shapes {

    public static int VERTICES = 3;
    private int height;
    private int width;

    public Triangle() {
        super(0, 0, "green");
        this.height = 30;
        this.width = 40;
    }

    @Override
    public Shape getShape() {
        int[] xpoints = { xPosition, xPosition + (width / 2), xPosition - (width / 2) };
        int[] ypoints = { yPosition, yPosition + height, yPosition + height };
        return new Polygon(xpoints, ypoints, 3);
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        this.height = newHeight;
        this.width = newWidth;
        draw();
    }
}