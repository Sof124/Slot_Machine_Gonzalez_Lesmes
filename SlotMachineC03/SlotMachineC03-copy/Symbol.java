import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa un símbolo de la máquina tragamonedas desacoplado de las subclases concretas.
 *
 * @author GonzalezM-LesmesA
 * @version 3.0
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

    /** Referencia polimórfica a la figura geométrica única del símbolo. */
    private Shapes shape;

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
     * Instancia de forma dinámica para evitar acoplamiento UML directo en BlueJ.
     *
     * @param x posición horizontal
     * @param y posición vertical
     * @param size tamaño de la figura
     */
    public void display(int x, int y, int size) {
        hide(); // Oculta y limpia cualquier figura previa

        int targetX = x;
        String className = "Circle"; // Clase base por defecto

        if ("rectangle".equals(shapeType) || "square".equals(shapeType)) {
            className = "Rectangle";
        } else if ("triangle".equals(shapeType)) {
            className = "Triangle";
            targetX = x + size / 2;
        }

        try {
            // Instanciación dinámica: BlueJ no detecta la dependencia directa
            shape = (Shapes) Class.forName(className).getDeclaredConstructor().newInstance();

            // Ajuste de tamaño específico según tipo usando reflexión para evitar tipos concretos
            if ("Circle".equals(className)) {
                shape.getClass().getMethod("changeSize", int.class).invoke(shape, size);
            } else {
                shape.getClass().getMethod("changeSize", int.class, int.class).invoke(shape, size, size);
            }

            // Configuración genérica de Shapes
            shape.changeColor(color);
            shape.moveHorizontal(targetX);
            shape.moveVertical(y);
            shape.makeVisible();

        } catch (Exception e) {
            System.err.println("Error instanciando la forma geométrica: " + e.getMessage());
        }
    }

    /** Oculta y destruye la figura del símbolo. */
    public void hide() {
        if (shape != null) {
            shape.makeInvisible();
            shape = null;
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