package grapher;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import bglib.display.shapes.Rect;
import bglib.input.InputDisplay;
import bglib.util.*;

public class FastMandelbrot {
    private InputDisplay d;
    private RectType screen;
    private HashSet<String> lastKeysPressed;

    private int zoom;
    private double drawInterval;

    private static final Color SET_COLOR = Color.BLACK;
    private static final float COLOR_OFFSET = 0.6f;

    public FastMandelbrot(InputDisplay d, RectType screen) {
        this.d = d;
        this.screen = screen;

        zoom = (int)(d.WIDTH/screen.getSize().x);
        drawInterval = screen.getSize().x/(double)d.WIDTH;
        lastKeysPressed = new HashSet<String>();
    }

    public void draw() {
        zoom = (int)(d.WIDTH/screen.getSize().x);
        final int maxIterations = zoom/10;

        final boolean useMirror = screen.getPos().y < 0 && screen.getPos().y+screen.getSize().y > 0;

        for (double y = screen.getPos().y; y < (useMirror?drawInterval*2 : screen.getPos().y+screen.getSize().y); y += drawInterval) {
            contLoop:
            for (double x = screen.getPos().x; x < screen.getPos().x+screen.getSize().x; x += drawInterval) {
                Complexd c = new Complexd(x, y);

                if (c.getDistance() > 2)
                    continue contLoop;
                
                Complexd z = new Complexd(0);
                for (int i = 0; i < maxIterations; i++) {
                    z = z.mul(z).add(c);

                    if (z.a*z.a+z.b*z.b >= 4) {
                        point(c, new Color(Color.HSBtoRGB(i/(float)maxIterations+COLOR_OFFSET, 1f, 1f)));
                        if (useMirror)
                            point(new Complexd(c.a, -c.b), new Color(Color.HSBtoRGB(i/(float)maxIterations+COLOR_OFFSET, 1f, 1f)));

                        continue contLoop;
                    }
                }

                point(c, SET_COLOR);
                if (useMirror)
                    point(new Complexd(c.a, -c.b), SET_COLOR);
            }
        }

        d.draw((pos) -> (pos.mul(zoom).sub(screen.getPos().mul(zoom)).round()));
    }

    private void point(Complexd c, Color color) {
        d.frameAdd(new Rect(new RectType(
            c.asVector2d(), new Vector2d(1)
        ), 0, color));
    }

    public boolean checkKeys() {
        boolean output = false;
        
        if (keyCanBePressed("w")) {
            screen.getPos().addY(-10*drawInterval);
            output = true;
        }
        if (keyCanBePressed("a")) {
            screen.getPos().addX(-10*drawInterval);
            output = true;
        }
        if (keyCanBePressed("s")) {
            screen.getPos().addY(10*drawInterval);
            output = true;
        }
        if (keyCanBePressed("d")) {
            screen.getPos().addX(10*drawInterval);
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
}
