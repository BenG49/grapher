package grapher;

import java.util.Iterator;

import bglib.display.Display;
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

    public void draw() {
        d.draw();
    }
}
