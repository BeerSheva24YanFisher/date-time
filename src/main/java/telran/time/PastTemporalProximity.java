package telran.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

public class PastTemporalProximity implements TemporalAdjuster {
    private Temporal[] temporals;

    public PastTemporalProximity(Temporal[] temporals) {
        this.temporals = temporals;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        Temporal nearestPast = null;
        long minDifference = Long.MAX_VALUE;
        for (Temporal temp : temporals) {
            long difference = temp.until(temporal, ChronoUnit.DAYS);
            if (difference > 0 && difference < minDifference) {
                nearestPast = temp;
                minDifference = difference;
            }
        }
    
        return nearestPast;
    }
}
