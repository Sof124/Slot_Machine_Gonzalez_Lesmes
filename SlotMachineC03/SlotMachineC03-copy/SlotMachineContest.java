/**
 * Módulo para solucionar y simular el problema de la maratón Slot Machine.
 * 
 * @author GonzalezM-LesmesA
 * @version 1.0
 */
public class SlotMachineContest {

    /**
     * Resuelve el problema en modo invisible de forma instantánea.
     * 
     * @param n número de ruedas y símbolos
     * @return número de acciones realizadas
     */
    public int solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();
        
        int actions = 0;
        int maxActions = 100;

        while (machine.distinctSymbols() > 1 && actions < maxActions) {
            for (int wheel = 2; wheel <= n; wheel++) {
                if (machine.distinctSymbols() == 1) {
                    break;
                }
                machine.spin(wheel);
                actions++;
            }
        }

        return actions;
    }

    /**
     * Simula visualmente la solución en modo visible.
     * 
     * @param n número de ruedas y símbolos
     */
    public void simulate(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        
        int actions = 0;
        int maxActions = 10;

        while (machine.distinctSymbols() > 1 && actions < maxActions) {
            for (int wheel = 2; wheel <= n; wheel++) {
                if (machine.distinctSymbols() == 1) {
                    break;
                }
                machine.spin(wheel, 1);
                actions++;
            }
        }
    }
}