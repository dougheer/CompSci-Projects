import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class tokenizer {

  Scanner scanIn,scanInStopWords;
  File f;
  File writeTo;
  FileWriter fw;
  PrintWriter pw;
  File writeTo2;
  FileWriter fw2;
  PrintWriter pw2;

  public tokenizer(String fileName) throws IOException {
    scanIn = new Scanner(new File(fileName));
    scanInStopWords = new Scanner(new File("stopwords.txt"));
    writeTo = new File("out");
    fw = new FileWriter(writeTo);
    pw = new PrintWriter(fw);
    writeTo2 = new File("terms.txt");
    fw2 = new FileWriter(writeTo2);
    pw2 = new PrintWriter(fw2);

  }

  public void tokenize() {
    ArrayList<String> tokenizedList = new ArrayList<String>();
    String currentLine;
    String currentToken ="";
    while(scanIn.hasNextLine())  {  // Runs through all lines n the document
      currentLine = scanIn.nextLine();
      System.out.println(currentLine);
      int i = 0;
      while(i<currentLine.length()) { //Runs through all characters in a line
        if(i == 0 && currentToken != "") {
          currentToken = currentToken.toLowerCase();
          tokenizedList.add(currentToken);
          currentToken = "";
        }
        if(isLetter(currentLine.charAt(i))) { //Checks if current char is a letter
          if(i+5<currentLine.length() && currentLine.charAt(i + 1) == 46 && currentLine.charAt(i + 3) == 46 && currentLine.charAt(i +5) == 46) { //Checks if there is a series of periods and letters that result in an abbreveation
            while(i+1<currentLine.length() && currentLine.charAt(i+1) == 46) { //loops through the abreviation
              currentToken+=currentLine.charAt(i);
              i+=2;
              if(!(i+1<currentLine.length() && currentLine.charAt(i+1) == 46)) {
                i--;
              }
            }
          } else {
            currentToken += currentLine.charAt(i); //adds letter to the current token
          }
          i++;
        }else if(isSeparator(currentLine.charAt(i))) { //checks if current char is a separator
          currentToken = currentToken.toLowerCase();
          if(currentToken != "")
            tokenizedList.add(currentToken);
          currentToken = "";
          i++;
        } else {
          currentToken += currentLine.charAt(i);
          i++;
        }

      }
      i = 0;

    }
    stopRemoval(tokenizedList);
    stemmer(tokenizedList);

    wordCounts(tokenizedList,findVocab(tokenizedList));
    writeTo(tokenizedList,pw);
  }

  public boolean isSeparator(char curr) {
    if(((curr>=33)&&(curr<46)) || ((curr>=58)&&(curr<=64)) || ((curr>=123)&&(curr<=96)) || ((curr>33)&&(curr<=126)) || curr == 32 || curr == 47) {
      return true;
    }
    return false;
  }

  public boolean isLetter(char curr) {
    if(((curr>=97)&&(curr<=122)) || ((curr>=65)&&(curr<=90)))
      return true;
    return false;
  }

  public void stopRemoval(ArrayList<String> list) {
    String word = "";
    Set<String> stopWords = new HashSet<String>();
    while(scanInStopWords.hasNextLine()) {
      word = scanInStopWords.nextLine();
      stopWords.add(word);
    }
    for(int i = list.size()-1;i >= 0;i--) {
      if(stopWords.contains(list.get(i))) {
        list.remove(i);
      }
    }
  }

  public void stemmer(ArrayList<String> list) {
    for(int i = 0;i < list.size(); i++) {
      String word = list.get(i);
      int len = word.length();

      //Stemmer Part A
      if(word.length() > 4 && word.subSequence(word.length()-4, word.length()).equals("sses")) {
        word = word.substring(0, word.length()-4) + "ss";
      } else if(word.length()>=2 && word.charAt(word.length()-1) == 115 && word.charAt(len-1) != word.charAt(len-2) && !(isVowel(word.charAt(word.length()-2)))) {
        word = word.substring(0, word.length() - 1);
      } else if(word.length() > 3 && (word.substring(word.length() - 3).equals("ied") || word.substring(word.length()-3).equals("ies"))) {
        if(word.length()>=5) {
          word = word.substring(0, word.length() - 3) + "i";
        } else {
          word = word.substring(0, word.length() - 3) + "ie";
        }
      }

      //Stemmer Part B
      if(word.length()>=4 && word.subSequence(word.length()-3, word.length()).equals("eed")) {
        for(int j = 0;j<word.length()-3;j++) {
          if(isVowel(word.charAt(j)) && !isVowel(word.charAt(j+1))) {
            word = word.substring(0,word.length()-3) + "ee";
          }
        }
      } else if(word.length()>=5 && word.subSequence(word.length()-5, word.length()).equals("eedly")) {
        for(int j = 0;j<word.length()-5;j++) {
          if(isVowel(word.charAt(j)) && !isVowel(word.charAt(j+1))) {
            word = word.substring(0,word.length()-5) + "ee";
          }
        }
      } else if(word.length() > 2 && word.substring(word.length()-2).equals("ed")) {
        for(int j = 1;j<word.length()-2;j++) {
          if(isVowel(word.charAt(j))) {
            word = word.substring(0, word.length()-2);
            len = word.length();
            if(word.substring(word.length()-2).equals("at") || word.substring(word.length()-2).equals("bl") || word.substring(word.length()-2).equals("iz")) {
              word += "e";
            } else if(word.charAt(len-1)==word.charAt(len-2) && word.charAt(len-1) != 108 && word.charAt(len-1) != 115 && word.charAt(len-1) != 122) {
              word = word.substring(0, len-1);
            } else if(len<=3) {
              word += "e";
            }
            break;
          }
        }    
      }else if(word.length() > 4 && word.substring(word.length()-4).equals("edly")) {
        for(int j = 1;j<word.length()-4;j++) {
          if(isVowel(word.charAt(j))) {
            word = word.substring(0, word.length()-4);
            len = word.length();
            if(word.substring(word.length()-2).equals("at") || word.substring(word.length()-2).equals("bl") || word.substring(word.length()-2).equals("iz")) {
              word += "e";
            } else if(word.charAt(len -1)==word.charAt(len-2) && word.charAt(len-1) != 108 && word.charAt(len-1) != 115 && word.charAt(len-1) != 122) {
              word = word.substring(0, len-1);
            } else if(len<=3) {
              word += "e";
            }
          }
        }
      } else if(word.length() > 3 && word.substring(word.length()-3).equals("ing")) {
        for(int j = 1;j<word.length()-3;j++) {
          if(isVowel(word.charAt(j))) {
            word = word.substring(0, word.length()-3);
            len = word.length();
            if(word.substring(word.length()-2).equals("at") || word.substring(word.length()-2).equals("bl") || word.substring(word.length()-2).equals("iz")) {
              word += "e";
            } else if(word.charAt(len -1)==word.charAt(len-2) && word.charAt(len-1) != 108 && word.charAt(len-1) != 115 && word.charAt(len-1) != 122) {
              word = word.substring(0, len-1);
            } else if(len<=3) {
              word += "e";
            }
          }
        }
      } else if(word.length() > 5 && word.substring(word.length()-5).equals("ingly")) {
        for(int j = 1;j<word.length()-5;j++) {
          if(isVowel(word.charAt(j))) {
            word = word.substring(0, word.length()-5);
            len = word.length();
            if(word.substring(word.length()-2).equals("at") || word.substring(word.length()-2).equals("bl") || word.substring(word.length()-2).equals("iz")) {
              word += "e";
            } else if(word.charAt(len -1)==word.charAt(len-2) && word.charAt(len-1) != 108 && word.charAt(len-1) != 115 && word.charAt(len-1) != 122) {
              word = word.substring(0, len-1);
            } else if(len<=3) {
              word += "e";
            }
          }
        }
      }

      list.set(i, word);
    }
  }

  public boolean isVowel(char c) {
    return c == 97 || c == 101 || c == 105 || c == 111 || c == 117;
  }

  public void writeTo(ArrayList<String> list, PrintWriter pw) {

    for(int j = 0; j<list.size(); j++) { //write the tokeinzed list to a new file
      pw.println(list.get(j));
    }
    list.clear();
    pw.close();
  }

  public Set<String> findVocab(ArrayList<String> list) {
    System.out.println(list.size());  //Gathers data for graph
    Set<String> vocab = new HashSet<String>();
    for(int i = 0; i<list.size(); i++) {
      vocab.add(list.get(i));
      if(i==5000) {
        System.out.println(i + " " + vocab.size()); //Gathers data for graph
      }
    }
    System.out.println(vocab.size()); //Gathers data for graph
    return vocab;
  }

  public void wordCounts(ArrayList<String> totalWords, Set<String> vocab) {
    Map<String,Integer> map = new HashMap<String,Integer>();
    for(String word:vocab) {
      map.put(word, 0);
    }
    for(String word:totalWords) {
      map.put(word, map.get(word)+1);
    }

    ArrayList<String> topWords = new ArrayList<String>();
    Set<String> extractedWords = new HashSet<String>();
    
    for(int i = 0; i<200; i++) {
      String maxKey = "";
      int maxValue = 0;

      for(String word:map.keySet()) {
        if(map.get(word)>maxValue && !(extractedWords.contains(word))) {
          maxKey = word;
          maxValue = map.get(word);
        }
      }
     topWords.add(maxKey + ": " + maxValue);
     extractedWords.add(maxKey);
    }
    writeTo(topWords,pw2);
  }
  
  
  
  
}





