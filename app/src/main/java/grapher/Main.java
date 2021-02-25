package grapher;

import java.util.Random;
import java.awt.Color;

import bglib.display.Display;
import bglib.display.shapes.Circle;
import bglib.display.shapes.Line;
import bglib.util.Vector2d;
import bglib.util.Vector2i;

public class Main {
    private final double TODEG = Math.PI / 180;

    public static void main(String[] args) {
        // Board b = new Board();
        // b.run();

        Display d = new Display(1000, 1000, Color.BLACK, "Graph");
        Random r = new Random();

        final int maxIndex = 25;

        Equation[] in = new Equation[4 * maxIndex];

        for (int e = 0; e < 4; e++) {
            for (int i = 0; i < maxIndex; i++) {
                double c = (i - maxIndex / 2) / 10.0;

                if (e == 0)
                    in[e * maxIndex + i] = (
                            x) -> (0.5 * (-Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x));
                else if (e == 1)
                    in[e * maxIndex + i] = (
                            x) -> (0.5 * (x - Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI)));
                else if (e == 2)
                    in[e * maxIndex + i] = (
                            x) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x));
                else if (e == 3)
                    in[e * maxIndex + i] = (
                            x) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) + x));
            }
        }

        graph(in,
                // new Equation[] {
                // (x) -> (x*x)
                // },
                d, new Vector2i(0), 1, false);
        d.draw();
    }

    public static void graph(Equation[] e, Display d, Vector2i screenPos, double zoom, boolean lineDraw) {
        final double INTERVAL = 0.001;
        final double MARK_LEN = 0.1;
        final Vector2i SIZE = new Vector2i(d.WIDTH, d.HEIGHT);
        zoom *= 100;

        Vector2i prevDraw = Vector2i.ORIGIN;

        d.frameAdd(new Line(new Vector2i(plotPoint(Vector2d.ORIGIN, zoom, screenPos, SIZE).x, 0),
                new Vector2i(plotPoint(Vector2d.ORIGIN, zoom, screenPos, SIZE).x, d.HEIGHT), Color.WHITE, 2));
        d.frameAdd(new Line(new Vector2i(0, plotPoint(Vector2d.ORIGIN, zoom, screenPos, SIZE).y),
                new Vector2i(d.WIDTH, plotPoint(Vector2d.ORIGIN, zoom, screenPos, SIZE).y), Color.WHITE, 2));

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
                    plotPoint(new Vector2d((x == 0) ? -MARK_LEN : x, (y == 0) ? -MARK_LEN : y), zoom, screenPos, SIZE),
                    plotPoint(new Vector2d((x == 0) ? MARK_LEN : x, (y == 0) ? MARK_LEN : y), zoom, screenPos, SIZE),
                    Color.WHITE, 2));
        }

        for (Equation i : e) {
            for (double x = -SIZE.x / 2 / zoom + screenPos.x; x < SIZE.x / 2 / zoom - screenPos.x; x += INTERVAL) {
                Vector2d point = new Vector2d(x, i.get(x));
                Vector2i drawPoint = plotPoint(point, zoom, screenPos, SIZE);
                double angle = Math.atan2(point.y, point.x) / (Math.PI * 2);
                Color color = Color.getHSBColor((float) angle, 1f, 1f);

                if (lineDraw) {
                    if (x > -SIZE.x / 2 / zoom + screenPos.x)
                        d.frameAdd(new Line(prevDraw, drawPoint, color, 2));
                } else
                    d.frameAdd(new Circle(drawPoint, 2, color));

                prevDraw = drawPoint;
            }
        }
    }

    private static Vector2i plotPoint(Vector2d point, double zoom, Vector2i screenPos, Vector2i size) {
        return new Vector2d(point.x * zoom + size.x / 2 + screenPos.x / 2,
                size.y - (point.y * zoom + size.y / 2 + screenPos.y / 2)).round();
    }

    interface Equation {
        public double get(double x);
        // public double calculate(double x, double[] constants);
    }
}
