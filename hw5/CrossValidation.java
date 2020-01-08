import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        // TODO : Implementcs
        // v is the size of the total vocabulary
        double acc = 0.0;
        int N = trainData.size();

        for (int t = 0; t < k; t++) {   
            double correct = 0;
            List<Instance> train = new ArrayList<Instance>();
            List<Instance> test = new ArrayList<Instance>();
            for (int i = 0; i < trainData.size(); i++) {
                if (i / (N / k) != t) {
                	train.add(trainData.get(i));    
                } 
                else {
                	test.add(trainData.get(i));
                }
            }
                clf.train(train, v);

            for (int i = 0; i < test.size(); i++) {
                if (test.get(i).label == clf.classify(test.get(i).words).label) {
                    correct = (double)correct + 1.0;
                }
            }
            acc += ((double) correct / test.size()) ; 
        }
        return acc / k; 
    }


}

