import java.awt.Shape;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)
 */

public class Rectangle extends Shapes {

    public static int EDGES = 4;
    private int height;
    private int width;

    public Rectangle() {
        super(0, 0, "magenta");
        this.height = 30;
        this.width = 40;
    }

    @Override
    public Shape getShape() {
        return new java.awt.Rectangle(xPosition, yPosition, width, height);
    }

    public void changeSize(int newHeight, int newWidth) {
        erase();
        this.height = newHeight;
        this.width = newWidth;
        draw();
    }
}
  