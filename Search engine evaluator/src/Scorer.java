import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Scorer {
  InvertedIndex index;
  final double k1 = 1.2;
  final int k2 = 10;
  final double b = .85;
  int K;
  final double mew = 200.0;


  public Scorer(InvertedIndex i) {
    index = i;
    index.averageScene();
  }


  public Map<String,Double> bm25(String quer) {
    Map<String,Double> re = new HashMap<String,Double>();
    String[] s = quer.split("\\s+");
    
    Set<String> w = new HashSet<String>();
    

    Map<String,Integer> inq = new HashMap<String,Integer>();
    for(int i = 0;i<s.length;i++) {
      if(inq.containsKey(s[i])) {
        inq.put(s[i], inq.get(s[i])+1);
      } else {
        inq.put(s[i],1);
      }
      w.add(s[i]);
    }


    for(Document d:index.collection) {
      double K = ((k1*(1-b)) + (index.size.get(d.Sceneid)/index.averageScene()));
      int Finq = 0;
      int Find = 0;
      
      double sum = 0;


      for(String t:w) {

        Finq = inq.get(t);
        Find = index.getCount(index.index.get(t), d.Sceneid);
        
        sum += Math.log(1/((index.index.get(t).size()+.5)/(index.collection.size()-index.index.get(t).size()+.5))) 
         * (((k1+1)*Find)/(K+Find)) 
         * (((k2+1)*Finq)/(k2+Finq));
      }
      re.put(d.Sceneid, sum);
    }
    
   

    return re; 
  }


  public Map<String,Double> QL(String quer){

    Map<String,Double> re = new HashMap<String,Double>();
    String[] s = quer.split("\\s+");
    double Find = 0;
    double sum = 0.0;
    double cqi = 0;
    for(Document d: index.collection) {
      sum =0.0;
        for(int i = 0;i<s.length;i++) {
          Find = index.getCount(index.index.get(s[i]), d.Sceneid);
          cqi = index.getCountTotal(index.index.get(s[i]));
          sum += Math.log((Find+mew*(cqi/index.totalWords))/(index.size.get(d.Sceneid)+mew));
        }
        re.put(d.Sceneid,sum);
    }
    return re;
  }
  
  public ArrayList<String> format(Map<String,Double> m, String type, String Qn){
    ArrayList<Pair> re = new ArrayList<Pair>();
    for(String k:m.keySet()) {
      re.add(new Pair(k,m.get(k)));
    }

    Collections.sort(re);
    ArrayList<String> format = new ArrayList<String>();
    int i = 1;
    for(Pair p: re) {
      format.add(Qn + " " + "skip" + " " + p.s + " " + i + " " + p.r + " " + "edougherty-" + type);
      i++;
    }
    return format;
  }
  
  
  public class Pair implements Comparable<Pair> {
    String s;
    double r;
    public Pair(String st,double rank) {
      s=st;
      r=rank;
    }
    @Override
    public int compareTo(Pair o) {
      if(this.r<o.r) {
        return 1;
      } else if(r>o.r) {
        return -1;
      }
      return 0;
    }
    
    
  }
  
  
  
  
  
  
  
  
}
