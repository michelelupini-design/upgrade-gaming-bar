package com.upgrade.pandora;
import android.content.*;
import android.content.pm.*;
import java.util.*;

public final class HomeSelector {
    public static void main(String[] args) throws Exception {
        try { run(args); } catch(Throwable error) { error.printStackTrace(System.err); System.exit(1); }
    }
    private static void run(String[] args) throws Exception {
        Object pm=Class.forName("android.app.AppGlobals").getMethod("getPackageManager").invoke(null);
        Class<?> api=Class.forName("android.content.pm.IPackageManager");
        Intent home=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolved=new ArrayList<ResolveInfo>();
        ComponentName before=(ComponentName)api.getMethod("getHomeActivities",java.util.List.class).invoke(pm,resolved);
        if(before!=null)System.out.println("PREVIOUS="+before.flattenToString());
        ArrayList<ComponentName> components=new ArrayList<ComponentName>();
        for(ResolveInfo r:resolved){ComponentName component=new ComponentName(r.activityInfo.packageName,r.activityInfo.name);components.add(component);System.out.println("HOME="+component.flattenToString());}
        if(args.length==0)return;
        ComponentName chosen=ComponentName.unflattenFromString(args[0]);
        if(chosen==null||!components.contains(chosen))throw new IllegalArgumentException("Home non disponibile");
        IntentFilter filter=new IntentFilter(Intent.ACTION_MAIN);filter.addCategory(Intent.CATEGORY_HOME);filter.addCategory(Intent.CATEGORY_DEFAULT);
        api.getMethod("replacePreferredActivity",IntentFilter.class,int.class,ComponentName[].class,ComponentName.class).invoke(pm,filter,IntentFilter.MATCH_CATEGORY_EMPTY,components.toArray(new ComponentName[components.size()]),chosen);
        ComponentName after=(ComponentName)api.getMethod("getHomeActivities",java.util.List.class).invoke(pm,new ArrayList<ResolveInfo>());
        String actual=after.flattenToString();
        if(!actual.equals(chosen.flattenToString()))throw new IllegalStateException("Home non applicata: "+actual);
        System.out.println("SELECTED="+actual);
    }
}
