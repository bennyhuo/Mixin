// SOURCE
package com.bennyhuo.kotlin.sample

import com.bennyhuo.kotlin.sample.annotations.Composite

annotation class A

annotation class B(val a: Int, val b: String)

annotation class C(val a: IntArray, val b: Array<String>)

@Composite("mn")
class M<T, P> {
    @A
    fun m0() {
        println("Hello m")
    }

    @B(1, "Hello")
    fun m1(value: String): String = value

    @C([1,2,3], ["Hello", "World"])
    fun m2(value: T) {

    }

    fun m3(): P? = null
}

@Composite("mn")
class N<X: Number>(val n0: IntArray, val n1: Array<String>) {
    fun n2() {
        println("Hello Y")
    }
    
    fun n3(value: X) {
        println(value)
    }
}
// GENERATED
//-------Mn.java------
package com.bennyhuo.kotlin.sample.annotations;

import com.bennyhuo.kotlin.sample.M;
import com.bennyhuo.kotlin.sample.N;
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

