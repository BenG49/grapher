package grapher;

import java.awt.Color;

import bglib.display.shapes.Rect;
import bglib.display.shapes.Shape.Conversion;
import bglib.display.Display;
import bglib.util.*;

public class FastMandelbrot {
    private Display d;
    private RectType screen;

    private static final Color SET_COLOR = Color.BLACK;
    private static final float COLOR_OFFSET = 0.6f;

    public FastMandelbrot(Display d, RectType screen) {
        this.d = d;
        this.screen = screen;
    }

    public void draw() {
        final double drawInterval = screen.getSize().x/(double)d.WIDTH;
        final int zoom = (int)(d.WIDTH/screen.getSize().x);
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
                        draw(c, new Color(Color.HSBtoRGB(i/(float)maxIterations+COLOR_OFFSET, 1f, 1f)));
                        if (useMirror)
                            draw(new Complexd(c.a, -c.b), new Color(Color.HSBtoRGB(i/(float)maxIterations+COLOR_OFFSET, 1f, 1f)));

                        continue contLoop;
                    }
                }

                draw(c, SET_COLOR);
                if (useMirror)
                    draw(new Complexd(c.a, -c.b), SET_COLOR);
            }
        }

        d.draw((pos) -> (pos.mul(zoom).sub(screen.getPos().mul(zoom)).round()));
        System.out.println("done");
    }

    private void draw(Complexd c, Color color) {
        d.frameAdd(new Rect(new RectType(
            c.asVector2d(), new Vector2d(1)
        ), 0, color));
    }
}
