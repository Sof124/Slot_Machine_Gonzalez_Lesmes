import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Casos de prueba de unidad para SlotMachineContest.
 */
public class SlotMachineContestTest {

    private SlotMachineContest contest;

    @Before
    public void setUp() {
        contest = new SlotMachineContest();
    }

    @Test
    public void shouldSolveValidMachine() {
        int result = contest.solve(3);
        assertTrue(result >= 0);
    }

    @Test
    public void shouldHandleSimulateWithoutErrors() {
        contest.simulate(3);
        assertTrue(true);
    }
}