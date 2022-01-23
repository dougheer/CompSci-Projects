package com.gradescope.anagram; // DO NOT MODIFY PACKAGE NAME OR CLASS NAME

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Anagram {

  // The type of the dictionary variable can be of your choice.
  private ArrayList<String> dictionary;
  ArrayList<ArrayList<String>> anagrams = new ArrayList<ArrayList<String>>();


  // Load dictionary.txt on class load
  public Anagram(String dictionaryFilename) {
    this.dictionary = loadDictionary(dictionaryFilename);
  }

  // Read the dictionary file into some data structure of your choice. ArrayList is given as an example.
  public ArrayList<String> loadDictionary(String filename) {
    // You can use any other data structure as well, provided you build it from scratch 
    // or use some primitive data structure like list.
    ArrayList<String> dictionary = new ArrayList<String>();

    try {
      //Create Scanner and reads in the file
      Scanner scanIn = new Scanner(new File(filename));
      while(scanIn.hasNext()) {
        dictionary.add(scanIn.nextLine());
      }
      scanIn.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return dictionary;
  }


  /**
   * Implement the algorithm here. Do not change the function signature.
   * The return type should be ArrayList<ArrayList<String>>, so that Gradescope can read your output.
   * Your output need not be sorted.
   * 
   * @returns - List of anagram classes, where each element in the list is a list of all the words in that anagram class.
   */
  public ArrayList<ArrayList<String>> getAnagrams(){
    
    
    Map<String,String> words = new HashMap<String,String>();
    StringKeyPair[] wordsArray = new StringKeyPair[dictionary.size()];

    //loops through the dictionary for all elements 
    for(String word:dictionary) {
      String key ="";
      Map<String,Integer> letters = new HashMap<String,Integer>();
      
      //iterates through all characters in the words and stores what characters are in it and how many there are
      for(int i = 0; i<word.length();i++) {
        if(letters.containsKey(word.charAt(i)+"")) {
          letters.put(word.charAt(i)+"", letters.get(word.charAt(i)+"")+1);
        }else {
          letters.put(word.charAt(i)+"",1);
        }            
        key ="";
        String[] ordered = new String[letters.size()];
        int index = 0;
        for(String c:letters.keySet()) {
          ordered[index] = c+"";
          index++;
        }

        //sorts the characters in the word into abs order so the keys can be created after they are created
        mergeSort(ordered,0,ordered.length-1);


        for(String c:ordered) {
          key+=""+c+letters.get(c);
        }
      }
      words.put(word, key);
    }

    int index = 0;
    //creates the array of the class stringkeypair allowing the keys to be sorted and still have the word attached to it
    for(String word:words.keySet()) {
      wordsArray[index] = new StringKeyPair(word,words.get(word));
      index++;
    }

    //sorts the stringkeypair array putting all equal keys next to each other allowing comparison in linear time
    mergeSort(wordsArray,0,wordsArray.length-1);


    String currentKey = wordsArray[0].key;
    ArrayList<String> currentClass = new ArrayList<String>();
    currentClass.add(wordsArray[0].word);

    int count =0;
    
    //iterates through all words and check all equal terms and add them to the smae anagram class
    for(int i = 1;i<wordsArray.length;i++) {
      
      
      //handles edge case of the code for  the last element
      if(i == wordsArray.length-1) {
        ArrayList<String> addList = new ArrayList<String>();
        for(int j = 0;j<currentClass.size();j++) {
          addList.add(currentClass.get(j));
        }
        anagrams.add(addList);
      }
      
      //adds words to class if keys are equal
      if(currentKey.equals(wordsArray[i].key)) {
        currentClass.add(wordsArray[i].word);
        if(i == wordsArray.length-1) {
          ArrayList<String> addList = new ArrayList<String>();
          for(int j = 0;j<currentClass.size();j++) {
            addList.add(currentClass.get(j));
          }
          anagrams.add(addList);
        }
      } else {
        //adds the class to anagrams and updates key if the current class has ended
        ArrayList<String> addList = new ArrayList<String>();
        for(int j = 0;j<currentClass.size();j++) {
          addList.add(currentClass.get(j));
        }
        anagrams.add(addList);
        currentClass.clear();

        currentKey = wordsArray[i].key;
        currentClass.add(wordsArray[i].word);
      }
    }
    return anagrams;
  }

  //Standard merge sort for type StringKeyPair.
  public static StringKeyPair[] mergeSort(StringKeyPair[] a, int start, int end) {
    if(start<end) {
      int middle = (start+end)/2;
      mergeSort(a,start,middle);
      mergeSort(a,middle+1,end);

      int length1 = middle-start+1;
      int length2 = end-middle;

      //Create 2 copies of the Array
      StringKeyPair[] left = new StringKeyPair[length1];
      StringKeyPair[] right = new StringKeyPair[length2];

      for(int i = 0; i <length1; i++) {
        left[i] = a[start+i];
      }
      for(int i = 0; i <length2; i++) {
        right[i] = a[middle+1+i];
      }


      int indexLeft = 0;
      int indexRight = 0;
      int index = start;

      while (indexLeft < length1 && indexRight < length2) {
        if ((left[indexLeft].key).compareTo(right[indexRight].key) >= 0) {
          a[index] = left[indexLeft];
          indexLeft++;
        } else {
          a[index] = right[indexRight];
          indexRight++;
        }
        index++;
      }

      while (indexLeft < length1) {
        a[index] = left[indexLeft];
        indexLeft++;
        index++;
      }

      while (indexRight < length2) {
        a[index] = right[indexRight];
        indexRight++;
        index++;
      }
    }
    return a;
  }


  //Standard merge sort for type String
  public static String[] mergeSort(String[] a, int start, int end) {
    if(start<end) {
      int middle = (start+end)/2;
      mergeSort(a,start,middle);
      mergeSort(a,middle+1,end);

      int length1 = middle-start+1;
      int length2 = end-middle;

      //Create 2 copies of the Array
      String[] left = new String[length1];
      String[] right = new String[length2];

      for(int i = 0; i <length1; i++) {
        left[i] = a[start+i];
      }
      for(int i = 0; i <length2; i++) {
        right[i] = a[middle+1+i];
      }


      int indexLeft = 0;
      int indexRight = 0;
      int index = start;

      while (indexLeft < length1 && indexRight < length2) {
        if ((left[indexLeft]).compareTo(right[indexRight]) >= 0) {
          a[index] = left[indexLeft];
          indexLeft++;
        } else {
          a[index] = right[indexRight];
          indexRight++;
        }
        index++;
      }

      while (indexLeft < length1) {
        a[index] = left[indexLeft];
        indexLeft++;
        index++;
      }

      while (indexRight < length2) {
        a[index] = right[indexRight];
        indexRight++;
        index++;
      }
    }
    return a;
  }

  //simple class that holds 2 strings
  public class StringKeyPair {

    String key;
    String word;

    public StringKeyPair(String w, String k) {
      word = w;
      key = k;
    }

  }





  /**
   * You can use this method for testing and debugging if you wish.
   * @throws FileNotFoundException 
   */
  public static void main(String[] args) {
    Anagram pf = new Anagram("dict1.txt");
    pf.getAnagrams();
  }

}
