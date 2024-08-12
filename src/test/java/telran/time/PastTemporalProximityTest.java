package telran.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class PastTemporalProximityTest {

    @Test
    void testAdjustInto() {
        Temporal[] temporals = {
                ZonedDateTime.of(2023, 8, 10, 15, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2023, 8, 8, 15, 0, 0, 0, ZoneId.of("UTC")),
                LocalDate.of(2023, 8, 5),
                LocalDateTime.of(2023, 8, 7, 10, 30),
                ZonedDateTime.of(2023, 8, 9, 9, 0, 0, 0, ZoneId.of("UTC"))
        };

        PastTemporalProximity proximity = new PastTemporalProximity(temporals);

        Temporal target = ZonedDateTime.of(2023, 8, 10, 15, 0, 0, 0, ZoneId.of("UTC"));
        Temporal expected = ZonedDateTime.of(2023, 8, 9, 9, 0, 0, 0, ZoneId.of("UTC"));
        assertEquals(expected, proximity.adjustInto(target));

        target = LocalDate.of(2023, 8, 8);
        expected = LocalDateTime.of(2023, 8, 7, 10, 30);
        assertEquals(expected, proximity.adjustInto(target));

        target = LocalDateTime.of(2023, 8, 7, 10, 30);
        expected = LocalDate.of(2023, 8, 5);
        assertEquals(expected, proximity.adjustInto(target));

        target = ZonedDateTime.of(2022, 8, 5, 9, 0, 0, 0, ZoneId.of("UTC"));
        assertNull(proximity.adjustInto(target));

        target = LocalDate.of(2023, 8, 5);
        assertNull(proximity.adjustInto(target));
    }
}