package playingCard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;


/**
 * 22 Sep 2014
 * 
 * How it works?
 * 
 * When input comes, SelectHand will be required to translate the input(data type as String), 
 * and client has to invoke method 'chooseCards' to get the result.
 * 
 * After passing input into the sole constructor of SelectHand, few steps will happen
 * 1. Translate, turn input (data type as String) into object 'Card'
 * 2. Card used to present ranking(A, 2, 3, 4...) and suit(Clubs, Heart...), and sort cards in sequence by comparing ranking.
 * 	  Sorting input here is mainly for performance purpose.
 * 3. After invoking 'chooseCards', SelectHand begin to pick up the most potential combination.
 * 4. pickCombo will compile all possible combo( it is always 4 cards ), and pass into another object 'CardSelection'.
 * 5. Only 1 constructor in CardSelection is designed to get 4 card combo. 
 * 6. CardSelection starts to calculate credit point, few methodes will be invoked: calculateNob, calculateFlush, calculateRuns, calculatePairs, calculate15s
 * 7. Getting credit points for all combo, sorting again, but this time sorting will base on the credit point.
 * 8. Choosing the composition with highest credit point and turn into output which is data type String.
 */
public class SelectHand {
	
	private Logger log = Logger.getAnonymousLogger();
	
	//cards is the collection of input after turning into Card object.
	private List<Card> cards = new ArrayList<Card>();
	//this is the final result waiting for turning into String.
	private List<Card> result = new ArrayList<Card>();
	
	private List<CardSelection> possibleCredits = new ArrayList<CardSelection>();
	
	public SelectHand(String[] args) {
		
		translate(args);
		
	}

	private void translate(String[] args) {
		if( null == args || args.length <= 0 )
			return;
		
		for( String input : args ){
			if( null == input || input.trim().length() <= 1 )
				continue;
			
			//assuming the input always contain 1 figure(present ranking) and 1 character(present suti) 
			Card newCard = new Card( input.charAt(0), input.charAt(1) );
			cards.add(newCard);
		}
		
		//sorting list of card according to ranking, highest ranking will be in the tail of list.
		Collections.sort(cards, new Comparator<Card>(){

			@Override
			public int compare(Card o1, Card o2) {
				
				if( o1.isHigher(o2) )
					return 1;
				if( o1.isLower(o2) )
					return -1;
				
				return 0;
			}
			
		});
		
	}

	public String chooseCards() {
		if( cards.isEmpty() )
			return null;
		
		pickCombo();
		
		return convertResult();
	}
	
	
	private void pickCombo() {
		
		compositeAllSelection();
		
		/*
		 * sorting all possible combo, according to credit point which the combo may get.
		 * highest credit point will be in the head of list. 
		 */
		Collections.sort(possibleCredits, new Comparator<CardSelection>(){

			@Override
			public int compare(CardSelection o1, CardSelection o2) {
				
				if( o1.getCredit() > o2.getCredit() )
					return -1;
				if( o1.getCredit() < o2.getCredit() )
					return 1;
				
				return 0;
			}
			
		});
		
		for( CardSelection cardSelection : possibleCredits ){
			log.info(cardSelection.getSelection() + " got score " + cardSelection.getCredit());
		}
		
		
		result.addAll( possibleCredits.get(0).getSelection() );
	}

	/**
	 * user may enter 4 - 6 different cards
	 * to calculate points, code has to know the possible combinations
	 * this function is to tell all possible cards combination out of n cards.
	 */
	private void compositeAllSelection() {
		
		//if input only contains 4 cards, then just return it.
		if( cards.size() == Constant.CARD_LIMIT ){
			possibleCredits.add( new CardSelection(cards) );
			return;
		}

		//if input contains 5 cards, then discard 1 by 1.
		if( cards.size() == 5 ){
			for( Integer discardNumber : Constant.DISCARD_ONE_OF_FIVE ){
				List<Card> temp = new ArrayList<Card>();
				temp.addAll(cards);
				temp.remove(temp.get(discardNumber));
				possibleCredits.add( new CardSelection(temp) );
			}
		}
		
		//if input contains 6 cards, then discard different 2 cards every time to get all kinds of composition.
		if( cards.size() == 6 ){
			for( List<Integer> discardNumbers : Constant.DISCARD_TWO_OF_SIX ){
				List<Card> temp = new ArrayList<Card>();
				temp.addAll(cards);
				List<Card> removeList = new ArrayList<Card>();
				for( Integer discardNumber : discardNumbers ){
					removeList.add(temp.get(discardNumber));
				}
				temp.removeAll(removeList);
				possibleCredits.add( new CardSelection(temp) );
			}
		}
		
	}

	/**
	 * 
	 * @return String, showing card chosen after calculation
	 */
	private String convertResult() {
		StringBuffer buffer = new StringBuffer();
		for( Card card : result ){
			buffer.append(card.toString()).append(" ");
		}
		
		return buffer.toString().trim();
	}
	
	public CardSelection getBestCombo() {
		return this.possibleCredits.get(0);
	}

	/**
	 * @param args
	 * 
	 * How to present cards
	 * The first character is 'number', the rule followed
	 * 	1. 	A for Ace, K for King, Q for Queen, J for Jack, T for Ten
	 * 	2.	others are 2-9
	 * 
	 * The second character should be a C for Clubs, D for Diamonds, H for Hearts, or a S for Spades
	 * 
	 * 4-6 different cards might be in the arrays.
	 */
	public static void main(String[] args) {
		
		args = new String[]{"6C", "8C", "5C", "4C"};
//		args = new String[]{"7H", "9S", "8C", "7C", "3C", "2S"};
//		args = new String[]{"7H", "9C", "6C", "7C", "2C"};
//		args = new String[]{"7H", "9C", "8C", "7C", "6C"};
//		args = new String[]{"9S", "8C", "3S", "2S", "JS"};
//		args = new String[]{"TH", "3S", "6C", "5C"};
//		args = new String[]{"7C", "QH", "2C", "JC"};
//		args = new String[]{"AS", "3H", "KH", "7H"};
//		args = new String[]{"AS", "3H", "5S", "8H", "9D"};
//		args = new String[]{"AS", "JH", "5S", "QH", "KH"};
		
		SelectHand selectHand = new SelectHand( args );
//		System.out.println("args : " + Arrays.toString(args) + ", " + selectHand.chooseCards());
		System.out.println(selectHand.chooseCards());
		
	}

}
