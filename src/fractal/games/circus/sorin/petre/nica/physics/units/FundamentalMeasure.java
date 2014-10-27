package fractal.games.circus.sorin.petre.nica.physics.units;

public enum FundamentalMeasure {
	Length("pix"), Time("millis"), Mass("kg");

	public final String	unitSymbol;

	private FundamentalMeasure(String symbol) {
		this.unitSymbol = symbol;
	}

	public static class DerviedMeasureBuilder {

	}

}
