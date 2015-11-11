import java.util.*;
import java.io.*;

public class A04{

   public static void main(String[] args) throws IOException{
      System.out.println("Welcome, quiz seeker!");
      
      String qF = "quiz.txt"; //create quiz File
      File f = new File(qF);
      Scanner sc = new Scanner(f);
      ArrayList<Integer> writeFile = new ArrayList<Integer>();//to hold statistics
      ArrayList<Question> inQuestions = new ArrayList<Question>();//hold questions
      int counter = 0;
      while(sc.hasNextLine()){//loop to end
         counter++;
         ArrayList<String> arr = new ArrayList<String>();
         arr.add(sc.nextLine());//question in
         int numQuestions = sc.nextInt();// # of questions in
         Question q = new Question(numQuestions);
         String grb = sc.nextLine();
         for(int i = 0; i < numQuestions ; i++){ //store in ArrayList
            arr.add(sc.nextLine());
         }
         q.qMute(arr.get(0));  //questions
         q.aMute(arr);  //answers
         q.kMute(sc.nextInt()); //Right answer
         q.tMute(sc.nextInt()); //Attempts
         q.rMute(sc.nextInt()); //Right Attempts
         if(sc.hasNextLine()){
            grb = sc.nextLine();
         }
         System.out.println("Question #" + counter + ":");//give output format
         System.out.println(q.qIn());
         System.out.println("Options:");
         for(int i = 0; i < numQuestions; i++){
            String[] ans = q.aIn();
            System.out.println("<"+ i + "> " + ans[i]);
         }
         System.out.println();
         writeFile.add(getAnswer(numQuestions));
         System.out.println();
         inQuestions.add(q);
      }
      sc.close();
      giveAnswers(inQuestions, counter, writeFile);
      giveStats(inQuestions, counter, writeFile, qF);
   }
   public static int getAnswer(int numQuestions){
      int input = -1;
      boolean boolCheck = false; //Loop until true
      while(!boolCheck){
         System.out.print("What do you think? > ");
         Scanner sc2 = new Scanner(System.in);
         if(sc2.hasNextInt()){
            input = sc2.nextInt();
            if(input >= 0 && input < numQuestions){ //Valid integer Check if
               boolCheck = true;
            }
         }
      }
      return input;
   }

   public static void giveAnswers(ArrayList<Question> inQuestions, int counter, ArrayList<Integer> writeFile){//Display Results
      System.out.println("Thanks for participating");
      System.out.println("Results:\n");
      for(int i = 0; i < counter; i++){
         Question q = inQuestions.get(i);
         System.out.println("Question: " + q.qIn());
         String[] allAnsr = q.aIn();
         String answer = allAnsr[q.kIn()];
         System.out.println("Correct Answer: " + answer);
         int input = writeFile.get(i);
         String userAnswer = allAnsr[input];
         System.out.println("Your guess: " + userAnswer);
         System.out.print("\tResult: ");
         if(q.kIn() == writeFile.get(i)){
            System.out.println("Nice Job! You actually got it right!\n");
         }else{
            System.out.println("Wrong-o.Study harder.\n");
         }
      }
   }
   public static void giveStats(ArrayList<Question> inQuestions, int counter, ArrayList<Integer> writeFile, String qF) throws IOException{//Display Statisics
      int right = 0, wrong = 0;
      for(int i = 0; i < counter; i++){
         Question q = inQuestions.get(i);
         if(q.kIn() == writeFile.get(i)){
            right++;
            q.tMute(1);
            q.rMute(1);
         }else{
            wrong++;
            q.tMute(1);
         }
      }
      double percentRight = ((double)right / (right + wrong)); //format output
      System.out.println("Here's how you did:");
      System.out.println("\tRight: " + right);
      System.out.println("\tWrong: " + wrong);
      System.out.println("\tPercentage Right (out of 1.00): " + percentRight + "\n");
      ArrayList<Double> addPer = new ArrayList<Double>();
      for(int i = 0; i < counter; i++){
         Question q = inQuestions.get(i);
         System.out.println("Overall:");
         System.out.println("Question: " + q.qIn());
         System.out.println("\tTimes Attempted: " + q.tIn());
         System.out.println("\tTimes Correct: " + q.rIn());
         percentRight = ((double)q.rIn() / q.tIn()) * 100; //determine percentage
         addPer.add(percentRight);
         System.out.println("\tPercentage: " + percentRight + "%");
         writeFile(qF, inQuestions);//input to file
      }
      int length = addPer.size(); 
      int smallV = 0;
      double sVal = addPer.get(0);
      for(int i = 1; i < length; i++){
         if(sVal > addPer.get(i)){
            sVal = addPer.get(i);
            smallV = i;
         }
      }
      double lVal = addPer.get(0);
      int largeV = 0;
      for(int i = 0; i < length; i++){
         if(lVal < addPer.get(i)){
            lVal = addPer.get(i);
            largeV = i;
         }
      }
      System.out.println("\nThe easiest question is:"); //format stats output
      Question q = inQuestions.get(largeV);
      System.out.println(q.qIn());
      System.out.println("\tAttempts: " + q.tIn());
      System.out.println("\tCorrect: " + q.rIn());
      System.out.println("\tPercentage: " +  addPer.get(largeV) + "%");
      System.out.println("\nThe hardest questions is:");
      q = inQuestions.get(smallV);
      System.out.println(q.qIn());
      System.out.println("\tattempts: " + q.tIn());
      System.out.println("\tCorrect: " + q.rIn());
      System.out.println("\tPercentage: " +  addPer.get(smallV) + "%\n");
   }
      public static void writeFile(String qF, ArrayList<Question> inQuestions) throws IOException{//Write to File
      PrintWriter pw = new PrintWriter(qF);
      for(int i = 0; i < inQuestions.size(); i++){
         Question q = inQuestions.get(i);
         pw.println(q.qIn());
         String[] tmp = q.aIn();
         pw.println(tmp.length);
         for(int j = 0; j < tmp.length; j++){
            pw.println(tmp[j]);
         }
         pw.println(q.kIn());
         pw.println(q.tIn());
         pw.println(q.rIn());
      }
      pw.close();
   }
}

class Question{ //Question class as specified
   private String question;
   private String[] allAnsr;
   private int key = 0;
   private int numTimes = 0;
   private int numRight = 0;
   public Question(int numQuestions){// Declare Question Constructor
      allAnsr = new String[numQuestions];
   }
   public String qIn(){ //Question Accessor
      return question;
   }
   public String[] aIn(){ //Answer Accessor
      return allAnsr;
   }
   public int kIn(){ // Key Accessor (Answer Key)
      return key;
   }
   public int tIn(){ //#Attempts Accessor 
      return numTimes;
   }
   public int rIn(){ //#Right Answer Accessor
      return numRight;
   }
   public void qMute(String tmp){ //Question Mutator
      question = tmp;
   }
   public void aMute(ArrayList<String> tmp){ //Answer Mutator
      for(int i = 0; i < allAnsr.length; i++){ //Copies Answers
         String answer = tmp.get(i+1);
         allAnsr[i] = answer;
      }
   }
   public void kMute(int tmp){ //Key Mutator
      key = tmp;
   }
   public void tMute(int tmp){ //#Attempts Mutator
      numTimes += tmp;
   }
   public void rMute(int tmp){//#Right Answer Mutator
      numRight += tmp;
   }
}