import java.util.*;
/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {
    private int vSize;
    private Map<Label,Integer> docLabel;
    private Map<Label,Integer> wordLabel;
    private Map<String,Integer> PosMap;
    private Map<String,Integer> NegMap;
    private double posLog;
    private double negLog;
    private int trainSize;
    
    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
    	vSize=v;
    	trainSize = trainData.size();
    	PosMap=new HashMap<>();
    	NegMap=new HashMap<>();
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store them. 
    	
    	wordLabel = getWordsCountPerLabel(trainData);
    	docLabel = getDocumentsCountPerLabel(trainData);
    	
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
    
    
        for(Instance inst: trainData){
            if(inst.label == Label.POSITIVE){
                for(String str: inst.words){
                	if(str==null) {
                		continue;
                	}
                
                	
                    PosMap.put(str,PosMap.getOrDefault(str,0)+1);
                }
            }
            else if(inst.label == Label.NEGATIVE){
                for(String str: inst.words){
                	if(str==null) {
                		continue;
                	}   
                    NegMap.put(str,NegMap.getOrDefault(str,0)+1);
                }
            }
        }
        
       
      
        
        // Save these information as you will need them to calculate the log probabilities later.
        //
        if(trainData.size()==0) {
        	posLog=0;
        	negLog=0;
        }
        else {
        	posLog=p_l(Label.POSITIVE);
        	negLog=p_l(Label.NEGATIVE);
        }
    	
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	Map<Label, Integer> map =new HashMap<>();
    	int positive=0;
    	int negative=0;
    	
    	for(Instance r: trainData) {
    		if(r.label==Label.POSITIVE) {
    			positive+=r.words.size();
    		}
    		else if(r.label==Label.NEGATIVE) {
    			negative+=r.words.size();
    		}
    	}
    	
    	map.put(Label.POSITIVE,positive);
    	map.put(Label.NEGATIVE,negative);
    	
        return map;
    }


    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
    	Map<Label, Integer> map =new HashMap<>();
    	int positive=0;
    	int negative=0;
    	
    	for(Instance r: trainData) {
    		if(r.label==Label.POSITIVE) {
    			positive+=1;
    		}
    		else if(r.label==Label.NEGATIVE) {
    			negative+=1;
    		}
    	}
    	
    	map.put(Label.POSITIVE,positive);
    	map.put(Label.NEGATIVE,negative);
    	
        return map;
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label) {
        // TODO : Implement
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
    	if(trainSize==0) {
    		return 0;
    	}
    	
    	if(docLabel.get(label)==0) {
    		return 0;
    	}
    	
        return (double)docLabel.get(label)/trainSize;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
   

    	int btm = 0;
    	if(label == Label.POSITIVE) {
    	if(PosMap.containsKey(word)) {
    		btm += (double) PosMap.getOrDefault(word, 0);
    	 }
    	}
    	if(label == Label.NEGATIVE) {
        if(NegMap.containsKey(word)) {
        		btm += (double) NegMap.getOrDefault(word, 0);
        	 }
        	}
    	double result = ((double)btm+1)/(vSize+wordLabel.get(label));
       

       
      return result;
    	
       
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
    	double posAns;
    	double negAns;
   
    	posAns = Math.log(posLog);
    	negAns = Math.log(negLog);
    	
    	for(String str: words){
            double posHelper = p_w_given_l(str, Label.POSITIVE);
            double negHelper = p_w_given_l(str, Label.NEGATIVE);
            if(posHelper == 0) {
            	posHelper = 1*(-1.0);
            	posAns+=posHelper;
            }
            else  {
            	posHelper = (double)Math.log(posHelper);
            	posAns+=posHelper;
            }
            
            if(negHelper == 0) {
            	negHelper = 1*(-1.0);
            	negAns+=negHelper;
            }
            else {
            	negHelper = (double)Math.log(negHelper);
            	negAns+=negHelper;
            }
            
        }
    	ClassifyResult result = new ClassifyResult();
        if(posAns<negAns){
            result.label = Label.NEGATIVE;
        }
        else{
            result.label = Label.POSITIVE;
        }

        Map<Label, Double> logs = new HashMap<Label, Double>();
        logs.put(Label.POSITIVE,posAns);
        logs.put(Label.NEGATIVE,negAns);

        result.logProbPerLabel = logs;
        
        return result;
        
      }
   

}
