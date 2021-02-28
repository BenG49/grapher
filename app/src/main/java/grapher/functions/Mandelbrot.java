package grapher.functions;

import java.util.HashMap;
import java.awt.Color;

import grapher.Engine;
import bglib.util.*;

public class Mandelbrot extends UpdateFunction {

    private static final Color IN_SET_COLOR = Color.BLACK;

    public Mandelbrot(Engine engine) {
        super(engine);

        engine.toggleAxes(false);
    }

    @Override
    public void update() {
        final RectType minMax = engine.getGraphMinMax();
        final double drawInterval = engine.getDrawInterval();
        final int MAX_ITERATIONS = engine.getZoom()/6;
        final boolean useMirror;

        final Vector2i xBounds = new Vector2d(
            Math.max(minMax.getPos().x, -2),
            Math.min(minMax.getSize().x, 2)
        ).floor();
        Vector2i yBounds = new Vector2d(
            Math.max(minMax.getPos().y, -2),
            Math.min(minMax.getSize().y, 2)
        ).floor();

        // only draw top half
        if (yBounds.x < 0 && yBounds.y > 0) {
            yBounds.setY(1);
            useMirror = true;
        } else
            useMirror = false;

        for (double y = yBounds.x; y < yBounds.y; y+=drawInterval) {
            continueLoop:
            for (double x = xBounds.x; x < xBounds.y; x+=drawInterval) {
                Complexd c = new Complexd(x, y);

                if (c.getDistance() > 2)
                    continue continueLoop;

                if (c.a > -0.5 && c.a < 0.25 && c.b < 0.5 && c.b > -0.5) {
                    plot(c, IN_SET_COLOR, useMirror);
                    continue continueLoop;
                }
                
                Complexd next = new Complexd(c);
                for (int i = 0; i < MAX_ITERATIONS; i++) {
                    next = next.mul(next).add(c);

                    // point escapes
                    if (next.a*next.a+next.b*next.b >= 4) {
                        plot(c, new Color(Color.HSBtoRGB(i/(float)MAX_ITERATIONS+0.6f, 1f, 1f)), useMirror);
                        continue continueLoop;
                    }
                }

                plot(c, IN_SET_COLOR, useMirror);
            }
        }

        engine.draw();
    }

    private void plot(Complexd c, Color color, boolean useMirror) {
        engine.plot(new Vector2d(c.a, c.b), color);

        if (useMirror)
            engine.plot(new Vector2d(c.a, -c.b), color);
    }
    
}
