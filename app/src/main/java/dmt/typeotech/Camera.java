/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

class Camera{
  public static final long serialVersionUID = 200511132123L;
  Rubik2x2Cube cube2x2;
  static double dist;
  double off, size=200;
  Set<Square> camViewSquares, camRemViewSquares;
  Matrix4x4 cammat, rotmat, solmat;
  
  Camera(double dist, Rubik2x2Cube cube2x2){
    camViewSquares = new TreeSet<>();
    camRemViewSquares = new TreeSet<>();
    Camera.dist = dist; 
    this.cube2x2 = cube2x2;
    cammat = new Matrix4x4();
    rotmat = new Matrix4x4();
    solmat = new Matrix4x4();
  }

  Square getClickedSquare(int x, int y){
    Object[] squares = camViewSquares.toArray();
    for(int i = squares.length-1; i > -1; i--){
      Square square = (Square)squares[i];
      if(square.intersectXY(x, y)){
        return square;
      }
    }
    return null;
  }
  
  public void paint(Graphics2D g2d){
    camViewSquares.clear();
    off = Math.min(cube2x2.jp.getWidth()/2, cube2x2.jp.getHeight()/2);
    Square[][] allsqrs = cube2x2.getRotationViewSquares();
    for(int c = 0; c < Rubik2x2Cube.CUBES; c++){ 
      Square[] sqrs = allsqrs[c];
      for(int s = 0; s<Rubik2x2Cube.SQUARES; s++){
        Square scamview = getCameraView(sqrs[s], true);
        if(scamview != null ){
          camViewSquares.add(scamview);
        }
      }
    }
    
    int[] xs = new int[4], ys = new int[4];
      for (Square sqr : camViewSquares) {
          sqr.setIntArrays(xs, ys);
          g2d.setColor(sqr.color);
          g2d.fillPolygon(xs, ys, 4);
          g2d.setColor(new Color(0,0,0));
          if(!sqr.inNer)g2d.drawPolygon(xs, ys, 4);
      }
  }
  
  void remView(){
    camRemViewSquares.clear();
    Square[][] allsqrs = cube2x2.getSquares();
    for(int c = 0; c < Rubik2x2Cube.CUBES; c++){
      Square[] sqrs = allsqrs[c];
      for(int s = 0; s < Rubik2x2Cube.SQUARES; s++){
        Square scamview = getCameraView(sqrs[s], false);
        if(scamview != null ){
          camRemViewSquares.add(scamview);
        }
      }
    }
  }
  
  boolean equals(Set<Square> s1, Set<Square> s2){
    if(s1.size() != s2.size()) return false;
    Iterator<Square> i1 = s1.iterator();
    Iterator<Square> i2 = s2.iterator();
    while(i1.hasNext()){
      Square sq1 = i1.next();
      Square sq2 = i2.next();
      if(!sq1.color.equals(sq2.color)) return false;
    }
    return true;
  }
  
  void adjust(){
    Set<Square> sqrset = new TreeSet<>();
    Square[][] allsqrs = cube2x2.getSquares();
    sqrset.clear();
    for(int c = 0; c < Rubik2x2Cube.CUBES; c++){
      Square[] sqrs = allsqrs[c];
      for(int s = 0; s < Rubik2x2Cube.SQUARES; s++){
        Square scamview = getCameraView(sqrs[s], false);
        if(scamview != null ){
          sqrset.add(scamview);
        }
      }
    }
      
    Random ran = new Random();
    int count = 0;
    while(!equals(camRemViewSquares, sqrset) && (count < 1000) ){
      count+=1;
      rotCubeView(ran.nextInt(3)+1);  //probalby less work than a regular method 4 finding the right state :) :) no seriosly. if this is'nt fixed I just forgot to make a propper solution, but I can't be f**d w i right now
      sqrset.clear();
      for(int c = 0; c < Rubik2x2Cube.CUBES; c++){
        Square[] sqrs = allsqrs[c];
        for(int s = 0; s < Rubik2x2Cube.SQUARES; s++){
          Square scamview = getCameraView(sqrs[s], false);
          if(scamview != null ){
            sqrset.add(scamview);
          }
        }
      }
    }
  }
  
  void rotCubeView(int i){
    switch(i){
      case 1 -> setMatrixRotation(90, 0, 0);
      case 2 -> setMatrixRotation(0, 90, 0);
      case 3 -> setMatrixRotation(0, 0, 90);
    }
  }
  
  void setAngles(double dx, double dy){
    setMatrixRotation(dy, dx);
  }
  
  void setMatrixRotation(double xtheta, double ytheta){
    rotmat.identity();
    rotmat.rotx(xtheta);
    rotmat.roty(ytheta);
    cammat = Matrix4x4.mult(rotmat, cammat);
  }
  
  void setMatrixRotation(double xtheta, double ytheta, double ztheta){
    rotmat.identity();
    rotmat.rotx(xtheta);
    rotmat.roty(ytheta);
    rotmat.rotz(ztheta);
    solmat = Matrix4x4.mult(rotmat, solmat);
  }
  
  Point3d camView(Point3d p){
    Matrix4x4 mat = Matrix4x4.mult(cammat, solmat);
    return Matrix4x4.mult(mat, p, 1);
  }
  
  Square getCameraView(Square sqr, boolean iner){
    if(!iner && sqr.inNer){
      return null;
    } else {
      Point3d center = camView(sqr.corners[6]);
      Point3d normal = camView(sqr.corners[7]);

      double dsqc = Math.sqrt(Math.pow(center.x, 2) + Math.pow(center.y, 2) + Math.pow(dist + center.z, 2));
      double dsqn = Math.sqrt(Math.pow(normal.x, 2) + Math.pow(normal.y, 2) + Math.pow(dist + normal.z, 2));
      if(dsqn < dsqc) {
        double dxyz;
        Square retur = new Square(sqr);        
        retur.corners[6] = center;
        retur.corners[7] = normal;
        for(int i = 0; i<6; i++){
          Point3d sq = camView(sqr.corners[i]);
          dxyz = Math.sqrt(Math.pow(sq.x, 2) + Math.pow(sq.y, 2) + Math.pow(dist + sq.z, 2));
          Double zoom = dist/dxyz;
          sq.x = sq.x * zoom * (off/size);
          sq.y = sq.y * zoom * (off/size);
          retur.corners[i].x = off+(int)sq.x;
          retur.corners[i].y = off+(int)sq.y;
        }
        retur.center = new Point3d(0,0,0);
        Point3d sq = camView(sqr.corners[6]);
        dxyz = Math.sqrt(Math.pow(sq.x, 2) + Math.pow(sq.y, 2) + Math.pow(dist + sq.z, 2));
        Double zoom = dist/dxyz;
        sq.x = sq.x * zoom * (off/size);
        sq.y = sq.y * zoom * (off/size);
        retur.center.x = off+(int)sq.x;
        retur.center.y = off+(int)sq.y;
        return retur;
      } else {
        return null;
      }
    }
  }
  
}

