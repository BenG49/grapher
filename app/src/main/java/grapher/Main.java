package grapher;

import java.awt.Color;
import java.util.Iterator;

import bglib.display.Display;
import bglib.display.shapes.Circle;
import bglib.util.*;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine(new Display(1000, 1000, Color.BLACK, "Graph"), new Vector2i(0), 40);

        engine.graph((x, c) -> (0.5 * (-Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
                new Range(-6, 5));
        engine.graph((x, c) -> (0.5 * (x - Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI))),
                new Range(-6, 5));
        engine.graph((x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
                new Range(-6, 5));
        engine.graph((x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) + x)),
                new Range(-6, 5));

        /*engine.graph((x, c) -> (Math.abs(Math.sin(Math.pow(x, x))/Math.pow(2, (Math.pow(x, x)-Math.PI/2)/Math.PI))),
                new Range());*/
        
        engine.draw();
    }

    public static Vector2i plotPoint(Vector2d point, RectType screenInfo, Vector2i size) {
        double zoom = screenInfo.getSize().x;
        Vector2i screenPos = screenInfo.getPos().round();
        return new Vector2d(point.x * zoom + size.x / 2 + screenPos.x / 2,
                size.y - (point.y * zoom + size.y / 2 + screenPos.y / 2)).round();
    }

    interface Equation {
        public double calculate(double x, double constant);

        static void graph(Equation e, Iterator<Double> constantValues, RectType screenInfo, Display d) {
            final double INTERVAL = 0.005;
            final boolean RAINBOW = true;

            final Vector2i screenPos = screenInfo.getPos().round();
            final double zoom = screenInfo.getSize().x;

            while(constantValues.hasNext()) {
                double c = constantValues.next();

                for (double x = -d.WIDTH/2/zoom+screenPos.x; x < d.WIDTH/2/zoom-screenPos.x; x+=INTERVAL) {
                    Vector2d point = new Vector2d(x, e.calculate(x, c));
                
                    d.frameAdd(
                        new Circle(plotPoint(point, screenInfo, new Vector2i(d.WIDTH, d.HEIGHT)),
                        2, 
                        (RAINBOW)?
                            Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
                            Color.WHITE
                    ));
                }
            }
        }
    }
}
