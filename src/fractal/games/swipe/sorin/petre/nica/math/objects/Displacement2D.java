package fractal.games.swipe.sorin.petre.nica.math.objects;

public final class Displacement2D {

    public final Float dx;
    public final Float dy;

    public Displacement2D(Float dx, Float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Displacement2D(Integer dx, Integer dy) {
        this.dx = dx.floatValue();
        this.dy = dy.floatValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dx == null) ? 0 : dx.hashCode());
        result = prime * result + ((dy == null) ? 0 : dy.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point2D) {
            Displacement2D d = (Displacement2D) o;
            return dx.floatValue() == d.dx.floatValue() && dy.floatValue() == d.dy.floatValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[dx=" + dx + ", dy=" + dy + "]";
    }

}
