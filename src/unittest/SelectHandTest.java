package unittest;

import java.util.logging.Logger;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import playingCard.SelectHand;

/**
 * 
 * @author ChaoChih Liu
 * Unit Test, rules:
 * 1. 1 jack get 1 point
 * 2. only 4 cards are in the same suit, then getting points.
 * 3. at least 3 cards are number in a row, get 3 points.
 * 4. any 2 cards in the same rank, get 2 points
 * 	  any 3 cards in the same rank, get 6 points
 *    any 4 cards in the same rank, get 12 points
 * 5. sum of card value (rank) is 15
 *
 */

public class SelectHandTest {
	
	private Logger log = Logger.getAnonymousLogger();

	
	@Test
	public void test5Cards() {
		
		String[] args = new String[]{"5H", "9C", "6C", "7C", "2C"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "2C 6C 7C 9C", selectHand.chooseCards()); 
		assertEquals( "success creidt", 8, selectHand.getBestCombo().getCredit()); //all Clubs->get 4 points, 9+6=>get 2 point, 2+6+7->get 2 points
	}
	
	@Test
	public void test6Cards() {
		
		String[] args = new String[]{"5H", "5S", "5D", "JS", "AC", "2S"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "5H 5S 5D JS", selectHand.chooseCards()); 
		//1 Jact->get 1 point, 5H 5S 5D in pars->get 6 points, (5, J) 3 times & (5, 5, 5)=15->get 8 points
		assertEquals( "success creidt", 15, selectHand.getBestCombo().getCredit()); 
		
	}
	
	@Test
	public void testNumberInRows() {
		
		String[] args = new String[]{"7H", "9S", "8C", "7C", "3C", "2S"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "7H 7C 8C 9S", selectHand.chooseCards()); 
		//(7H, 8C, 9S) number in the row->get 3 points, (7C, 8C, 9S) number in the row->3 points, (7H, 7C) in pairs->get 2 point, (7C, 8C) & (7H, 8C)->sum is 15, get 4 points in total
		assertEquals( "success creidt", 12, selectHand.getBestCombo().getCredit()); 
	}
	
	@Test
	public void testNumberClubs() {
		
		String[] args = new String[]{"4H", "3S", "6C", "JC"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "3S 4H 6C JC", selectHand.chooseCards()); 
		//1 Jack->get 1 points
		assertEquals( "success creidt", 1, selectHand.getBestCombo().getCredit()); 
	}
	
	@Test
	public void testNumber15s() {
		
		String[] args = new String[]{"TH", "3S", "6C", "5C"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "3S 5C 6C TH", selectHand.chooseCards()); 
		//(TH, 5C) sum is 15->get 2 points
		assertEquals( "success creidt", 2, selectHand.getBestCombo().getCredit()); 
	}
	
	@Test
	public void testPairs() {
		
		String[] args = new String[]{"AH", "6S", "6C", "5C", "5H"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "5C 5H 6S 6C", selectHand.chooseCards()); 
		//2 different pars->get 4 points
		assertEquals( "success creidt", 4, selectHand.getBestCombo().getCredit()); 
	}
	
	@Test
	public void testFlush() {
		
		String[] args = new String[]{"AH", "6C", "8C", "5C", "4C"};
		SelectHand selectHand = new SelectHand( args );
		assertEquals( "success combo", "4C 5C 6C 8C", selectHand.chooseCards()); 
		//(4, 5, 6) in th row->3 point, (4+5+6=15)->2 point, all Clubs->4 points
		assertEquals( "success creidt", 9, selectHand.getBestCombo().getCredit()); 
	}

}
