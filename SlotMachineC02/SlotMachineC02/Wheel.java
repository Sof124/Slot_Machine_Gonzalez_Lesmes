import java.util.ArrayList;
import java.util.List;

/**
 * Representa una rueda con una secuencia circular de símbolos.
 *
 * @author GonzalezM-LesmesA
 * @version 1.5
 */
public class Wheel {

    /** Posición vertical de la ventana de la rueda. */
    private static final int WINDOW_Y = 55;

    /** Posición vertical del símbolo dentro de la ventana. */
    private static final int SYMBOL_Y = 90;

    /** Lista de símbolos de la rueda. */
    private List<Symbol> symbols;

    /** Índice del símbolo actualmente visible. */
    private int visibleIndex;

    /** Indica si la rueda está fija. */
    private boolean locked;

    /** Ventana visual de la rueda. */
    private Rectangle window;

    /**
     * Crea una rueda vacía y libre.
     */
    public Wheel() {
        this.symbols = new ArrayList<>();
        this.visibleIndex = 0;
        this.locked = false;
    }

    /**
     * Agrega un símbolo en la posición indicada.
     *
     * @param pos posición del símbolo
     * @param color color del símbolo
     * @return true si el símbolo fue agregado
     */
    public boolean addSymbol(int pos, String color) {
        if (!Symbol.isValidColor(color)) {
            return false;
        }

        int index = adjustIndex(pos);
        symbols.add(index, new Symbol(color));
        return true;
    }

    /**
     * Agrega un símbolo con color y figura específicos.
     *
     * @param pos posición del símbolo
     * @param color color del símbolo
     * @param shapeType figura geométrica del símbolo
     * @return true si el símbolo fue agregado
     */
    public boolean addSymbol(int pos, String color, String shapeType) {
        if (!Symbol.isValidColor(color)) {
            return false;
        }

        int index = adjustIndex(pos);
        symbols.add(index, new Symbol(color, shapeType));
        return true;
    }

    /**
     * Elimina el primer símbolo del color indicado.
     *
     * @param color color del símbolo a eliminar
     * @return true si se encontró y eliminó el símbolo
     */
    public boolean delSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equalsIgnoreCase(color)) {
                symbols.remove(i);
                adjustVisibleIndex();
                return true;
            }
        }

        return false;
    }

    /**
     * Coloca como visible el primer símbolo del color indicado.
     *
     * @param color color del símbolo
     * @return true si se encontró y ubicó el símbolo
     */
    public boolean placeSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equalsIgnoreCase(color)) {
                visibleIndex = i;
                return true;
            }
        }

        return false;
    }

    /**
     * Gira la rueda un paso hacia adelante.
     *
     * @return true si la rueda giró
     */
    public boolean spin() {
        if (locked || symbols.isEmpty()) {
            return false;
        }

        visibleIndex = (visibleIndex + 1) % symbols.size();
        return true;
    }

    /**
     * Fija la rueda para impedir que gire.
     *
     * @return true si la rueda no estaba fija
     */
    public boolean lock() {
        if (locked) {
            return false;
        }

        locked = true;
        return true;
    }

    /**
     * Libera la rueda previamente fijada.
     *
     * @return true si la rueda estaba fija
     */
    public boolean unlock() {
        if (!locked) {
            return false;
        }

        locked = false;
        return true;
    }

    /**
     * Indica si la rueda está fija.
     *
     * @return true si la rueda está fija
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Devuelve los colores de los símbolos de la rueda.
     *
     * @return arreglo con los colores
     */
    public String[] colors() {
        String[] result = new String[symbols.size()];

        for (int i = 0; i < symbols.size(); i++) {
            result[i] = symbols.get(i).getColor();
        }

        return result;
    }

    /**
     * Devuelve el color del símbolo actualmente visible.
     *
     * @return color visible o cadena vacía si no hay símbolos
     */
    public String visibleColor() {
        Symbol visible = currentSymbol();
        return (visible != null) ? visible.getColor() : "";
    }

    /**
     * Muestra la rueda en la posición y ancho indicados.
     *
     * @param x posición horizontal de la rueda
     * @param width ancho de la rueda
     */
    public void display(int x, int width) {
        hide();

        window = new Rectangle();
        window.changeSize(110, width);
        window.changeColor(locked ? "gray" : "white");
        window.moveHorizontal(x);
        window.moveVertical(WINDOW_Y);
        window.makeVisible();

        Symbol visible = currentSymbol();

        if (visible != null) {
            int size = Math.max(8, width - 12);
            visible.display(x + 6, SYMBOL_Y, size);
        }
    }

    /** Oculta la ventana de la rueda y su símbolo visible. */
    public void hide() {
        if (window != null) {
            window.makeInvisible();
            window = null;
        }

        Symbol visible = currentSymbol();

        if (visible != null) {
            visible.hide();
        }
    }

    /**
     * Devuelve el símbolo actualmente visible.
     *
     * @return símbolo visible o null si la rueda está vacía
     */
    private Symbol currentSymbol() {
        if (symbols.isEmpty()) {
            return null;
        }

        return symbols.get(visibleIndex);
    }

    /**
     * Ajusta una posición a un índice válido de la lista.
     *
     * @param pos posición indicada
     * @return índice válido
     */
    private int adjustIndex(int pos) {
        if (pos < 1) {
            return 0;
        }

        if (pos > symbols.size()) {
            return symbols.size();
        }

        return pos - 1;
    }

    /** Corrige el índice visible después de eliminar un símbolo. */
    private void adjustVisibleIndex() {
        if (visibleIndex >= symbols.size() && !symbols.isEmpty()) {
            visibleIndex = 0;
        }
    }
}
