package com.upgrade.pandora;
import java.util.*;

/** Language-independent dictionary. Missing translations fall back to English. */
public final class UiDictionary {
 private final Map<String,String> fallback,translated;
 public UiDictionary(Map<String,String> fallback,Map<String,String> translated){this.fallback=new HashMap<String,String>(fallback);this.translated=new HashMap<String,String>(translated);}
 public String get(String key){String value=translated.get(key);if(value==null||value.trim().isEmpty())value=fallback.get(key);return value==null?"["+key+"]":value;}
 public String format(String key,Object...args){return String.format(Locale.ROOT,get(key),args);}
}
