package grapher.functions;

import grapher.Engine;

public abstract class UpdateFunction {
    protected Engine engine;

    public UpdateFunction(Engine engine) {
        this.engine = engine;
    }

    public abstract void update();
}
