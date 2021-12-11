// SOURCE
package com.bennyhuo.kotlin.mixin

import com.bennyhuo.kotlin.mixin.annotations.Mixin

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "mn")
class M<T, P> {
    fun m0() {
        println("Hello m")
    }
    
    fun m1(value: String): String = value
    
    fun m2(value: T) {
        
    }
    
    fun m3(): P? = null
}

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "mn")
class N<X: Number>(val n0: IntArray, val n1: Array<String>) {
    fun n2() {
        println("Hello Y")
    }
    
    fun n3(value: X) {
        println(value)
    }
}
// GENERATED
// FILE: Mn.java
package com.bennyhuo.kotlin.mixin.annotations;

import com.bennyhuo.kotlin.mixin.M;
import com.bennyhuo.kotlin.mixin.N;
import java.lang.Number;
import java.lang.String;

public class Mn<T, P, X extends Number> {
  private final M<T, P> m;

  private final N<X> n;

  public Mn(int[] n0, String[] n1) {
    m = new M<T, P>();
    n = new N<X>(n0,n1);
  }

  public void m0() {
     m.m0();
  }

  public String m1(String value) {
    return m.m1(value);
  }

  public void m2(T value) {
     m.m2(value);
  }

  public P m3() {
    return m.m3();
  }

  public int[] getN0() {
    return n.getN0();
  }

  public String[] getN1() {
    return n.getN1();
  }

  public void n2() {
     n.n2();
  }

  public void n3(X value) {
     n.n3(value);
  }
}