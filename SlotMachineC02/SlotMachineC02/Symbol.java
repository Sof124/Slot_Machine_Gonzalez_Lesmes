import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa un símbolo de la máquina tragamonedas.
 *
 * @author GonzalezM-LesmesA
 * @version 1.0
 */
public class Symbol {

    /** Nombres de colores CSS reconocidos por el simulador. */
    private static final Set<String> VALID_COLORS = new HashSet<>(Arrays.asList(
            "black", "white", "red", "green", "blue", "yellow", "cyan", "magenta",
            "gray", "grey", "orange", "purple", "pink", "brown", "lime", "navy",
            "teal", "maroon", "olive", "silver", "gold", "indigo", "violet",
            "turquoise", "coral", "salmon", "khaki", "orchid", "crimson", "chocolate"
    ));

    /** Color del símbolo. */
    private String color;

    /** Tipo de figura geométrica del símbolo. */
    private String shapeType;

    /** Figura circular del símbolo. */
    private Circle circleShape;

    /** Figura rectangular del símbolo. */
    private Rectangle rectShape;

    /** Figura triangular del símbolo. */
    private Triangle triangleShape;

    /**
     * Crea un símbolo asociando automáticamente una figura a un color.
     *
     * @param color color CSS del símbolo
     */
    public Symbol(String color) {
        this.color = color.toLowerCase();
        this.shapeType = assignShapeByColor(this.color);
    }

    /**
     * Crea un símbolo con un color y una figura específicos.
     *
     * @param color color CSS del símbolo
     * @param shapeType figura geométrica asociada al símbolo
     */
    public Symbol(String color, String shapeType) {
        this.color = color.toLowerCase();
        this.shapeType = shapeType.toLowerCase();
    }

    /**
     * Indica si un color es válido.
     *
     * @param color color que se quiere validar
     * @return true si el color es válido
     */
    public static boolean isValidColor(String color) {
        return color != null && VALID_COLORS.contains(color.toLowerCase());
    }

    /**
     * Devuelve el color del símbolo.
     *
     * @return color del símbolo
     */
    public String getColor() {
        return color;
    }

    /**
     * Devuelve el tipo de figura geométrica del símbolo.
     *
     * @return tipo de figura geométrica
     */
    public String getShapeType() {
        return shapeType;
    }

    /**
     * Muestra la figura del símbolo en la posición y tamaño indicados.
     *
     * @param x posición horizontal
     * @param y posición vertical
     * @param size tamaño de la figura
     */
    public void display(int x, int y, int size) {
        hide();

        if ("rectangle".equals(shapeType) || "square".equals(shapeType)) {
            rectShape = new Rectangle();
            rectShape.changeSize(size, size);
            rectShape.changeColor(color);
            rectShape.moveHorizontal(x);
            rectShape.moveVertical(y);
            rectShape.makeVisible();
        } else if ("triangle".equals(shapeType)) {
            triangleShape = new Triangle();
            triangleShape.changeSize(size, size);
            triangleShape.changeColor(color);
            triangleShape.moveHorizontal(x + size / 2);
            triangleShape.moveVertical(y);
            triangleShape.makeVisible();
        } else {
            circleShape = new Circle();
            circleShape.changeSize(size);
            circleShape.changeColor(color);
            circleShape.moveHorizontal(x);
            circleShape.moveVertical(y);
            circleShape.makeVisible();
        }
    }

    /** Oculta la figura del símbolo. */
    public void hide() {
        if (circleShape != null) {
            circleShape.makeInvisible();
            circleShape = null;
        }

        if (rectShape != null) {
            rectShape.makeInvisible();
            rectShape = null;
        }

        if (triangleShape != null) {
            triangleShape.makeInvisible();
            triangleShape = null;
        }
    }

    /**
     * Asigna una figura geométrica según el color.
     *
     * @param color color del símbolo
     * @return figura geométrica asignada
     */
    private String assignShapeByColor(String color) {
        switch (color) {
            case "blue":
            case "cyan":
                return "rectangle";
            case "yellow":
            case "green":
                return "triangle";
            default:
                return "circle";
        }
    }
}

