package telran.time;

import java.time.LocalDate;
import java.time.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PastTemporalProximityTest {
    private PastTemporalProximity proximity;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindNearestPast() {
        Temporal[] temporals = {
            LocalDate.of(2023, 7, 15),
            LocalDate.of(2023, 6, 1),
            LocalDate.of(2023, 8, 1)
        };
        proximity = new PastTemporalProximity(temporals);
        Temporal target = LocalDate.of(2023, 7, 15);

        Temporal result = proximity.adjustInto(target);
        assertEquals(LocalDate.of(2023, 6, 1), result);
    }

    @Test
    public void testAllDatesInFuture() {
        Temporal[] temporals = {
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 3, 1)
        };
        proximity = new PastTemporalProximity(temporals);
        Temporal target = LocalDate.of(2023, 12, 31);

        Temporal result = proximity.adjustInto(target);
        assertNull(result);
    }

    @Test
    public void testEmptyArray() {
        Temporal[] temporals = {};
        proximity = new PastTemporalProximity(temporals);
        Temporal target = LocalDate.of(2023, 7, 15);
        Temporal result = proximity.adjustInto(target);
        assertNull(result);
    }

    @Test
    public void testDateEqualToTarget() {
        Temporal[] temporals = {
            LocalDate.of(2023, 7, 15),
            LocalDate.of(2023, 6, 1)
        };
        proximity = new PastTemporalProximity(temporals);
        Temporal target = LocalDate.of(2023, 7, 15);

        Temporal result = proximity.adjustInto(target);
        assertEquals(LocalDate.of(2023, 6, 1), result);
    }
}