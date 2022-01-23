import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class indexMain {

  static File writeTo;
  static FileWriter fw;
  static PrintWriter pw;


  public static void main(String[] args) throws IOException, ParseException {
    writeTo = new File("write");
    fw = new FileWriter(writeTo);
    pw = new PrintWriter(fw);
    File f = new File("shakespeare-scenes.json");
    ArrayList<Document> col = new ArrayList<Document>();

    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(f));
    JSONObject json = (JSONObject) obj;
    JSONArray ja = (JSONArray) json.get("corpus");


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

    ArrayList<String> wrq1 = new ArrayList<String>();
    ArrayList<String> q1a = list.compareFrequancey("thee","you");
    ArrayList<String> q1b = list.compareFrequancey("thou","you");
    Set<String> q1 = new HashSet<String>();
    for(String s:q1a) {
      q1.add(s);
    }
    for(String s:q1b) {
      q1.add(s);
    }
    for(String s:q1) {
      wrq1.add(s);
    }
    Collections.sort(wrq1);

    ArrayList<String> wrq2 = new ArrayList<String>();
    ArrayList<String> q2a = list.getByTerm("venice", "sceneId");
    ArrayList<String> q2b = list.getByTerm("rome", "sceneId");
    ArrayList<String> q2c = list.getByTerm("denmark", "sceneId");

    Set<String> q2 = new HashSet<String>();
    for(String s:q2a) {
      q2.add(s);
    }
    for(String s:q2b) {
      q2.add(s);
    }
    for(String s:q2c) {
      q2.add(s);
    }
    for(String s:q2) {
      wrq2.add(s);
    }
    Collections.sort(wrq2);

    ArrayList<String> wrq3 = list.getByTerm("goneril", "playId");
    ArrayList<String> wrq4 = list.getByTerm("soldier", "playid");

    ArrayList<String> wrq5 = list.getPhrases("poor yorick", "sceneId");

    ArrayList<String> wrq6 = list.getPhrases("wherefore art thou romeo", "sceneId");

   ArrayList<String> wrq7 = list.getPhrases("let slip", "sceneId");


    writeTo(wrq1,pw);
    
    System.out.println(wrq5.toString());
    System.out.println(wrq6.toString());
    System.out.println(wrq7.toString());
    System.out.println("DONE");
  }

  static public void writeTo(ArrayList<String> list, PrintWriter pw) {

    for(int j = 0; j<list.size(); j++) { //write the tokeinzed list to a new file
      pw.println(list.get(j));
    }
    list.clear();
    pw.close();
  }


}
