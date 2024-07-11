/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

class Cube{
  public static final long serialVersionUID = 200511132124L;
  static int id=0;
  static final int SQUARES = 6, POINTS = 9;
  Square[] squares = new Square[SQUARES];
  Square[] rotationSquares = new Square[SQUARES];
  Point3d[] points = new Point3d[POINTS];
  
  Cube(double size){
    id++;
    double s = size/2;
    points[0] = new Point3d(0,0,0);
    points[1] = new Point3d(-s, -s,  s);
    points[2] = new Point3d(s,  -s,  s);
    points[3] = new Point3d(s,  s, s);
    points[4] = new Point3d(-s, s, s);
    points[5] = new Point3d(-s, -s,  -s);
    points[6] = new Point3d(s,  -s,  -s);
    points[7] = new Point3d(s,  s, -s);
    points[8] = new Point3d(-s, s, -s);
    
    Point3d c, n;
    c = new Point3d((points[1].x + points[2].x + points[3].x +points[4].x)/4, (points[1].y + points[2].y + points[3].y + points[4].y)/4, (points[1].z + points[2].z + points[3].z + points[4].z)/4);
    n = new Point3d(c.x, c.y, c.z+10);
    squares[0] = new Square(points[1], points[2], points[3], points[4], c, n, this);
    
    c = new Point3d((points[5].x + points[6].x + points[2].x +points[1].x)/4, (points[5].y + points[6].y + points[2].y + points[1].y)/4, (points[5].z + points[6].z + points[2].z + points[1].z)/4);
    n = new Point3d(c.x, c.y-10, c.z);
    squares[1] = new Square(points[5], points[6], points[2], points[1], c, n, this);
    
    c = new Point3d((points[2].x + points[6].x + points[7].x +points[3].x)/4, (points[2].y + points[6].y + points[7].y + points[3].y)/4, (points[2].z + points[6].z + points[7].z + points[3].z)/4);
    n = new Point3d(c.x+10, c.y, c.z);
    squares[2] = new Square(points[2], points[6], points[7], points[3], c, n, this);
    
    c = new Point3d((points[5].x + points[1].x + points[4].x +points[8].x)/4, (points[5].y + points[1].y + points[4].y + points[8].y)/4, (points[5].z + points[1].z + points[4].z + points[8].z)/4);
    n = new Point3d(c.x-10, c.y, c.z);
    squares[3] = new Square(points[5], points[1], points[4], points[8], c, n, this);
    
    c = new Point3d((points[4].x + points[3].x + points[7].x +points[8].x)/4, (points[4].y + points[3].y + points[7].y + points[8].y)/4, (points[4].z + points[3].z + points[7].z + points[8].z)/4);
    n = new Point3d(c.x, c.y+10, c.z);
    squares[4] = new Square(points[4], points[3], points[7], points[8], c, n, this);
    
    c = new Point3d((points[6].x + points[5].x + points[8].x +points[7].x)/4, (points[6].y + points[5].y + points[8].y + points[7].y)/4, (points[6].z + points[5].z + points[8].z + points[7].z)/4);
    n = new Point3d(c.x, c.y, c.z-10);
    squares[5] = new Square(points[6], points[5], points[8], points[7], c, n, this);
  }

  void setPos(Point3d to){
    double dx, dy, dz;
    dx = to.x-points[0].x; dy = to.y-points[0].y; dz = to.z-points[0].z;
    points[0]=to;
    points[1].x+=dx; points[1].y+=dy; points[1].z+=dz;
    points[2].x+=dx; points[2].y+=dy; points[2].z+=dz;
    points[3].x+=dx; points[3].y+=dy; points[3].z+=dz;
    points[4].x+=dx; points[4].y+=dy; points[4].z+=dz;
    points[5].x+=dx; points[5].y+=dy; points[5].z+=dz;
    points[6].x+=dx; points[6].y+=dy; points[6].z+=dz;
    points[7].x+=dx; points[7].y+=dy; points[7].z+=dz;
    points[8].x+=dx; points[8].y+=dy; points[8].z+=dz;
    for(int i = 0; i<SQUARES; i++){
      squares[i].corners[6].x += dx; squares[i].corners[6].y += dy; squares[i].corners[6].z += dz;
      squares[i].corners[7].x += dx; squares[i].corners[7].y += dy; squares[i].corners[7].z += dz;
      squares[i].setPointers();
    }
  }
  
  void setPointer(int square, int nr, int layer){
    squares[square].p3dLayer[nr] = layer;
    squares[square].inNer = false;
  }
  void setPointer(int square, int nr, int layer, boolean inv){
    squares[square].p3dLayer[nr] = layer;
    squares[square].inv[nr] = inv;
    squares[square].inNer = false;
  }
  
  boolean contains(int layer){
    boolean ret = false;
    for(Square s : squares){
      ret = ret || s.contains(layer);
      if(ret) return ret;
    }
    return ret;
  }
  
  Square[] getSquares(){
    return squares;
  }
  
  Square[] getRotationViewSquares(double ang, int dim){
    for(int i=0;i<SQUARES;i++){
      rotationSquares[i] = squares[i].getRotationView(ang, dim);
    }
    return rotationSquares;
  }
    
}//**class Cube