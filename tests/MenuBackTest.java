import com.upgrade.pandora.MenuActions;
public class MenuBackTest {public static void main(String[] args){
 for(int key:new int[]{40,147,100,37,149,97,4,111})if(!MenuActions.isBack(key))throw new AssertionError("Console/keyboard back "+key);
 for(int key:new int[]{49,148,39,146,43,150})if(MenuActions.isBack(key))throw new AssertionError("Action/page buttons must keep function "+key);
 System.out.println("MENU_BACK_TEST_OK");}}
