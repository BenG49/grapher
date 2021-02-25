package grapher;

import java.util.Iterator;

class Range implements Iterator<Double> {
    private double min, max, interval;
    private int currentIndex = 0;


    public Range(double min, double max) {
        this(min, max, 1);
    }

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

    public Iterator<Double> iterator() {
        currentIndex = 0;
        return this;
    }
}