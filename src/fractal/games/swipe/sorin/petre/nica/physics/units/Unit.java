package fractal.games.swipe.sorin.petre.nica.physics.units;

import java.util.ArrayList;
import java.util.List;

public class Unit<T extends Unit<T>> {

	@SuppressWarnings("rawtypes")
	public static final Unit<?>	ADIMENSIONAL	= new Unit("", 1.0, null, null, null);

	public final String			symbol;
	public final Double			magnitudeOrder;

	public LengthUnit			lengthComponent;
	public TimeUnit				timeComponent;
	public MassUnit				massComponent;

	public Unit(String symbol, Double magnitudeOrder, LengthUnit lengthComponent, TimeUnit timeComponent, MassUnit massComponent) {
		super();
		this.symbol = symbol;
		this.magnitudeOrder = magnitudeOrder;
		this.lengthComponent = lengthComponent;
		this.timeComponent = timeComponent;
		this.massComponent = massComponent;
	}

	public Double convert(Double magnitude, T otherUnit) {
		return magnitude * evaluateMagnitudeOrderTransformation(otherUnit);
	}

	public Double convert(Long magnitude, T otherUnit) {
		return convert(magnitude.doubleValue(), otherUnit);
	}

	public Double evaluateMagnitudeOrderTransformation(Unit<?> otherUnit) {
		return otherUnit.magnitudeOrder / magnitudeOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((magnitudeOrder == null) ? 0 : magnitudeOrder.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit<?> other = (Unit<?>) obj;
		if (magnitudeOrder == null) {
			if (other.magnitudeOrder != null)
				return false;
		} else if (!magnitudeOrder.equals(other.magnitudeOrder))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return symbol + ", mo = " + magnitudeOrder;
	}

	public static class DerivedUnitBuilder {

		private Double			magnitudeOrder			= 1.0;

		private List<Unit<?>>	proportionalUnits		= new ArrayList<Unit<?>>(3);
		private List<Unit<?>>	inversProportionalUnits	= new ArrayList<Unit<?>>(3);

		public LengthUnit		lengthComponent;
		public TimeUnit			timeComponent;
		public MassUnit			massComponent;

		public Double			lengthMagnitude;
		public Double			timeMagnitude;
		public Double			massMagnitude;

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

		@SuppressWarnings("rawtypes")
		public Unit<?> build() {
			StringBuilder symbol = new StringBuilder("");
			for (Unit<?> unit : proportionalUnits) {
				symbol.append(unit.symbol);
				contributeToComponents(unit, new Operator<Double>() {
					@Override
					public Double apply(Double leftHandOperand, Double rightHandOperand) {
						return leftHandOperand * rightHandOperand;
					}
				});
			}
			if (!inversProportionalUnits.isEmpty()) {
				symbol.append("/");
				for (Unit<?> unit : inversProportionalUnits) {
					symbol.append(unit.symbol);
					contributeToComponents(unit, new Operator<Double>() {
						@Override
						public Double apply(Double leftHandOperand, Double rightHandOperand) {
							return leftHandOperand / rightHandOperand;
						}
					});
				}
			}

			if (lengthMagnitude != null) {
				lengthComponent = new LengthUnit("l'", lengthMagnitude);
			}
			if (timeMagnitude != null) {
				timeComponent = new TimeUnit("t'", timeMagnitude);
			}
			if (massMagnitude != null) {
				massComponent = new MassUnit("m'", massMagnitude);
			}

			return new Unit(symbol.toString(), magnitudeOrder, lengthComponent, timeComponent, massComponent);
		}

		private void contributeToComponents(Unit<?> unit, Operator<Double> operator) {
			if (unit == null) {
				return;
			}
			if (unit instanceof LengthUnit) {
				if (lengthMagnitude == null) {
					lengthMagnitude = operator.apply(1.0, unit.magnitudeOrder);
				} else {
					lengthMagnitude = operator.apply(lengthMagnitude, unit.magnitudeOrder);
				}
			} else if (unit instanceof TimeUnit) {
				if (timeMagnitude == null) {
					timeMagnitude = operator.apply(1.0, unit.magnitudeOrder);
				} else {
					timeMagnitude = operator.apply(timeMagnitude, unit.magnitudeOrder);
				}
			} else if (unit instanceof MassUnit) {
				if (massMagnitude == null) {
					massMagnitude = operator.apply(1.0, unit.magnitudeOrder);
				} else {
					massMagnitude = operator.apply(massMagnitude, unit.magnitudeOrder);
				}
			} else {
				contributeToComponents(unit.lengthComponent, operator);
				contributeToComponents(unit.timeComponent, operator);
				contributeToComponents(unit.massComponent, operator);
			}
		}

		public static interface Operator<T> {
			T apply(T leftHandOperand, T rightHandOperand);
		}

	}

}
