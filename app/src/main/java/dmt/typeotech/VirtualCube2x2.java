/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.util.ArrayList;
import java.util.List;

final class VirtualCube2x2{
  public static final long serialVersionUID = 200511132132L;
  static final int back = 1, left = 2, right = 3, top = 4, bottom = 5, front = 6;
  static final int WCB = 1, WCL = 2, WCL2 = 3;
  private long state;
  int[][] cube;
    

  VirtualCube2x2(int[][] cube){ 
    int newcube[][] = new int[8][3];
    copyCube(cube, newcube);
    this.cube = newcube; 
  }
  
  VirtualCube2x2(){
    cube = new int[8][3];
    
    cube[0][0] = back;
    cube[0][1] = left;
    cube[0][2] = top;
    
    cube[1][0] = back;
    cube[1][1] = top;
    cube[1][2] = right;
    
    cube[2][0] = back;
    cube[2][1] = right;
    cube[2][2] = bottom;
    
    cube[3][0] = back;
    cube[3][1] = bottom;
    cube[3][2] = left;
    
    cube[4][0] = right;
    cube[4][1] = top;
    cube[4][2] = front;
    
    cube[5][0] = right;
    cube[5][1] = front;
    cube[5][2] = bottom;
    
    cube[6][0] = left;
    cube[6][1] = bottom;
    cube[6][2] = front;
    
    cube[7][0] = left;
    cube[7][1] = front;
    cube[7][2] = top;

  }
  
  void copyCube(int[][] org, int[][] copy){
    System.arraycopy(org[0], 0, copy[0], 0, 3);
    System.arraycopy(org[1], 0, copy[1], 0, 3);
    System.arraycopy(org[2], 0, copy[2], 0, 3);
    System.arraycopy(org[3], 0, copy[3], 0, 3);
    System.arraycopy(org[4], 0, copy[4], 0, 3);
    System.arraycopy(org[5], 0, copy[5], 0, 3);
    System.arraycopy(org[6], 0, copy[6], 0, 3);
    System.arraycopy(org[7], 0, copy[7], 0, 3);
  }

  int getBits(int shift){
    long mask = 7;
    long val = state >> shift;
    val = val & mask;
    return (int)val;
  }

  void setBits(int shift, long val){
    long full = 0xffffffff;
    long pre = full << (shift+3);
    long post = (long)Math.pow(2, shift)-1;
    full = pre | post;
    state = state & full;
    long mask=0;
    mask = mask | val << shift;
    state = state | mask;
  }
  
  long getNomalizedState(){
    VirtualCube2x2 retur = new VirtualCube2x2(cube);
    retur.normalize();
    return retur.getState();
  }
  
  void setState(long l){
    state = l;
    int p = 0;
    cube[0][0] = back;
    cube[0][1] = left;
    cube[0][2] = top;
    for(int i = 1; i<8; i++)
      for(int j = 0; j<3; j++){
          cube[i][j] = getBits(p);
        p+=3;
      }
  }
  
  long getState(){
    state=0x0000000000000000;
    int p = 0;
    for(int i = 1; i<8; i++)
      for(int j = 0; j<3; j++){
        setBits(p, cube[i][j]);
        p+=3;
      }
    return state;
  }
  
  boolean isNormalized(){
    return (
      cube[0][0] == back &&
      cube[0][1] == left &&
      cube[0][2] == top
    );
  }
  
  boolean isHalfNormalized(){
    List<Integer> list = new ArrayList<>(3);
    list.add(cube[0][0]);
    list.add(cube[0][1]);
    list.add(cube[0][2]);
    return (list.contains(back) && list.contains(left) && list.contains(top));
  }
  
  void normalize(){
    int i=0;
    int[] normlMoves = {WCB, WCB, WCB, WCL2, WCB, WCB, WCB};
    while(!isHalfNormalized()){
      rotWholeCube(normlMoves[i++]);
    }
    while(!isNormalized()){
      rotWholeCube(WCB);
      rotWholeCube(WCL);
    }
  }
  
  void rotWholeCube(int i){
    switch(i){
      case WCB : 
        rotLeftCCW();
        rotRightCW();
      break;
      case WCL : 
        rotFrontCW();
        rotBackCW();
      break;
      case WCL2 : 
        rotFrontCW();
        rotFrontCW();
        rotBackCW();
        rotBackCW();
      break;
    }
  }
  
  void rotBottomCW(){
    turnBottom(cube, 1);
  }
  
  void rotBottomCCW(){
    turnBottom(cube, 3);
  }
  
  void turnBottom(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n2 = {intArr[5][0], intArr[5][1], intArr[5][2]};
      int[] n3 = {intArr[2][1], intArr[2][2], intArr[2][0]};
      int[] n5 = {intArr[6][2], intArr[6][0], intArr[6][1]};
      int[] n6 = {intArr[3][0], intArr[3][1], intArr[3][2]};
      intArr[2] = n2;
      intArr[3] = n3;
      intArr[5] = n5;
      intArr[6] = n6;
    }
  }
    
  void rotRightCW(){
    turnRight(cube, 1);
  }
  
  void rotRightCCW(){
    turnRight(cube, 3);
  }
  
  void turnRight(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n1 = {intArr[4][1], intArr[4][2], intArr[4][0]};
      int[] n2 = {intArr[1][1], intArr[1][2], intArr[1][0]};
      int[] n4 = {intArr[5][0], intArr[5][1], intArr[5][2]};
      int[] n5 = {intArr[2][1], intArr[2][2], intArr[2][0]};
      intArr[1] = n1;
      intArr[2] = n2;
      intArr[4] = n4;
      intArr[5] = n5;
    }
  } 
  
  void rotFrontCW(){
    turnFront(cube, 1);
  }
  
  void rotFrontCCW(){
    turnFront(cube, 3);
  }
  
  void turnFront(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n4 = {intArr[7][2], intArr[7][0], intArr[7][1]};
      int[] n5 = {intArr[4][1], intArr[4][2], intArr[4][0]};
      int[] n6 = {intArr[5][2], intArr[5][0], intArr[5][1]};
      int[] n7 = {intArr[6][1], intArr[6][2], intArr[6][0]};
      intArr[4] = n4;
      intArr[5] = n5;
      intArr[6] = n6;
      intArr[7] = n7;
    }
  }
  
  void rotTopCW(){
    turnTop(cube, 1);
  }
  
  void rotTopCCW(){
    turnTop(cube, 3);
  }
  
  void turnTop(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n0 = {intArr[7][0], intArr[7][1], intArr[7][2]};
      int[] n1 = {intArr[0][1], intArr[0][2], intArr[0][0]};
      int[] n4 = {intArr[1][0], intArr[1][1], intArr[1][2]};
      int[] n7 = {intArr[4][2], intArr[4][0], intArr[4][1]};
      intArr[0] = n0;
      intArr[1] = n1;
      intArr[4] = n4;
      intArr[7] = n7;
    }
  }
  
  void rotLeftCW(){
    turnLeft(cube, 1);
  }
    
  void rotLeftCCW(){
    turnLeft(cube, 3);
  }
  
  void turnLeft(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n0 = {intArr[3][1], intArr[3][2], intArr[3][0]};
      int[] n3 = {intArr[6][1], intArr[6][2], intArr[6][0]};
      int[] n6 = {intArr[7][0], intArr[7][1], intArr[7][2]};
      int[] n7 = {intArr[0][1], intArr[0][2], intArr[0][0]};
      intArr[0] = n0;
      intArr[3] = n3;
      intArr[6] = n6;
      intArr[7] = n7;
    }
  } 
  
  void rotBackCW(){
    turnBack(cube, 1);
  }
  
  void rotBackCCW(){
    turnBack(cube, 3);
  }
  
  void turnBack(int[][] intArr, int turns){
    for(int i = 0; i<turns; i++){
      int[] n0 = {intArr[3][0], intArr[3][1], intArr[3][2]};
      int[] n1 = {intArr[0][0], intArr[0][1], intArr[0][2]};
      int[] n2 = {intArr[1][0], intArr[1][1], intArr[1][2]};
      int[] n3 = {intArr[2][0], intArr[2][1], intArr[2][2]};
      intArr[0] = n0;
      intArr[1] = n1;
      intArr[2] = n2;
      intArr[3] = n3;
    }
  }
  
  void turn(int i){
    switch(i){
      case 1  : rotFrontCW();   break;
      case 2  : rotFrontCCW();  break;
        
      case 3  : rotBottomCW();   break;
      case 4  : rotBottomCCW();  break;
        
      case 5  : rotRightCW();  break;
      case 6  : rotRightCCW(); break;
    }
  }
  
  @Override
  public String toString(){
    String ret = "{\n";
    for(int i = 0;i<8;i++){
      for(int j = 0; j<3;j++)
        ret+="["+cube[i][j]+"]";
      ret+="\n";
    }
    return ret+="}";
  }
  
}