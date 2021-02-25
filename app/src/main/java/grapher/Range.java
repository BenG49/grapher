package grapher;

import java.util.Iterator;

class Range implements Iterable<Double> {
    private double min, max, interval;

    public Range(double min, double max) {
        this(min, max, 1);
    }

    public Range(double min, double max, double interval) {
        this.min = min;
        this.max = max;
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
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return min+interval*currentIndex < max;
            }

            @Override
            public Double next() {
                return min+interval*currentIndex++;
            }
        };
    }
}