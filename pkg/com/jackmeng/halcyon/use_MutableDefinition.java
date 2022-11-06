package com.jackmeng.halcyon;

import com.jackmeng.halcyon.apps.impl_Callback;
import com.jackmeng.halcyon.apps.impl_ForYou;
import com.jackmeng.halcyon.apps.impl_Guard;
import com.jackmeng.halcyon.apps.impl_Identifiable;

public final class use_MutableDefinition implements impl_Identifiable {
  public String name, key, defaultVal;
  private impl_Guard<String> onValidate;
  private impl_ForYou<String> onOk;
  private impl_Callback<String> callMe;

  public use_MutableDefinition(String name, String key, String defVal, impl_Guard<String> r, impl_ForYou<String> ok,
      impl_Callback<String> callMe) {
    assert name != null && key != null && defaultVal != null && r != null && ok != null;
    this.name = name;
    this.key = key;
    this.defaultVal = defVal;
    this.onOk = ok;
    this.onValidate = r;
    this.callMe = callMe;
  }

  public use_MutableDefinition(String name, String key, String defVal, String[] l, impl_ForYou<String> ok,
      impl_Callback<String> callMe) {
    this(name, key, defVal, x -> {
      for (String r : l) {
        if (r.equalsIgnoreCase(x)) {
          return true;
        }
      }
      return false;
    }, ok, callMe);
  }

  public String check(String value) {
    return onValidate.check(value) ? value : defaultVal;
  }

  public void validate(String val) {
    if (onValidate.check(val)) {
      onOk.forYou(val);
    }
  }

  public String get() {
    return callMe.call((Object[]) null);
  }
}
