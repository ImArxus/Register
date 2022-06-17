import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import stev.kwikemart.AmountException;
import stev.kwikemart.InvalidQuantityException;
import stev.kwikemart.InvalidQuantityException.InvalidQuantityForCategoryException;
import stev.kwikemart.Item;
import stev.kwikemart.PaperRoll;
import stev.kwikemart.Register;
import stev.kwikemart.Register.NoSuchItemException;
import stev.kwikemart.RegisterException.EmptyGroceryListException;
import stev.kwikemart.RegisterException.TooManyItemsException;
import stev.kwikemart.Upc;

public class RegisterTest {

	private List<Item> grocery = new ArrayList<Item>();
	// Récupère l'instance de la caisse et met un petit rouleau de papier
	private Register register = Register.getRegister().changePaper(PaperRoll.LARGE_ROLL);

	@AfterEach
	public void clear() {
		grocery.clear();
	}

	// TEST VALIDES

	/**
	 * Tester le CUP. Il doit être différent de -1 et de 12 caractères pour être
	 * considéré comme valide
	 */
	@Test
	public void validCUP() {
		String CUP = Upc.generateCode("12345678901");
		assertNotEquals(-1, Upc.getCheckDigit(CUP));
		assertEquals(12, CUP.length());
	}

	/**
	 * Tester si le CUP d'un produit pesable (quantité farctionnaire) commence bien
	 * par un 2
	 */
	@Test
	public void validCUP_WeighableItem() {
		String CUP = Upc.generateCode("22345678901");
		grocery.add(new Item(CUP, "Bananas", 0.37, 1.96));
		register.print(grocery);
	}

	/**
	 * Tester si le prix au détail d'un produit est bien celui entré dans son
	 * constructeur
	 */
	@Test
	public void validItemAmount() {
		assertEquals(3.05, new Item(Upc.generateCode("12345678901"), "Chocolate", 1, 3.05).getRetailPrice());
	}

	/**
	 * Tester si il est possible d'ajouter au ticket une liste de produits de taille
	 * comprise entre 0 et 10 inclus
	 */
	@Test
	public void validListLength() {
		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 7.50));

		System.out.println(register.print(grocery)); // TODO : pourquoi on a des tickets vides avant ?
	}

	// TEST INVALIDES

	@Test
	public void invalidSuperiorAmount() {
		assertThrows(AmountException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.96));
			register.print(grocery);
		});
	}

	@Test
	public void invalidNegativeAmount() {
		assertThrows(AmountException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, -1));
			register.print(grocery);
		});
	}

	/*
	 * Tester si un produit vendu au poids peut prendre une autre valeur que 2 pour
	 * le premier chiffre de son code Résultat attendu : Exception Résultat obtenu :
	 * Correct
	 */
	@Test
	public void invalidCodeForFractionnaryQuantityValue() {
		assertThrows(InvalidQuantityException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12804918500"), "Beef", 0.5, 5.75));
			register.print(grocery);
		});
	}

	/*
	 * Tester si un produit qui n'est pas vendu au poids peut prendre la valeur 2
	 * pour le premier chiffre de son code Résultat attendu : Exception Résultat
	 * obtenu : Pas correct
	 */
	@Test
	public void invalidCodeQuantityValue() {
		assertThrows(InvalidQuantityException.class, () -> {
			grocery.add(new Item(Upc.generateCode("22804918500"), "Flower", 3, 5.75));
			register.print(grocery);
		});
	}

	/*
	 * Tester si la caisse n'accepte pas les listes avec 0 articles Résultat attendu
	 * : Exception Résultat obtenu : Correct
	 */
	@Test
	public void emptyGroceryListItem() {
		assertThrows(EmptyGroceryListException.class, () -> {
			register.print(grocery);
		});
	}

	/*
	 * Tester si la caisse n'accepte pas les listes plus de 10 articles Ici testé
	 * avec 11 produits Résultat attendu : Exception Résultat obtenu : Correct
	 */
	@Test
	public void tooManyItemGroceryList() {
		assertThrows(TooManyItemsException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918502"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918503"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918504"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918505"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918506"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918507"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918508"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918509"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918510"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918511"), "Beef", 0.5, 5.75));

			register.print(grocery);
		});
	}

	/*
	 * Tester l'ajout de plusieurs produits avec le même code dans la liste d'item
	 * de la caisse Ici testé avec 2 produits Résultat attendu : Exception Résultat
	 * obtenu : Correct
	 */
	@Test
	public void duplicateItemGroceryList() {
		assertThrows(InvalidQuantityForCategoryException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
			register.print(grocery);
		});
	}

	/*
	 * Tester la suppression d'un produit qui n'existe pas dans la liste Résultat
	 * attendu : Exception Résultat obtenu : Correct
	 */
	@Test
	public void deleteItemGroceryListWhenItemNotExisting() {
		assertThrows(NoSuchItemException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", -1, 5.75));
			register.print(grocery);
		});
	}

}
