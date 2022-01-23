import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

  Map<String,ArrayList<Posting>> index;
  ArrayList<Document> collection;
  Map<String,Integer> size;
  Map<String,Integer> sizePlay;
  Map<String,Integer> sceneToNum;
  Map<String, HashMap<String, ArrayList<Integer>>> in = new HashMap<String,HashMap<String,ArrayList<Integer>>>();

  public InvertedIndex(ArrayList<Document> d) {
    index = new HashMap<String,ArrayList<Posting>>();
    collection = d;
    size = new HashMap<String,Integer>();
    sizePlay = new HashMap<String,Integer>();
    sceneToNum = new HashMap<String,Integer>();
    buildIndex();
  }

  public void buildIndex() {

    for(Document d:collection) {
      size.put(d.Sceneid,d.text.split("\\s+").length);
      sceneToNum.put(d.Sceneid, d.num);

      if(sizePlay.containsKey(d.Playid)) {
        sizePlay.put(d.Playid, sizePlay.get(d.Playid) + d.text.split("\\s+").length);
      }else {
        sizePlay.put(d.Playid, d.text.split("\\s+").length);
      }
      String[] s = d.text.split("\\s+");
      int pos = 0;    
      for(int i = 0; i<s.length;i++) {
        ArrayList<Posting> postings;
        if(!in.containsKey(s[i])) {
          in.put(s[i],new HashMap<String,ArrayList<Integer>>());
        }
        pos++;
        if(!index.containsKey(s[i])) {
          postings = new ArrayList<Posting>();
        }else {
          postings = index.get(s[i]);
        }      
        if(index.containsKey(s[i])) {
          if(!in.get(s[i]).containsKey(d.Sceneid)) {
            in.get(s[i]).put(d.Sceneid,new ArrayList<Integer>());
            in.get(s[i]).get(d.Sceneid).add(pos);
            postings = index.get(s[i]);
            postings.add(new Posting(d.Playid,d.Sceneid));
          }else {
            ArrayList<Integer> nm = in.get(s[i]).get(d.Sceneid);
            nm.add(pos);
            in.get(s[i]).put(d.Sceneid,nm);
          }          
        }else {
          in.get(s[i]).put(d.Sceneid,new ArrayList<Integer>());
          in.get(s[i]).get(d.Sceneid).add(pos);
          postings.add(new Posting(d.Playid,d.Sceneid));
        }
        index.put(s[i], postings);
      }
    }
    for(String term:index.keySet()) {
      Map<String, ArrayList<Integer>> termpo = in.get(term);
      ArrayList<Posting> termlist = index.get(term);
      for(Posting p:termlist) {
        p.setPositions(termpo.get(p.sceneId));
      }
    }    
  }

  public ArrayList<String> getByTerm(String term,String idType){
    ArrayList<Posting> postings = index.get(term);
    ArrayList<String> re = new ArrayList<String>();
    Set<String> play = new HashSet<String>();
    if(idType.equalsIgnoreCase("playid") && postings != null) {
      for(Posting p:postings) {
        play.add(p.playId);
      }
      for(String p:play) {
        re.add(p);
      }
      Collections.sort(re);
    } else if(idType.equalsIgnoreCase("sceneid")&& postings != null){
      for(Posting p:postings) {
        re.add(p.sceneId);
      }
      Collections.sort(re);
    } else {
      System.out.println("Error");
      return null;
    }
    return re;
  }

  public int numberOf(String term,String idType) {
    if(idType.equalsIgnoreCase("sceneId")) {
      ArrayList<Posting> postings = index.get(term);
      for(Posting p:postings) {
        if(idType.equalsIgnoreCase(p.sceneId)) {
          return p.pos.size();
        }
      }
    } else {
      int sum = 0;
      ArrayList<Posting> postings = index.get(term);
      for(Posting p:postings) {
        if(idType.equalsIgnoreCase(p.playId)) {
          sum += p.pos.size();
        }
      }
      return sum;
    }


    return 0;
  }

  public ArrayList<String> getPhrases(String p, String idType){

    String[] phrase = p.split("\\s+");
    ArrayList<String> re = new ArrayList<String>();
    Map<String,Boolean> status = new HashMap<String,Boolean>();

    for(int i = 0;i<phrase.length-1;i++) {
      ArrayList<Posting> term1 = index.get(phrase[i]);
      ArrayList<Posting> term2 = index.get(phrase[i+1]);
      for(Posting doc1:term1) {
        boolean isGood = false;
        for(Posting doc2:term2) {
          if(doc1.sceneId.equals(doc2.sceneId)) {
            for(int pos1:doc1.pos) {
              for(int pos2:doc2.pos) {
                if(pos2-pos1==1) {
                  isGood = true;
                }
              }
            }
          } 
        }
        if(status.containsKey(doc1.sceneId) && status.get(doc1.sceneId)) {
          status.put(doc1.sceneId, isGood);
        }else if (!status.containsKey(doc1.sceneId) &&  i == 0){
          status.put(doc1.sceneId, isGood);
        }
      }
    }


    for(String s:status.keySet()) {
      if(status.get(s)) {
        re.add(s);
      }
    }
    return re;
  }

  public ArrayList<String> compareFrequancey(String term1, String term2) {
    ArrayList<Posting> p1 = index.get(term1);
    ArrayList<Posting> p2 = index.get(term2);

    ArrayList<String> re = new ArrayList<String>();

    if(p1 == null) {
      return null;
    }
    if(p2 == null) {
      for(Posting post1:p1) {
        re.add(post1.sceneId);
      }
      return re;
    }
    for(Posting post1:p1) {
      for(Posting post2:p2) {
        if(post1.sceneId.equals(post2.sceneId) && (post1.pos.size()>post2.pos.size())) {
          re.add(post1.sceneId);
        }
      }
    }
    Collections.sort(re);
    return re;
  }

  public boolean listPostingEqual(ArrayList<Posting> po,String id) {
    for(Posting p:po) {
      if(p.sceneId.equalsIgnoreCase(id)) {
        return true;
      }
    }
    return false;
  }

  public double averageScene() {
    double sum = 0;
    for(String i:size.keySet()) {
      sum+=size.get(i);
    }

    return sum/size.size();
  }

  public String largestScene() {
    String s = "";
    int check= 0;
    for(String i:size.keySet()) {
      if(check<size.get(i)) {
        s=i + " :" + size.get(i);
        check = size.get(i);
      }
    }    
    return s;
  }

  public String smallestScene() {
    String s = "";
    int check= 1000000000;
    for(String i:size.keySet()) {
      if(check>size.get(i)) {
        s=i + " :" + size.get(i);
        check = size.get(i);
      }
    }    
    return s;
  }

  public String largestPlay() {
    String s = "";
    int check= 0;
    for(String i:sizePlay.keySet()) {
      if(check<sizePlay.get(i)) {
        s=i + " :" + sizePlay.get(i);
        check = sizePlay.get(i);
      }
    }    
    return s;
  }

  public String smallestPlay() {
    String s = "";
    int check= 1000000000;
    for(String i:sizePlay.keySet()) {
      if(check>sizePlay.get(i)) {
        s=i + " :" + sizePlay.get(i);
        check = sizePlay.get(i);
      }
    }    
    return s;
  }

}
