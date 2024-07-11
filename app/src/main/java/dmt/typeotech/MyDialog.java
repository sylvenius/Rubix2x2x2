/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public final class MyDialog extends JDialog implements ActionListener, Runnable{
  public static final long serialVersionUID = 202407042116L;

  JButton jbcalc = new JButton("Calc"), jbstop = new JButton("Stop");
  JTextArea ta = new JTextArea();
  JProgressBar jpb = new JProgressBar(0, 0, Info.total_permutations);
  JScrollPane scp = new JScrollPane(ta);
  boolean go = true;
  String no_map_resource="no_map.txt";
  String you_aborted="you_aborted.txt";
  String you_are_stupid="you_stupid.txt";

  LinkedHashMap<Long,Short> jl1; 
  LinkedHashMap<Long,Short> jl2; 
  TheMap tmap;
  Thread calcThread,progThread;
  int totMoves=0;

  private class ProgressThread extends Thread{
    @Override
    public void run(){
      while(go){
        try {
          jpb.setValue(totMoves);
          Thread.sleep(500);
        } catch (InterruptedException igInterruptedException) {}
      }
    }
  }





  MyDialog(TheMap tmap){
    this.tmap=tmap;
    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {endMe();}
    });
    JPanel jpSouth = new JPanel(new GridLayout(1,2));
    jpSouth.add(jbcalc);
    jpSouth.add(jbstop);
    add("South", jpSouth);
    add("Center", scp);
    add("North", jpb);
    jbcalc.addActionListener(this);
    jbstop.addActionListener(this);
    setSize(500,500);
    setVisible(true);
    readResource(no_map_resource);
  }

  void readResource(String recs){
    InputStream no_map_input = MyDialog.class.getResourceAsStream("/resources/" + recs);
    if (no_map_input == null) {
        no_map_input =  MyDialog.class.getClassLoader().getResourceAsStream(recs);
    }
    try{
      BufferedReader reader = new BufferedReader(new InputStreamReader(no_map_input));
      String line;
      while((line = reader.readLine()) != null){
        ta.append(line+"\n");
      }
      reader.close();
      ta.setCaretPosition(ta.getDocument().getLength());
    }catch (IOException igIOException) {} 
  }
  
  void calcInRam(){
    long startT=System.currentTimeMillis();
    short thisDept = 0;
    short sten = 10;
    int moves=0;
    totMoves=0;
    VirtualCube2x2 cc = new VirtualCube2x2();
    String state = "Level "+ thisDept +" = "+ moves +" permutations, and "+ totMoves + " permutations totaly \n"; 
    ta.append(state);
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
        for(short j=1; (go && j<7); j++){
          cc.setState(key);
          cc.turn(j);
          newKey = cc.getState();
          short kode=(short)(code+j);
          if(!jl2.containsKey(newKey) && jl1.putIfAbsent(newKey,kode) == null){
            moves++;
            totMoves++;
            jpb.setValue(totMoves);
          }
        }
      }
      state = "Level "+ thisDept +" = "+ moves +" permutations, and "+ totMoves + " permutations totaly \n"; 
      ta.append(state);
      ta.setCaretPosition(ta.getDocument().getLength());
    }while(go && !jl1.isEmpty());
    state = "Time sek = "+((System.currentTimeMillis()-startT)/1000);
    ta.append(state+"\n");
    
    
    ta.append("Try to write to file\n");
    startT=System.currentTimeMillis();
    try {
        try (BufferedWriter br = new BufferedWriter(new FileWriter("HashCube.txt"))) {
            jl2.forEach((key, val)->{
                if(go) try {
                    br.write(key+"\n");
                    br.write(val+"\n");
                } catch (IOException ignorOException) {}
            });
        }
    } catch (IOException igIOException) {} 
    
    ta.append("Done write to file\n\n");
    state = "Time sek = "+((System.currentTimeMillis()-startT)/1000);
    ta.append(state+"\n");
    ta.setCaretPosition(ta.getDocument().getLength());
    jl2.clear();
  }

  void testFile(){
    String lastKey="";
    String lastVal="";
    String state;
    ta.append("Try read from file\n");
    long startT=System.currentTimeMillis();
    try{

      Path filePath = new File("HashCube.txt").toPath();
      Charset charset = Charset.defaultCharset();        
      List<String> stringList = Files.readAllLines(filePath, charset);
      Iterator<String> iter = stringList.iterator();
      
      while(iter.hasNext()){
        String key = iter.next();
        String val = iter.next();
        lastKey=key;
        lastVal=val;
        tmap.put(key,val);
      }
    } catch (IOException igIOException) {}
    ta.append("Done read from file\n");
    state = "Time sek = "+((System.currentTimeMillis()-startT)/1000);
    ta.append(state+"\n\n");
    ta.append("Last key = "+lastKey+" Last val = "+lastVal+"\n");
    VirtualCube2x2 cc = new VirtualCube2x2();
    int max = 17,turn = 0;
    int dept =0;
    if(go)do{
      
      cc.setState(Long.parseLong(lastKey));
      turn=Short.parseShort(lastVal)%10;
      dept=Short.parseShort(lastVal)/10;
      
      if(turn%2==0) turn-=1;
      else turn++;
  
      ta.append("State "+lastKey+" Dept "+dept+" turn "+turn+"\n");
      cc.turn(turn);
      lastKey=cc.getState()+"";
      lastVal=tmap.get(lastKey)+"";
      
    }while(max-- > 0 && Short.parseShort(lastVal) > 0);
    ta.append("State "+lastKey+" Dept "+dept+" turn "+turn+"\n");
    ta.setCaretPosition(ta.getDocument().getLength());
    tmap.readMapFile();
  }
  
  //From the net
  String hRByteCount(long bytes) {
    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
    if (absB < 1024) {
        return bytes + " B";
    }
    long value = absB;
    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
        value >>= 10;
        ci.next();
    }
    value *= Long.signum(bytes);
    return String.format("%.1f %ciB", value / 1024.0, ci.current());
  }

  @Override
  public void run(){
    MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
    MemoryUsage beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
    jl1 = new LinkedHashMap<>(Info.total_permutations); 
    jl2 = new LinkedHashMap<>(Info.total_permutations); 
    calcInRam();
    testFile();
    jl1=null;
    jl2=null;
    mbean.gc();
    MemoryUsage afterHeapMemoryUsage = mbean.getHeapMemoryUsage();
    long consumed = afterHeapMemoryUsage.getUsed() - 
                    beforeHeapMemoryUsage.getUsed();
    ta.append("\n");
    ta.append("Total consumed Memory:  " + hRByteCount(consumed));
    ta.append("\n\nDone, press Close and have fun");
    ta.setCaretPosition(ta.getDocument().getLength());
  }

  void endMe(){
    go=false;
    try {
      try{
        calcThread.join();
        progThread.join();
      } catch (InterruptedException e1) {}
    } catch (Exception e1) {}
    dispose();
  }

  int stopps=0;
  @Override
  public void actionPerformed(ActionEvent e){ 
    if(e.getActionCommand().equals("Calc")){ 

      if(tmap.ok()){
        ta.append("TheMap.txt is Ok, I will work now\n");
      } else{
        ta.append("TheMap.txt is fucked upp, or non existent, will try now\n\n");
        jbcalc.setEnabled(false);
        go=true;
        calcThread = new Thread(this);
        calcThread.start();
        progThread = new ProgressThread();
        progThread.start();
      }
      ta.setCaretPosition(ta.getDocument().getLength());
    }
    
    if(e.getActionCommand().equals("Stop")){ 
      go=false;
      if(stopps++ > 3){
        endMe();
      }
      if(tmap.ok()){
        endMe();
      }else{
        if(calcThread == null){
          readResource(you_are_stupid);
        }else{
          try {
            try{
              calcThread.join();
              progThread.join();
            } catch (InterruptedException e1) {}
          } catch (Exception e1) {}
          readResource(you_aborted);
        }
      }
    }
  }
}
