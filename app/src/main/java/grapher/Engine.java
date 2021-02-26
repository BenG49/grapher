package grapher;

import java.util.Iterator;
import java.awt.Color;

import bglib.display.Display;
import bglib.display.shapes.*;
import bglib.display.shapes.Shape.Conversion;
import bglib.util.*;
import grapher.Main.Equation;

public class Engine {
    private Display d;
    private Vector2i screenPos;
    private double zoom;
    private RectType screenInfo;

    public Engine(Display d, Vector2i screenPos, double zoom) {
        this.d = d;
        this.screenPos = screenPos;
        this.zoom = zoom;
        screenInfo = new RectType(screenPos.asVector2d(), new Vector2d(zoom));
    }

    public void graph(Equation e, Iterator<Double> constantValues) {
        Equation.graph(e, constantValues, screenInfo, d);
    }

    public void plot(Vector2d point, Color color) {
        final boolean RAINBOW = false;

        d.frameAdd(new Circle(point, 5,(RAINBOW)?
            Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
            color
        ));
    }

    public void draw() {
        final double MARK_LEN = 0.1;
        zoom *= 100;

        d.frameAdd(new Line(
                new Vector2d(0, screenPos.y-d.HEIGHT/2/zoom),
                new Vector2d(0, screenPos.y+d.HEIGHT/2/zoom), Color.WHITE, 2));
        d.frameAdd(new Line(
                new Vector2d(screenPos.x-d.WIDTH/2/zoom, 0),
                new Vector2d(screenPos.x+d.WIDTH/2/zoom, 0), Color.WHITE, 2));

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
                    new Vector2d((x == 0) ? -MARK_LEN : x, (y == 0) ? -MARK_LEN : y),
                    new Vector2d((x == 0) ? MARK_LEN : x, (y == 0) ? MARK_LEN : y),
                    Color.WHITE, 2));
        }
        
        final Vector2d total = d.getDSize().add(screenPos).div(2);
        d.draw((pos) -> (pos.mul(zoom).add(total).mul(new Vector2d(1, -1)).addY(d.getDSize().y).round()));
    }
}
