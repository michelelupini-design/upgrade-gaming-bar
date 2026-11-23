import com.upgrade.pandora.MenuActions;
public class ConsoleNavigationTest {public static void main(String[] args){
for(int k:new int[]{51,19})if(MenuActions.optionAction(k)!=2)throw new AssertionError("up "+k);
for(int k:new int[]{47,20})if(MenuActions.optionAction(k)!=3)throw new AssertionError("down "+k);
for(int k:new int[]{49,148,9,11,96,108,66,23})if(MenuActions.optionAction(k)!=0)throw new AssertionError("confirm "+k);
for(int k:new int[]{40,147,37,149,4,111})if(MenuActions.optionAction(k)!=1)throw new AssertionError("back "+k);
if(MenuActions.optionAction(29)!=-1)throw new AssertionError("unrelated");
System.out.println("CONSOLE_NAVIGATION_OK");}}
