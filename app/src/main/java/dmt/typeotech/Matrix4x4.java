/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

class Matrix4x4{
  public static final long serialVersionUID = 200511132126L;
  double[][] matrix = {{1,0,0,0},
                        {0,1,0,0},
                         {0,0,1,0},
                          {0,0,0,1} };
  
  // Matrix4x4(double[][] matrix){
  //   this.matrix = matrix;
  // }
  
  Matrix4x4(){
  }
  
  static Point3d mult(Matrix4x4 mat, Point3d p, double d){
    double[] dxs = p.getP4(d);
    double[] dys = new double[4];
    for (int i = 0; i < 4; ++i){
      dys[i] = 0.0d;
      for (int j = 0; j < 4; ++j){
          dys[i] += mat.matrix[i][j] * dxs[j];
      }
    }
    Point3d np = new Point3d(dys[0], dys[1], dys[2]);
    return np;
  }

  static Matrix4x4 mult(Matrix4x4 mA, Matrix4x4 mB){
    Matrix4x4 mC = new Matrix4x4();
    for (int i = 0; i < 4; ++i){
      for (int j = 0; j < 4; ++j){
        mC.matrix[i][j] = 0.0d;
        for (int k = 0; k < 4; ++k)
          mC.matrix[i][j] += mA.matrix[i][k] * mB.matrix[k][j];
      }
    }
    return mC;
  }

  // void mult(Matrix4x4 mO){
  //   Matrix4x4 mN = new Matrix4x4();
  //   for (int i = 0; i < 4; ++i){
  //     for (int j = 0; j < 4; ++j){
  //       mN.matrix[i][j] = 0.0d;
  //       for (int k = 0; k < 4; ++k)
  //         mN.matrix[i][j] += matrix[i][k] * mO.matrix[k][j];
  //     }
  //   }
  //   this.matrix = mN.matrix;
  // }
  
  void rotx(double theta){
    theta *= 0.017453292519943295D;
    double ct = Math.cos(theta);
    double st = Math.sin(theta);
    double nyx = matrix[1][0] * ct + matrix[2][0] * st;
    double nyy = matrix[1][1] * ct + matrix[2][1] * st;
    double nyz = matrix[1][2] * ct + matrix[2][2] * st;
    double nyo = matrix[1][3] * ct + matrix[2][3] * st;
    double nzx = matrix[2][0] * ct - matrix[1][0] * st;
    double nzy = matrix[2][1] * ct - matrix[1][1] * st;
    double nzz = matrix[2][2] * ct - matrix[1][2] * st;
    double nzo = matrix[2][3] * ct - matrix[1][3] * st;
    matrix[1][3] = nyo;
    matrix[1][0] = nyx;
    matrix[1][1] = nyy;
    matrix[1][2] = nyz;
    matrix[2][3] = nzo;
    matrix[2][0] = nzx;
    matrix[2][1] = nzy;
    matrix[2][2] = nzz;
  }
  
  void roty(double theta){
    theta *= 0.017453292519943295D;
    double ct = Math.cos(theta);
    double st = Math.sin(theta);
    double nxx = matrix[0][0] * ct + matrix[2][0] * st;
    double nxy = matrix[0][1] * ct + matrix[2][1] * st;
    double nxz = matrix[0][2] * ct + matrix[2][2] * st;
    double nxo = matrix[0][3] * ct + matrix[2][3] * st;
    double nzx = matrix[2][0] * ct - matrix[0][0] * st;
    double nzy = matrix[2][1] * ct - matrix[0][1] * st;
    double nzz = matrix[2][2] * ct - matrix[0][2] * st;
    double nzo = matrix[2][3] * ct - matrix[0][3] * st;
    matrix[0][3] = nxo;
    matrix[0][0] = nxx;
    matrix[0][1] = nxy;
    matrix[0][2] = nxz;
    matrix[2][3] = nzo;
    matrix[2][0] = nzx;
    matrix[2][1] = nzy;
    matrix[2][2] = nzz;
  }
    
  void rotz(double theta){
    theta *= 0.017453292519943295D;
    double ct = Math.cos(theta);
    double st = Math.sin(theta);
    double nxx = matrix[0][0] * ct + matrix[1][0] * st;
    double nxy = matrix[0][1] * ct + matrix[1][1] * st;
    double nxz = matrix[0][2] * ct + matrix[1][2] * st;
    double nxo = matrix[0][3] * ct + matrix[1][3] * st;
    double nyx = matrix[1][0] * ct - matrix[0][0] * st;
    double nyy = matrix[1][1] * ct - matrix[0][1] * st;
    double nyz = matrix[1][2] * ct - matrix[0][2] * st;
    double nyo = matrix[1][3] * ct - matrix[0][3] * st;
    matrix[0][3] = nxo;
    matrix[0][0] = nxx;
    matrix[0][1] = nxy;
    matrix[0][2] = nxz;
    matrix[1][3] = nyo;
    matrix[1][0] = nyx;
    matrix[1][1] = nyy;
    matrix[1][2] = nyz;
  }
  
  void identity(){
    double[][] ret = {  {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0}, 
                        {0, 0, 0, 1} };
    matrix = ret;
  }
  

  // static Matrix4x4 yrot(double theta){
  //   double[][] ret = { {Math.cos(theta),  0,                Math.sin(theta),  0},
  //                      {0,                1,                0,                0},
  //                      {-Math.sin(theta), 0,                Math.cos(theta),  0}, 
  //                      {0,                0,                0,                1} };
  //   return new Matrix4x4(ret);
  // }

  // static Matrix4x4 zrot(double theta){
  //   double[][] ret = { {Math.cos(theta), -Math.sin(theta) , 0,                0},
  //                      {Math.sin(theta), Math.cos(theta),   0,                0},
  //                      {0,               0,                 1,                0}, 
  //                      {0,               0,                 0,                1} };
  //   return new Matrix4x4(ret);
  // }
  
  @Override
  public String toString(){
    return "{ \n" +
           " ["+matrix[0][0]+"]["+matrix[0][1]+"]["+matrix[0][2]+"]["+matrix[0][3]+"],\n" +
           " ["+matrix[1][0]+"]["+matrix[1][1]+"]["+matrix[1][2]+"]["+matrix[1][3]+"],\n" +  
           " ["+matrix[2][0]+"]["+matrix[2][1]+"]["+matrix[2][2]+"]["+matrix[2][3]+"],\n" + 
           " ["+matrix[3][0]+"]["+matrix[3][1]+"]["+matrix[3][2]+"]["+matrix[3][3]+"]\n}"; 
  }

}







