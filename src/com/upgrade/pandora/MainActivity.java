package com.upgrade.pandora;

import android.app.*;
import android.content.*;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.Toast;
import org.json.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
    public static final String ROOT="/storage/external_storage/sda1/Pandora-Prototipo/";
    public static final String CORE_ROOT="/data/data/com.retroarch.ra32/cores/";
    private static final String CONFIG="/storage/sdcard0/Android/data/com.retroarch.ra32/files/retroarch-menu.cfg";
    MenuModel model;
    MenuView view;
    boolean categoryFocus=false;
    int categoryIndex=0;
    boolean searchOpen=false;
    int keyboardIndex=0,imageIndex=0,guidePage=0;
    boolean isOptions(){return model!=null&&model.category().equals("Opzioni console");}
    boolean isGuide(){return model!=null&&model.category().equals("Guida ai comandi");}
    void guideTurn(int direction){guidePage=(guidePage+direction+3)%3;view.invalidate();}
    static final String[] KEYS=SearchKeyboard.KEYS;
    boolean suggestionFocus=false;
    int suggestionIndex=0;
    private boolean launching=false;
    private android.content.SharedPreferences prefs;
    private BroadcastReceiver mediaReceiver;
    private String loadedCatalogStamp="";
    private android.net.wifi.WifiManager.WifiLock maintenanceWifiLock;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state); UiText.init(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        prefs=getSharedPreferences("menu",MODE_PRIVATE);
        // A power-on starts a clean public session. Activity recreation and
        // returning from a game in the same boot keep the user's place.
        String boot="";
        try { boot=read(new FileInputStream("/proc/sys/kernel/random/boot_id")).trim(); }
        catch(IOException e) { android.util.Log.w("UpGradeMenu","Identificativo avvio non disponibile",e); }
        if(boot.isEmpty() || !boot.equals(prefs.getString("session_boot",""))) {
            prefs.edit().putString("session_boot",boot).putString("category","Cerca un gioco")
                .putString("query","").putString("scope","").remove("last").commit();
            categoryFocus=true;
        }
        try{
            android.net.wifi.WifiManager wifi=(android.net.wifi.WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            maintenanceWifiLock=wifi.createWifiLock(android.net.wifi.WifiManager.WIFI_MODE_FULL_HIGH_PERF,"UpGrade-maintenance");
            maintenanceWifiLock.setReferenceCounted(false);maintenanceWifiLock.acquire();
        }catch(Exception e){android.util.Log.w("UpGradeMenu","Wi-Fi lock non disponibile",e);}
        loadCatalog();
        view=new MenuView(this);
        setContentView(view);
        IntentFilter media=new IntentFilter();media.addAction(Intent.ACTION_MEDIA_MOUNTED);media.addAction(Intent.ACTION_MEDIA_UNMOUNTED);media.addAction(Intent.ACTION_MEDIA_EJECT);media.addDataScheme("file");
        mediaReceiver=new BroadcastReceiver(){public void onReceive(Context c,Intent i){if(view!=null){loadCatalog();view.clearThumbnails();view.invalidate();}}};
        registerReceiver(mediaReceiver,media);
    }
    private static String read(InputStream input) throws IOException {
        try { ByteArrayOutputStream out=new ByteArrayOutputStream(); byte[] b=new byte[4096]; int n;
            while((n=input.read(b))!=-1) { if(out.size()+n>1024*1024)throw new IOException("Catalogo troppo grande"); out.write(b,0,n); }
            return new String(out.toByteArray(),"UTF-8");
        } finally { input.close(); }
    }
    private List<MenuModel.Game> parseCatalog(String text) throws JSONException {
        List<MenuModel.Game> games=new ArrayList<MenuModel.Game>();
        Set<String> ids=new HashSet<String>();
        JSONArray items=new JSONObject(text).getJSONArray("games");
        for(int i=0;i<items.length();i++) {
            try {
                JSONObject j=items.getJSONObject(i); String id=j.getString("id"), path=j.getString("path"),core=j.getString("core");
                String title=j.getString("title").trim();
                if(id.isEmpty() || title.isEmpty() || !path.startsWith(ROOT) || path.contains("..") || !core.matches("[a-zA-Z0-9_]+\\.so") || !ids.add(id)) continue;
                String thumbnail=j.optString("thumbnail","");
                if(!thumbnail.startsWith(ROOT+"menu/") || thumbnail.contains(".."))thumbnail="";
                MenuModel.Game game=new MenuModel.Game(id,title,j.optString("category","Classici"),Math.min(2,Math.max(1,j.optInt("players",1))),path,core,thumbnail);
                game.mainCatalog=j.optString("catalog_visibility","review").equals("main");games.add(game);
            } catch(JSONException e) { android.util.Log.w("UpGradeMenu","Voce catalogo ignorata: "+i,e); }
        }
        return games;
    }
    private void loadCatalog() {
        File catalogFile=new File(ROOT+"menu/catalogo.json"), galleryFile=new File(ROOT+"menu/galleria.json");
        String stamp=catalogFile.isFile()+":"+catalogFile.lastModified()+":"+catalogFile.length()+":"+galleryFile.lastModified()+":"+galleryFile.length();
        if(model!=null&&stamp.equals(loadedCatalogStamp))return;
        if(model==null){int saved=Arrays.asList(MenuModel.CATEGORIES).indexOf(prefs.getString("category","Tutti"));categoryIndex=Math.max(0,saved);}
        int previousCategory=categoryIndex;
        String previousQuery=model==null?prefs.getString("query",""):model.query(),previousScope=model==null?prefs.getString("scope",""):model.scope();
        boolean previousFocus=categoryFocus;
        List<MenuModel.Game> games=null;
        File file=new File(ROOT+"menu/catalogo.json");
        if(file.isFile())try {
            games=parseCatalog(read(new FileInputStream(file)));
            if(games.isEmpty())games=null;
        } catch(Exception e) { android.util.Log.w("UpGradeMenu","Uso il catalogo integrato",e); }
        if(games==null)try { games=parseCatalog(read(getAssets().open("catalogo.json"))); }
        catch(Exception e) { games=new ArrayList<MenuModel.Game>(); android.util.Log.e("UpGradeMenu","Catalogo non disponibile",e); }
        Collections.sort(games,new Comparator<MenuModel.Game>(){public int compare(MenuModel.Game a,MenuModel.Game b){return a.title.compareToIgnoreCase(b.title);}});
        Set<String> favorites=new HashSet<String>(prefs.getStringSet("favorites",new HashSet<String>()));
        model=new MenuModel(games,favorites);
        model.filter(MenuModel.CATEGORIES[previousCategory]);
        if(previousCategory==1)model.search(previousQuery,previousScope);
        File gallery=new File(ROOT+"menu/galleria.json");
        if(gallery.isFile())try{
            JSONObject images=new JSONObject(read(new FileInputStream(gallery)));
            for(MenuModel.Game g:games){JSONArray list=images.optJSONArray(g.id);if(list==null)continue;ArrayList<String> valid=new ArrayList<String>();
                for(int n=0;n<Math.min(2,list.length());n++){String path=list.optString(n,"");if(path.startsWith(ROOT+"menu/")&&!path.contains("..")&&new File(path).isFile())valid.add(path);}
                if(!valid.isEmpty())g.screenshots=valid.toArray(new String[valid.size()]);
            }
        }catch(Exception e){android.util.Log.w("UpGradeMenu","Galleria non disponibile",e);}
        String last=prefs.getString("last","");
        for(int i=0;i<model.visible().size();i++)if(model.visible().get(i).id.equals(last)){model.select(i);break;}
        categoryIndex=previousCategory; categoryFocus=previousFocus;
        suggestionIndex=Math.max(0,Math.min(suggestionIndex,model.visible().size()-1));if(!hasSuggestions())suggestionFocus=false;
        loadedCatalogStamp=stamp;
    }
    @Override protected void onResume(){super.onResume();UiText.init(this);launching=false;if(view!=null){loadCatalog();view.clearThumbnails();view.invalidate();}}
    @Override protected void onPause(){if(view!=null)view.clearCommands();if(model!=null)prefs.edit().putString("category",model.category()).putString("query",model.query()).putString("scope",model.scope()).apply();super.onPause();}
    @Override protected void onNewIntent(Intent intent){super.onNewIntent(intent);setIntent(intent);if(view!=null){loadCatalog();view.clearThumbnails();view.invalidate();}}
    @Override protected void onDestroy(){if(mediaReceiver!=null)unregisterReceiver(mediaReceiver);if(maintenanceWifiLock!=null&&maintenanceWifiLock.isHeld())maintenanceWifiLock.release();super.onDestroy();}
    void launchSelected() {
        MenuModel.Game g=model.selected(); if(g==null || launching)return;
        if(!new File(g.path).isFile()){Toast.makeText(this,UiText.get("toast.game_missing"),Toast.LENGTH_LONG).show();return;}
        prefs.edit().putString("last",g.id).putString("category",model.category()).putString("query",model.query()).putString("scope",model.scope()).commit();
        Intent intent=new Intent();
        intent.setComponent(new ComponentName("com.retroarch.ra32","com.retroarch.browser.retroactivity.RetroActivityFuture"));
        intent.putExtra("LIBRETRO",CORE_ROOT+g.core); intent.putExtra("ROM",g.path); intent.putExtra("CONFIGFILE",CONFIG);
        intent.putExtra("QUITFOCUS",true);
        intent.putExtra("IME","com.moorechip.gameime/com.open.inputmethod.service.IMEService");
        try{launching=true;Toast.makeText(this,UiText.get("toast.exit"),Toast.LENGTH_LONG).show();startActivity(intent);}catch(Exception e){launching=false;Toast.makeText(this,UiText.get("toast.launch_failed"),Toast.LENGTH_LONG).show();android.util.Log.e("UpGradeMenu","Avvio fallito",e);}
    }
    void favorite(){model.toggleFavorite();prefs.edit().putStringSet("favorites",new HashSet<String>(model.favorites)).apply();view.invalidate();}
    void category(int n){categoryIndex=Math.max(0,Math.min(n,MenuModel.CATEGORIES.length-1));if(categoryIndex!=1)model.filter(MenuModel.CATEGORIES[categoryIndex]);else model.search(model.query(),model.scope());imageIndex=0;view.invalidate();}
    void openSearch(){
        String scope=model.availablePlatforms().contains(model.category())?model.category():model.category().equals("Cerca un gioco")?model.scope():"";
        String query=model.category().equals("Cerca un gioco")?model.query():"";
        categoryIndex=1;model.search(query,scope);keyboardIndex=0;suggestionIndex=0;suggestionFocus=false;searchOpen=true;categoryFocus=false;imageIndex=0;view.invalidate();
    }
    void editSearch(String key){
        String q=model.query();
        if(key.equals("INDIETRO")){searchOpen=false;categoryFocus=false;}
        else if(key.equals("PIATTAFORMA")){cycleScope(1);return;}
        else {if(key.equals("CANCELLA")){if(q.length()>0)q=q.substring(0,q.length()-1);}else q+=key.equals("SPAZIO")?" ":key;model.search(q,model.scope());}
        suggestionIndex=0;suggestionFocus=false;imageIndex=0;view.invalidate();
    }
    void cycleScope(int direction){ArrayList<String> scopes=new ArrayList<String>();scopes.add("");scopes.addAll(model.availablePlatforms());int n=scopes.indexOf(model.scope());n=(n+direction+scopes.size())%scopes.size();model.search(model.query(),scopes.get(n));suggestionIndex=0;suggestionFocus=false;view.invalidate();}
    boolean hasSuggestions(){return MenuModel.suggestionsReady(model.query())&&!model.visible().isEmpty();}
    void chooseSuggestion(int index){if(!hasSuggestions())return;model.select(index);searchOpen=false;suggestionFocus=false;categoryFocus=false;imageIndex=0;view.invalidate();}
    void image(int direction){MenuModel.Game g=model.selected();if(g!=null&&g.screenshots.length>0)imageIndex=(imageIndex+direction+g.screenshots.length)%g.screenshots.length;view.invalidate();}
    void serviceMenu(){startActivity(new Intent(this,ConsoleSettingsActivity.class));}
    private int commandIndex(int key){
        if(key==KeyEvent.KEYCODE_U||key==KeyEvent.KEYCODE_NUMPAD_4||key==KeyEvent.KEYCODE_BUTTON_A||key==KeyEvent.KEYCODE_BUTTON_START||key==KeyEvent.KEYCODE_ENTER||key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_2||key==KeyEvent.KEYCODE_4)return 0;
        if(key==KeyEvent.KEYCODE_I||key==KeyEvent.KEYCODE_NUMPAD_5||key==KeyEvent.KEYCODE_BUTTON_B||key==KeyEvent.KEYCODE_BACK)return 1;
        if(key==KeyEvent.KEYCODE_J||key==KeyEvent.KEYCODE_NUMPAD_1||key==KeyEvent.KEYCODE_BUTTON_X)return 2;
        if(key==KeyEvent.KEYCODE_K||key==KeyEvent.KEYCODE_NUMPAD_2)return 3;
        if(key==KeyEvent.KEYCODE_O||key==KeyEvent.KEYCODE_NUMPAD_6)return 4;
        if(key==KeyEvent.KEYCODE_L||key==KeyEvent.KEYCODE_NUMPAD_3||key==KeyEvent.KEYCODE_BUTTON_Y||key==KeyEvent.KEYCODE_SEARCH)return 5;
        return -1;
    }
    @Override public boolean onKeyUp(int key,KeyEvent event){int command=commandIndex(key);if(command>=0){view.command(command,false);return true;}return super.onKeyUp(key,event);}
    @Override public boolean onKeyDown(int key,KeyEvent event) {
        int command=commandIndex(key);if(command>=0)view.command(command,true);
        boolean up=key==KeyEvent.KEYCODE_W||key==KeyEvent.KEYCODE_DPAD_UP;
        boolean down=key==KeyEvent.KEYCODE_S||key==KeyEvent.KEYCODE_DPAD_DOWN;
        boolean left=key==KeyEvent.KEYCODE_A||key==KeyEvent.KEYCODE_DPAD_LEFT;
        boolean right=key==KeyEvent.KEYCODE_D||key==KeyEvent.KEYCODE_DPAD_RIGHT;
        boolean confirm=key==KeyEvent.KEYCODE_U||key==KeyEvent.KEYCODE_NUMPAD_4||key==KeyEvent.KEYCODE_BUTTON_A||key==KeyEvent.KEYCODE_BUTTON_START||key==KeyEvent.KEYCODE_ENTER||key==KeyEvent.KEYCODE_DPAD_CENTER||key==KeyEvent.KEYCODE_2||key==KeyEvent.KEYCODE_4;
        boolean back=MenuActions.isBack(key);
        boolean favorite=key==KeyEvent.KEYCODE_J||key==KeyEvent.KEYCODE_NUMPAD_1||key==KeyEvent.KEYCODE_BUTTON_X;
        boolean prev=key==KeyEvent.KEYCODE_K||key==KeyEvent.KEYCODE_NUMPAD_2;
        boolean next=key==KeyEvent.KEYCODE_O||key==KeyEvent.KEYCODE_NUMPAD_6;
        if(isGuide()){
            if(back){if(event.getRepeatCount()==0)handleBack(key);return true;}
            if(prev||next){if(event.getRepeatCount()==0)guideTurn(prev?-1:1);return true;}
            if(confirm){if(event.getRepeatCount()==0){if(categoryFocus){categoryFocus=false;view.invalidate();}else guideTurn(1);}return true;}
            if(up||down||left||right){
                if(categoryFocus){if(up)category(categoryIndex-1);else if(down)category(categoryIndex+1);else if(right)categoryFocus=false;}
                else if(left)categoryFocus=true;else if(event.getRepeatCount()==0)guideTurn(up?-1:1);
                view.invalidate();return true;
            }
            return super.onKeyDown(key,event);
        }
        if(searchOpen){
            if(suggestionFocus&&!hasSuggestions())suggestionFocus=false;
            if(up||down||left||right){
                if(suggestionFocus){
                    if(down&&suggestionIndex==model.visible().size()-1){suggestionFocus=false;}
                    else if(up||down)suggestionIndex=Math.max(0,Math.min(model.visible().size()-1,suggestionIndex+(up?-1:1)));
                    else if(left||right)suggestionFocus=false;
                }else if(up&&keyboardIndex<10&&hasSuggestions()){suggestionFocus=true;suggestionIndex=0;}
                else keyboardIndex=SearchKeyboard.move(keyboardIndex,left?-1:right?1:0,up?-1:down?1:0);
                view.invalidate();return true;
            }
            if(confirm){if(event.getRepeatCount()==0){if(suggestionFocus)chooseSuggestion(suggestionIndex);else editSearch(KEYS[keyboardIndex]);}return true;}
            if(back){if(event.getRepeatCount()==0)handleBack(key);return true;}
            if(favorite){if(event.getRepeatCount()==0)editSearch("CANCELLA");return true;}
            if(prev||next){if(event.getRepeatCount()==0)cycleScope(prev?-1:1);return true;}
        }
        if(up||down||left||right){
            if(categoryFocus){if(up)category(categoryIndex-1);if(down)category(categoryIndex+1);if(right){if(categoryIndex==1)openSearch();else if(isOptions())serviceMenu();else categoryFocus=false;}}
            else if(up||down){model.move(0,up?-1:1,1);imageIndex=0;}
            else if(left||right){MenuModel.Game g=model.selected();if(g!=null&&g.screenshots.length>1)image(left?-1:1);else if(left)categoryFocus=true;}
            view.invalidate();return true;
        }
        if(confirm){if(event.getRepeatCount()==0){if(isOptions())serviceMenu();else if(categoryFocus){if(categoryIndex==1)openSearch();else {categoryFocus=false;view.invalidate();}}else launchSelected();}return true;}
        if(back){if(event.getRepeatCount()==0)handleBack(key);return true;}
        if(favorite){if(event.getRepeatCount()==0)favorite();return true;}
        if(prev||next){if(!categoryFocus&&event.getRepeatCount()==0){model.move(0,prev?-6:6,1);imageIndex=0;view.invalidate();}return true;}
        if(key==KeyEvent.KEYCODE_SEARCH){if(event.getRepeatCount()==0)openSearch();return true;}
        if(key==KeyEvent.KEYCODE_MENU||key==KeyEvent.KEYCODE_F10){if(event.getRepeatCount()==0)serviceMenu();return true;}
        return super.onKeyDown(key,event);
    }
    private void handleBack(int key){
        int destination=MenuActions.backDestination(key,model.category().equals("Cerca un gioco"),searchOpen,suggestionFocus);
        if(destination==1){openSearch();return;}
        searchOpen=false;suggestionFocus=false;categoryFocus=destination==0;view.invalidate();
    }
    @Override public void onBackPressed(){handleBack(KeyEvent.KEYCODE_BACK);}
}
