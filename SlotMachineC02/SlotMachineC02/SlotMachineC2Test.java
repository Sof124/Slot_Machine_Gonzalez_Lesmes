import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de la clase SlotMachine para el ciclo 2.
 *
 * @author GonzalezM-LesmesA
 * @version 1.0
 */
public class SlotMachineC2Test {

    /** Máquina tragamonedas utilizada en las pruebas. */
    private SlotMachine machine;

    /** Configura una máquina nueva antes de cada prueba. */
    @Before
    public void setUp() {
        machine = new SlotMachine();
    }

    /** Libera la máquina después de cada prueba. */
    @After
    public void tearDown() {
        machine.exit();
    }

    // ---------------------------------------------------------------
    // swap
    // ---------------------------------------------------------------

    @Test
    public void swapShouldExchangeVisibleColorsOfTwoWheels() {
        machine.spin(new String[] { "red", "blue", "yellow" });
        machine.swap(1, 3);
        assertTrue("swap con posiciones válidas debe ser exitoso", machine.ok());
        assertArrayEquals(new String[] { "yellow", "blue", "red" },
                machine.configuration());
    }

    @Test
    public void swapWithSameWheelTwiceShouldLeaveConfigurationUnchanged() {
        machine.spin(new String[] { "red", "blue", "yellow" });
        machine.swap(2, 2);
        assertTrue(machine.ok());
        assertArrayEquals(new String[] { "red", "blue", "yellow" },
                machine.configuration());
    }

    @Test
    public void swapWithOutOfRangePositionsShouldClampAndNotCrash() {
        machine.spin(new String[] { "red", "blue", "yellow" });
        machine.swap(0, 100);
        assertTrue(machine.ok());
        assertArrayEquals(new String[] { "yellow", "blue", "red" },
                machine.configuration());
    }

    @Test
    public void swapOnEmptyMachineShouldFail() {
        machine.exit();
        machine.swap(1, 2);
        assertFalse("no debería poder intercambiar ruedas si no hay ninguna",
                machine.ok());
    }

    // ---------------------------------------------------------------
    // lock / unlock
    // ---------------------------------------------------------------

    @Test
    public void lockShouldSucceedOnAFreeWheel() {
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void lockingAnAlreadyLockedWheelShouldFail() {
        machine.lock(1);
        machine.lock(1);
        assertFalse("fijar una rueda ya fija no debería reportarse como éxito",
                machine.ok());
    }

    @Test
    public void unlockShouldSucceedOnALockedWheel() {
        machine.lock(1);
        machine.unlock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void unlockingAFreeWheelShouldFail() {
        machine.unlock(1);
        assertFalse("soltar una rueda que ya está libre no debería reportarse como éxito",
                machine.ok());
    }

    @Test
    public void lockOnEmptyMachineShouldFail() {
        machine.exit();
        machine.lock(1);
        assertFalse(machine.ok());
    }

    @Test
    public void lockedWheelShouldNotSpinWithSingleStepSpin() {
        machine.lock(2);
        String before = machine.configuration()[1];
        machine.spin(2);
        assertFalse("girar una rueda fija debe fallar", machine.ok());
        assertEquals("una rueda fija no debe cambiar de símbolo visible",
                before, machine.configuration()[1]);
    }

    // ---------------------------------------------------------------
    // spin(wheel) - un paso
    // ---------------------------------------------------------------

    @Test
    public void spinSingleWheelShouldAdvanceToNextSymbolInOrder() {
        machine.spin(1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void spinSingleWheelShouldWrapAroundCircularly() {
        machine.spin(1);
        machine.spin(1);
        machine.spin(1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinOnEmptyMachineShouldFail() {
        machine.exit();
        machine.spin(1);
        assertFalse(machine.ok());
    }

    // ---------------------------------------------------------------
    // spin(wheel, steps)
    // ---------------------------------------------------------------

    @Test
    public void spinWithStepsShouldAdvanceExactlyThatManyPositions() {
        machine.spin(1, 2);
        assertTrue(machine.ok());
        assertEquals("yellow", machine.configuration()[0]);
    }

    @Test
    public void spinWithStepsEqualToWheelSizeShouldReturnToSameSymbol() {
        machine.spin(1, 3);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinWithZeroStepsShouldFail() {
        machine.spin(1, 0);
        assertFalse("girar cero pasos no es una operación válida", machine.ok());
    }

    @Test
    public void spinWithNegativeStepsShouldFail() {
        machine.spin(1, -3);
        assertFalse("un número de pasos negativo no es válido", machine.ok());
    }

    @Test
    public void spinWithStepsOnLockedWheelShouldFail() {
        machine.lock(1);
        machine.spin(1, 2);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    @Test
    public void spinWithStepsOnInvalidWheelNumberShouldClampNotCrash() {
        machine.spin(99, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[2]);
    }

    // ---------------------------------------------------------------
    // spin(setSymbols) - configuración directa
    // ---------------------------------------------------------------

    @Test
    public void spinWithValidConfigurationShouldSetExactColors() {
        machine.spin(new String[] { "yellow", "red", "blue" });
        assertTrue(machine.ok());
        assertArrayEquals(new String[] { "yellow", "red", "blue" },
                machine.configuration());
    }

    @Test
    public void spinWithWrongLengthConfigurationShouldFail() {
        String[] before = machine.configuration();
        machine.spin(new String[] { "red", "blue" });
        assertFalse(machine.ok());
        assertArrayEquals("la configuración no debe cambiar si la operación falla",
                before, machine.configuration());
    }

    @Test
    public void spinWithUnknownColorInConfigurationShouldFail() {
        machine.spin(new String[] { "red", "purple", "yellow" });
        assertFalse(machine.ok());
    }

    @Test
    public void spinWithNullConfigurationShouldFail() {
        machine.spin((String[]) null);
        assertFalse(machine.ok());
    }

    @Test
    public void spinWithConfigurationOnEmptyMachineShouldFail() {
        machine.exit();
        machine.spin(new String[] { "red" });
        assertFalse(machine.ok());
    }

    // ---------------------------------------------------------------
    // spin() - todas las ruedas
    // ---------------------------------------------------------------

    @Test
    public void spinAllShouldSucceedWhenAtLeastOneWheelIsFree() {
        machine.spin();
        assertTrue("si hay al menos una rueda libre, la operación debe ser exitosa",
                machine.ok());
    }

    @Test
    public void spinAllResultingColorsShouldAlwaysBelongToTheirOwnWheel() {
        machine.spin();
        String[] result = machine.configuration();
        String[] possibleColors = { "red", "blue", "yellow" };

        for (String color : result) {
            assertTrue("cada rueda solo puede mostrar uno de sus propios colores",
                    Arrays.asList(possibleColors).contains(color));
        }
    }

    @Test
    public void spinAllShouldNotMoveALockedWheel() {
        machine.lock(1);
        String beforeLockedWheel = machine.configuration()[0];
        machine.spin();
        assertTrue(machine.ok());
        assertEquals("una rueda fija nunca debe cambiar aunque se gire toda la máquina",
                beforeLockedWheel, machine.configuration()[0]);
    }

    @Test
    public void spinAllWithEveryWheelLockedShouldFail() {
        machine.lock(1);
        machine.lock(2);
        machine.lock(3);
        machine.spin();
        assertFalse("si todas las ruedas están fijas, no debería poder girar ninguna",
                machine.ok());
    }

    @Test
    public void spinAllOnEmptyMachineShouldFail() {
        machine.exit();
        machine.spin();
        assertFalse(machine.ok());
    }

    // ---------------------------------------------------------------
    // isJackpot
    // ---------------------------------------------------------------

    @Test
    public void settingAllWheelsToSameColorShouldBeJackpot() {
        machine.spin(new String[] { "blue", "blue", "blue" });
        assertTrue(machine.isJackpot());
    }

    @Test
    public void settingDifferentColorsShouldNotBeJackpot() {
        machine.spin(new String[] { "red", "blue", "yellow" });
        assertFalse(machine.isJackpot());
    }
}

