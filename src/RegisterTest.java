import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import stev.kwikemart.AmountException;
import stev.kwikemart.AmountException.NegativeAmountException;
import stev.kwikemart.InvalidQuantityException;
import stev.kwikemart.InvalidQuantityException.InvalidQuantityForCategoryException;
import stev.kwikemart.InvalidUpcException.InvalidCheckDigitException;
import stev.kwikemart.InvalidUpcException.NoUpcException;
import stev.kwikemart.InvalidUpcException.UpcTooLongException;
import stev.kwikemart.InvalidUpcException.UpcTooShortException;
import stev.kwikemart.Item;
import stev.kwikemart.PaperRoll;
import stev.kwikemart.PaperRollException;
import stev.kwikemart.Register;
import stev.kwikemart.Register.NoSuchItemException;
import stev.kwikemart.RegisterException.EmptyGroceryListException;
import stev.kwikemart.RegisterException.TooManyItemsException;
import stev.kwikemart.Upc;

//TODO : storeException, Coupon exception, 


/**
 * @author LE BALANGER Alexandre - LEBA20129906
 * @author DOLLO Vincent - DOLV26029901
 * @author MARTINEZ Eloy - MARE12089900
 * @author KHODJA Meziane - KHOM26099900
 */

/**
 * TODO : liste des classes d'équivalence TODO : heuristique
 */

public class RegisterTest {

	private List<Item> grocery = new ArrayList<Item>();
	// Récupère l'instance de la caisse et met un petit rouleau de papier
	private Register register = Register.getRegister().changePaper(PaperRoll.LARGE_ROLL);

	@AfterEach
	public void clear() {
		grocery.clear();
	}
	
	
	/*
	 * Test qui correspond au test1 de la doc
	 */
	@Test
	public void test1() {
		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 7));
		grocery.add(new Item(Upc.generateCode("22804918511"), "Beef", 0.5, 7));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		grocery.add(new Item(Upc.generateCode("12345678921"), "Bananas", 1, 3));
		grocery.add(new Item(Upc.generateCode("12645678931"), "Chocolate", 1, 4));
		grocery.add(new Item(Upc.generateCode("12804918542"), "Sheep", 2,5));
		grocery.add(new Item(Upc.generateCode("12804918542"), "Sheep", -1,5));
		grocery.add(new Item(Upc.generateCode("52804918542"), "Reduction Sheep", 1,2));
		System.out.print(register.print(grocery));
	}
	
	
	/*
	 * Test qui correspond au test2 de la doc
	 */
	@Test
	public void test2() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		assertThrows(UpcTooShortException.class, () -> {
			grocery.add(new Item(Upc.generateCode("1280491850"), "Beef", 1, 7));
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test3 de la doc
	 */
	@Test
	public void test3() {
		grocery.add(new Item(Upc.generateCode("128049185012"), "Beef", 1, 7));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		assertThrows(UpcTooLongException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test4 de la doc
	 */
	@Test
	public void test4() {
		String CUP = "123456789125";
		grocery.add(new Item(CUP,"Beef", 1, 4));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		assertThrows(InvalidCheckDigitException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test5 de la doc
	 */
	@Test
	public void test5() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, -1));
		grocery.add(new Item(Upc.generateCode("52345678901"), "Reduction Bananas", 1, 0.5));
		assertThrows(AmountException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test6 de la doc
	 */
	@Test
	public void test6() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 37));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		grocery.add(new Item(Upc.generateCode("12804918502"), "Beef", 1, 7));
		grocery.add(new Item(Upc.generateCode("12804918503"), "Carrot", 1, 3));
		grocery.add(new Item(Upc.generateCode("12804918504"), "Chocolate", 1, 5));
		assertThrows(AmountException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test7 de la doc
	 */
	@Test
	public void test7() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1.4, 3));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", -1, 2));
		assertThrows(InvalidQuantityForCategoryException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test8 de la doc
	 */
	@Test
	public void test8() {
		grocery.add(new Item(Upc.generateCode("22345678901"), "Livres", 2, 20));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		assertThrows(InvalidQuantityForCategoryException.class, () -> {
			register.print(grocery);
		});
	}
	
	
	/*
	 * Test qui correspond au test9 de la doc
	 */
	@Test
	public void test9() {
		assertThrows(EmptyGroceryListException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test10 de la doc
	 */
	@Test
	public void test10() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 3));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 2));
		grocery.add(new Item(Upc.generateCode("12804918502"), "Beef", 1, 7));
		grocery.add(new Item(Upc.generateCode("12804918503"), "Carrot", 1, 3));
		grocery.add(new Item(Upc.generateCode("12804918504"), "Chocolate", 1, 5));
        grocery.add(new Item(Upc.generateCode("64748119599"), "Chewing gum", 2, 0.99));
        grocery.add(new Item(Upc.generateCode("44348225996"), "Gobstoppers", 1, 0.99));
        grocery.add(new Item(Upc.generateCode("34323432343"), "Nerds", 2, 1.44));
        grocery.add(new Item(Upc.generateCode("14323432343"), "Doritos Club", 1, 0.5));
        grocery.add(new Item(Upc.generateCode("61519314159"), "Doritos", 1, 1.25));
        grocery.add(new Item(Upc.generateCode("11519314158"), "Chips", 1, 1.25));
		assertThrows(TooManyItemsException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test11 de la doc
	 */
	@Test
	public void test11() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 3));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 6));
		grocery.add(new Item(Upc.generateCode("52804918500"), "Reduction Flower", 1, -2));
		assertThrows(NegativeAmountException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test12 de la doc
	 */
	@Test
	public void test12() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 6));
		grocery.add(new Item(Upc.generateCode("52804918500"), "Flower", 1, 8));
		System.out.print(register.print(grocery));
	}
	
	/*
	 * Test qui correspond au test13 de la doc
	 */
	@Test
	public void test13() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 6));
		assertThrows(UpcTooShortException.class, () -> {
			grocery.add(new Item(Upc.generateCode("528049"), "Flower", 1, 2));
			grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 0.5, 6));
			register.print(grocery);
		});	
	}
	
	/*
	 * Test qui correspond au test14 de la doc
	 */
	@Test
	public void test14() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 6));
		grocery.add(new Item(Upc.generateCode("5280492123211"), "Reduction Flower", 1, 2));
		grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 0.5, 6));
		assertThrows(UpcTooLongException.class, () -> {
			register.print(grocery);
		});	
	}
	
	/*
	 * Test qui correspond au test15 de la doc
	 */
	@Test
	public void test15() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 6));
		grocery.add(new Item(Upc.generateCode("92804921232"), "Reduction Flower", 1, 2));
		register.print(grocery);
	}
	
	
	/*
	 * Test qui correspond au test16 de la doc
	 */
	@Test
	public void test16() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 6));
		grocery.add(new Item(Upc.generateCode("13044968430"), "Beef", -1, 5));
		assertThrows(NoSuchItemException.class, () -> {
			register.print(grocery);
		});
	}
	
	/*
	 * Test qui correspond au test17 de la doc
	 */
	@Test
	public void test17() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 3));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", -4, 3));
		assertThrows(NoSuchItemException.class, () -> {
			register.print(grocery);
		});
	}
	
	
	/*
	 * Test qui correspond au test18 de la doc
	 */
	@Test
	public void test18() {
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 3));
		grocery.add(new Item(Upc.generateCode("22804918501"), "Beef", 0.5, 1));
		System.out.print(register.print(grocery));
	}
	
	/*
	 * Test qui correspond au test19 de la doc
	 */
	@Test
	public void test19() {
		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 7, 3));
		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 1, 2));
		grocery.add(new Item(Upc.generateCode("12804918502"), "Beef", 1, 7));
		grocery.add(new Item(Upc.generateCode("12804918503"), "Carrot", 1, 3));
		System.out.print(register.print(grocery));
	}
	
	
	
		/******************************************************************************************************
		 *  																								  *
		 *  					 /!\ ATTENTION COMMENTAIRE IMPORTANT										  *
		 *  																								  *
		 *  Voici les tests que nous avons réalisés avant de faire la combinaison des classes en test.		  *
		 *  En effet, nous avons commencé par réalisés ces tests avant de nous rendre compte de notre erreur. *
		 *  Nous avons donc choisi de les laisser commentés afin que vous apperceviez notre progression	      *
		 *  																								  *
		 *   																								  * 
		 ******************************************************************************************************/

	
	
//	 * On vérifie que la taille du rouleau de papier est suffisante pour toutes les
//	 * infos à écrire sur le reçu
//	 * 
//	 * Résultat attendu : toutes les lignes de la facture apparaissent bien sur le
//	 * reçu et nous ne recevons aucune Exception
//	 */
//	@Test
//	public void validPaperRollSize() {
//		register.changePaper(PaperRoll.SMALL_ROLL);
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.5));
//		grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", -1, 1.5));
//		grocery.add(new Item(Upc.generateCode("64748119599"), "Chewing gum", 2, 0.99));
//		grocery.add(new Item(Upc.generateCode("44348225996"), "Gobstoppers", 1, 0.99));
//		grocery.add(new Item(Upc.generateCode("34323432343"), "Nerds", 2, 1.44));
//		grocery.add(new Item(Upc.generateCode("54323432343"), "Doritos Club", 1, 0.5));
//		grocery.add(new Item(Upc.generateCode("61519314159"), "Doritos", 1, 1.25));
//		System.out.println(register.print(grocery));
//		register.changePaper(PaperRoll.LARGE_ROLL);
//	}
//
//	/**
//	 * Tester le CUP. Il doit être différent de -1 et de 12 caractères pour être
//	 * considéré comme valide
//	 * 
//	 * Résultat attendu : pas d'erreur ou d'échec
//	 */
//	@Test
//	public void validCUP() {
//		String CUP = Upc.generateCode("12345678901");
//		assertNotEquals(-1, Upc.getCheckDigit(CUP));
//		assertEquals(12, CUP.length());
//	}
//
//	/**
//	 * Tester si le CUP d'un produit pesable (quantité farctionnaire) commence bien
//	 * par un 2
//	 * 
//	 * Résultat attendu : l'article apparait bien sur le reçu
//	 */
//	@Test
//	public void validCUP_WeighableItem() {
//		String CUP = Upc.generateCode("22345678901");
//		grocery.add(new Item(CUP, "Bananas", 0.37, 1.96));
//		register.print(grocery);
//	}
//
//	/**
//	 * Tester si le prix au détail d'un produit est bien celui entré dans son
//	 * constructeur. On vérifie donc que le prix au détail de l'article est bien
//	 * celui donnée en paramètre constructeur
//	 * 
//	 * Résultat attendu : pas d'erreur ou d'échec
//	 */
//	@Test
//	public void validItemAmount() {
//		double retailPrice = 3.05;
//		assertEquals(retailPrice,
//				new Item(Upc.generateCode("12345678901"), "Chocolate", 1, retailPrice).getRetailPrice());
//	}
//
//	/**
//	 * Tester si il est possible d'ajouter au ticket une liste de produits de taille
//	 * comprise entre 0 et 10 inclus
//	 * 
//	 * Résultat attendu : les articles apparaissent bien sur le reçu
//	 */
//	@Test
//	public void validListLength() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 7.50));
//		System.out.println(register.print(grocery));
//	}
//
//	/*
//	 * On teste l'application d'un coupon dans le cas où le coupon est inferieur au
//	 * prix total et le coupon est d'une valeur positif
//	 * 
//	 * Résultat attendu : les articles ainsi que le coupon apparaissent bien sur le
//	 * reçu et le coupon déduit la somme indiquée de la facture
//	 */
//	@Test
//	public void validCoupon() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 7.50));
//		grocery.add(new Item(Upc.generateCode("52804918502"), "Sheep", 1, 1.50));
//		System.out.println(register.print(grocery));
//	}
//
//	/*
//	 * On vérifie l'application du rabais de 1$ quand 5 articles sont ajoutés au
//	 * panier
//	 * 
//	 * Résultat attendu : les articles apparaissent bien sur le reçu et 1$ de rabais
//	 * et appliquée à la facture
//	 */
//	@Test
//	public void valid5ItemsDiscount() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 5.75));
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.96));
//		grocery.add(new Item(Upc.generateCode("12645678901"), "Chocolate", 1, 3.05));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 7.50));
//		System.out.println(register.print(grocery));
//	}
//
//	/**
//	 * On teste si l'on peut enlever un produit déjà existant en ajoutant ce même
//	 * produit (même CUP) mais avec une quantité négative
//	 * 
//	 * Résultat attendu : les articles de même CUP apparaissent bien sur le reçu et
//	 * la quantité négative est déduit de la quantité totale pour ce produit
//	 */
//	@Test
//	public void validRemoveItem() {
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 3, 7.50));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", -1, 7.50));
//		System.out.println(register.print(grocery));
//	}
//
//	// TEST INVALIDES
//
//	/**
//	 * On vérifie que la taille du rouleau de papier est suffisante pour toutes les
//	 * infos à écrire sur le reçu
//	 * 
//	 * Résultat attendu : PaperRollException.OutOfPaperException
//	 */
//	@Test
//	public void invalidPaperRollSize() {
//		register.changePaper(PaperRoll.SMALL_ROLL);
//
//		// Premier reçu
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.5));
//		grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", -1, 1.5));
//		grocery.add(new Item(Upc.generateCode("64748119599"), "Chewing gum", 2, 0.99));
//		grocery.add(new Item(Upc.generateCode("44348225996"), "Gobstoppers", 1, 0.99));
//		grocery.add(new Item(Upc.generateCode("34323432343"), "Nerds", 2, 1.44));
//		grocery.add(new Item(Upc.generateCode("54323432343"), "Doritos Club", 1, 0.5));
//		grocery.add(new Item(Upc.generateCode("61519314159"), "Doritos", 1, 1.25));
//		System.out.println(register.print(grocery));
//
//		// Second reçu
//		grocery.clear();
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.5));
//		grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("64748119599"), "Chewing gum", 2, 0.99));
//		grocery.add(new Item(Upc.generateCode("44348225996"), "Gobstoppers", 1, 0.99));
//		assertThrows(PaperRollException.OutOfPaperException.class, () -> {
//			register.print(grocery);
//		});
//
//		register.changePaper(PaperRoll.LARGE_ROLL);
//	}
//
//	/**
//	 * On teste le cas où le prix unitaire d'un article est supérieur à 35$
//	 * 
//	 * Résultat attendu : AmountException
//	 */
//	@Test
//	public void invalidSuperiorAmount() {
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 37));
//		assertThrows(AmountException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	
//	/**
//	 * On teste le cas où le prix unitaire d'un article est supérieur à 35$
//	 * 
//	 * Résultat attendu : AmountException
//	 */
//	@Test
//	public void invalidNoCUP() {
//		assertThrows(NoUpcException.class, () -> {
//			grocery.add(new Item(Upc.generateCode(null), "Bananas", 1, 37));
//			register.print(grocery);
//		});
//	}
//	
//	/**
//	 * On teste le cas où lep prix d'un article est négatif
//	 * 
//	 * Résultat attendu : AmountException
//	 */
//	@Test
//	public void invalidNegativeAmount() {
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, -1));
//		assertThrows(AmountException.class, () -> {
//			register.print(grocery);
//		});
//	}
//	
//
//
//	/*
//	 * Tester si un produit vendu au poids peut prendre une autre valeur que 2 pour
//	 * le premier chiffre de son code
//	 * 
//	 * Résultat attendu : InvalidQuantityException
//
//	 */
//	@Test
//	public void invalidCodeForFractionnaryQuantityValue() {
//		grocery.add(new Item(Upc.generateCode("12804918500"), "Beef", 0.5, 5.75));
//		assertThrows(InvalidQuantityException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * Tester si un produit qui n'est pas vendu au poids peut prendre la valeur 2
//	 * pour le premier chiffre de son code
//	 * 
//	 * Résultat attendu : InvalidQuantityException
//	 */
//	@Test
//	public void invalidCodeQuantityValue() {
//		grocery.add(new Item(Upc.generateCode("22804918500"), "Flower", 3, 5.75));
//		assertThrows(InvalidQuantityException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * Tester si la caisse n'accepte pas les listes avec 0 articles
//	 * 
//	 * Résultat attendu : EmptyGroceryListException
//	 */
//	@Test
//	public void emptyGroceryListItem() {
//		assertThrows(EmptyGroceryListException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * Tester si la caisse n'accepte pas les listes plus de 10 articles; Ici testé
//	 * avec 11 produits
//	 * 
//	 * Résultat attendu : TooManyItemsException
//	 */
//	@Test
//	public void tooManyItemGroceryList() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918503"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918504"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918505"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918506"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918507"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918508"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918509"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918510"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918511"), "Beef", 0.5, 5.75));
//		assertThrows(TooManyItemsException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * Tester l'ajout de plusieurs produits avec le même code dans la liste d'item
//	 * de la caisse; Ici testé avec 2 produits
//	 * 
//	 * Résultat attendu : InvalidQuantityForCategoryException
//	 */
//	@Test
//	public void duplicateItemGroceryList() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
//		assertThrows(InvalidQuantityForCategoryException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * Tester la suppression d'un produit qui n'existe pas dans la liste
//	 * 
//	 * Résultat attendu : NoSuchItemException
//	 */
//	@Test
//	public void deleteItemGroceryListWhenItemNotExisting() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", -1, 5.75));
//		assertThrows(NoSuchItemException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * On teste l'application d'un coupon dans le cas où le coupon est d'une valeur
//	 * négative
//	 * 
//	 * Résultat attendu : NegativeAmountException
//	 */
//	@Test
//	public void invalidNegativeCouponApplication() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("54323432343"), "Doritos Club", 1, -0.5));
//		assertThrows(NegativeAmountException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * On teste l'application d'un coupon dans le cas où le coupon est d'une valeur
//	 * superieur au total d'achat
//	 * 
//	 * Résultat attendu : le coupon ne doit pas être appliqué
//	 */
//	@Test
//	public void invalidSuperieurCouponApplication() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("54323432343"), "Doritos Club", 1, 10));
//		System.out.println(register.print(grocery));
//	}
//
//	/*
//	 * On teste l'application d'un coupon dans le cas où le coupon possède un CUP de
//	 * moins de 12 chiffres
//	 * 
//	 * Résultat attendu : UpcTooShortException
//	 */
//	@Test
//	public void invalidSmallCUPCoupon() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("5434334343"), "Doritos Club", 1, 0.5));
//		assertThrows(UpcTooShortException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * On teste l'application d'un coupon dans le cas où le coupon possede un CUP de
//	 * plus de 12 chiffres
//	 * 
//	 * Résultat attendu : UpcTooLongException
//	 */
//	@Test
//	public void invalidLargeCUPCoupon() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 5.75));
//		grocery.add(new Item(Upc.generateCode("5434334343343"), "Doritos Club", 1, 0.5));
//		assertThrows(UpcTooLongException.class, () -> {
//			register.print(grocery);
//		});
//	}
//
//	/*
//	 * On verifie l'application du rabais quand 5 articles du même CUP (5 fois le
//	 * même article) sont ajoutés au panier
//	 * 
//	 * Résultat attendu : le rabais de 1$ ne doit pas être appliqué
//	 */
//	@Test
//	public void invalidSameCUPItemsDiscount() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 6, 5.75));
//		System.out.println(register.print(grocery));
//	}
//
//	/*
//	 * On verifie l'application du rabais quand moins de 5 articles sont ajoutés au
//	 * panier
//	 * 
//	 * Résultat attendu : le rabais de 1$ ne doit pas être appliqué
//	 */
//	@Test
//	public void invalid4ItemsDiscount() {
//		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 5.75));
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 1.96));
//		grocery.add(new Item(Upc.generateCode("12645678901"), "Chocolate", 1, 3.05));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 7.50));
//		System.out.println(register.print(grocery));
//	}
//
//	/*
//	 * On verifie l'application du rabais quand 5 articles avec une valeur totale de
//	 * moins de 2$ sont ajoutés au panier
//	 * 
//	 * Résultat attendu : le rabais de 1$ ne doit pas être appliqué
//	 */
//	@Test
//	public void invalidPriceDiscount() {
//		grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 1, 0.1));
//		grocery.add(new Item(Upc.generateCode("12804918500"), "Flower", 3, 0.1));
//		grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 0.1));
//		grocery.add(new Item(Upc.generateCode("12645678901"), "Chocolate", 1, 0.1));
//		grocery.add(new Item(Upc.generateCode("12804918502"), "Sheep", 1, 0.1));
//		System.out.print(register.print(grocery));
//	}
	
	
	
	
	
	
}
