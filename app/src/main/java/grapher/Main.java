package grapher;

import java.awt.Color;
import java.util.Iterator;

import bglib.display.Display;
import bglib.display.shapes.Circle;
import bglib.display.shapes.Line;
import bglib.util.*;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine(new Display(1000, 1000, Color.BLACK, "Graph"), new Vector2i(0), 0.5);

        // engine.graph((x, c) -> (x+c), new Range());

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

    interface Equation {
        public double calculate(double x, double constant);

        static void graph(Equation e, Iterator<Double> constantValues, RectType screenInfo, Display d) {
            final double INTERVAL = 0.01;
            final boolean RAINBOW = true;
            final boolean USELINE = false;

            final double zoom = screenInfo.getSize().x;
            final Vector2i screenPos = screenInfo.getPos().round();
            final Vector2d xMinMax = new Vector2d(
                -d.WIDTH/2/zoom+screenPos.x,
                 d.WIDTH/2/zoom-screenPos.x
            );

            while(constantValues.hasNext()) {
                double c = constantValues.next();
                Vector2d prevPoint = Vector2d.ORIGIN;

                for (double x = xMinMax.x; x < xMinMax.y; x+=INTERVAL) {
                    Vector2d point = new Vector2d(x, e.calculate(x, c));
                    Color color = (RAINBOW)?
                        Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
                        Color.WHITE;
                
                    if (USELINE) {
                        if (x > xMinMax.x)
                            d.frameAdd(new Line(prevPoint, point, color, 2));
                        prevPoint = point;
                    } else
                        d.frameAdd(new Circle(point, 2, color));
                }
            }
        }
    }
}
