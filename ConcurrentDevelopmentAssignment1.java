import java.util.Random;
import java.lang.StringBuilder;

class SearchThread extends Thread{

    public final int MatrixSize = 1000;
    private int start;
    private int end;
    private char[][] matrix;
    private int length;
    private int total;

    public SearchThread(int start, int end, int length, char[][] matrix){
        this.start = start;
        this.end = end;
        this.length = length;
        this.matrix  = matrix;
        this.total = 0;
    }

    public int getTotal(){
        return total;
    }

    public void run(){

        for(int j = start; j < end; j++){  //right to left
            for(int k = MatrixSize - 1; k > length - 1; k--){
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < length; i++){
                    char c = matrix[j][k - i];
                    sb.append(c);
                }

                if(isPalindrome(sb.toString())){
                    total++;
                }
            }
        }

        for(int i = 0; i < MatrixSize; i++){ //up to down
            for(int j = start; j < end - length; j++){
                StringBuilder sb = new StringBuilder();
                for(int k = 0; k < length; k++){
                    char c = matrix[j+k][i];
                    sb.append(c);
                }

                if(isPalindrome(sb.toString())){
                    total++;
                }
            }
        }
        
        for(int j = start; j < end - length; j++){ //up to down diagonally
            for(int i = 0; i < MatrixSize - length; i++){
                StringBuilder sb = new StringBuilder();
                for(int k = 0; k < length; k++){
                    char c = matrix[j+k][i+k];
                    sb.append(c);
                }

                if(isPalindrome(sb.toString())){
                    total++;
                }
            }
        }

    }


    public Boolean isPalindrome(String str) { 
        int length = str.length(); 

        for (int i = 0; i < length / 2; i++) { 
            if (str.charAt(i) != str.charAt(length - i - 1)) { 
                return false; 
            } 
        }
        return true;
    } 
}

public class ConcurrentDevelopmentAssignment1 {
    
    public static final int MatrixSize = 1000;
    
    public static void main(String[] args) {
        Random rand = new Random();
        
        char[][] matrix = new char[MatrixSize][MatrixSize];

        int numThreads = Runtime.getRuntime().availableProcessors();

        for(int i = 0;i < MatrixSize;i++) {
            for(int j = 0;j < MatrixSize;j++) {
                matrix[i][j] = (char)('a' + rand.nextInt(26)); //assign random char in matrix
            }
        }
        
        for(int i = 1; i < numThreads; i++){ 
            for(int j = 3; j <= 6; j++){ //find the palindrome from 3 - 6 length
                long startTime = System.nanoTime();
                int ans = Parallel(i, j, matrix);
                long endTime = System.nanoTime();
                double time = (endTime - startTime) / 1000000000.0; //convert nano seconds to seconds
                System.out.printf("%d palindromes of size %d found in %.6fs using %d threads.\n", ans, j, time, i);
            }
        }

    }

    public static int Parallel(int numThreads, int length, char[][] matrix){
        SearchThread[] t = new SearchThread[numThreads];
        int rowsPerThread = MatrixSize / numThreads;
        int total = 0;

        for(int i = 0; i < numThreads;i++){
            int start = i * rowsPerThread; // start row for each thread
            int end = 0;
            if(i == numThreads - 1){
                end = MatrixSize; // if is last thread then the end point the size
            }else{
                end = start + rowsPerThread; // else just do this
            }
            
            t[i] = new SearchThread(start, end, length, matrix);
            t[i].start();         
        }

        try{
            for(int i = 0; i < numThreads; i++){
                t[i].join();
                total += t[i].getTotal();
            }
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

        return total;
    }    
}
