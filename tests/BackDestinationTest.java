import com.upgrade.pandora.MenuActions;
public class BackDestinationTest {public static void main(String[] args){
for(int f:new int[]{40,147,100})for(boolean keyboard:new boolean[]{false,true})if(MenuActions.backDestination(f,true,keyboard,false)!=0)throw new AssertionError("F categories");
if(MenuActions.backDestination(37,true,false,false)!=1)throw new AssertionError("B results -> keyboard");
if(MenuActions.backDestination(149,true,true,false)!=2)throw new AssertionError("B keyboard -> results");
if(MenuActions.backDestination(37,true,true,true)!=1)throw new AssertionError("B suggestions -> keyboard");
if(MenuActions.backDestination(37,false,false,false)!=0)throw new AssertionError("B ordinary -> categories");
System.out.println("BACK_DESTINATIONS_OK");}}
