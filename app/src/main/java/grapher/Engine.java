package grapher;

import java.util.Iterator;
import java.awt.Color;

import bglib.display.Display;
import bglib.display.shapes.*;
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

    public void plot(Vector2d point) {
        final boolean RAINBOW = true;

        d.frameAdd(new Circle(
            Main.plotPoint(point, screenInfo, new Vector2i(d.WIDTH, d.HEIGHT)),
            5,
            (RAINBOW)?
                Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
                Color.WHITE
        ));
    }

    public void draw() {
        final double MARK_LEN = 0.1;
        final Vector2i SIZE = new Vector2i(d.WIDTH, d.HEIGHT);
        final RectType screenInfo = new RectType(screenPos.asVector2d(), new Vector2d(zoom, 0));
        zoom *= 100;

        d.frameAdd(new Line(new Vector2i(Main.plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).x, 0),
                new Vector2i(Main.plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).x, d.HEIGHT), Color.WHITE, 2));
        d.frameAdd(new Line(new Vector2i(0, Main.plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).y),
                new Vector2i(d.WIDTH, Main.plotPoint(Vector2d.ORIGIN, screenInfo, SIZE).y), Color.WHITE, 2));

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
                    Main.plotPoint(new Vector2d((x == 0) ? -MARK_LEN : x, (y == 0) ? -MARK_LEN : y), screenInfo, SIZE),
                    Main.plotPoint(new Vector2d((x == 0) ? MARK_LEN : x, (y == 0) ? MARK_LEN : y), screenInfo, SIZE),
                    Color.WHITE, 2));
        }
        
        d.draw();
    }
}
