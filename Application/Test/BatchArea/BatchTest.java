package BatchArea;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Enumerations.TastingNote;

public class BatchTest {

	Product product = new Product("product1", 1000);
	TasteProfile tasteProfile1 = new TasteProfile("tasteProfile1", "Tastes like sucess.");
	TasteProfile tasteProfile2 = new TasteProfile("tasteProfile2", "Tastes like sucess.");
	TasteProfile tasteProfile3 = new TasteProfile("tasteProfile3", "Tastes like sucess.");
	Map<TasteProfile, Double> bluePrint1 = new HashMap<>();
	Formula formula1 = new Formula("formula1", bluePrint1);
	Map<TasteProfile, Double> bluePrint2 = new HashMap<>();
	Formula formula2 = new Formula("formula2", bluePrint2);

	@BeforeEach
	public void setUp() {

		tasteProfile1.addTastingNote(TastingNote.SMOKEY);
		tasteProfile1.addTastingNote(TastingNote.APPLE);
		tasteProfile1.addTastingNote(TastingNote.BLACK_PEPPER);
		
		tasteProfile2.addTastingNote(TastingNote.COFFEE);
		tasteProfile2.addTastingNote(TastingNote.CARAMEL);
		tasteProfile2.addTastingNote(TastingNote.APPLE);

		tasteProfile3.addTastingNote(TastingNote.PEAR);
		tasteProfile3.addTastingNote(TastingNote.HONEY);
		tasteProfile3.addTastingNote(TastingNote.LEMON);

		bluePrint1.put(tasteProfile1, 25.0);
		bluePrint1.put(tasteProfile2, 25.0);
		bluePrint1.put(tasteProfile3, 50.0);

		bluePrint2.put(tasteProfile1, 20.0);
		bluePrint2.put(tasteProfile2, 65.0);
		bluePrint2.put(tasteProfile3, 15.0);
	}

	@Test
    public void testGetWeightedTastingNotes() {

		product.defineFormula(formula1);
		Batch batch = new Batch(product, 100);

		String actual = batch.getWeightedTastingNotes();
		String expected = "Black Pepper, Smokey, Apple";

		System.out.println("Actual: " + actual);
		system.out.println("Expected: " + expected);


		assertEquals(expected, actual);
    }
}
