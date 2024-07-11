/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JProgressBar;

class Rubik2x2Cube implements Runnable, MouseListener, MouseMotionListener{
  public static final long serialVersionUID = 2005110111589L;
  VirtualCube2x2 vicube;
  Camera cam;
  MyJPanel jp;
  static final int MAXDEPTH = 14, CUBES = 8, SQUARES = 6, X = 0, Y = 1, Z = 2;
  final int FREE = 0, FIXING = 1, TWISTING = 2, LOCKED = 4;
  int  size = 105, ofset = 54, camMode = FREE, twistMode = FREE, beginX, beginY, lastX, lastY;
  Cube[] cubes = new Cube[CUBES];
  boolean twinvert, solving = false;
  VirtualCubeMapper vcm;
  Thread thread;
  double ang = 0; 
  int layer;
  Square sqr;
  CubeGlobal global = new CubeGlobal();
  JButton jbSolve = new JButton("Solve");
  JButton jbHint = new JButton("Hint");
  JButton jbRanCol = new JButton("RanCol");
  JButton[] ranButts = new JButton[13];
  JProgressBar jpb = new JProgressBar(0, 0, Rubik2x2Cube.MAXDEPTH);
  Solution solution;
  String identity = "Cube2x2:", error = "Map is coruppt, or not there.";
  TheMap map;
  
  Rubik2x2Cube(Color col){
    int i=0,b=2;
    for(;b<15;b++){ranButts[i++]=new JButton(""+b);}
    vicube = new VirtualCube2x2();
    vcm = new VirtualCubeMapper(vicube);
    cubes[0] = new Cube(size);
    cubes[1] = new Cube(size);
    cubes[2] = new Cube(size);
    cubes[3] = new Cube(size);
    cubes[4] = new Cube(size);
    cubes[5] = new Cube(size);
    cubes[6] = new Cube(size);
    cubes[7] = new Cube(size);
    mapVirtualCubeColorsToMyCubes();
    placeCubes();
    cam = new Camera(700, this);
    cam.setMatrixRotation(-15, -15);
    
    jp = new MyJPanel(cam, col);
    jbSolve.addActionListener(new ButtLiznr());
    jbHint.addActionListener(new ButtLiznr());
    jbRanCol.addActionListener(new ButtLiznr());
    for(i=0;i<13;i++){
      ranButts[i].addActionListener(new ButtLiznr());
    }
    jpb.setStringPainted(true);
    jpb.setBackground(col);
    jp.addMouseListener(this);
    jp.addMouseMotionListener(this);
    map = new TheMap(this);
  }

  String getData(String quis) {
    if(!map.ok()){
      return "";
    }
    VirtualCube2x2 cc = new VirtualCube2x2();
    jpb.setString("Will try to read map");
    String hela = "";
    try{
      String[] quiz = quis.split(":");
      if("Cube2x2".equals(quiz[0])){
        String[] ranDepp = quiz[1].split("-");

        if(ranDepp[0].equals("Random")){
          return map.getRandomOnLevel(Integer.parseInt(ranDepp[1]));
        }
        if(quiz[1].startsWith(";")){ // Whole solution
          String state = quiz[1].substring(1);
          Short smove;
          do{
            smove = map.get(state);
            int ival = smove;
            int dept = ival/10;
            int move = ival%10;
            hela=hela+dept+":"+move+";";
            if(move%2==0) move--;
            else move++;
            
            cc.setState(Long.parseLong(state));
            cc.turn(move);
            state = cc.getState()+"";
          }while(smove!=0);
        }else{  // Dept info
          Short val = map.get(quiz[1]);
          int ival = val;
          int dept = ival/10;
          int move = ival%10;
          if(move%2==0) move--;
          else move++;
          hela=dept+":"+move;
        }
      }
    } catch (NumberFormatException e){
      hela = error;
    }
    if(hela.equals("")){
      hela = error;
      solving = false;
    }
    return hela;
	}
  
  void parseAnswer(String answer, CubeGlobal global) throws NoSuchElementException, NumberFormatException{
    StringTokenizer toker = new StringTokenizer(answer, ":");
    if(toker.countTokens() == 2){
      global.dept = Byte.parseByte(toker.nextToken().trim());
      global.move = Byte.parseByte(toker.nextToken().trim());
    } else {      
      global.state = Long.parseLong(toker.nextToken().trim());
      global.dept = Byte.parseByte(toker.nextToken().trim());
      global.move = Byte.parseByte(toker.nextToken().trim());
    }
  }
    
  void setButtonsOnOff(boolean on){
    for(int i=0;i<13;i++){
      ranButts[i].setEnabled(on);
    }
    jbSolve.setEnabled(on);
  }

  void updateProgress(String quiz){
    quiz = identity+quiz;
    String answer;
    if(quiz.equals(identity)) quiz = identity + vicube.getNomalizedState();
    answer = getData(quiz);
    try{
      parseAnswer(answer, global);
      if(global.dept<0){
        setButtonsOnOff(false);
        jpb.setString(error);
        solving = false;
      } else {
        setButtonsOnOff(true);
        if(hint)jpb.setValue(global.dept);
        if(hint)jpb.setString("Moves from completion : "+global.dept);
      }
    }catch(NoSuchElementException | NumberFormatException nsee){
      jpb.setString(error);
    }
    if(hint)jpb.setValue(global.dept);
    jp.repaint();
  }
  
  Square[][] getRotationViewSquares(){
    Square[][] sqrs = new Square[CUBES][SQUARES];
    int dim = getDim(layer);
    for(int i = 0; i<CUBES; i++){
      if(cubes[i].contains(layer)) sqrs[i] = cubes[i].getRotationViewSquares(ang, dim);
      else sqrs[i] = cubes[i].getSquares();
    }
    return sqrs;
  }
  
  Square[][] getSquares(){
    Square[][] sqrs = new Square[CUBES][SQUARES];
    for(int i = 0; i<CUBES; i++){
      sqrs[i] = cubes[i].getSquares();
    }
    return sqrs;
  }
  
  private void placeCubes(){    
    cubes[0].setPos(new Point3d(-ofset, -ofset, ofset));
    cubes[0].setPointer(3, 0, 2, true);
    cubes[0].setPointer(3, 1, 0);
    cubes[0].setPointer(1, 0, 4, true);
    cubes[0].setPointer(1, 1, 0, true);
    cubes[0].setPointer(0, 0, 2);
    cubes[0].setPointer(0, 1, 4);
    
    cubes[1].setPos(new Point3d(ofset, -ofset, ofset));
    cubes[1].setPointer(2, 0, 2);
    cubes[1].setPointer(2, 1, 0, true);
    cubes[1].setPointer(1, 0, 5, true);
    cubes[1].setPointer(1, 1, 0);
    cubes[1].setPointer(0, 0, 2, true);
    cubes[1].setPointer(0, 1, 5);
    
    cubes[2].setPos(new Point3d(ofset, ofset, ofset));
    cubes[2].setPointer(2, 0, 0);
    cubes[2].setPointer(2, 1, 3);
    cubes[2].setPointer(4, 0, 0, true);
    cubes[2].setPointer(4, 1, 5);
    cubes[2].setPointer(0, 0, 5, true);
    cubes[2].setPointer(0, 1, 3, true);
    
    cubes[3].setPos(new Point3d(-ofset, ofset, ofset));
    cubes[3].setPointer(3, 0, 0, true);
    cubes[3].setPointer(3, 1, 3, true);
    cubes[3].setPointer(4, 0, 0);
    cubes[3].setPointer(4, 1, 4);
    cubes[3].setPointer(0, 0, 4, true);
    cubes[3].setPointer(0, 1, 3);
    
    cubes[4].setPos(new Point3d(ofset, -ofset, -ofset));
    cubes[4].setPointer(2, 0, 2, true);
    cubes[4].setPointer(2, 1, 1, true);
    cubes[4].setPointer(1, 0, 1);
    cubes[4].setPointer(1, 1, 5);
    cubes[4].setPointer(5, 0, 2);
    cubes[4].setPointer(5, 1, 5, true);
    
    cubes[5].setPos(new Point3d(ofset, ofset, -ofset));
    cubes[5].setPointer(2, 0, 1);
    cubes[5].setPointer(2, 1, 3, true);
    cubes[5].setPointer(4, 0, 5, true);
    cubes[5].setPointer(4, 1, 1, true);
    cubes[5].setPointer(5, 0, 5);
    cubes[5].setPointer(5, 1, 3);
    
    cubes[6].setPos(new Point3d(-ofset, ofset, -ofset));
    cubes[6].setPointer(4, 0, 4, true);
    cubes[6].setPointer(4, 1, 1);
    cubes[6].setPointer(3, 0, 1, true);
    cubes[6].setPointer(3, 1, 3);
    cubes[6].setPointer(5, 0, 4);
    cubes[6].setPointer(5, 1, 3, true);  
    
    cubes[7].setPos(new Point3d(-ofset, -ofset, -ofset));
    cubes[7].setPointer(1, 0, 1, true);
    cubes[7].setPointer(1, 1, 4);
    cubes[7].setPointer(3, 0, 2);
    cubes[7].setPointer(3, 1, 1);
    cubes[7].setPointer(5, 0, 2, true);
    cubes[7].setPointer(5, 1, 4, true);
  }
  
  private void mapVirtualCubeColorsToMyCubes(){
    cubes[0].squares[0].color = vcm.colorMapping(0,0);
    cubes[0].squares[3].color = vcm.colorMapping(0,1);
    cubes[0].squares[1].color = vcm.colorMapping(0,2);

    cubes[1].squares[0].color = vcm.colorMapping(1,0);
    cubes[1].squares[1].color = vcm.colorMapping(1,1);
    cubes[1].squares[2].color = vcm.colorMapping(1,2);

    cubes[2].squares[0].color = vcm.colorMapping(2,0);
    cubes[2].squares[2].color = vcm.colorMapping(2,1);
    cubes[2].squares[4].color = vcm.colorMapping(2,2);
    
    cubes[3].squares[0].color = vcm.colorMapping(3,0);
    cubes[3].squares[4].color = vcm.colorMapping(3,1);
    cubes[3].squares[3].color = vcm.colorMapping(3,2);
    
    cubes[4].squares[2].color = vcm.colorMapping(4,0);
    cubes[4].squares[1].color = vcm.colorMapping(4,1);
    cubes[4].squares[5].color = vcm.colorMapping(4,2);

    cubes[5].squares[2].color = vcm.colorMapping(5,0);
    cubes[5].squares[5].color = vcm.colorMapping(5,1);
    cubes[5].squares[4].color = vcm.colorMapping(5,2);

    cubes[6].squares[3].color = vcm.colorMapping(6,0);
    cubes[6].squares[4].color = vcm.colorMapping(6,1);
    cubes[6].squares[5].color = vcm.colorMapping(6,2);
    
    cubes[7].squares[3].color = vcm.colorMapping(7,0);
    cubes[7].squares[5].color = vcm.colorMapping(7,1);
    cubes[7].squares[1].color = vcm.colorMapping(7,2);
  }
  
  void startTwist(){
    setButtonsOnOff(false);
    thread = new Thread(this);
    thread.start();
  }

class Solution{
  CubeGlobal[] solution;
  int pointer;
  
  Solution(CubeGlobal[] solution){
    this.solution = solution;
    pointer = 0;
  }
  
  CubeGlobal getNext(){
    try{ 
      return solution[pointer++];
    }catch(Exception e){
      return null;
    }
  }
}

  @Override
  public void run(){
    int degs = 90;
    for(int i = 1; i<degs+1; i++){
      try{ extracted(); } catch(InterruptedException igInterruptedException){}
        if(twinvert) ang = Math.toRadians(i) ;
        else ang = -Math.toRadians(i);
      jp.repaint();
    }
    ang=0;
    twistMode = FREE;
    switch(layer){
      case 0 -> {
          if(twinvert) vicube.rotBackCW();  else vicube.rotBackCCW();
          }
      case 1 -> {
          if(twinvert) vicube.rotFrontCW(); else vicube.rotFrontCCW();
          }
      case 2 -> {
          if(twinvert) vicube.rotTopCCW();  else vicube.rotTopCW();
          }
      case 3 -> {
          if(twinvert) vicube.rotBottomCW();else vicube.rotBottomCCW();
          }
      case 4 -> {
          if(twinvert) vicube.rotLeftCW();  else vicube.rotLeftCCW();
          }
      case 5 -> {
          if(twinvert) vicube.rotRightCCW();else vicube.rotRightCW();
          }
    }
    mapVirtualCubeColorsToMyCubes();
    jp.repaint();
    if(solving) {
      solve();
    }else{
      updateProgress("");
      setButtonsOnOff(true);
    }
  }

  private void extracted() throws InterruptedException {
    Thread.sleep(10);
  }
  
  Solution getSolution(){    
    String quiz = identity+";" + vicube.getNomalizedState();
    String answer = getData(quiz);
    StringTokenizer toker = new StringTokenizer(answer, ";");
    int tokens = toker.countTokens();
    CubeGlobal[] ret = new CubeGlobal[tokens];
    for(int t = 0; t < tokens; t++){
      StringTokenizer subToker = new StringTokenizer(toker.nextToken(),":");
      ret[t] = new CubeGlobal(0, 
        Byte.parseByte(subToker.nextToken().trim()),
        Byte.parseByte(subToker.nextToken().trim())
      );
    }
    return new Solution(ret);
  }
  
  public void solve(){
    global = solution.getNext();
    if(global.move > 0){
      switch(global.move){
        case 1 -> {twinvert = false;  layer = 1;}
        case 2 -> {twinvert = true; layer = 1;}
        case 3 -> {twinvert = false;  layer = 3;}
        case 4 -> {twinvert = true; layer = 3;}
        case 5 -> {twinvert = true; layer = 5;}
        case 6 -> {twinvert = false;  layer = 5;}
      }
      startTwist();
    } else {
      solving = false;
      setButtonsOnOff(true);
    }
    if(hint)jpb.setValue(global.dept);
    if(hint)jpb.setString("Moves from completion : "+global.dept);
  }
    
  @Override
  public void mousePressed(MouseEvent e){
    e.getButton();
    lastX = beginX = e.getX();
    lastY = beginY = e.getY();
    sqr = cam.getClickedSquare(e.getX(), e.getY());
    camMode = FREE;
    if(!solving && (e.getButton() != 3) && (twistMode == FREE) && (sqr != null) && (sqr.p3dLayer[0] != -1)) {
      twistMode = FIXING;
      camMode = LOCKED;
    }
  }
  
  @Override
  public void mouseDragged(MouseEvent e){
    int currentX = e.getX(), currentY = e.getY();
    if(camMode == FREE ){
      cam.setAngles((lastX - currentX), (lastY - currentY));
      lastX = currentX;
      lastY = currentY;
    } else if(twistMode == FIXING){ 
      int dx = currentX-beginX, dy = currentY-beginY ;
      if( 10 < Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) ){
        double cx1 = sqr.center.x+dx;
        double cy1 = sqr.center.y+dy;
        double cx2 = sqr.center.x-dx;
        double cy2 = sqr.center.y-dy;
        int d01 = (int)(Math.sqrt(Math.pow(sqr.corners[4].x-cx1, 2) + Math.pow(sqr.corners[4].y-cy1, 2))), 
            d11 = (int)(Math.sqrt(Math.pow(sqr.corners[5].x-cx1, 2) + Math.pow(sqr.corners[5].y-cy1, 2)));
        int d02 = (int)(Math.sqrt(Math.pow(sqr.corners[4].x-cx2, 2) + Math.pow(sqr.corners[4].y-cy2, 2))), 
            d12 = (int)(Math.sqrt(Math.pow(sqr.corners[5].x-cx2, 2) + Math.pow(sqr.corners[5].y-cy2, 2)));
        int d0 = d02-d01, d1 = d12-d11;
        twistMode=TWISTING;
        boolean who = (Math.abs(d0) > Math.abs(d1));
        if(who){
          layer = sqr.p3dLayer[0];
          twinvert = d0 < 0; 
          if(sqr.inv[0]) twinvert = !twinvert;
        } else {
          layer = sqr.p3dLayer[1];
          twinvert = d1 < 0; 
          if(sqr.inv[1]) twinvert = !twinvert;
        } // if(Math.abs(twistX-currentX) < Math.abs(twistY-currentY) ){
        startTwist();
      }//if( (int)(twistDx0-dx0) != (int)(twistDx1-dx1) ){
    }
    jp.repaint();
  }
  
  @Override
  public void mouseReleased(MouseEvent e){
    if(twistMode != TWISTING) twistMode = FREE;
  }
  
boolean hint = true;
class ButtLiznr implements ActionListener, Runnable{
  ActionEvent e;
  
  @Override
  public void run(){    
    setButtonsOnOff(false);
    
    if(map.ok()){
      try{
        switch(e.getActionCommand()){
          case "RanCol" ->{
            vcm.randColors();
            mapVirtualCubeColorsToMyCubes();
            jp.repaint();
            setButtonsOnOff(true);
          } 
          case "Hint" ->{
            hint = !hint;
            setButtonsOnOff(true);
          } 
          case "Solve" ->{
            solving = true;
            cam.remView();
            vicube.normalize();
            mapVirtualCubeColorsToMyCubes();
            cam.adjust();
            jp.repaint();
            solution = getSolution();
            solve();
          } 
          default ->{
            
            updateProgress("Random-"+e.getActionCommand());
            if(global.state != 0){
              vicube.setState(global.state);
              mapVirtualCubeColorsToMyCubes();
            }
            jp.repaint();
            setButtonsOnOff(true);
          }
        }
      }catch(Exception ignore){}
    }else{
      System.out.println("My map is corupt.");
    }
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
    this.e = e;
    Thread t = new Thread(this);
    t.start();
  }

} // * class *

  int getDim(int layer){
    int retur = 0;
    switch(layer){
      case 0 -> retur = Z;
      case 1 -> retur = Z;
      case 2 -> retur = Y;
      case 3 -> retur = Y;
      case 4 -> retur = X;
      case 5 -> retur = X;
    }
    return retur;
  }
  
  @Override
  public void mouseMoved(MouseEvent e){}  
  @Override
  public void mouseClicked(MouseEvent e){}
  @Override
  public void mouseEntered(MouseEvent e){}
  @Override
  public void mouseExited(MouseEvent e){} 
}//**class RubCube