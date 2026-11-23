import com.upgrade.pandora.MenuModel;
import java.util.*;
public class MenuModelTest {
 static MenuModel.Game g(String id,String category){return new MenuModel.Game(id,id,category,1,"/storage/external_storage/sda1/Pandora-Prototipo/arcade/"+id+".zip","mame","");}
 public static void main(String[] args){
  MenuModel m=new MenuModel(Arrays.asList(g("a","Classici"),g("b","Sport"),g("c","Sport"),g("d","Sport"),g("e","Sport")),new HashSet<String>());
  m.move(0,1,3);if(!m.selected().id.equals("d"))throw new AssertionError("Next row");
  m.move(0,1,3);if(!m.selected().id.equals("e"))throw new AssertionError("Short final row");
  m.filter("Preferiti");m.move(1,1,3);if(m.selected()!=null)throw new AssertionError("Empty filter");
  m.filter("Tutti");m.toggleFavorite();m.filter("Preferiti");if(m.visible().size()!=1)throw new AssertionError("Favorite filter");
  m.toggleFavorite();if(m.selected()!=null)throw new AssertionError("Remove final favorite");
  m.filter("Sport");if(m.visible().size()!=4 || !m.selected().id.equals("b"))throw new AssertionError("Category filter");
  MenuModel n=new MenuModel(Arrays.asList(g("first","Sport"),g("second","Sport")),new HashSet<String>());
  n.move(1,0,3);n.toggleFavorite();n.filter("Preferiti");if(!n.selected().id.equals("second"))throw new AssertionError("Favorite identity");
  n.filter("Missing");n.move(-1,-1,3);if(n.index()!=0)throw new AssertionError("Empty navigation bounds");
  MenuModel platforms=new MenuModel(Arrays.asList(
   new MenuModel.Game("arc","Arcade","Azione",2,"x","mame2016_libretro_android.so",""),
   new MenuModel.Game("nes","NES","Classici",1,"y","fceumm_libretro_android.so",""),
   new MenuModel.Game("md","MD","Azione",2,"z","genesis_plus_gx_libretro_android.so","")),new HashSet<String>());
  platforms.filter("NES / Famicom");if(platforms.visible().size()!=1||!platforms.selected().id.equals("nes"))throw new AssertionError("NES platform filter");
  platforms.filter("Mega Drive");if(platforms.visible().size()!=1||!platforms.selected().id.equals("md"))throw new AssertionError("MD platform filter");
  platforms.filter("Arcade");if(platforms.visible().size()!=1)throw new AssertionError("Arcade platform filter");
  ArrayList<MenuModel.Game> many=new ArrayList<MenuModel.Game>();for(int i=0;i<22;i++)many.add(g("g"+i,"Classici"));
  MenuModel pages=new MenuModel(many,new HashSet<String>());pages.move(0,3,3);if(pages.index()!=9)throw new AssertionError("Next page");
  pages.move(0,3,3);pages.move(0,3,3);if(pages.index()!=21)throw new AssertionError("Final page clamp");
  MenuModel extra=new MenuModel(Arrays.asList(
   new MenuModel.Game("snes","SNES","Classici",1,"a.sfc","snes9x2005_plus_libretro_android.so",""),
   new MenuModel.Game("gb","GB","Classici",1,"b.gb","gambatte_libretro_android.so",""),
   new MenuModel.Game("gba","GBA","Classici",1,"c.gba","gpsp_libretro_android.so",""),
   new MenuModel.Game("pce","PCE","Classici",1,"d.pce","mednafen_pce_fast_libretro_android.so","")),new HashSet<String>());
  for(String cat:new String[]{"Super Nintendo","Game Boy / Color","Game Boy Advance","PC Engine"}){extra.filter(cat);if(extra.visible().size()!=1)throw new AssertionError("Extra platform "+cat);}
  int last=MenuModel.CATEGORIES.length-1,top=MenuModel.categoryWindowStart(last,11);
  if(top<0||last<top||last>=top+11)throw new AssertionError("Last category visible");
  System.out.println("MENU_MODEL_TEST_OK");
 }
}
