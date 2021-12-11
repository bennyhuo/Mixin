// SOURCE
package com.bennyhuo.kotlin.mixin

import com.bennyhuo.kotlin.mixin.annotations.Mixin

annotation class A

annotation class B(val a: Int, val b: String)

annotation class C(val a: IntArray, val b: Array<String>)

@Mixin("com.bennyhuo.kotlin.mixin.annotations", "mn")
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
package com.bennyhuo.kotlin.mixin.annotations;

import com.bennyhuo.kotlin.mixin.A;
import com.bennyhuo.kotlin.mixin.B;
import com.bennyhuo.kotlin.mixin.C;
import com.bennyhuo.kotlin.mixin.M;
import com.bennyhuo.kotlin.mixin.N;
import java.lang.Number;
import java.lang.String;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mn<T, P, X extends Number> {
  private final M<T, P> m;

  private final N<X> n;

  public Mn(int[] n0, String[] n1) {
    m = new M<T, P>();
    n = new N<X>(n0,n1);
  }

  @A
  public void m0() {
     m.m0();
  }

  @NotNull("")
  @B(
      a = 1,
      b = "Hello"
  )
  public String m1(String value) {
    return m.m1(value);
  }

  @C(
      a = { 1, 2, 3 },
      b = { "Hello", "World" }
  )
  public void m2(T value) {
     m.m2(value);
  }

  @Nullable("")
  public P m3() {
    return m.m3();
  }

  @NotNull("")
  public int[] getN0() {
    return n.getN0();
  }

  @NotNull("")
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
