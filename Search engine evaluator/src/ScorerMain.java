import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ScorerMain {
  static File writeTo;
  static FileWriter fw;
  static PrintWriter pw;
  static File writeTo1;
  static FileWriter fw1;
  static PrintWriter pw1;
  public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
    
    File f = new File("shakespeare-scenes.json");
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(f));
    JSONObject json = (JSONObject) obj;
    JSONArray ja = (JSONArray) json.get("corpus");
    ArrayList<Document> col = new ArrayList<Document>();

    for(int i = 0;i<ja.size();i++) {
      JSONObject a = (JSONObject) ja.get(i);
      String pl = (String) a.get("playId");
      String si = (String) a.get("sceneId");
      String t = (String) a.get("text");
      long num = (long) a.get("sceneNum");

      Document d = new Document(pl,num,si,t);
      col.add(d);
    }

    InvertedIndex list = new InvertedIndex(col);
    Scorer score = new Scorer(list);
    
    ArrayList<String> q1b = score.format(score.bm25("the king queen royalty"),"BM25","Q1");
    ArrayList<String> q2b = score.format(score.bm25("servant guard soldier"),"BM25","Q2");
    ArrayList<String> q3b = score.format(score.bm25("hope dream sleep"),"BM25","Q3");
    ArrayList<String> q4b = score.format(score.bm25("ghost spirit"),"BM25","Q4");
    ArrayList<String> q5b = score.format(score.bm25("fool jester player"),"BM25","Q5");
    ArrayList<String> q6b = score.format(score.bm25("to be or not to be"),"BM25","Q6");
    
    ArrayList<String> q1c = score.format(score.QL("the king queen royalty"),"QL","Q1");
    ArrayList<String> q2c = score.format(score.QL("servant guard soldier"),"QL","Q2");
    ArrayList<String> q3c = score.format(score.QL("hope dream sleep"),"QL","Q3");
    ArrayList<String> q4c = score.format(score.QL("ghost spirit"),"QL","Q4");
    ArrayList<String> q5c = score.format(score.QL("fool jester player"),"QL","Q5");
    ArrayList<String> q6c = score.format(score.QL("to be or not to be"),"QL","Q6");
    
    ArrayList<String> t1 = new ArrayList<String>();
    t1.addAll(q1b); t1.addAll(q2b); t1.addAll(q3b); t1.addAll(q4b); t1.addAll(q5b); t1.addAll(q6b);
    
    ArrayList<String> t2 = new ArrayList<String>();
    t2.addAll(q1c); t2.addAll(q2c); t2.addAll(q3c); t2.addAll(q4c); t2.addAll(q5c); t2.addAll(q6c);
    
    writeTo1 = new File("ql.trecrun");
    fw1 = new FileWriter(writeTo1);
    pw1 = new PrintWriter(fw1);
    
    writeTo = new File("bm25.trecrun");
    fw = new FileWriter(writeTo);
    pw = new PrintWriter(fw);
    
    for(String s:t1) {
      pw.println(s);
    }
    pw.close();
    for(String s:t2) {
      pw1.println(s);
    }
    pw1.close();
  }

}
