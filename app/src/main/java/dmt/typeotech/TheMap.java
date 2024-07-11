/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class TheMap {
  public static final long serialVersionUID = 202407042129L;
  private final Rubik2x2Cube owner;

  private class MapReader extends Thread{
    @Override
    public void run(){
      readMapFile();
    }
  }

  private boolean isOk = false;
  public boolean ok(){
    return isOk;
  }

  Random ran = new Random();
  ArrayList<HashMap<Long,Short>> mapl = new ArrayList<>(15);

  TheMap(Rubik2x2Cube owner){
    this.owner=owner;
    owner.jbFarthest.setEnabled(false);
    owner.jbRandom.setEnabled(false);
    owner.jbSolve.setEnabled(false);
    for(int i=0;i<15;i++){
      mapl.add(i,new HashMap<>(Info.per_level[i][0]));
    }
    Thread read = new MapReader();
    read.start();
  }

  MyDialog diag;
  public void readMapFile(){
    owner.jpb.setValue(0);
    owner.jpb.setString("Try read from file");
    try{
      Path filePath = new File("HashCube.txt").toPath();
      Charset charset = Charset.defaultCharset();        
      List<String> stringList = Files.readAllLines(filePath, charset);
      Iterator<String> iter = stringList.iterator();
      while(iter.hasNext()){
        String key = iter.next();
        String val = iter.next();
        put(key,val);
      }
    } catch (IOException igIOException) {}
    owner.jpb.setString("Done read from file");
    long mySize = Info.getBytesFromList(mapl);
    if(mySize == Info.map_size){
      owner.jpb.setString("ThisMap got the right size\n");
      isOk=true;
      owner.jbFarthest.setEnabled(true);
      owner.jbRandom.setEnabled(true);
      owner.jbSolve.setEnabled(true);
    }else{
      owner.jpb.setString("ThisMap DID NOT got the right size\n");
      isOk=false;
      owner.jbFarthest.setEnabled(false);
      owner.jbRandom.setEnabled(false);
      owner.jbSolve.setEnabled(false);
      diag = new MyDialog(this);
      diag.setAlwaysOnTop(true);
    }

  }

  protected void put(String state, String val){
    long  ls = Long.parseLong(state);
    short sv = Short.parseShort(val);
    int dept = sv/10;
    mapl.get(dept).put(ls,sv);
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
