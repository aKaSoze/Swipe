package fractal.games.swipe.sorin.petre.nica.physics.units;

import java.util.ArrayList;
import java.util.List;

public class Unit<T extends Unit<T>> {

	public static final Unit<?>	ADIMENSIONAL	= new Unit<>("", 1.0);

	public final String			symbol;
	public final Double			magnitudeOrder;

	public Unit(String symbol, Double magnitudeOrder) {
		this.symbol = symbol;
		this.magnitudeOrder = magnitudeOrder;
	}

	public Double convert(Double magnitude, T otherUnit) {
		return magnitude * otherUnit.magnitudeOrder;
	}

	public static class DerivedUnitBuilder {

		private Double			magnitudeOrder			= 1.0;

		private List<Unit<?>>	proportionalUnits		= new ArrayList<>(3);
		private List<Unit<?>>	inversProportionalUnits	= new ArrayList<>(3);

		public static DerivedUnitBuilder newUnit() {
			return new DerivedUnitBuilder();
		}

		public DerivedUnitBuilder proportionalTo(Unit<?> unit) {
			proportionalUnits.add(unit);
			magnitudeOrder *= unit.magnitudeOrder;
			return this;
		}

		public DerivedUnitBuilder inversProportionalTo(Unit<?> unit) {
			inversProportionalUnits.add(unit);
			magnitudeOrder /= unit.magnitudeOrder;
			return this;
		}

		public Unit<?> build() {
			StringBuilder symbol = new StringBuilder("");
			for (Unit<?> unit : proportionalUnits) {
				symbol.append(unit.symbol);
			}
			if (!inversProportionalUnits.isEmpty()) {
				symbol.append("/");
				for (Unit<?> unit : inversProportionalUnits) {
					symbol.append(unit.symbol);
				}
			}

			return new Unit<>(symbol.toString(), magnitudeOrder);
		}

	}

}
