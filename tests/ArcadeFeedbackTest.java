import com.upgrade.pandora.ArcadeFeedback;
public class ArcadeFeedbackTest {
 public static void main(String[] args){ArcadeFeedback b=new ArcadeFeedback();
 if(b.strength(0,1000)!=0)throw new AssertionError("Idle no glow");
 b.press(0);b.press(1);if(b.strength(0,1000)!=1||b.strength(1,1000)!=1)throw new AssertionError("Multiple held buttons");
 if(!b.isHeld(0))throw new AssertionError("Held animation state");
 b.release(0,1000);if(b.isHeld(0))throw new AssertionError("Release must schedule fading even at full strength");if(b.strength(0,1080)<.49||b.strength(0,1080)>.51)throw new AssertionError("Release fade");
 if(b.strength(0,1200)!=0||b.strength(1,1200)!=1)throw new AssertionError("Independent release");
 b.clear();if(b.strength(1,1200)!=0)throw new AssertionError("Focus loss clears held state");
 System.out.println("ARCADE_FEEDBACK_TEST_OK");}
}