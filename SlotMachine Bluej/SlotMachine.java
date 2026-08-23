import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * máquina tragamonedas
 * 
 * @author GonzalezM-LesmesA
 * @version 1.0
 */
public class SlotMachine {
    
    private List<Wheel> wheels;
    private boolean isVisible;
    private boolean lastOperationOk;

    // Componentes visuales estáticos 
    private Rectangle frame;
    private Rectangle base;
    private Rectangle leverArm;
    private Circle leverBall;

    // Figuras geométricas 
    private List<Rectangle> wheelWindows;
    private List<Circle> circleShapes;
    private List<Rectangle> rectShapes;
    private List<Triangle> triangleShapes;

    /**
     * Inicializa la máquina tragamonedas con la configuración por defecto
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.lastOperationOk = true;

        this.wheelWindows = new ArrayList<>();
        this.circleShapes = new ArrayList<>();
        this.rectShapes = new ArrayList<>();
        this.triangleShapes = new ArrayList<>();

        initStructure();
        initDefaultConfiguration();
    }

    /**
     * Adiciona una rueda en la posición especificada
     * 
     * @param pos Posición basada en 1
     */
    public void addWheel(int pos) {
        int index = adjustWheelPosition(pos);
        wheels.add(index, new Wheel());
        lastOperationOk = true;
        updateVisuals();
    }

    /**
     * Elimina la rueda ubicada en la posición dada
     * 
     * @param pos Posición basada en 1
     */
    public void delWheel(int pos) {
        if (pos >= 1 && pos <= wheels.size()) {
            wheels.remove(pos - 1);
            lastOperationOk = true;
            updateVisuals();
        } else {
            handleError("La posición de la rueda no existe.");
        }
    }

    /**
     * Adiciona un símbolo por color a todas las ruedas en la posición especificada
     * 
     * @param pos   Posición en la rueda
     * @param color Color del símbolo
     */
    public void addSymbol(int pos, String color) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para adicionar símbolos.");
            return;
        }
        for (Wheel w : wheels) {
            w.addSymbol(pos, color);
        }
        lastOperationOk = true;
        updateVisuals();
    }

    /**
     * Adiciona un símbolo por color a una rueda específica
     * 
     * @param wheel Número de rueda (1 a N)
     * @param pos   Posición en la rueda
     * @param color Color CSS del símbolo
     */
    public void addSymbol(int wheel, int pos, String color) {
        if (wheel >= 1 && wheel <= wheels.size()) {
            wheels.get(wheel - 1).addSymbol(pos, color);
            lastOperationOk = true;
            updateVisuals();
        } else {
            handleError("Número de rueda inválido.");
        }
    }

    /**
     * Elimina la primera ocurrencia del símbolo según su color en todas las ruedas
     * 
     * @param symbol Color del símbolo a eliminar
     */
    public void delSymbol(String symbol) {
        boolean removedAny = false;
        for (Wheel w : wheels) {
            if (w.delSymbol(symbol)) removedAny = true;
        }
        lastOperationOk = removedAny;
        if (!lastOperationOk) {
            handleError("Símbolo no encontrado.");
        } else {
            updateVisuals();
        }
    }

    /**
     * Ubica un símbolo de un color específico en la rueda indicada
     * 
     * @param wheel  Número de rueda (1 a N)
     * @param symbol Color del símbolo
     */
    public void placeSymbol(int wheel, String symbol) {
        if (wheel >= 1 && wheel <= wheels.size()) {
            lastOperationOk = wheels.get(wheel - 1).placeSymbol(symbol);
            if (!lastOperationOk) handleError("Símbolo no encontrado en la rueda.");
            else updateVisuals();
        } else {
            handleError("Número de rueda inválido.");
        }
    }

    /**
     * Gira secuencialmente la rueda especificada un paso adelante
     * 
     * @param wheel Número de rueda (1 a N)
     */
    public void spin(int wheel) {
        if (wheel >= 1 && wheel <= wheels.size()) {
            wheels.get(wheel - 1).spin();
            lastOperationOk = true;
            updateVisuals();
        } else {
            handleError("Número de rueda inválido para girar.");
        }
    }

    /**
     * Gira secuencialmente todas las ruedas de la máquina
     */
    public void spin() {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para girar.");
            return;
        }
        for (Wheel w : wheels) {
            w.spin();
        }
        lastOperationOk = true;
        updateVisuals();
    }

    /**
     * Retorna un arreglo con los colores de los símbolos de la primera rueda
     * 
     * @return Nombres de los colores
     */
    public String[] symbols() {
        if (wheels.isEmpty()) return new String[0];
        List<Symbol> syms = wheels.get(0).getSymbols();
        String[] result = new String[syms.size()];
        for (int i = 0; i < syms.size(); i++) {
            result[i] = syms.get(i).getColor();
        }
        lastOperationOk = true;
        return result;
    }

    /**
     * Retorna el número de símbolos con colores distintos en la primera rueda
     * 
     * @return Cantidad de colores únicos
     */
    public int distinctSymbols() {
        String[] allSyms = symbols();
        Set<String> distinct = new HashSet<>();
        for (String color : allSyms) {
            distinct.add(color.toLowerCase());
        }
        lastOperationOk = true;
        return distinct.size();
    }

    /**
     * Retorna la combinación de colores de los símbolos actualmente visibles
     * 
     * @return Arreglo de colores visibles de izquierda a derecha
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            Symbol s = wheels.get(i).getVisibleSymbol();
            config[i] = (s != null) ? s.getColor() : "";
        }
        lastOperationOk = true;
        return config;
    }

    /**
     * Indica si la combinación actual es ganadora 
     * 
     * @return {@code true} si todas las ruedas muestran el mismo símbolo
     */
    public boolean isjackpot() {
        if (wheels.isEmpty()) return false;
        String[] config = configuration();
        String first = config[0];
        if (first == null || first.isEmpty()) return false;

        for (String color : config) {
            if (!color.equalsIgnoreCase(first)) return false;
        }
        return true;
    }

    /**
     * Hace visible la interfaz del simulador
     */
    public void makeVisible() {
        this.isVisible = true;
        lastOperationOk = true;
        drawMachine();
        updateVisuals();
    }

    /**
     * Oculta la interfaz gráfica del simulador
     */
    public void makeInvisible() {
        this.isVisible = false;
        lastOperationOk = true;
        eraseMachine();
    }

    /**
     * Finaliza la sesión del simulador y limpia sus recursos
     */
    public void exit() {
        makeInvisible();
        wheels.clear();
        lastOperationOk = true;
    }

    /**
     * Consulta si la última operación solicitada fue exitosa
     * 
     * @return {@code true} si la operación se ejecutó correctamente
     */
    public boolean ok() {
        return lastOperationOk;
    }

    //privados//
    /**
     * Crea e inicializa los elementos gráficos estáticos de la máquina (marco, base y palanca)
     */
    private void initStructure() {
        this.frame = new Rectangle();
        this.frame.changeSize(160, 240);
        this.frame.moveHorizontal(30);
        this.frame.moveVertical(30);
        this.frame.changeColor("blue");

        this.base = new Rectangle();
        this.base.changeSize(20, 200);
        this.base.moveHorizontal(50);
        this.base.moveVertical(190);
        this.base.changeColor("black");

        this.leverArm = new Rectangle();
        this.leverArm.changeSize(60, 8);
        this.leverArm.moveHorizontal(270);
        this.leverArm.moveVertical(70);
        this.leverArm.changeColor("black");

        this.leverBall = new Circle();
        this.leverBall.changeSize(22);
        this.leverBall.moveHorizontal(263);
        this.leverBall.moveVertical(55);
        this.leverBall.changeColor("red");
    }

    /**
     * Carga el estado inicial por defecto con 3 ruedas y sus símbolos básicos
     */
    private void initDefaultConfiguration() {
        addWheel(1);
        addWheel(2);
        addWheel(3);

        addSymbol(1, "red");
        addSymbol(2, "blue");
        addSymbol(3, "yellow");
    }

    /**
     * Convierte una posición externa basada en 1 a un índice válido para la lista (basado en 0)
     */
    private int adjustWheelPosition(int pos) {
        if (pos < 1) return 0;
        if (pos > wheels.size()) return wheels.size();
        return pos - 1;
    }

    /**
     * Registra el fallo de la última operación y despliega una alerta gráfica si la GUI está activa
     */
    private void handleError(String message) {
        lastOperationOk = false;
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Hace visibles los componentes estáticos del chasis de la máquina
     */
    private void drawMachine() {
        if (!isVisible) return;
        frame.makeVisible();
        base.makeVisible();
        leverArm.makeVisible();
        leverBall.makeVisible();
    }

    /**
     * Oculta el chasis estático y elimina todas las figuras dibujadas en pantalla
     */
    private void eraseMachine() {
        frame.makeInvisible();
        base.makeInvisible();
        leverArm.makeInvisible();
        leverBall.makeInvisible();
        clearVisuals();
    }

    /**
     * Elimina e invisibiliza las ventanas y figuras geométricas creadas dinámicamente
     */
    private void clearVisuals() {
        for (Rectangle rect : wheelWindows) rect.makeInvisible();
        for (Circle circ : circleShapes) circ.makeInvisible();
        for (Rectangle rect : rectShapes) rect.makeInvisible();
        for (Triangle tri : triangleShapes) tri.makeInvisible();

        wheelWindows.clear();
        circleShapes.clear();
        rectShapes.clear();
        triangleShapes.clear();
    }

    /**
     * Recalcula y redibuja la posición, ventanas y símbolos de las ruedas según el estado actual
     */
    private void updateVisuals() {
        if (!isVisible) return;

        clearVisuals();
        frame.changeColor((isjackpot() && !wheels.isEmpty()) ? "green" : "blue");

        int numWheels = wheels.size();
        if (numWheels == 0) return;

        int availableWidth = 200; 
        int wheelWidth = Math.min(50, (availableWidth / numWheels) - 10);
        int totalWheelsWidth = numWheels * wheelWidth + (numWheels - 1) * 10;
        int startX = 30 + (240 - totalWheelsWidth) / 2;

        for (int i = 0; i < numWheels; i++) {
            int posX = startX + i * (wheelWidth + 10);
            renderWheelWindow(posX, wheelWidth);
            renderWheelSymbol(wheels.get(i).getVisibleSymbol(), posX, wheelWidth);
        }
    }

    /**
     * Dibuja un rectángulo blanco que sirve de fondo para la rueda 
     */
    private void renderWheelWindow(int posX, int wheelWidth) {
        Rectangle window = new Rectangle();
        window.changeSize(110, wheelWidth);
        window.changeColor("white");
        window.moveHorizontal(posX);
        window.moveVertical(55);
        window.makeVisible();
        wheelWindows.add(window);
    }

    /**
     * Instancia y muestra la figura geométrica (círculo, rectángulo o triángulo) según el símbolo
     */
    private void renderWheelSymbol(Symbol sym, int posX, int wheelWidth) {
        if (sym == null) return;

        String type = sym.getShapeType();
        String color = sym.getColor();
        int size = wheelWidth - 12;

        if (type.equalsIgnoreCase("circle")) {
            Circle circle = new Circle();
            circle.changeSize(size);
            circle.changeColor(color);
            circle.moveHorizontal(posX + 6);
            circle.moveVertical(90);
            circle.makeVisible();
            circleShapes.add(circle);
        } else if (type.equalsIgnoreCase("rectangle") || type.equalsIgnoreCase("square")) {
            Rectangle rect = new Rectangle();
            rect.changeSize(size, size);
            rect.changeColor(color);
            rect.moveHorizontal(posX + 6);
            rect.moveVertical(90);
            rect.makeVisible();
            rectShapes.add(rect);
        } else if (type.equalsIgnoreCase("triangle")) {
            Triangle tri = new Triangle();
            tri.changeSize(size, size);
            tri.changeColor(color);
            tri.moveHorizontal(posX + 6 + (size / 2));
            tri.moveVertical(90);
            tri.makeVisible();
            triangleShapes.add(tri);
        }
    }
}