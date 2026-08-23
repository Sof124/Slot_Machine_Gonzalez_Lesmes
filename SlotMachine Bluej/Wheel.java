import java.util.ArrayList;
import java.util.List;

/**
 * Representa una rueda con una secuencia circular de símbolos
 * 
 * @author GonzalezM-LesmesA
 */
public class Wheel {
    
    private List<Symbol> symbols;
    private int visibleIndex;

    /**
     * Inicializa la rueda vacía
     */
    public Wheel() {
        this.symbols = new ArrayList<>();
        this.visibleIndex = 0;
    }

    /**
     * Agrega un símbolo por color según la posición indicada
     */
    public void addSymbol(int pos, String color) {
        int index = adjustIndex(pos);
        symbols.add(index, new Symbol(color));
    }

    /**
     * Agrega un símbolo con color y figura en la posición indicada
     */
    public void addSymbol(int pos, String color, String shapeType) {
        int index = adjustIndex(pos);
        symbols.add(index, new Symbol(color, shapeType));
    }

    /**
     * Elimina el primer símbolo que coincida con el color
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
     * Ubica la rueda en la posición del símbolo del color especificado
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
     * Avanza secuencialmente un paso en la rueda
     */
    public void spin() {
        if (!symbols.isEmpty()) {
            visibleIndex = (visibleIndex + 1) % symbols.size();
        }
    }

    /**
     * Retorna el símbolo que se muestra actualmente en pantalla
     */
    public Symbol getVisibleSymbol() {
        if (symbols.isEmpty()) return null;
        return symbols.get(visibleIndex);
    }

    /**
     * Obtiene la lista completa de símbolos
     */
    public List<Symbol> getSymbols() {
        return symbols;
    }

    private int adjustIndex(int pos) {
        if (pos < 1) return 0;
        if (pos > symbols.size()) return symbols.size();
        return pos - 1;
    }

    private void adjustVisibleIndex() {
        if (visibleIndex >= symbols.size() && !symbols.isEmpty()) {
            visibleIndex = 0;
        }
    }
}