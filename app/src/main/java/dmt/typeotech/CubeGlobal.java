/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

class CubeGlobal{
  public static final long serialVersionUID = 200511132125L;
  long state;
  byte move, dept;
  CubeGlobal(){
    state = 0;
    move = 0;
    dept = 0;
  }

  CubeGlobal(long state, byte dept, byte move){
    this.state = state;
    this.move = move;
    this.dept = dept;
  }
  
  @Override
  public String toString(){
    return "[" +state+ "][" +dept+ "]["+move+"]";
  }
  
}//**class Point3d