import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Pruebas de unidad compartidas para SlotMachineContest.
 */
public class SlotMachineContestCTest {

    @Test
    public void sharedTestCase1() {
        SlotMachineContest contest = new SlotMachineContest();
        int steps = contest.solve(3);
        assertNotNull(steps);
    }

    @Test
    public void sharedTestCase2() {
        SlotMachineContest contest = new SlotMachineContest();
        int steps = contest.solve(4);
        assertTrue(steps >= 0);
    }
}