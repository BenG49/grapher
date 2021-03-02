package grapher;

import java.util.HashSet;
import java.util.Iterator;

import java.awt.Color;

import bglib.input.InputDisplay;
import bglib.display.shapes.*;
import bglib.display.shapes.Shape.Conversion;
import bglib.util.*;

public class Engine {
    public InputDisplay d;
    private Vector2i screenPos;
    private int zoom;
    private RectType graphMinMax;
    private double graphDrawInterval;
    private HashSet<String> lastKeysPressed;

    private boolean showAxes;

    public Engine(InputDisplay d, Vector2i screenPos, int zoom) {
        this(d, screenPos, zoom, true);
    }
    public Engine(InputDisplay d, Vector2i screenPos, int zoom, boolean showAxes) {
        this.d = d;
        this.screenPos = screenPos;
        this.zoom = zoom;

        graphMinMax = new RectType(
            new Vector2d(-d.WIDTH/2/this.zoom+screenPos.x, -d.HEIGHT/2/this.zoom+screenPos.y).floor(),
            new Vector2d(d.WIDTH/2/this.zoom-screenPos.x, d.HEIGHT/2/this.zoom-screenPos.y).floor()
        );
        graphDrawInterval = (graphMinMax.getSize().x-graphMinMax.getPos().x)/d.WIDTH;
        lastKeysPressed = new HashSet<String>();
    }


    public Vector2d getMousePos() {
        return d.getMousePos();
    }

    public Vector2i getDSize() {
        return d.getDSize();
    }

    public RectType getGraphMinMax() {
        return graphMinMax;
    }

    public double getDrawInterval() {
        return graphDrawInterval;
    }

    public Conversion getConversion() {
        return (pos) -> (pos.mul(zoom).add(d.getDSize().add(screenPos).div(2)).mul(new Vector2d(1, -1)).addY(d.getDSize().y).round());
    }

    public int getZoom() {
        return zoom;
    }


    public void toggleAxes(boolean showAxes) {
        this.showAxes = showAxes;
    }

    public void graph(Equation[] f, Range[] constantValues) {
        for (int i = 0; i < f.length; i++)
            graph(f[i], constantValues[i%constantValues.length]);
    }
    public void graph(Equation[] f, Range constantValues) {
        for (Equation e : f)
            graph(e, new Range(constantValues));
    }
    public void graph(Equation e, Range constantValues) {
        Equation.graph(e, constantValues, this);
    }

    public void plot(Vector2d plot, Color color) {
        plot(plot, color, true);
    }
    public void plot(Vector2d point, Color color, boolean useConversion) {
        d.frameAdd(new FillRect(new RectType(point, new Vector2d(1)), 0, color, useConversion));
    }

    public boolean checkKeys() {
        boolean output = false;
        
        if (keyCanBePressed("w")) {
            screenPos.addY(-1*zoom);
            output = true;
        }
        if (keyCanBePressed("a")) {
            screenPos.addX(-1*zoom);
            output = true;
        }
        if (keyCanBePressed("s")) {
            screenPos.addY(1*zoom);
            output = true;
        }
        if (keyCanBePressed("d")) {
            screenPos.addX(1*zoom);
            output = true;
        }

        if (keyCanBePressed("e")) {
            if (zoom(true) && !output)
                output = true;
        }
        if (keyCanBePressed("r")) {
            if (zoom(false) && !output)
                output = true;
        }

        return output;
    }

    private boolean zoom(boolean in) {
        boolean output = false;

        if (in) {
            zoom += 10;
            output = true;
        } else if (zoom > 0) {
            zoom -= 10;
            output = true;
        }

        if (output) {
            graphMinMax = new RectType(
                new Vector2d(-d.WIDTH/2/this.zoom+screenPos.x, -d.HEIGHT/2/this.zoom+screenPos.y).floor(),
                new Vector2d(d.WIDTH/2/this.zoom-screenPos.x, d.HEIGHT/2/this.zoom-screenPos.y).floor()
            );
            graphDrawInterval = (graphMinMax.getSize().x-graphMinMax.getPos().x)/d.WIDTH;
        }
            
        return output;
    }

    private boolean keyCanBePressed(String key) {
        if (d.hasKey(key)) {
            if (!lastKeysPressed.contains(key)) {
                lastKeysPressed.add(key);
                return true;
            } else
                return false;
        } else {
            lastKeysPressed.remove(key);
            return false;
        }
    }

    public void draw() {
        final double MARK_LEN = 0.1;

        if (showAxes) {
            // add axes
            d.frameAdd(new Line(
                    new Vector2d(0, graphMinMax.getPos().y),
                    new Vector2d(0, graphMinMax.getSize().y), Color.WHITE, 2));
            d.frameAdd(new Line(
                    new Vector2d(graphMinMax.getPos().x, 0),
                    new Vector2d(graphMinMax.getSize().x, 0), Color.WHITE, 2));

            // add marks at unit length
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
        }
        
        d.draw(getConversion());
    }

    public interface Equation {
        public double calculate(double x, double constant);

        static void graph(Equation e, Iterator<Double> constantValues, Engine engine) {
            final boolean RAINBOW = true;
            final boolean USELINE = false;

            while(constantValues.hasNext()) {
                double c = constantValues.next();
                Vector2d prevPoint = Vector2d.ORIGIN;

                for (double x = engine.graphMinMax.getPos().x; x < engine.graphMinMax.getSize().x; x+=engine.graphDrawInterval) {
                    Vector2d point = new Vector2d(x, e.calculate(x, c));
                    Color color = (RAINBOW)?
                        Color.getHSBColor((float)(Math.atan2(point.y, point.x)/(Math.PI*2)), 1f, 1f):
                        Color.WHITE;
                
                    if (USELINE) {
                        if (x > engine.graphMinMax.getPos().x)
                            engine.d.frameAdd(new Line(prevPoint, point, color, 2));
                        prevPoint = point;
                    } else
                        engine.d.frameAdd(new Circle(point, 2, color));
                }
            }
        }
    }
}
