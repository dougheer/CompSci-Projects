
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PageRank {
  
  Map<String,Set<String>> g;
  double lam;
  double tau;
  
  public PageRank(double l,double t,Map<String,Set<String>> graph){
    lam = l;
    tau = t;
    g = graph;
  }
  
  public Map<String,Double> rank(){
    
    Map<String,Double> I = new  HashMap<String,Double>();
    Map<String,Double> R = new  HashMap<String,Double>();
    double toAdd = 0;
    
    for(String key:g.keySet()) {
      I.put(key, 1.0/g.size() );
    }
    
    do {
      for(String key:g.keySet()) {
        R.put(key,  0.0/g.size());
      }
      
      for(String p:g.keySet()) {
        Set<String> Q = g.get(p);
        if(Q.isEmpty()) {
          toAdd+=(I.get(p))*((1.0-lam)/g.size());
        } else {
          for(String q:Q) {
            R.put(q, (R.get(q))+((1.0-lam)*(I.get(p)/Q.size())));
          }
        }
      }
      for(String key:R.keySet()) {
        I.put(key, R.get(key));
      }
      
      for(String key:R.keySet()) {
        R.put(key, R.get(key)+toAdd/R.size());
      }
    
    } while(!isConverged(I,R));
    System.out.println("Done");
    return R;
  }

  private boolean isConverged( Map<String,Double> i, Map<String,Double> r) {
    double norm = 0;
    for(String key:i.keySet()) {
      norm+=Math.abs(i.get(key)-r.get(key));
    }
    return norm<tau;
  }
  

}