package memoranda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class responsible for testing whether Junit 5 is configured and working correctly. Feel free to use this class as a
 * learning tool and template for JUnit. The example test cases are taken from GeeksForGeeks.
 */
public class JunitWorkingTest {

    @Test
    public void testAdd() {
        assertEquals(42, Integer.sum(19, 23));
    }

    @Test
    public void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> {
            Integer.divideUnsigned(42, 0);
        });
    }
}
