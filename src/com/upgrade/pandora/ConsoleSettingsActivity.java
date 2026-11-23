package com.upgrade.pandora;
import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.net.wifi.*;
import android.view.*;
import android.widget.*;
import java.util.*;

/** Network and language options remain usable from the arcade control panel. */
public class ConsoleSettingsActivity extends Activity {
 private WifiManager wifi;
 private TextView title,status,help;
 private ListView list;
 private boolean languages=false;
 private int choicesWifiState=-1;
 private final ArrayList<WifiConfiguration> networks=new ArrayList<WifiConfiguration>();
 private final Handler handler=new Handler();
 private final Runnable refresh=new Runnable(){public void run(){try{if(!languages&&wifi.getWifiState()!=choicesWifiState)loadChoices();}catch(Exception ignored){}refreshStatus();handler.postDelayed(this,3000);}};
 @Override public void onCreate(Bundle state){
  super.onCreate(state);UiText.init(this);getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  wifi=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
  FrameLayout root=new FrameLayout(this);root.setBackgroundDrawable(ThemeArt.background(this,0));
  LinearLayout panel=new LinearLayout(this);panel.setOrientation(LinearLayout.VERTICAL);panel.setPadding(30,20,30,20);panel.setBackgroundColor(Color.argb(160,8,20,34));
  FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams((int)(getResources().getDisplayMetrics().widthPixels*.80),-1,Gravity.CENTER);lp.setMargins(0,18,0,18);root.addView(panel,lp);
  title=new TextView(this);title.setTextSize(30);title.setTextColor(Color.WHITE);panel.addView(title);
  status=new TextView(this);status.setTextColor(Color.WHITE);status.setTextSize(17);status.setPadding(0,8,0,12);panel.addView(status);
  list=new ListView(this);list.setBackgroundColor(Color.argb(100,0,12,24));panel.addView(list,new LinearLayout.LayoutParams(-1,0,1));
  help=new TextView(this);help.setTextColor(Color.WHITE);help.setTextSize(16);help.setPadding(0,12,0,0);panel.addView(help);
  setContentView(root);loadChoices();
  list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){public void onItemClick(android.widget.AdapterView<?> parent,View view,int position,long id){choose(position);}});
 }
 private String interfaceAddress(String name){
  try{java.net.NetworkInterface iface=java.net.NetworkInterface.getByName(name);
   if(iface!=null){java.util.Enumeration<java.net.InetAddress> addresses=iface.getInetAddresses();while(addresses.hasMoreElements()){java.net.InetAddress address=addresses.nextElement();if(address instanceof java.net.Inet4Address&&!address.isLoopbackAddress())return address.getHostAddress();}}
   return UiText.get("network.unassigned");
  }catch(Exception e){return UiText.get("network.unavailable");}
 }
 private void refreshStatus(){
  if(languages){status.setText(UiText.get("language.partial"));return;}
  try{WifiInfo info=wifi.getConnectionInfo();status.setText(UiText.format("network.status",interfaceAddress("eth0"),interfaceAddress("wlan0"),info==null?"":info.getSSID()));}
  catch(Exception e){status.setText(UiText.get("network.status_error"));}
 }
 private void loadChoices(){
  int selected=list.getSelectedItemPosition();ArrayList<String> choices=new ArrayList<String>();
  title.setText(UiText.get(languages?"language.heading":"nav.settings"));help.setText(UiText.get("options.help"));
  if(languages){choices.add(UiText.get("language.back"));for(int i=0;i<UiText.CODES.length;i++)choices.add((UiText.CODES[i].equals(UiText.language())?"✓  ":"")+UiText.NAMES[i]);}
  else{
   choicesWifiState=wifi.getWifiState();choices.add(UiText.get("network.back"));choices.add(UiText.get("network.refresh"));choices.add(UiText.format("language.choice",UiText.languageName()));choices.add(UiText.get("network.enable"));networks.clear();
   try{List<WifiConfiguration> saved=wifi.getConfiguredNetworks();if(saved!=null)for(WifiConfiguration config:saved){if(config.SSID!=null&&!config.SSID.isEmpty()){networks.add(config);choices.add(UiText.format("network.connect",config.SSID));}}}catch(Exception ignored){}
  }
  ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,choices){@Override public View getView(int position,View convert,android.view.ViewGroup parent){TextView view=(TextView)super.getView(position,convert,parent);view.setTextColor(Color.WHITE);view.setTextSize(20);return view;}};
  list.setAdapter(adapter);list.setSelection(Math.max(0,Math.min(selected,choices.size()-1)));list.requestFocus();refreshStatus();
 }
 private void back(){if(languages){languages=false;loadChoices();list.setSelection(2);}else finish();}
 private void choose(int position){
  if(languages){if(position==0){back();return;}int index=position-1;if(index>=0&&index<UiText.CODES.length){UiText.select(this,UiText.CODES[index]);languages=false;loadChoices();list.setSelection(2);}return;}
  if(position==0){finish();return;}if(position==1){loadChoices();return;}
  if(position==2){languages=true;loadChoices();list.setSelection(Arrays.asList(UiText.CODES).indexOf(UiText.language())+1);return;}
  try{
   if(position==3){if(!wifi.isWifiEnabled())wifi.setWifiEnabled(true);Toast.makeText(this,UiText.get("network.enabled"),Toast.LENGTH_SHORT).show();}
   else{int index=position-4;if(index<0||index>=networks.size())return;if(!wifi.isWifiEnabled()){Toast.makeText(this,UiText.get("network.enable_first"),Toast.LENGTH_SHORT).show();return;}boolean ok=wifi.enableNetwork(networks.get(index).networkId,true);if(ok)wifi.reconnect();Toast.makeText(this,UiText.get(ok?"network.requested":"network.connect_error"),Toast.LENGTH_SHORT).show();}
  }catch(Exception e){Toast.makeText(this,UiText.get("network.change_error"),Toast.LENGTH_LONG).show();}
  refreshStatus();
 }
 @Override public boolean dispatchKeyEvent(KeyEvent event){
  int action=MenuActions.optionAction(event.getKeyCode());if(action<0)return super.dispatchKeyEvent(event);
  if(event.getAction()==KeyEvent.ACTION_DOWN){if(action==1){back();return true;}int selected=Math.max(0,list.getSelectedItemPosition());if(action==2)list.setSelection(Math.max(0,selected-1));else if(action==3)list.setSelection(Math.min(list.getCount()-1,selected+1));else if(action==0&&event.getRepeatCount()==0)choose(selected);}return true;
 }
 @Override public void onBackPressed(){back();}
 @Override protected void onResume(){super.onResume();handler.post(refresh);}
 @Override protected void onPause(){handler.removeCallbacks(refresh);super.onPause();}
}
