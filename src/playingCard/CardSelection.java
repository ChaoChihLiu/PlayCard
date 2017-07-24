package playingCard;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author ChaoChih Liu
 * CardSelection present cards chosen from card piles, 
 * and calculate the points according to combination
 *
 */
public class CardSelection {

	private List<Card> cards = new ArrayList<Card>();
	private int optimisticCredit = 0;
	
	public CardSelection(List<Card> selection){
		this.cards.addAll(selection);
		calculateCredit();
	}
	
	public void calculateCredit() {
		calculateNob();
		calculateFlush();
		calculateRuns();
		calculatePairs();
		calculate15s();
	}

	/**
	 * sum of card value (rank) is 15, get 2 points
	 */
	private void calculate15s() {
		int point = 0;
		
		//turn all cards into String which is face value. it is easier to compare with all permutation combinations to get 15s 
		List<Integer> cardFaceValue = turnToFaceValue( cards );
		
		for( List<Integer> pattern : Constant.PATTERN_FOR_15S ){
			if( ! contains( cardFaceValue, pattern ) )
				continue;
			
			int count = hitPatternCount( pattern, cards );
			point = point + (2*count);
		}
		optimisticCredit += point;
	}
	
	private boolean contains( List<Integer> cards, List<Integer> pattern ){
		Map<Integer, Integer> cards_map = countNumOfElement( cards );
		Map<Integer, Integer> patter_map = countNumOfElement( pattern );
		
		Set<Map.Entry<Integer, Integer>> set = patter_map.entrySet();
		for( Map.Entry<Integer, Integer> entry : set ){
			if( ! cards_map.containsKey(entry.getKey()) ) return false;
			
			int num = cards_map.get(entry.getKey());
			if( num < entry.getValue() ) return false;
		}
		
		return true;
	}
	
	private Map<Integer, Integer> countNumOfElement( List<Integer> a ){
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for( Integer el : a ){
			
			if( ! map.containsKey(el) ){
				map.put(el, 0);
			}
			
			map.put(el, map.get(el)+1);
			
		}
		
		return map;
	}
	
	private List<Integer> turnToFaceValue(List<Card> cards) {
		
		List<Integer> list = new ArrayList<Integer>();
		if( cards.isEmpty() )
			return list;
		
		for( Card card : cards ){
			list.add(card.getFaceValue());
		}
		return list;
	}

	/**
	 * 
	 * @param pattern, permutation combination of cards to calculate 15.
	 * @param cardsInHand, the list of cards.
	 * @return Integer
	 * 
	 * this is the tricky part of this project, 
	 * for example, assuming card input is [5S, 5D, 10S, 6C], 
	 * and 1 of all permutation combination getting 15 is '5 + 10', 
	 * in this case, here are 2 different combinations to fulfill this pattern, 
	 * 1 is 5S + 10S, another is 5D + 10S, 
	 * however, neither 'ArrayList.contains' nor 'String.contains' is able to find all possibility, 
	 * because these 2 methods are merely telling you whether List or String contains those elements.
	 * so input cards need to be discarded in for loop 1 more time to find out how many card combinations to meet pattern.
	 * 
	 */
	private int hitPatternCount(List<Integer> pattern, List<Card> cardsInHand) {
		
		if( pattern.size() == 4 )
			return 1;
		
		int hits = 0;
		
		if( pattern.size() == 2 ){
			
			for( List<Integer> discards : Constant.DISCARD_TWO_OF_FOUR ){
				List<Card> temp = new ArrayList<Card>();
				temp.addAll(cardsInHand);
				List<Card> removeList = new ArrayList<Card>();
				for( Integer discardNumber : discards ){
					removeList.add(temp.get(discardNumber));
				}
				temp.removeAll(removeList);
				
				List<Integer> cardFaceValue = turnToFaceValue( temp );
				if( ! contains(cardFaceValue, pattern) )
					continue;
				
				hits += 1;
			}
			
		}
		
		if( pattern.size() == 3 ){
			
			for( Integer discardNumber : Constant.DISCARD_ONE_OF_FOUR ){
				
				List<Card> temp = new ArrayList<Card>();
				temp.addAll(cardsInHand);
				temp.remove(temp.get(discardNumber));
				
				List<Integer> cardFaceValue = turnToFaceValue( temp );
				if( !contains(cardFaceValue, pattern) )
					continue;
				
				hits += 1;
			}
			
		}
		
		return hits;
	}

	/**
	 * any 2 cards in the same rank, get 2 points
	 * any 3 cards in the same rank, get 6 points
	 * any 4 cards in the same rank, get 12 points
	 * 
	 */
	private void calculatePairs() {
		int point = 0;
		Map<Integer, Integer> cardMap = this.countNumOfElement(turnToFaceValue(cards));
		Set<Map.Entry<Integer, Integer>> set = cardMap.entrySet();
		for( Map.Entry<Integer, Integer> entry : set ){
			
			int inPairs = entry.getValue();
			
			if( inPairs == 2 )
				point += 2;
			if( inPairs == 3 )
				point += 6;
			if( inPairs == 4 )
				point += 12;
		}
		
		optimisticCredit += point;
	}

	/**
	 * according to rule, at least 3 numbers are continuous to get point.
	 */
	private void calculateRuns() {
		int point = 0;
		int cardInRow = 1;
		
		boolean sameRank = false;
		for( int i = 0; i<(cards.size()-1); i++ ){
			Card firstCard = cards.get(i);
			Card secondCard = cards.get(i+1);
			
			if( secondCard.isNextCardOf(firstCard) )
				cardInRow += 1;
			
			if( firstCard.getRank() == secondCard.getRank() )
				sameRank = true;
		}
	
		if( cardInRow >= 3 )
			point += cardInRow;
		
		/*
		 * Assuming 2S, 3D, 4C, 4H are in hand, 
		 * here are 2 different combinations for runs, 
		 * that is why we have to know whether any cards are the same rank.
		 * Because only 4 cards are allowed to stay in hand, 
		 * 
		 * a reasonable assumption is at most 2 cards are in the same rank, 
		 * at the same time the cards chosen meet 3 number in a row.
		 */
		if( sameRank )
			point = point * 2;
		
		optimisticCredit += point;
	}

	/**
	 * only 4 cards are in the same suit, then getting points.
	 */
	private void calculateFlush() {
		int point = 0;
		
		Map<Card.CardSuit, Integer> countSameSuit = new HashMap<Card.CardSuit, Integer>();
		for( Card cardInHand : cards ){
			
			Card.CardSuit suit = cardInHand.getSuit();
			if( countSameSuit.containsKey(suit) ){
				int count = countSameSuit.get(suit) + 1;
				countSameSuit.put(suit, count);
			}else{
				countSameSuit.put(suit, 1);
			}
			
		}
		
		Collection<Integer> counters = countSameSuit.values();
		for( Integer counter : counters ){
			if(counter == 4)
				point += 4;
		}
		
		optimisticCredit += point;
	}

	/**
	 * 1 jack get 1 point
	 */
	private void calculateNob() {
		int point = 0;
		for( Card cardInHand : cards ){
			if( cardInHand.getRank() != Card.CardRank.JACK )
				continue;
			
			point+=1;
		}
		optimisticCredit += point;
	}

	public int getCredit(){
		return optimisticCredit;
	}

	public Collection<Card> getSelection() {
		return cards;
	}
	
}

