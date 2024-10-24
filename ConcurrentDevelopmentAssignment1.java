import java.util.Random;

class SearchThread extends Thread{

    private int start;
    private int end;

    public SearchThread(int start, int end){
        this.start = start;
        this.end = end;
    }

    public Boolean isPalindrome(String str) { 

        int length = str.length(); 

        for (int i = 0; i < length / 2; i++) { 

            if (str.charAt(i) != str.charAt(length - i - 1)) { 

                break; 

            } 

        }

        return true;
    } 
}

public class ConcurrentDevelopmentAssignment1 {
    
    public static final int MatrixSize = 10;
    
    public static void main(String[] args) {
        Random rand = new Random();
        
        char[][] matrix = new char[MatrixSize][MatrixSize];

        int numThreads = Runtime.getRuntime().availableProcessors();

        for(int i = 0;i < MatrixSize;i++) {
            for(int j = 0;j < MatrixSize;j++) {
                matrix[i][j] = (char)('a' + rand.nextInt(26)); //assign random char in matrix
            }
        }

        for(int i = 0; i < numThreads; i++){
            Parallel(i);
        }

    }

    public static void Parallel(int numThreads){
        SearchThread[] t = new SearchThread[numThreads];
        int rowsPerThread = MatrixSize / numThreads;

        for(int i = 0; i < numThreads;i++){
            int start = i * rowsPerThread; // start row for each thread
            int end = 0;
            if(i == numThreads - 1){
                end = MatrixSize; // if is last thread then the end point the size
            }else{
                end = start + rowsPerThread; // else just do this
            }
            
            t[i] = new SearchThread(start, end);
            t[i].start();         
        }

        try{
            for(int i = 0; i < numThreads; i++){
                t[i].join();
            }
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }    
}
