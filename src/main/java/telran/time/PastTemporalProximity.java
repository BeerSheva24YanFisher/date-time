package telran.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalQueries;
import java.util.Arrays;
import java.util.Comparator;

public class PastTemporalProximity implements TemporalAdjuster {
    private Temporal[] temporals;

    public PastTemporalProximity(Temporal[] temporals) {
        this.temporals = Arrays.copyOf(temporals, temporals.length);
        Arrays.sort(this.temporals, Comparator.comparingLong(this::toEpochSeconds));
    }

    public static ZonedDateTime toZonedDateTime(Temporal temporal) {
        int year = temporal.get(ChronoField.YEAR);
        int month = temporal.get(ChronoField.MONTH_OF_YEAR);
        int day = temporal.get(ChronoField.DAY_OF_MONTH);
        int hour = temporal.isSupported(ChronoField.HOUR_OF_DAY) ? temporal.get(ChronoField.HOUR_OF_DAY) : 0;
        int minute = temporal.isSupported(ChronoField.MINUTE_OF_HOUR) ? temporal.get(ChronoField.MINUTE_OF_HOUR) : 0;
        int second = temporal.isSupported(ChronoField.SECOND_OF_MINUTE) ? temporal.get(ChronoField.SECOND_OF_MINUTE) : 0;
        int nano = temporal.isSupported(ChronoField.NANO_OF_SECOND) ? temporal.get(ChronoField.NANO_OF_SECOND) : 0;
        ZoneId zoneId = temporal.query(TemporalQueries.zone()) != null ? temporal.query(TemporalQueries.zone()) : ZoneId.systemDefault();
        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zoneId);
    }

    private long toEpochSeconds(Temporal temporal) {
        ZonedDateTime zonedDateTime = toZonedDateTime(temporal);
        return zonedDateTime.toEpochSecond();
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        long targetEpochSeconds = toEpochSeconds(temporal);
        int low = 0;
        int high = temporals.length - 1;
        Temporal nearestPast = null;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Temporal midTemporal = temporals[mid];
            long midEpochSeconds = toEpochSeconds(midTemporal);

            if (midEpochSeconds < targetEpochSeconds) {
                nearestPast = midTemporal;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return nearestPast == null ? null : convertTo(toZonedDateTime(nearestPast), temporal);
    }

    private Temporal convertTo(ZonedDateTime zonedDateTime, Temporal temporal) {
        Temporal result;
        if (temporal instanceof ZonedDateTime) {
            result = zonedDateTime;
        } else if (temporal instanceof LocalDateTime) {
            result = zonedDateTime.toLocalDateTime();
        } else if (temporal instanceof LocalDate) {
            result = zonedDateTime.toLocalDate();
        } else {
            result = zonedDateTime.toOffsetDateTime();
        }
        return result;
    }


}
