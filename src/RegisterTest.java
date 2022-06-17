import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import stev.kwikemart.AmountException;
import stev.kwikemart.InvalidQuantityException;
import stev.kwikemart.InvalidQuantityException.InvalidQuantityForCategoryException;
import stev.kwikemart.InvalidUpcException;
import stev.kwikemart.Item;
import stev.kwikemart.PaperRoll;
import stev.kwikemart.Register;
import stev.kwikemart.Register.NoSuchItemException;
import stev.kwikemart.RegisterException.EmptyGroceryListException;
import stev.kwikemart.RegisterException.TooManyItemsException;
import stev.kwikemart.Upc;

class RegisterTest {

	/*Test invalides
	 * 
	 * - Ajouter un item dans le panier avec prix unitaire négatif, 0 ou supérieur à 35$ 
	 * - Ajouter une autre valeur que 2 pour le code des produit vendu au poids
	 * 
	 * 
	 * 
	 */
	
	
	/*Test valides
	 * 
	 */
	
	
	
	
	private List<Item> grocery;
	private Register register;


	@BeforeEach
	public void setUp() {
		grocery = new ArrayList<Item>();
		register = Register.getRegister();
		
        // Put the small roll of paper into the register
        register.changePaper(PaperRoll.SMALL_ROLL);

	}
	
	
    @AfterEach
	public void tearDown() {
        grocery.clear();
	}
    
    
    
    //INVALIDES TEST
    
    
    @Test
	public void invalidSuperiorAmount() {
		assertThrows(AmountException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, 37));
   	        System.out.println(register.print(grocery));
		});
        grocery.clear();
	}
    
    
    @Test
	public void invalidNegativeAmount() {
		assertThrows(AmountException.class, () -> {
			grocery.add(new Item(Upc.generateCode("12345678901"), "Bananas", 1, -1));
   	        System.out.println(register.print(grocery));
		});
        grocery.clear();
	}
    
    
    
    

    
    
    /*
     * Tester si un produit vendu au poids peut prendre une autre valeur que 2 pour le premier chiffre de son code
     * Résultat attendu : Exception
     * Résultat obtenu : Correct
     */
    @Test
   	public void InvalidCodeForFractionnaryQuantityValue() {
   		assertThrows(InvalidQuantityException.class, () -> {
   	    	grocery.add(new Item(Upc.generateCode("12804918500"), "Beef", 0.5, 5.75));
   	        System.out.println(register.print(grocery));
   		});
        grocery.clear();
   	}
    
    /*
     * Tester si un produit qui n'est pas vendu au poids peut prendre la valeur 2 pour le premier chiffre de son code
     * Résultat attendu : Exception
     * Résultat obtenu : Pas correct
     */
    @Test
   	public void InvalidCodeQuantityValue() {
   		assertThrows(InvalidQuantityException.class, () -> {
   	    	grocery.add(new Item(Upc.generateCode("22804918500"), "Beef", 3, 5.75));
   	        System.out.println(register.print(grocery));
   		});
        grocery.clear();
   	}
    
    
    
    
    /*
     * Tester si la caisse n'accepte pas les listes avec 0 articles  
     * Résultat attendu : Exception
     * Résultat obtenu : Correct
     */
    @Test
   	public void EmptyGroceryListItem() {
   		assertThrows(EmptyGroceryListException.class, () -> {
   	        System.out.println(register.print(grocery));
   		});
   	}
    
    /*
     * Tester si la caisse n'accepte pas les listes plus de 10 articles  
     * Ici testé avec 11 produits
     * Résultat attendu : Exception
     * Résultat obtenu : Correct
     */
    @Test
   	public void TooManyItemGroceryList() {
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

   	        System.out.println(register.print(grocery));
   		});
   	}
    
    
    /*
     * Tester l'ajout de plusieurs produits avec le même code dans la liste d'item de la caisse
     * Ici testé avec 2 produits 
     * Résultat attendu : Exception
     * Résultat obtenu : Correct
     */
    @Test
   	public void DuplicateItemGroceryList() {
   		assertThrows(InvalidQuantityForCategoryException.class, () -> {
   			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
   	    	grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", 0.5, 5.75));
   	        System.out.println(register.print(grocery));
   		});
   	}
    
    /*
     * Tester la suppression d'un produit qui n'existe pas dans la liste
     * Résultat attendu : Exception
     * Résultat obtenu : Correct
     */
    @Test
   	public void DeleteItemGroceryListWhenItemNotExisting() {
   		assertThrows(NoSuchItemException.class, () -> {
   			grocery.add(new Item(Upc.generateCode("12804918501"), "Beef", -1, 5.75));
   	        System.out.println(register.print(grocery));
   		});
   	}
	


}
