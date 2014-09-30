package fractal.games.circus.sorin.petre.nica.views;

import com.google.gson.annotations.Expose;

public class LayoutProportions {

    @Expose
    public final Double widthRatio;

    @Expose
    public final Double heightRatio;

    @Expose
    public final Double xRatio;

    @Expose
    public final Double yRatio;

    public LayoutProportions(Double widthRatio, Double heightRatio, Double xRatio, Double yRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public LayoutProportions(LayoutProportions l1, LayoutProportions l2) {
        this.widthRatio = Math.abs(l1.xRatio - l2.xRatio);
        this.heightRatio = Math.abs(l1.yRatio - l2.yRatio);
        this.xRatio = (l1.xRatio + l2.xRatio) / 2.0;
        this.yRatio = (l1.yRatio + l2.yRatio) / 2.0;
    }
}
