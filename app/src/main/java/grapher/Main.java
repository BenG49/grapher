package grapher;

import java.awt.Color;

import bglib.input.InputDisplay;
import bglib.util.*;

public class Main {
    public static void main(String[] args) {
        FastMandelbrot fm = new FastMandelbrot(new InputDisplay(1000, 1000, Color.BLACK, "Mandelbrot"), new RectType(
            new Vector2d(-1.25, -1),
            new Vector2d(1)
        ));
        fm.draw();

        for (;;) {
            if (fm.checkKeys())
                fm.draw();;
        }
    }
}
