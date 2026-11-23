package com.upgrade.pandora;
import android.os.SystemClock;
import android.view.*;
import java.lang.reflect.*;

/** Root-only command-line helper for the local input acceptance checks. */
public final class InputProbe {
    public static void main(String[] args) throws Exception {
        Class<?> managerClass=Class.forName("android.hardware.input.InputManager");
        Object manager=managerClass.getMethod("getInstance").invoke(null);
        Method inject=managerClass.getMethod("injectInputEvent",InputEvent.class,int.class);
        long down=SystemClock.uptimeMillis();
        for(String arg:args){
            String[] pair=arg.split(":");
            int action=pair[0].equals("down")?KeyEvent.ACTION_DOWN:KeyEvent.ACTION_UP;
            KeyEvent event=new KeyEvent(down,SystemClock.uptimeMillis(),action,Integer.parseInt(pair[1]),0);
            event.setSource(InputDevice.SOURCE_KEYBOARD);
            if(!((Boolean)inject.invoke(manager,event,2)))throw new IllegalStateException("Input non iniettato");
            Thread.sleep(90);
        }
        System.out.println("INPUT_PROBE_OK");
    }
}
