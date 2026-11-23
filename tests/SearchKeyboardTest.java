import com.upgrade.pandora.SearchKeyboard;
import com.upgrade.pandora.MenuModel;
public class SearchKeyboardTest {
 public static void main(String[] args){
  for(int i=0;i<10;i++)if(!SearchKeyboard.KEYS[i].equals(""+i))throw new AssertionError("Digits dedicated row");
  for(int i=0;i<26;i++)if(!SearchKeyboard.KEYS[i+10].equals(""+(char)('A'+i)))throw new AssertionError("Alphabetic letters");
  for(String k:SearchKeyboard.KEYS)if(k.equals("RISULTATI"))throw new AssertionError("No results button");
  if(SearchKeyboard.move(0,-1,0)!=0||SearchKeyboard.move(9,1,0)!=9)throw new AssertionError("Horizontal clamp");
  if(SearchKeyboard.move(5,0,1)!=15||SearchKeyboard.move(35,0,1)!=35)throw new AssertionError("Rows clamp");
  if(MenuModel.suggestionsReady("si")||MenuModel.suggestionsReady(" s "))throw new AssertionError("Minimum three");
  if(!MenuModel.suggestionsReady("sim")||!MenuModel.suggestionsReady("194"))throw new AssertionError("Suggestions available");
  System.out.println("SEARCH_KEYBOARD_TEST_OK");
 }
}