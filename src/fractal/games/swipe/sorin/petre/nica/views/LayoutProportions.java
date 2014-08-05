package fractal.games.swipe.sorin.petre.nica.views;

public class LayoutProportions {

	public final Double	widthRatio;
	public final Double	heightRatio;
	public final Double	xRatio;
	public final Double	yRatio;

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
