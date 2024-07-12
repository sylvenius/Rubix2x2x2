/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class TheMap {
  public static final long serialVersionUID = 202407042129L;
  private final Rubik2x2Cube owner;
  Random ran = new Random();
  ArrayList<HashMap<Long,Short>> mapl = new ArrayList<>(5);

  private boolean isOk = false;
  public boolean ok(){
    return isOk;
  }

  private class MapReader extends Thread{
    @Override
    public void run(){
      calcInRam();
    }
  }

  TheMap(Rubik2x2Cube owner){
    this.owner=owner;
    owner.setButtonsOnOff(false);
    for(int i=0;i<5;i++){
      mapl.add(i,new HashMap<>(Info.per_level[i][0]));
    }
    Thread read = new MapReader();
    read.start();
  }
  LinkedHashMap<Long,Short> jl1 = new LinkedHashMap<>();
  LinkedHashMap<Long,Short> jl2 = new LinkedHashMap<>(); 
  void calcInRam(){
    short thisDept = 0;
    short sten = 10;
    int moves=0;
    VirtualCube2x2 cc = new VirtualCube2x2();
    short code = 0;
    jl1.put(cc.getState(),code);
    do{
      moves=0;
      jl2.putAll(jl1);
      jl1.clear();
      thisDept++;
      Set<Long> keys = jl2.keySet();
      for(Long key : keys){
        long newKey;
        code = (short)(thisDept*sten);
        for(short j=1; (j<7); j++){
          cc.setState(key);
          cc.turn(j);
          cc.turn(j);
          newKey = cc.getState();
          short kode=(short)(code+j);
          if(!jl2.containsKey(newKey) && jl1.putIfAbsent(newKey,kode) == null){
            moves++;
          }
        }
      }
    }while(!jl1.isEmpty());
    intoMap();
    isOk=true;
    owner.setButtonsOnOff(isOk);
  }

  public void intoMap(){
    for (var entry : jl2.entrySet()) {
      put(entry.getKey(),entry.getValue());
    }
  }

  protected void put(String state, String val){
    long  ls = Long.parseLong(state);
    short sv = Short.parseShort(val);
    int dept = sv/10;
    mapl.get(dept).put(ls,sv);
  }

  protected void put(Long state, Short val){
    int dept = val/10;
    mapl.get(dept).put(state,val);
  }

  public Short get(String state){
    return get(Long.valueOf(state));
  }

  public Short get(Long state){
    Short retur = -1;
    for(HashMap<Long,Short> map : mapl){
      retur = map.get(state);
      if(retur!=null){
        return retur;
      }
    }
    return retur;
  }

  public String getRandomOnLevel(int level){
    String retur = "-2;-2;-2";
    HashMap<Long,Short> map = mapl.get(level);
    int size = map.size();
    int stop = ran.nextInt(size);
    int pointer=0;
    Set<Entry<Long,Short>> set = map.entrySet();
      for (Entry<Long,Short> ent : set) {
          if(stop <= pointer++){
              Long state = ent.getKey();
              Short val = ent.getValue();
              int dept = val/10;
              int move = val%10;
              return state+":"+dept+":"+move;
          }
      }
    return retur;
  }
}
