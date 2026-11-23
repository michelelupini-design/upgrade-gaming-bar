import com.upgrade.pandora.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public final class CommunityContractTest {
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 static Map<String,String> language(Path directory,String code)throws Exception{
  Properties p=new Properties();Reader r=Files.newBufferedReader(directory.resolve(code+".properties"),StandardCharsets.UTF_8);try{p.load(r);}finally{r.close();}
  Map<String,String> values=new HashMap<String,String>();for(String key:p.stringPropertyNames())values.put(key,p.getProperty(key));return values;
 }
 public static void main(String[] args)throws Exception{
  Path directory=Paths.get(args[0]);Map<String,String> english=language(directory,"en"),italian=language(directory,"it");
  check(english.keySet().equals(italian.keySet()),"Italian and English coverage differs");
  for(String code:new String[]{"it","en","es","pt","de","fr","zh-Hans","ja"}){
   Map<String,String> translated=language(directory,code);check(english.keySet().containsAll(translated.keySet()),"Unknown keys in "+code);
   UiDictionary d=new UiDictionary(english,translated);
   for(String key:english.keySet())check(!d.get(key).isEmpty()&&!d.get(key).startsWith("["),"Missing fallback "+code+" "+key);
   check(d.format("header.count",484).contains("484"),"Count placeholder lost "+code);
   check(d.format("network.connect","network-test").contains("network-test"),"SSID parameter lost "+code);
  }
  Map<String,String> partial=new HashMap<String,String>();partial.put("nav.search","");UiDictionary fallback=new UiDictionary(english,partial);
  check(fallback.get("nav.search").equals(english.get("nav.search")),"Empty translations must fall back");
  check(fallback.get("missing.key").equals("[missing.key]"),"Unknown key must remain observable");
  check(MenuModel.normalize("Pokémon").equals(MenuModel.normalize("pokemon")),"Accent search regression");
  check(MenuModel.normalize("魔界村").equals("魔界村"),"CJK search must preserve letters");
  check(MenuModel.suggestionsReady("魔界村")&&!MenuModel.suggestionsReady("魔界"),"Three-character rule");
  List<MenuModel.Game> games=new ArrayList<MenuModel.Game>();
  games.add(new MenuModel.Game("latin","Pokémon test","Classici",1,"/test/a","gambatte_libretro_android.so",""));
  games.add(new MenuModel.Game("cjk","魔界村","Classici",1,"/test/b","fceumm_libretro_android.so",""));
  MenuModel model=new MenuModel(games,new HashSet<String>());model.search("pokemon","");check(model.visible().size()==1&&model.selected().id.equals("latin"),"Accent lookup");
  model.search("魔界村","");check(model.visible().size()==1&&model.selected().id.equals("cjk"),"CJK lookup");
  for(String code:new String[]{"it","en","es","pt","de","fr","zh-Hans","ja"}){new UiDictionary(english,language(directory,code));model.filter("NES / Famicom");check(model.visible().size()==1&&model.selected().id.equals("cjk"),"Language changed canonical filtering");}
  System.out.println("COMMUNITY_CONTRACTS_OK: fallback, UTF-8, placeholders, accents, CJK and canonical filters");
 }
}
