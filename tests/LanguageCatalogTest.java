import com.upgrade.pandora.MenuModel;import java.util.*;
public class LanguageCatalogTest {public static void main(String[] args){
MenuModel.Game en=new MenuModel.Game("en","Sim English","Classici",1,"x","quicknes","");
MenuModel.Game jp=new MenuModel.Game("jp","Sim Japan","Classici",1,"y","quicknes","");jp.mainCatalog=false;
MenuModel m=new MenuModel(Arrays.asList(en,jp),new HashSet<String>());
for(String c:new String[]{"Tutti","NES / Famicom","Classici"}){m.filter(c);if(m.visible().size()!=1||m.selected()!=en)throw new AssertionError(c);}
m.search("Sim","");if(m.visible().size()!=1)throw new AssertionError("default search");
m.filter(MenuModel.IMPORTS);if(m.visible().size()!=1||m.selected()!=jp)throw new AssertionError("imports");
m.search("Sim",MenuModel.IMPORTS);if(m.visible().size()!=1||m.selected()!=jp)throw new AssertionError("import search");
System.out.println("LANGUAGE_CATALOG_OK");}}
