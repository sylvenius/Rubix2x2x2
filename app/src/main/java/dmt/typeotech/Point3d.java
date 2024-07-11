/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

class Point3d{
  public static final long serialVersionUID = 200511132127L;
  double x, y , z;
  
  @Override
  public String toString(){
    return "["+x+"]["+y+"]["+z+"]";
  }
  
  Point3d(double x, double y, double z){
    this.x = x;  this.y = y;  this.z = z;
  }
  
  // Point3d(Point3d p){
  //   this.x = p.x;  this.y = p.y;  this.z = p.z;
  // }

  double[] getP4(double d){
    double ret[] = {x, y, z, d};
    return ret;
  }

  static Point3d turnPoint(double ang, int dim, Point3d p){
    final int X=0, Y=1, Z=2;
    double x=p.x, y=p.y, z=p.z;
    switch (dim){
      case X : 
        y = p.y * Math.cos(ang) - p.z * Math.sin(ang); //rotating about x axis
        z = p.y * Math.sin(ang) + p.z * Math.cos(ang); 
      break;
      case Y : 
        x = p.x * Math.cos(ang) - p.z * Math.sin(ang); // for Y axis
        z = p.x * Math.sin(ang) + p.z * Math.cos(ang);
      break;
      case Z : 
        x = p.x * Math.cos(ang) - p.y * Math.sin(ang); //about Z axis
        y = p.x * Math.sin(ang) + p.y * Math.cos(ang);
      break;
    }
    return new Point3d(x, y, z);
  }
  
}//**class Point3d