package edu.analyzer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberFormatter {
    private NumberFormatter() {}

    public static Number format(double value) {

        BigDecimal r = BigDecimal.
                valueOf(value).
                setScale(1, RoundingMode.HALF_UP);

        if (r.signum() ==0) return 0L;
        if (r.stripTrailingZeros().scale() <= 0) return r.longValue();
        return r.doubleValue();
    }
}
