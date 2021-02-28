package grapher.functions;

import grapher.Engine;

public abstract class Function {

    protected Engine engine;

    public Function(Engine engine) {
        this.engine = engine;
    }

    public abstract void graph();
}
