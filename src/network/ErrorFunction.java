public abstract class ErrorFunction {
    public static final ErrorFunction halfSquare = new _halfSquare();

    private static class _halfSquare extends ErrorFunction {
        public double compute(double value, double target) {
            return Math.pow(target - value, 2) / 2;
        }

        public double derivative(double value, double target) {
            return value - target;
        }
    }

    public abstract double compute(double value, double target);

    public abstract double derivative(double value, double target);
}
