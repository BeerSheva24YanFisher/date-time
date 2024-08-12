package telran.time;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class PastTemporalProximityTest {

    @Test
    void pastTemporalDateProximityTest() {
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 2, 14);
        LocalDate date3 = LocalDate.of(2024, 4, 10);
        LocalDate date4 = LocalDate.of(2024, 6, 5);
        LocalDate date5 = LocalDate.of(2024, 8, 15);
        LocalDate date6 = LocalDate.of(2024, 12, 20);

        PastTemporalProximity adjuster = new PastTemporalProximity(new LocalDate[]{
            date4, date3, date2
        });

        assertNull(adjuster.adjustInto(date1));
        assertNull(adjuster.adjustInto(date2));
        assertEquals(date4, adjuster.adjustInto(date6));
        assertEquals(date4, adjuster.adjustInto(date5));
        assertEquals(date3, adjuster.adjustInto(date4));
        assertEquals(date2, adjuster.adjustInto(date1.plusDays(100)));
    }
}