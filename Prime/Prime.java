import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Prime {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    String in = scan.nextLine();
    int num = Integer.parseInt(in);
    Instant starts = Instant.now();
    System.out.println(isPrime(num));
    Instant ends = Instant.now();
    System.out.println(Duration.between(starts, ends));
    System.out.println("---------------------------------------");
    primeFactors(num);
  }
  
  
 public static boolean isPrime(int num) {
  for(int i = 1; i<num; i++) {
    if(num % i == 0 & i != 1) {
      return false;
    } 
  }
   return true;
 }
 
 
 public static boolean isFactor(int num1, int num2) {
   return num1 % num2 == 0;
 }
 
 
 public static void primeFactors(int num) {
   ArrayList<Integer> results = new ArrayList<Integer>();
   if(!isPrime(num)) {
     while(!isPrime(num)) {
       for(int i = 2;i<num;i++) {
         if(isFactor(num,i)) {
           results.add(i);
           num = num/i;
           break;
         }
       }
     }
   }
   results.add(num);
   Collections.sort(results);
   System.out.println(results.toString());
  } 
 
 
 
 }
  

