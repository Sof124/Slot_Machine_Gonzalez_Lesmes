import java.awt.Shape;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000) 
 */



public class Circle extends Shapes {

    public static final double PI = 3.1416;
    private int diameter;

    public Circle() {
        super(0, 0, "blue");
        this.diameter = 30;
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Double(xPosition, yPosition, diameter, diameter);
    }

    public void changeSize(int newDiameter) {
        erase();
        this.diameter = newDiameter;
        draw();
    }
}