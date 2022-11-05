// SOURCE
package com.bennyhuo.kotlin.mixin
        
import com.bennyhuo.kotlin.mixin.annotations.Mixin

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "xyz")
class X(val x0: Int, val x1: String) {
    fun x2() {
        println("Hello X")
    }
} 

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "xyz")
class Y(val y0: IntArray, val y1: Array<String>) {
    fun y2() {
        println("Hello Y")
    }
}

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "xyz")
class Z {
    fun z0() {
        println("Hello Z")
    }
    
    fun z1(value: String): String = value
}
// EXPECT
// FILE: Xyz.java
package com.bennyhuo.kotlin.mixin.annotations;

import com.bennyhuo.kotlin.mixin.X;
import com.bennyhuo.kotlin.mixin.Y;
import com.bennyhuo.kotlin.mixin.Z;
import java.lang.String;

public class Xyz {
  private final X x;

  private final Y y;

  private final Z z;

  public Xyz(int x0, String x1, int[] y0, String[] y1) {
    x = new X(x0,x1);
    y = new Y(y0,y1);
    z = new Z();
  }

  public int getX0() {
    return x.getX0();
  }

  public String getX1() {
    return x.getX1();
  }

  public void x2() {
     x.x2();
  }

  public int[] getY0() {
    return y.getY0();
  }

  public String[] getY1() {
    return y.getY1();
  }

  public void y2() {
     y.y2();
  }

  public void z0() {
     z.z0();
  }

  public String z1(String value) {
    return z.z1(value);
  }
}
