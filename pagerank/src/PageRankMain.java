import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class PageRankMain {
  
  static Map<String,Set<String>> g = new HashMap<String,Set<String>>();
  static File writeTo2;
  static FileWriter fw2;
  static PrintWriter pw2;
  
  static File writeTo;
  static FileWriter fw;
  static PrintWriter pw;
  
  static Map<String,Integer> inlinks = new HashMap<String,Integer>();
  
  
  public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
    
    String fileName = "links.srt.gz";
    read(fileName);
    PageRank rank = new PageRank(.15,.0001,g);
    Map<String,Double> ranked = rank.rank();
    writeTo2 = new File("pagerank.txt");
    fw2 = new FileWriter(writeTo2);
    pw2 = new PrintWriter(fw2);

    writeTo = new File("inlinks.txt");
    fw = new FileWriter(writeTo);
    pw = new PrintWriter(fw);

    
    ArrayList<String> topPages = new ArrayList<String>();
    Set<String> extractedPages = new HashSet<String>();
    
    
    
    //Top Pages
    for(int i = 0; i<75; i++) {
      String maxKey = "";
      double maxValue = 0;
      System.out.println(i);
      for(String word:ranked.keySet()) {
        if(ranked.get(word)>maxValue && !(extractedPages.contains(word))) {
          maxKey = word;
          maxValue = ranked.get(word);
        }
      }
      topPages.add(maxKey+ " "+(i+1) + " " + maxValue);
      extractedPages.add(maxKey);
    }
    writeTo(topPages,pw2);
    
    
    
    
    
    
    
    
    ArrayList<String> topPages2 = new ArrayList<String>();
    Set<String> extractedPages2 = new HashSet<String>();
    
    //Top In Links
    for(int i = 0; i<75; i++) {
      String maxKey = "";
      double maxValue = 0;
      for(String word:inlinks.keySet()) {
        if(inlinks.get(word)>maxValue && !(extractedPages2.contains(word))) {
          maxKey = word;
          maxValue = inlinks.get(word);
        }
      }
      topPages2.add(maxKey+ " "+(i+1) + " " + maxValue);
      extractedPages2.add(maxKey);
    }
    writeTo(topPages2,pw);
  }
  
  
  
   static public void writeTo(ArrayList<String> list, PrintWriter pw) {

    for(int j = 0; j<list.size(); j++) { //write the tokeinzed list to a new file
      pw.println(list.get(j));
    }
    list.clear();
    pw.close();
  }
  
  private static void read(String in) throws UnsupportedEncodingException, FileNotFoundException, IOException {
     BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(in)),"UTF-8"));
     String s;
     while((s = br.readLine()) != null) {
       String tok[] = s.split("\t");
       String source = tok[0];
       String target = tok[1];
       if(!g.containsKey(source)) {
         Set<String> set = new HashSet<String>();
         g.put(source, set);
         inlinks.put(source, 0);
       }
       if(!g.containsKey(target)) {
         Set<String> set = new HashSet<String>();
         g.put(target, set);
         inlinks.put(target, 0);
       }
       g.get(source).add(target);
       inlinks.put(target, inlinks.get(target)+1);
     }
  }
}
