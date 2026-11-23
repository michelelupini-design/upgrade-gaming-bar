import com.upgrade.pandora.MenuModel;
import java.util.*;
public class MenuSearchTest {
 static MenuModel.Game g(String id,String title,String core){return new MenuModel.Game(id,title,"Classici",1,"x",core,"");}
 static void check(boolean v,String message){if(!v)throw new AssertionError(message);}
 public static void main(String[] a){
 MenuModel m=new MenuModel(Arrays.asList(g("z","Zelda","fceumm"),g("s","The Simpsons","mame"),g("c","Café Racer","genesis_plus_gx"),g("s2","Simpsons: Arcade","fceumm")),new HashSet<String>());
 check(m.visible().get(0).id.equals("c"),"Alphabetic independent from source order");
 m.search("SIMP","");check(m.visible().size()==2,"Partial case insensitive");
 m.search("simp","Arcade");check(m.visible().size()==1&&m.selected().id.equals("s"),"Optional platform");
 m.search("cafe-racer","");check(m.visible().size()==1&&m.selected().id.equals("c"),"Accents punctuation");
 m.search("doesnotexist","");check(m.selected()==null,"Empty search safe");m.move(1,1,1);check(m.index()==0,"Empty clamp");
 m.search("","");check(m.visible().size()==4,"Empty query all titles");
 check(m.visible().get(1).id.equals("s"),"The ignored for sorting, original title preserved");
 check(m.availablePlatforms().size()==4,"Available platform options only");
 m.filter("NES / Famicom");check(m.query().equals(""),"Regular filter clears query");
 MenuModel tags=new MenuModel(Arrays.asList(g("metadata","Puzzle (Simplified)","fceumm")),new HashSet<String>());
 tags.search("sim","");check(tags.visible().isEmpty(),"Region/language metadata is not a game title");
 System.out.println("MENU_SEARCH_TEST_OK");
 }
}