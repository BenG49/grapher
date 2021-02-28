package grapher.functions;

import grapher.*;
import grapher.Engine.Equation;

public class CoolDrawing extends Function {

    public CoolDrawing(Engine engine) {
        super(engine);
    }

    @Override
    public void graph() {
        engine.graph(new Equation[] {
            (x, c) -> (0.5 * (-Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
            (x, c) -> (0.5 * (x - Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI))),
            (x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) - x)),
            (x, c) -> (0.5 * (Math.sqrt(-8 * (2 * Math.PI * c + Math.PI) - 3 * x * x + 2 * Math.PI) + x)),
        }, new Range(-6, 5));
    }
    
}
