package Enumerations;

public enum TastingNote {

	APPLE,
	PEAR,
	VANILLA,
	CARAMEL,
	CINNAMON,
	HONEY,
	SMOKEY,
	PEATY,
	OAK,
	COFFEE,
	DARK_CHOCOLATE,
	LEMON,
	BLACK_PEPPER,
	RAISIN,
	TOFFEE;

	@Override
	public String toString() {
		switch (this) {
			case APPLE:
				return "Apple";
			case PEAR:
				return "Pear";
			case VANILLA:
				return "Vanilla";
			case CARAMEL:
				return "Caramel";
			case CINNAMON:
				return "Cinnamon";
			case HONEY:
				return "Honey";
			case SMOKEY:
				return "Smokey";
			case PEATY:
				return "Peaty";
			case OAK:
				return "Oak";
			case COFFEE:
				return "Coffee";
			case DARK_CHOCOLATE:
				return "Dark Chocolate";
			case LEMON:
				return "Lemon";
			case BLACK_PEPPER:
				return "Black Pepper";
			case RAISIN:
				return "Raisin";
			case TOFFEE:
				return "Toffee";
			default:
				return super.toString();
		}
	}
}
