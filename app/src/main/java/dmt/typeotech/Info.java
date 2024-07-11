/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Info{
  public static final long serialVersionUID = 202407042122L;
    // static int nof_depts = 14;
  // static int max_perm_per_dept  = 1350852;
  static int total_permutations = 3674159;
  static long map_size = 70081515;
  static int[][] per_level = {
          {0,1},
          {1,6},
          {2,27},
          {3,120},
          {4,534},
          {5,2256},
          {6,8969},
          {7,35058},
          {8,114149},
          {9,360508},
          {10,930588},
          {11,1350852},
          {12,782536},
          {13,90280},
          {14,276}
    };

    public static long getBytesFromList(ArrayList<HashMap<Long,Short>> list){
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
          try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
              out.writeObject(list);
          }
      } catch (IOException igIOException) {}
      return baos.toByteArray().length;
    }
}