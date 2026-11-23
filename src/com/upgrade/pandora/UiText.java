package com.upgrade.pandora;
import android.content.Context;
import java.io.*;
import java.util.*;

/** UI strings only. Catalog titles, storage paths and canonical filters are never translated. */
public final class UiText {
 public static final String[] CODES={"it","en","es","pt","de","fr","zh-Hans","ja"};
 public static final String[] NAMES={"Italiano","English","Español","Português","Deutsch","Français","简体中文","日本語"};
 private static String language="it";
 private static UiDictionary dictionary=new UiDictionary(Collections.<String,String>emptyMap(),Collections.<String,String>emptyMap());
 private UiText(){}
 private static Map<String,String> load(Context context,String code){
  Properties p=new Properties();
  try { Reader r=new InputStreamReader(context.getAssets().open("i18n/"+code+".properties"),"UTF-8");try{p.load(r);}finally{r.close();} }
  catch(IOException e){android.util.Log.w("UpGradeMenu","Language resource missing: "+code);}
  Map<String,String> result=new HashMap<String,String>();for(String key:p.stringPropertyNames())result.put(key,p.getProperty(key));return result;
 }
 public static void init(Context context){String requested=context.getSharedPreferences("menu",Context.MODE_PRIVATE).getString("language","it");language=Arrays.asList(CODES).contains(requested)?requested:"en";dictionary=new UiDictionary(load(context,"en"),load(context,language));}
 public static void select(Context context,String code){if(!Arrays.asList(CODES).contains(code))throw new IllegalArgumentException("Unsupported locale");context.getSharedPreferences("menu",Context.MODE_PRIVATE).edit().putString("language",code).commit();init(context);}
 public static String language(){return language;}
 public static String languageName(){return NAMES[Math.max(0,Arrays.asList(CODES).indexOf(language))];}
 public static String get(String key){return dictionary.get(key);}
 public static String format(String key,Object...args){return dictionary.format(key,args);}
 public static String category(String value){
  String[] ids={"Tutti","Cerca un gioco","Preferiti","Guida ai comandi","Classici","Azione","Sparatutto","Rompicapo","Sport","Import e altre lingue","Opzioni console"};
  String[] keys={"nav.all","nav.search","nav.favorites","nav.guide","genre.classics","genre.action","genre.shooters","genre.puzzle","genre.sports","nav.imports","nav.settings"};
  for(int i=0;i<ids.length;i++)if(ids[i].equals(value))return get(keys[i]);return value;
 }
 public static String keyboard(String value){if(value.equals("SPAZIO"))return get("key.space");if(value.equals("CANCELLA"))return get("key.delete");if(value.equals("PIATTAFORMA"))return get("key.filter");if(value.equals("INDIETRO"))return get("key.back");return value;}
 public static boolean systemFont(String text){for(int i=0;i<text.length();i++)if(text.charAt(i)>=0x2e80)return true;return false;}
}
