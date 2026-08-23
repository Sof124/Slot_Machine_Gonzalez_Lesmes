/**
 * Representa un símbolo definido por su color CSS y figura geométrica.
 * 
 * @author GonzalezM-LesmesA
 */
public class Symbol {
    
    private String color;
    private String shapeType;

    /**
     * Crea un símbolo asociando automáticamente una figura a un color 
     */
    public Symbol(String color) {
        this.color = color.toLowerCase();
        this.shapeType = assignShapeByColor(this.color);
    }

    /**
     * Crea un símbolo con un color y una figura específica
     */
    public Symbol(String color, String shapeType) {
        this.color = color.toLowerCase();
        this.shapeType = shapeType.toLowerCase();
    }

    public String getColor() {
        return color;
    }

    public String getShapeType() {
        return shapeType;
    }

    /**
     * Mapea un color a una figura por defecto para su representación
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