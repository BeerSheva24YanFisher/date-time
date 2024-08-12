package telran.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;

public class PastTemporalProximity implements TemporalAdjuster {
    private Temporal[] temporals;

    public PastTemporalProximity(Temporal[] temporals) {
        temporals = temporals.clone();
        Arrays.sort(temporals, this::compare);
        this.temporals = temporals;
    }

    private int compare(Temporal t1, Temporal t2) {
        long range = ChronoUnit.DAYS.between(t2,t1);
        return range > 0 ? 1 : range == 0 ? 0 : -1;
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

        return nearestPast!=null ? temporal.plus(ChronoUnit.DAYS.between(temporal,nearestPast), ChronoUnit.DAYS) : nearestPast;
    }


}
