package playingCard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 22 Sep 2014
 * 
 * The purpose of this class is to keep not only code easy-to-maintain, 
 * but also better performance.
 * 
 * for example, the array list with prefix: 'DISCARD_' pre-defined the all permutation combinations, 
 * other methods are able to discard based on the index of element in list.
 * 
 */
public class Constant {

	//we only consider 4 cards every time.
	public static final int CARD_LIMIT = 4;
	public static final List<Integer> DISCARD_ONE_OF_FIVE = Arrays.asList( 0, 1, 2, 3, 4);
	public static final List<Integer> DISCARD_ONE_OF_FOUR = Arrays.asList( 0, 1, 2, 3 );
	
	public static final List<List<Integer>> DISCARD_TWO_OF_SIX = new ArrayList<List<Integer>>();
	public static final List<List<Integer>> DISCARD_TWO_OF_FOUR = new ArrayList<List<Integer>>();
	static{
		
		if( DISCARD_TWO_OF_SIX.isEmpty() ){
			int[] a = {0, 1, 2, 3, 4, 5};  
			int n = 2;  
			DISCARD_TWO_OF_SIX.addAll(generateDiscardCombo(a,n));
		}
        
		if( DISCARD_TWO_OF_FOUR.isEmpty() ){
			int[] b = {0, 1, 2, 3};  
			int n = 2;
			DISCARD_TWO_OF_FOUR.addAll(generateDiscardCombo(b,n));
		}
		
	}
	private static List<List<Integer>> generateDiscardCombo(int[] numbers, int discardAmount) {  
        
		List<List<Integer>> result = new ArrayList<List<Integer>>();
        if(null == numbers || numbers.length == 0 )  
            return result;  
        if( discardAmount <= 0 || discardAmount > numbers.length )
        	return result;
              
        int[] b = new int[discardAmount]; 
        result.addAll(compositeDiscards(numbers, discardAmount , 0, b, 0));  
        
        return result;
    }  
  
    private static List<List<Integer>> compositeDiscards(int[] numbers, int discardAmount, int begin, int[] temp, int index) {  
          
    	List<List<Integer>> result = new ArrayList<List<Integer>>();
        if(discardAmount == 0){ 
        	List<Integer> list = new ArrayList<Integer>();
            for(int i = 0; i < index; i++){  
            	list.add(temp[i]);
            }  
            result.add(list);
            return result;  
        }  
              
        for(int i = begin; i < numbers.length; i++){  
              
        	temp[index] = numbers[i];  
            result.addAll(compositeDiscards(numbers, discardAmount-1, i+1, temp, index+1));  
        }  
         
        return result;
    }  
	
    /**
     * Here is another permutation combination to find out all possible way to get 15 with 2, 3, or 4 given number. 
     * e.g. [2, 5, 8] [7, 8] or [2, 2, 3, 8]
     * Then other methods are able to match the pattern to figure out whether the cards in hand match these patterns.
     */
	public static final List<List<Integer>> PATTERN_FOR_15S = new ArrayList<List<Integer>>();
	static{
		
		if( PATTERN_FOR_15S.isEmpty() ){
		
			for( int i = 2; i<=4; i++ ){
				generate( 15, i );
			}
		}
	}
	
	private static void generate(int lumpSum, int numbersSplitTo) {  
        
        if(lumpSum <= 0 || numbersSplitTo <=0)  
            return;  
          
        getCombination(lumpSum, numbersSplitTo, new int[numbersSplitTo], 0, 1, PATTERN_FOR_15S);
    }  
    private static void getCombination(int lumpSum, int numbersSplitTo, int[] temp, 
    									int index, int begin, List<List<Integer>> list) {  
    	
        if(lumpSum == 0 && numbersSplitTo == 0){  
        	List<Integer> result = new ArrayList<Integer>();
            for(Integer i : temp)  
            	result.add(i);
            
            list.add(result);
            return;
        }  
        
        if(numbersSplitTo == 0)  
            return;
          
        for(int i = begin; i <= 10; i++){  
            temp[index] = i;  
            getCombination(lumpSum-i,numbersSplitTo-1, temp,index+1,i, list);
        }  
          
    }
	
}
