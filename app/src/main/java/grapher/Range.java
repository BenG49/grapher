package grapher;

import java.util.Iterator;

public class Range implements Iterator<Double> {
    private double min, max, interval;
    private int currentIndex = 0;


    public Range(Range r) {
        this(r.min, r.max, r.interval);
    }
    public Range() { this(0, 1); }
    public Range(double min, double max) { this(min, max, 1); }
    public Range(double min, double max, double interval) {
        if (min > max) {
            this.min = max;
            this.max = min;
        } else {
            this.min = min;
            this.max = max;
        }
        this.interval = interval;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getInterval() {
        return interval;
    }

    @Override
    public boolean hasNext() {
        return min+interval*currentIndex < max;
    }

    @Override
    public Double next() {
        return min+interval*currentIndex++;
    }
}