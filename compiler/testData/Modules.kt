// SOURCE
// MODULE: library-a #mixin.library:true
package com.bennyhuo.kotlin.mixin.sample.library1

import com.bennyhuo.kotlin.mixin.annotations.Mixin
import kotlin.reflect.full.declaredMembers

@Mixin("com.bennyhuo.kotlin.mixin.sample", "Apis")
class Api1 {

    val url1 = "https://www.api1.com"

    fun getObject1(key: String): Any {
        return ""
    }

    fun getObjects1(keys: Array<String>): List<Any> {
        return emptyList()
    }

    fun save() {

    }

    fun save(key: String, value: String) {

    }

    fun save(key: String, value: Any?) {

    }
}
// MODULE: library-b / library-a   , library-c #mixin.library:true
package com.bennyhuo.kotlin.mixin.sample.library2

import com.bennyhuo.kotlin.mixin.annotations.Mixin

@Mixin("com.bennyhuo.kotlin.mixin.sample", "Apis")
class Api2 {

    val url2 = "https://www.api1.com"

    fun getObject2(key: String): Any {
        return ""
    }

    fun getObjects2(keys: Array<String>): List<Any> {
        return emptyList()
    }

    fun save() {

    }

    fun save(k: String, v: String) {

    }

    fun save(k: String, v: Any?, options: Int = 0) {

    }
}
// MODULE: main / library-a, library-b, c, d
package com.bennyhuo.kotlin.mixin.sample

import com.bennyhuo.kotlin.mixin.annotations.Mixin

@Mixin("com.bennyhuo.kotlin.mixin.sample", "Apis")
class Api {

}
// GENERATED
// MODULE: library-a
// FILE: MixinIndex_1c45500c60bd256c7e223b64fb158d32.java
package com.bennyhuo.kotlin.processor.module;

@LibraryIndex({"com.bennyhuo.kotlin.mixin.sample.library1.Api1"})
class MixinIndex_1c45500c60bd256c7e223b64fb158d32 {
}
// MODULE: library-b
// FILE: MixinIndex_9851104842a1a2fab326a8e3527e37f3.java
package com.bennyhuo.kotlin.processor.module;

@LibraryIndex({"com.bennyhuo.kotlin.mixin.sample.library2.Api2"})
class MixinIndex_9851104842a1a2fab326a8e3527e37f3 {
}
// MODULE: main
// FILE: Apis.java
package com.bennyhuo.kotlin.mixin.sample;

import com.bennyhuo.kotlin.mixin.sample.library1.Api1;
import com.bennyhuo.kotlin.mixin.sample.library2.Api2;
import java.lang.Object;
import java.lang.String;
import java.util.List;

public class Apis {
  private final Api api;

  private final Api1 api1;

  private final Api2 api2;

  public Apis() {
    api = new Api();
    api1 = new Api1();
    api2 = new Api2();
  }

  public Object getObject1(String key) {
    return api1.getObject1(key);
  }

  public List<Object> getObjects1(String[] keys) {
    return api1.getObjects1(keys);
  }

  public String getUrl1() {
    return api1.getUrl1();
  }

  public void save() {
     api1.save();
     api2.save();
  }

  public void save(String key, Object value) {
     api1.save(key,value);
  }

  public void save(String key, String value) {
     api1.save(key,value);
     api2.save(key,value);
  }

  public Object getObject2(String key) {
    return api2.getObject2(key);
  }

  public List<Object> getObjects2(String[] keys) {
    return api2.getObjects2(keys);
  }

  public String getUrl2() {
    return api2.getUrl2();
  }

  public void save(String k, Object v, int options) {
     api2.save(k,v,options);
  }
}
