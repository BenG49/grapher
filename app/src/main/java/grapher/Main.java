package grapher;

import java.awt.Color;
import java.util.Iterator;

import bglib.display.Display;
import bglib.display.shapes.Circle;
import bglib.display.shapes.Line;
import bglib.util.*;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine(new Display(1000, 1000, Color.BLACK, "Graph"), new Vector2i(0), 50);

        engine.graph((x, c) -> (0.5 * (-Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
                new Range(-6, 5, 1).iterator());
        engine.graph((x, c) -> (0.5 * (x - Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI))),
                new Range(-6, 5, 1).iterator());
        engine.graph((x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
                new Range(-6, 5, 1).iterator());
        engine.graph((x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) + x)),
                new Range(-6, 5, 1).iterator());
        
        engine.draw();
    }

    public static void graph(Equation[] e, Display d, Vector2i screenPos, double zoom, boolean lineDraw) {
        final double MARK_LEN = 0.1;
        final Vector2i SIZE = new Vector2i(d.WIDTH, d.HEIGHT);
        final RectType screenInfo = new RectType(screenPos.asVector2d(), new Vector2d(zoom, 0));
        zoom *= 100;

        d.frameAdd(new Line(new Vector2i(plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).x, 0),
                new Vector2i(plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).x, d.HEIGHT), Color.WHITE, 2));
        d.frameAdd(new Line(new Vector2i(0, plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).y),
                new Vector2i(d.WIDTH, plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).y), Color.WHITE, 2));

        for (int i = 0; i < 4; i++) {
            int x = 0, y = 0;

            if (i == 0)
                x = -1;
            if (i == 2)
                x = 1;

            if (i == 1)
                y = -1;
            if (i == 3)
                y = 1;

            d.frameAdd(new Line(
                    plotPoint(new Vector2d((x == 0) ? -MARK_LEN : x, (y == 0) ? -MARK_LEN : y), screenInfo, SIZE),
                    plotPoint(new Vector2d((x == 0) ? MARK_LEN : x, (y == 0) ? MARK_LEN : y), screenInfo, SIZE),
                    Color.WHITE, 2));
        }

        for (Equation i : e)
            Equation.graph(i, new Range(-6, 5, 1).iterator(), screenInfo, d);
        
        System.out.println("done");
    }

    private static Vector2i plotPoint(Vector2d point, RectType screenInfo, Vector2i size) {
        double zoom = screenInfo.getSize().x;
        Vector2i screenPos = screenInfo.getPos().round();
        return new Vector2d(point.x * zoom + size.x / 2 + screenPos.x / 2,
                size.y - (point.y * zoom + size.y / 2 + screenPos.y / 2)).round();
    }

    interface Equation {
        public double calculate(double x, double constant);

        static void graph(Equation e, Iterator<Double> constantValues, RectType screenInfo, Display d) {
            final double INTERVAL = 0.01;
            final boolean RAINBOW = true;

            final Vector2i screenPos = screenInfo.getPos().round();
            final double zoom = screenInfo.getSize().x;

            while(constantValues.hasNext()) {
                double c = constantValues.next();

                for (double x = -d.WIDTH/2/zoom+screenPos.x; x < d.WIDTH/2/zoom-screenPos.x; x+=INTERVAL) {
                    Vector2d point = new Vector2d(x, e.calculate(x, c));
                    Color color = (RAINBOW)?
                            Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
                            Color.WHITE;
                
                    d.frameAdd(new Circle(plotPoint(point, screenInfo, new Vector2i(d.WIDTH, d.HEIGHT)), 2, color));
                }
            }
        }
    }
}
