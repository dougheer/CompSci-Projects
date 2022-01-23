import java.util.ArrayList;

public class Posting {
  String playId;
  String sceneId;
  ArrayList<Integer> pos;
  
  public Posting(String i,String s) {
    playId = i;
    sceneId = s;
    pos = new ArrayList<Integer>();
  }
  
  public void addPosition(int p) {
    pos.add(p);
  }
  
  public void setPositions(ArrayList<Integer> p) {
    pos = p;
  }
}