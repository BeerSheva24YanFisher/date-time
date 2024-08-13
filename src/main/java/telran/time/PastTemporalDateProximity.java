package telran.time;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;

public class PastTemporalDateProximity implements TemporalAdjuster {
    private Temporal[] temporals;

    public PastTemporalDateProximity(Temporal[] temporals) {
        temporals = temporals.clone();
        Arrays.sort(temporals, this::compare);
        this.temporals = temporals;
    }

    private int compare(Temporal t1, Temporal t2) {
        long range = betweenDays(t2, t1);
        return range > 0 ? 1 : range == 0 ? 0 : -1;
    }

    private long betweenDays(Temporal from, Temporal to) {
        return ChronoUnit.DAYS.between(LocalDate.from(from), LocalDate.from(to));
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        int low = 0;
        int high = temporals.length - 1;
        Temporal nearestPast = null;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Temporal midTemporal = temporals[mid];
            if (compare(temporal, midTemporal)>0) {
                nearestPast = midTemporal;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return nearestPast!=null ? temporal.plus(betweenDays(temporal,nearestPast), ChronoUnit.DAYS) : nearestPast;
    }




}
