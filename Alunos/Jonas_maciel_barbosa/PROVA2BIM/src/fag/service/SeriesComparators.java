package fag.service;

import fag.model.Series;

import java.util.Comparator;

public final class SeriesComparators {

    private SeriesComparators() {
    }

    public static Comparator<Series> byName() {
        return Comparator.comparing(
                s -> s.getName() == null ? "" : s.getName(),
                String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Series> byRatingDesc() {
        return (a, b) -> {
            Double ra = a.getRating();
            Double rb = b.getRating();
            if (ra == null && rb == null) return 0;
            if (ra == null) return 1;
            if (rb == null) return -1;
            return Double.compare(rb, ra);
        };
    }

    public static Comparator<Series> byStatus() {
        return Comparator.comparing(s -> s.getStatus() == null ? "" : s.getStatus().name());
    }

    public static Comparator<Series> byPremiereDate() {
        return (a, b) -> {
            String da = a.getPremiered();
            String db = b.getPremiered();
            if (da == null && db == null) return 0;
            if (da == null) return 1;
            if (db == null) return -1;
            return da.compareTo(db);
        };
    }
}
