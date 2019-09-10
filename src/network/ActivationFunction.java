package network;

public abstract class ActivationFunction {
    static final ActivationFunction sigmoid = new _sigmoid();
    static final ActivationFunction relu = new _relu();

    private static class _sigmoid extends ActivationFunction {
        public double compute(double netinput) {
            return 1 / (1 + Math.exp(-(netinput)));
        }
    }

    private static class _relu extends ActivationFunction {
        public double compute(double netinput) {
            if (netinput < 0) return 0;
            return netinput;
        }
    }

    public abstract double compute(double netinput);
}
