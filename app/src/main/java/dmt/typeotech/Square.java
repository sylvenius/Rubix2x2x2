/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.Color;

class Square implements Comparable<Square>{
  public static final long serialVersionUID = 200511132128L;
  static int id=0, CENTER=6;
  int myId, p3dLayer[] = new int[2];
  final int POINTS=8;
  Point3d[] corners;
  Point3d center;
  Color color;
  Cube parent;
  boolean inv[] = new boolean[2];  
  boolean inNer = true;
    
  // void turnPrivatePoints(int dim, double ang){
  //   for(int i = 4; i<8; i++){
  //     corners[i] = Point3d.turnPoint(ang, dim, corners[i]);
  //   }
  // }
  
  Square(Square other){
    myId = other.myId;
    corners = new Point3d[POINTS];
    this.color = other.color;
    // this.parent = parent;
    for(int i=0; i<POINTS; i++){
      corners[i] = new Point3d(other.corners[i].x, other.corners[i].y, other.corners[i].z);
    }
    p3dLayer[0] = other.p3dLayer[0];
    p3dLayer[1] = other.p3dLayer[1];
    inv[0] = other.inv[0];
    inv[1] = other.inv[1];
    parent = other.parent;
    inNer = other.inNer;
  }
  
  Square(Point3d c1, Point3d c2, Point3d c3, Point3d c4, Point3d center, Point3d normal, Cube parent){
    myId = id++;
    corners = new Point3d[POINTS];
    corners[0] = c1;  corners[1] = c2;  corners[2] = c3;  corners[3] = c4;     
    corners[6] = center;
    corners[7] = normal;
    color = new Color(0,0,0);
    this.parent = parent;
    corners[4] = new Point3d(0,0,0);
    corners[5] = new Point3d(0,0,0); 
    p3dLayer[0] = -1;
    p3dLayer[1] = -1;
    inv[0] = false;
    inv[1] = false;
  }

  boolean contains(int layer){
    return (p3dLayer[0]==layer ||  p3dLayer[1] == layer);
  }
  
  void setPointers(){
    int c=0, f=0;
    double closestD, farestD, dist;
    farestD = closestD = Math.sqrt(Math.pow(corners[0].x, 2) + Math.pow(corners[0].y, 2) + Math.pow(corners[0].z, 2) );
    for(int i = 0; i<4; i++){
      dist = Math.sqrt(Math.pow(corners[i].x, 2) + Math.pow(corners[i].y, 2) + Math.pow(corners[i].z, 2) );
      if (dist < closestD){
        closestD = dist;
        c = i;
      }
      if(dist > farestD){
        farestD = dist;
        f = i;
      }
    }
    int j=4;
    for(int i = 0; i<4; i++){
      if( (i!= c) && (i != f) ){
        corners[j] = new Point3d((corners[c].x + corners[i].x)/2, (corners[c].y + corners[i].y)/2, (corners[c].z + corners[i].z)/2 );
        j++;
      }
    }
  }
  
  Square getRotationView(double ang, int dim){
    Square sqr = new Square(this);
    for(int i = 0;i<POINTS;i++){
      sqr.corners[i] = Point3d.turnPoint(ang, dim, corners[i]);
    } 
    return sqr;
  }
   
  /**
  * intersectXY is translated (and slightly simplyfied for only 4-pointed polygons) from  Java Randolph Franklin  C algoritm 
  * int pnpoly(int npol, float *xp, float *yp, float x, float y)
  * at http://astronomy.swin.edu.au/~pbourke/geometry/insidepoly/
  */
  boolean intersectXY(int x, int y){
    int i, j;
    boolean c = false;
    for (i = 0, j = 3; i < 4; j = i++) {
      if ((((corners[i].y <= y) && (y < corners[j].y)) || 
         ((corners[j].y <= y) && (y < corners[i].y))) &&
         (x < (corners[j].x - corners[i].x) * (y - corners[i].y) / (corners[j].y - corners[i].y) + corners[i].x))
      c = !c;
    }
    return c;
  }

  @Override
  public String toString(){
    return "[Squre : " + myId + "  of  " + id + " my color is " + color + "] ";
  }
  
  @Override
  public int compareTo(Square o){
    Square other = o;
    double mydist = Math.sqrt(Math.pow(corners[CENTER].x, 2) + Math.pow(corners[CENTER].y, 2) + Math.pow(Camera.dist + corners[CENTER].z, 2));
    double otherdist = Math.sqrt(Math.pow(other.corners[CENTER].x, 2) + Math.pow(other.corners[CENTER].y, 2) + Math.pow(Camera.dist + other.corners[CENTER].z, 2));
    if( (inNer && other.inNer) || (!inNer && !other.inNer) ){
      if(otherdist < mydist) return -1;
      return 1;
    }else{
      if(!other.inNer && inNer) return -1;
      return 1;
    }

  }
  
  void setIntArrays(int[] xs, int[] xy){
    for(int i = 0; i<4; i++){
      xs[i] = (int)corners[i].x;
      xy[i] = (int)corners[i].y;
    }
  }
  
}//**class Square