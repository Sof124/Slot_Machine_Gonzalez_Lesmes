import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * Simula una máquina tragamonedas y controla sus ruedas y apariencia.
 *
 * @author GonzalezM-LesmesA
 * @version 1.0
 */
public class SlotMachine {

    /** Tiempo de pausa entre los giros. */
    private static final int SPIN_STEP_DELAY_MS = 150;

    /** Lista de ruedas de la máquina. */
    private List<Wheel> wheels;

    /** Indica si la máquina está visible. */
    private boolean isVisible;

    /** Indica si la última operación fue correcta. */
    private boolean lastOperationOk;

    /** Genera números aleatorios para los giros. */
    private final Random random;

    /** Partes visuales de la máquina. */
    private Rectangle frame;
    private Rectangle base;
    private Rectangle leverArm;
    private Circle leverBall;

    /** Crea una máquina con tres ruedas y colores iniciales. */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.isVisible = false;
        this.lastOperationOk = true;
        this.random = new Random();
        initStructure();
        initDefaultConfiguration();
    }

    /**
     * Agrega una rueda en la posición indicada.
     *
     * @param pos posición donde se agrega la rueda
     */
    public void addWheel(int pos) {
        int index = clampInsertPosition(pos, wheels.size());
        wheels.add(index, new Wheel());
        lastOperationOk = true;
        refreshDisplay();
    }

    /**
     * Elimina una rueda de la posición indicada.
     *
     * @param pos número de la rueda
     */
    public void delWheel(int pos) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para eliminar.");
            return;
        }

        int index = clampWheelNumber(pos);
        wheels.get(index - 1).hide();
        wheels.remove(index - 1);
        lastOperationOk = true;
        refreshDisplay();
    }

    /**
     * Intercambia dos ruedas de posición.
     *
     * @param wheel1 primera rueda
     * @param wheel2 segunda rueda
     */
    public void swap(int wheel1, int wheel2) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para intercambiar.");
            return;
        }

        int index1 = clampWheelNumber(wheel1);
        int index2 = clampWheelNumber(wheel2);

        Wheel temp = wheels.get(index1 - 1);
        wheels.set(index1 - 1, wheels.get(index2 - 1));
        wheels.set(index2 - 1, temp);

        lastOperationOk = true;
        refreshDisplay();
    }

    /**
     * Bloquea una rueda.
     *
     * @param wheel número de la rueda
     */
    public void lock(int wheel) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para fijar.");
            return;
        }

        int index = clampWheelNumber(wheel);
        lastOperationOk = wheels.get(index - 1).lock();

        if (!lastOperationOk) {
            handleError("La rueda ya estaba fija.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Desbloquea una rueda.
     *
     * @param wheel número de la rueda
     */
    public void unlock(int wheel) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para soltar.");
            return;
        }

        int index = clampWheelNumber(wheel);
        lastOperationOk = wheels.get(index - 1).unlock();

        if (!lastOperationOk) {
            handleError("La rueda ya estaba libre.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Agrega un símbolo a todas las ruedas.
     *
     * @param pos posición del símbolo
     * @param color color del símbolo
     */
    public void addSymbol(int pos, String color) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para adicionar símbolos.");
            return;
        }

        boolean allOk = true;

        for (Wheel w : wheels) {
            allOk = w.addSymbol(pos, color) && allOk;
        }

        lastOperationOk = allOk;

        if (!lastOperationOk) {
            handleError("El color '" + color + "' no es un color CSS válido.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Elimina un símbolo de las ruedas.
     *
     * @param symbol color del símbolo
     */
    public void delSymbol(String symbol) {
        boolean removedAny = false;

        for (Wheel w : wheels) {
            if (w.delSymbol(symbol)) {
                removedAny = true;
            }
        }

        lastOperationOk = removedAny;

        if (!lastOperationOk) {
            handleError("Símbolo no encontrado.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Coloca un símbolo en una rueda.
     *
     * @param wheel número de la rueda
     * @param symbol color del símbolo
     */
    public void placeSymbol(int wheel, String symbol) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas disponibles.");
            return;
        }

        int index = clampWheelNumber(wheel);
        lastOperationOk = wheels.get(index - 1).placeSymbol(symbol);

        if (!lastOperationOk) {
            handleError("Símbolo no encontrado en la rueda.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Gira una rueda un paso.
     *
     * @param wheel número de la rueda
     */
    public void spin(int wheel) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas disponibles.");
            return;
        }

        int index = clampWheelNumber(wheel);
        lastOperationOk = wheels.get(index - 1).spin();

        if (!lastOperationOk) {
            handleError("La rueda está fija o vacía; no puede girar.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Gira una rueda varios pasos.
     *
     * @param wheel número de la rueda
     * @param steps cantidad de pasos
     */
    public void spin(int wheel, int steps) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas disponibles.");
            return;
        }

        if (steps < 1) {
            handleError("El número de pasos debe ser mayor a cero.");
            return;
        }

        int index = clampWheelNumber(wheel);
        Wheel w = wheels.get(index - 1);

        if (w.isLocked()) {
            handleError("La rueda está fija; no puede girar.");
            return;
        }

        for (int i = 0; i < steps; i++) {
            w.spin();

            if (isVisible) {
                refreshDisplay();
                pause();
            }
        }

        lastOperationOk = true;
        refreshDisplay();
    }

    /** Gira todas las ruedas que no estén bloqueadas. */
    public void spin() {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas para girar.");
            return;
        }

        boolean anySpun = false;

        for (Wheel w : wheels) {
            if (w.isLocked()) {
                continue;
            }

            int symbolCount = w.colors().length;

            if (symbolCount == 0) {
                continue;
            }

            int steps = 1 + random.nextInt(symbolCount);

            for (int i = 0; i < steps; i++) {
                anySpun = w.spin() || anySpun;
            }
        }

        lastOperationOk = anySpun;

        if (!lastOperationOk) {
            handleError("Todas las ruedas están fijas o vacías; no giró ninguna.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Coloca un color en cada rueda.
     *
     * @param setSymbols colores que se quieren mostrar
     */
    public void spin(String[] setSymbols) {
        if (wheels.isEmpty()) {
            handleError("No hay ruedas disponibles.");
            return;
        }

        if (setSymbols == null || setSymbols.length != wheels.size()) {
            handleError("La configuración debe tener exactamente un color por rueda.");
            return;
        }

        boolean allOk = true;

        for (int i = 0; i < wheels.size(); i++) {
            allOk = wheels.get(i).placeSymbol(setSymbols[i]) && allOk;
        }

        lastOperationOk = allOk;

        if (!lastOperationOk) {
            handleError("Alguno de los colores indicados no existe en su rueda.");
        } else {
            refreshDisplay();
        }
    }

    /**
     * Devuelve los colores de la primera rueda.
     *
     * @return arreglo con los colores
     */
    public String[] symbols() {
        lastOperationOk = true;

        if (wheels.isEmpty()) {
            return new String[0];
        }

        return wheels.get(0).colors();
    }

    /**
     * Cuenta los colores diferentes de la primera rueda.
     *
     * @return cantidad de colores diferentes
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
     * Devuelve los colores que se muestran.
     *
     * @return arreglo con los colores visibles
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];

        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).visibleColor();
        }

        lastOperationOk = true;
        return config;
    }

    /**
     * Indica si la máquina tiene jackpot.
     *
     * @return true si todos los colores son iguales, false si no
     */
    public boolean isJackpot() {
        if (wheels.isEmpty()) {
            return false;
        }

        String[] config = configuration();
        String first = config[0];

        if (first == null || first.isEmpty()) {
            return false;
        }

        for (String color : config) {
            if (!color.equalsIgnoreCase(first)) {
                return false;
            }
        }

        return true;
    }

    /** Muestra la máquina en pantalla. */
    public void makeVisible() {
        this.isVisible = true;
        lastOperationOk = true;

        frame.makeVisible();
        base.makeVisible();
        leverArm.makeVisible();
        leverBall.makeVisible();

        refreshDisplay();
    }

    /** Oculta la máquina. */
    public void makeInvisible() {
        this.isVisible = false;
        lastOperationOk = true;

        for (Wheel w : wheels) {
            w.hide();
        }

        frame.makeInvisible();
        base.makeInvisible();
        leverArm.makeInvisible();
        leverBall.makeInvisible();
    }

    /** Cierra la máquina y elimina sus ruedas. */
    public void exit() {
        makeInvisible();
        wheels.clear();
        lastOperationOk = true;
    }

    /**
     * Indica si la última operación fue correcta.
     *
     * @return true si fue correcta, false si hubo un error
     */
    public boolean ok() {
        return lastOperationOk;
    }

    // Métodos privados

    /** Crea las partes visuales de la máquina. */
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

    /** Crea las tres ruedas y sus colores iniciales. */
    private void initDefaultConfiguration() {
        addWheel(1);
        addWheel(2);
        addWheel(3);

        addSymbol(1, "red");
        addSymbol(2, "blue");
        addSymbol(3, "yellow");
    }

    /**
     * Ajusta una posición de inserción.
     *
     * @param pos posición indicada
     * @param size tamaño actual de la lista
     * @return posición válida
     */
    private int clampInsertPosition(int pos, int size) {
        if (pos < 1) {
            return 0;
        }

        if (pos > size) {
            return size;
        }

        return pos - 1;
    }

    /**
     * Ajusta el número de rueda.
     *
     * @param wheel número de rueda
     * @return número de rueda válido
     */
    private int clampWheelNumber(int wheel) {
        if (wheel < 1) {
            return 1;
        }

        if (wheel > wheels.size()) {
            return wheels.size();
        }

        return wheel;
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param message mensaje que se muestra
     */
    private void handleError(String message) {
        lastOperationOk = false;

        if (isVisible) {
            JOptionPane.showMessageDialog(
                null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /** Hace una pausa para la animación. */
    private void pause() {
        try {
            Thread.sleep(SPIN_STEP_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Actualiza la apariencia de la máquina. */
    private void refreshDisplay() {
        if (!isVisible) {
            return;
        }

        frame.changeColor(
            isJackpot() && !wheels.isEmpty() ? "green" : "blue"
        );

        int numWheels = wheels.size();

        if (numWheels == 0) {
            return;
        }

        int availableWidth = 200;
        int wheelWidth = Math.min(50, (availableWidth / numWheels) - 10);
        int totalWheelsWidth =
            numWheels * wheelWidth + (numWheels - 1) * 10;
        int startX = 30 + (240 - totalWheelsWidth) / 2;

        for (int i = 0; i < numWheels; i++) {
            int posX = startX + i * (wheelWidth + 10);
            wheels.get(i).display(posX, wheelWidth);
        }
    }
}