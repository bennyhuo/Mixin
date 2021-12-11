// SOURCE
// MODULE: library-a
package com.bennyhuo.kotlin.mixin
        
import com.bennyhuo.kotlin.mixin.annotations.Mixin

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "xyz")
class X(val x0: Int, val x1: String) {
    fun x2() {
        println("Hello X")
    }
} 

// MODULE: library-b / library-a   , library-c
package com.bennyhuo.kotlin.mixin

import com.bennyhuo.kotlin.mixin.annotations.Mixin

class X1(val x: X)

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
// GENERATED
// MODULE: library-a
// FILE: Xyz.java
package com.bennyhuo.kotlin.mixin.annotations;

import com.bennyhuo.kotlin.mixin.X;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

public class Xyz {
  private final X x;

  public Xyz(int x0, String x1) {
    x = new X(x0,x1);
  }

  public int getX0() {
    return x.getX0();
  }

  @NotNull("")
  public String getX1() {
    return x.getX1();
  }

  public void x2() {
     x.x2();
  }
}
// MODULE: library-b
// FILE: Xyz.java
package com.bennyhuo.kotlin.mixin.annotations;

import com.bennyhuo.kotlin.mixin.Y;
import com.bennyhuo.kotlin.mixin.Z;
import java.lang.String;
import org.jetbrains.annotations.NotNull;

public class Xyz {
  private final Y y;

  private final Z z;

  public Xyz(int[] y0, String[] y1) {
    y = new Y(y0,y1);
    z = new Z();
  }

  @NotNull("")
  public int[] getY0() {
    return y.getY0();
  }

  @NotNull("")
  public String[] getY1() {
    return y.getY1();
  }

  public void y2() {
     y.y2();
  }

  public void z0() {
     z.z0();
  }

  @NotNull("")
  public String z1(String value) {
    return z.z1(value);
  }
}
