package fractal.games.swipe.sorin.petre.nica.physics.units;

public abstract class Unit<T extends Unit<T>> {

	public final String	symbol;
	public final Double	magnitudeOrder;

	public Unit(String symbol, Double magnitudeOrder) {
		this.symbol = symbol;
		this.magnitudeOrder = magnitudeOrder;
	}

	public Double convert(Double magnitude, T otherUnit) {
		return magnitude * otherUnit.magnitudeOrder;
	}

}
