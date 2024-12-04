package Common;

import Enumerations.Unit;

public abstract class UnitConverter {

	public static double convertUnits(Unit fromUnit, Unit toUnit, double fromNumber) {
		if (fromUnit == toUnit) {
			return fromNumber;
		}

		switch (fromUnit) {
			case LITERS:
				return convertFromLiters(toUnit, fromNumber);
			case MILLILITERS:
				return convertFromMilliliters(toUnit, fromNumber);
			case KILOGRAM:
				return convertFromKilograms(toUnit, fromNumber);
			case GRAMS:
				return convertFromGrams(toUnit, fromNumber);
			case POUNDS:
				return convertFromPounds(toUnit, fromNumber);
			case TONNES:
				return convertFromTonnes(toUnit, fromNumber);
			default:
				throw new IllegalArgumentException("Unsupported unit: " + fromUnit);
		}
	}

	// LITERS to other units
	private static double convertFromLiters(Unit toUnit, double value) {
		switch (toUnit) {
			case MILLILITERS:
				return value * 1000;
			default:
				throw new IllegalArgumentException("Cannot convert from LITERS to " + toUnit);
		}
	}

	// MILLILITERS to other units
	private static double convertFromMilliliters(Unit toUnit, double value) {
		switch (toUnit) {
			case LITERS:
				return value / 1000;
			default:
				throw new IllegalArgumentException("Cannot convert from MILLILITERS to " + toUnit);
		}
	}

	// KILOGRAM to other units
	private static double convertFromKilograms(Unit toUnit, double value) {
		switch (toUnit) {
			case GRAMS:
				return value * 1000;
			case POUNDS:
				return value * 2.20462; // 1 kg = 2.20462 lbs
			case TONNES:
				return value / 1000;
			default:
				throw new IllegalArgumentException("Cannot convert from KILOGRAM to " + toUnit);
		}
	}

	// GRAMS to other units
	private static double convertFromGrams(Unit toUnit, double value) {
		switch (toUnit) {
			case KILOGRAM:
				return value / 1000;
			default:
				throw new IllegalArgumentException("Cannot convert from GRAMS to " + toUnit);
		}
	}

	// POUNDS to other units
	private static double convertFromPounds(Unit toUnit, double value) {
		switch (toUnit) {
			case KILOGRAM:
				return value / 2.20462;
			default:
				throw new IllegalArgumentException("Cannot convert from POUNDS to " + toUnit);
		}
	}

	// TONNES to other units
	private static double convertFromTonnes(Unit toUnit, double value) {
		switch (toUnit) {
			case KILOGRAM:
				return value * 1000;
			default:
				throw new IllegalArgumentException("Cannot convert from TONNES to " + toUnit);
		}
	}
}
