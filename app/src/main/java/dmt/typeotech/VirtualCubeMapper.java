/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.Color;
import java.util.Random;

final class VirtualCubeMapper{
  public static final long serialVersionUID = 200511132131L;
  Color[] colors = new Color[7];
  Random ran = new Random();
  VirtualCube2x2 vicube;

  VirtualCubeMapper(VirtualCube2x2 vicube){
    this.vicube = vicube;

    colors[0] = new Color(255, 0, 0);
    colors[1] = new Color(0, 0, 255);
    colors[2] = new Color(255,140,0);
    colors[3] = new Color(255, 0, 255);
    colors[4] = new Color(245, 249, 60);
    colors[5] = new Color(77, 194, 62);
    colors[6] = new Color(128,128,128);

  }//VirtualCubeMapper(VirtualCube2x2 vicube)
    
  public void randColors(){
    colors[0] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[1] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[2] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[3] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[4] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[5] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    colors[6] = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
  }

  Color colorMapping(int x, int y){
      // Color retur;
      return switch (vicube.cube[x][y]) {
          case 1 -> colors[0];
          case 2 -> colors[1];
          case 3 -> colors[2];
          case 4 -> colors[3];
          case 5 -> colors[4];
          case 6 -> colors[5];
          default -> colors[6];
      };
  }

}