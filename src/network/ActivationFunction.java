public abstract class ActivationFunction {
    public static final ActivationFunction sigmoid = new _sigmoid();

    private static class _sigmoid extends ActivationFunction {
        public double compute(double x) {
            return 1 / (1 + Math.exp(-x));
        }

        public double derivative(double x) {
            return (1 - x) * x;
        }
    }

    public abstract double compute(double x);

    public abstract double derivative(double x);
}
