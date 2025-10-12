public class Velocity {
    /**
     * Velocity class for calculating ball velocity & movement
     */
    private double dx;
    private double dy;

    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    public void invertX() {
        dx = -dx;
    }

    public void invertY() {
        dy = -dy;
    }

    public double getSpeed() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Normalize function to scale back moving speed to avoid sum of vertical and horizontal positions
     * larger & faster than original speed value to maintain stable speed
     */
    public void normalize(double originalSpeed) {
        double length = getSpeed();
        if (length == 0) {
            return;
        }
        dx = (dx / length) * originalSpeed;
        dy = (dy / length) * originalSpeed;
    }
}
