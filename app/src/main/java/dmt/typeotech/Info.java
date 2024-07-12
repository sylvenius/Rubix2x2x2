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
  static int nof_depts = 4;
  static int max_perm_per_dept  = 9;
  static int total_permutations = 23;
  static long map_size = 70081515;
  static int[][] per_level = {
          {0,1},
          {1,3},
          {2,6},
          {3,9},
          {4,5}
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