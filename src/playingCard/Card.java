package playingCard;
/**
 * 
 * @author ChaoChih Liu
 * this data object is to present playing cards
 *
 */
public class Card {

	private CardRank cardRank = null;
	private CardSuit cardSuit = null;
	
	/*
	 * rank means the 'number' on the card, like A, 10, 9...
	 * suit means the 'shape' on the card, like Spade, Club...
	 */
	public Card( char rank, char suit ){
		
		this.cardRank = CardRank.takeCardRank(rank);
		this.cardSuit = CardSuit.takeSuit(suit);
	}
	
	public String toString(){
		
		return new StringBuffer().append(this.cardRank.rank).append(this.cardSuit.abbrev).toString();
		
	}
	
	public boolean isHigher(Card card){
		
		if( this.cardRank.isHigher(card.cardRank) )
			return true;
		
		return false;
	}
	
	public boolean isLower(Card card){
		
		if( this.cardRank.isLower(card.cardRank) )
			return true;
		
		return false;
	}
	
	public CardRank getRank(){
		return cardRank;
	}
	
	public CardSuit getSuit(){
		return cardSuit;
	}
	
	/**
	 * 
	 * @param firstCard
	 * @return boolean
	 * 
	 * verify 'this' card is after parameter card
	 * 
	 */
	public boolean isNextCardOf(Card firstCard) {
		
		return isNextCardOf(firstCard, 1);
	}
	
	public boolean isNextCardOf(Card firstCard, int interval) {
		
		if( (firstCard.getRank().sequence+interval) == cardRank.sequence )
			return true;
		
		return false;
	}
	
	public boolean isSameRank(Card secondCard) {
		
		if( cardRank == secondCard.cardRank )
			return true;
		
		return false;
	}
	
	public int getFaceValue() {
		return cardRank.getFaceValue();
	}
	
	enum CardRank{
		ACE('A', 1, 1),
	    TWO('2', 2, 2),
	    THREE('3', 3, 3),
	    FOUR('4', 4, 4),
	    FIVE('5', 5, 5),
	    SIX('6', 6, 6),
	    SEVEN('7', 7, 7),
	    EIGHT('8', 8, 8),
	    NINE('9', 9, 9),
	    TEN('T', 10, 10),
	    JACK('J', 10, 11),
	    QUEEN('Q', 10, 12),
	    KING('K', 10, 13);
		
		private char rank;
		private int value;
		private int sequence;
		
		private CardRank(char rank, int faceValue, int sequence){
			this.rank = rank;
			this.value = faceValue;
			this.sequence = sequence;
		}
		CardRank(char rank){
			this.rank = rank;
		}
		
		public int getFaceValue(){
			return this.value;
		}
		
		public boolean isPair( CardRank secondCard ){
			if( null == secondCard )
				return false;
			
			if( this.rank == secondCard.rank )
				return true;
				
			return false;
		}
		
		public boolean isHigher( CardRank secondCard ){
			if( null == secondCard )
				return false;
			
			if( this.sequence > secondCard.sequence )
				return true;
			
			return false;
		}
		
		public boolean isLower( CardRank secondCard ){
			if( null == secondCard )
				return false;
			
			if( this.sequence < secondCard.sequence )
				return true;
			
			return false;
		}
		
		public static CardRank takeCardRank( char rank ){
			
			for( CardRank value : values() ){
				if( value.rank == rank )
					return value;
			}
			
			return null;
		}
	}
	
	enum CardSuit{
		CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S');
		
		private char abbrev;
		CardSuit(char abbrev){
			this.abbrev = abbrev;
		}
		
		public boolean isSameSuit( CardSuit secondCard ){
			if( null == secondCard )
				return false;
			
			if( abbrev == secondCard.abbrev )
				return true;
			
			return false;
		}
		
		public static CardSuit takeSuit( char abbrev ){
			
			for( CardSuit value : values() ){
				if( value.abbrev == abbrev )
					return value;
			}
			
			return null;
		}
		
	}

	public boolean isSameSuit(Card secondCard) {
		
		if( cardSuit.isSameSuit(secondCard.cardSuit) )
			return true;
		
		return false;
	}

}
