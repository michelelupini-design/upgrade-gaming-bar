package com.upgrade.pandora;
import android.graphics.*;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.*;
import java.util.*;

final class MenuView extends View {
 private final MainActivity a;
 private final ArcadeFeedback feedback=new ArcadeFeedback();
 private final LruCache<String,Bitmap> themes=new LruCache<String,Bitmap>(2);
 private Bitmap brand,publisherBrand,buttonAtlas;
 private GuideRenderer embeddedGuide;
 private Typeface arcadeFace;
 private boolean keyboardText=false,navigationText=false;
 private final LruCache<String,Bitmap> platformMarks=new LruCache<String,Bitmap>(12);
 private void glass(Canvas c,float x,float y,float w,float h,boolean focus){
  p.setColor(Color.WHITE);p.setShader(new LinearGradient(x,y,x+w,y+h,new int[]{Color.argb(focus?85:42,160,220,235),Color.argb(focus?100:58,8,22,42)},null,Shader.TileMode.CLAMP));c.drawRoundRect(new RectF(x,y,x+w,y+h),10,10,p);p.setShader(null);
  p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(focus?2:1);p.setColor(focus?accent:Color.argb(90,225,245,255));c.drawRoundRect(new RectF(x,y,x+w,y+h),10,10,p);p.setStyle(Paint.Style.FILL);
 }
 private String markName(String category){if(category.equals("Arcade"))return "arcade";if(category.equals("NES / Famicom"))return "nes";if(category.equals("Super Nintendo"))return "snes";if(category.equals("Mega Drive"))return "megadrive";if(category.equals("Game Boy / Color"))return "gbc";if(category.equals("Game Boy Advance"))return "gba";if(category.equals("Nintendo 64"))return "n64";if(category.equals("PlayStation"))return "psx";return "";}
 private boolean platformMark(Canvas c,String category,float x,float y){String name=markName(category);if(category.equals("PC Engine")){float size=22;p.setTextSize(size*1.22f);p.setTypeface(arcadeFace);label(c,"PC Engine",x+(190-p.measureText("PC Engine"))/2,y+26,size,white,true);return true;}if(name.isEmpty())return false;Bitmap b=platformMarks.get(name);if(b==null)try{java.io.InputStream in=getContext().getAssets().open("theme/platforms/"+name+".png");try{b=BitmapFactory.decodeStream(in);}finally{in.close();}if(b!=null)platformMarks.put(name,b);}catch(java.io.IOException e){return false;}if(b==null)return false;float scale=Math.min(176f/b.getWidth(),29f/b.getHeight()),w=b.getWidth()*scale,h=b.getHeight()*scale;RectF mark=new RectF(x+(190-w)/2,y+(36-h)/2,x+(190+w)/2,y+(36+h)/2);
 p.setColorFilter(new PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_IN));p.setAlpha(170);
 for(int dx=-1;dx<=1;dx++)for(int dy=-1;dy<=1;dy++)if(dx!=0||dy!=0){RectF edge=new RectF(mark);edge.offset(dx,dy);c.drawBitmap(b,null,edge,p);}
 p.setColorFilter(null);p.setAlpha(255);c.drawBitmap(b,null,mark,p);return true;}

 private final float[] buttonXs=new float[6],buttonWidths=new float[6];
 private int touchCommand=-1;
 private final int[] buttonColors={Color.rgb(222,62,70),Color.rgb(52,133,232),Color.rgb(246,203,60),Color.rgb(57,194,107),Color.rgb(245,139,51),Color.rgb(173,99,225)};
 void command(int n,boolean down){if(down)feedback.press(n);else feedback.release(n,android.os.SystemClock.uptimeMillis());invalidate();}
 void clearCommands(){feedback.clear();touchCommand=-1;invalidate();}
 private Bitmap asset(String name){Bitmap b=themes.get(name);if(b==null)try{BitmapFactory.Options o=new BitmapFactory.Options();o.inPreferredConfig=Bitmap.Config.RGB_565;java.io.InputStream in=getContext().getAssets().open("theme/"+name);try{b=BitmapFactory.decodeStream(in,null,o);}finally{in.close();}if(b!=null)themes.put(name,b);}catch(java.io.IOException e){android.util.Log.w("UpGradeMenu","Risorsa grafica non disponibile: "+name);}return b;}
 private int mix(int color,int target,float amount){return Color.rgb((int)(Color.red(color)*(1-amount)+Color.red(target)*amount),(int)(Color.green(color)*(1-amount)+Color.green(target)*amount),(int)(Color.blue(color)*(1-amount)+Color.blue(target)*amount));}
 private void background(Canvas c){
  String category=a.model.category();int quadrant=-1;
  if(a.searchOpen||category.equals("Cerca un gioco"))quadrant=0;
  else if(category.equals("Arcade")||category.equals("Azione")||category.equals("Sparatutto"))quadrant=1;
  else if(category.equals("PlayStation")||category.equals("Nintendo 64"))quadrant=3;
  else if(a.model.availablePlatforms().contains(category))quadrant=2;
  if(quadrant<0)quadrant=0;Bitmap b=asset("background-atlas.png");
  if(b!=null){Rect src=new Rect(0,0,b.getWidth(),b.getHeight());if(quadrant>=0){int w=b.getWidth()/2,h=b.getHeight()/2;src.set((quadrant%2)*w,(quadrant/2)*h,(quadrant%2+1)*w,(quadrant/2+1)*h);}c.drawBitmap(b,src,new RectF(0,0,1280,720),p);}
  box(c,0,0,1280,720,Color.argb(24,5,11,20));
  if(quadrant==2){int tint=category.equals("NES / Famicom")?Color.argb(28,200,20,30):category.equals("Mega Drive")?Color.argb(28,10,60,210):category.equals("Game Boy / Color")?Color.argb(28,20,190,60):Color.argb(16,80,20,140);box(c,0,0,1280,666,tint);}
  glass(c,16,130,230,526,false);
 }
 private void logo(Canvas c){
  if(brand==null)try{java.io.InputStream in=getContext().getAssets().open("theme/upgrade-arcade-v2.png");try{BitmapFactory.Options o=new BitmapFactory.Options();o.inSampleSize=2;brand=BitmapFactory.decodeStream(in,null,o);}finally{in.close();}}catch(java.io.IOException e){android.util.Log.w("UpGradeMenu","Logo UpGrade non disponibile");}
  p.setColor(Color.WHITE);
  if(brand!=null){float w=240,h=w*brand.getHeight()/brand.getWidth();c.drawBitmap(brand,null,new RectF(8,18+(100-h)/2,248,18+(100+h)/2),p);}else label(c,"UpGrade",30,66,32,white,true);
  if(publisherBrand==null)try{java.io.InputStream in=getContext().getAssets().open("theme/q11-arcade-horizontal-v1.png");try{BitmapFactory.Options o=new BitmapFactory.Options();o.inSampleSize=2;publisherBrand=BitmapFactory.decodeStream(in,null,o);}finally{in.close();}}catch(java.io.IOException e){android.util.Log.w("UpGradeMenu","Publisher mark unavailable");}
  if(publisherBrand!=null){float w=286,h=w*publisherBrand.getHeight()/publisherBrand.getWidth();c.drawBitmap(publisherBrand,null,new RectF(963,61-h/2,1249,61+h/2),p);}
 }
 private boolean spriteButton(Canvas c,int index,float cx,float cy,float strength){
  if(buttonAtlas==null)try{java.io.InputStream in=getContext().getAssets().open("theme/arcade-buttons-v3.png");try{BitmapFactory.Options o=new BitmapFactory.Options();o.inSampleSize=2;buttonAtlas=BitmapFactory.decodeStream(in,null,o);}finally{in.close();}}catch(java.io.IOException ignored){}
  if(buttonAtlas==null)return false;
  int w=buttonAtlas.getWidth()/6,h=buttonAtlas.getHeight()/2;RectF dest=new RectF(cx-30,cy-36,cx+30,cy+24);
  p.setShader(null);p.setColor(Color.WHITE);p.setAlpha((int)(255*(1-strength)));c.drawBitmap(buttonAtlas,new Rect(index*w,0,(index+1)*w,h),dest,p);
  if(strength>0){p.setAlpha((int)(255*strength));c.drawBitmap(buttonAtlas,new Rect(index*w,h,(index+1)*w,2*h),dest,p);}p.setAlpha(255);
  c.save();c.translate(cx,cy-14+2*strength);c.scale(1,.72f);label(c,""+(char)('A'+index),-7,8,21,index==2||index==3||index==4?bg:white,true);c.restore();
  return true;
 }
 private void arcadeButton(Canvas c,int index,float x,String caption){
  float strength=feedback.strength(index,android.os.SystemClock.uptimeMillis()),cx=x+25,cy=692;int color=buttonColors[index];
  if(spriteButton(c,index,cx,cy,strength)){label(c,caption,x+58,699,17,white,false);if(strength>0&&!feedback.isHeld(index))postInvalidateDelayed(30);return;}
  if(strength>0){p.setShader(new RadialGradient(cx,cy,32,new int[]{Color.argb((int)(150*strength),Color.red(color),Color.green(color),Color.blue(color)),Color.TRANSPARENT},null,Shader.TileMode.CLAMP));c.drawCircle(cx,cy,32,p);p.setShader(null);}
  p.setColor(Color.argb(95,0,0,0));c.drawOval(new RectF(cx-29,cy+8,cx+29,cy+28),p);
  p.setColor(Color.WHITE);p.setShader(new LinearGradient(cx,cy-12,cx,cy+24,new int[]{Color.rgb(130,145,157),Color.rgb(18,27,37),Color.rgb(58,72,83)},null,Shader.TileMode.CLAMP));c.drawOval(new RectF(cx-26,cy-11,cx+26,cy+24),p);p.setShader(null);
  p.setColor(mix(color,Color.BLACK,.55f));c.drawRoundRect(new RectF(cx-20,cy-7,cx+20,cy+17),6,6,p);c.drawOval(new RectF(cx-20,cy+4,cx+20,cy+23),p);
  float offset=4*strength;int face=mix(color,Color.WHITE,.16f*strength);
  p.setShader(new LinearGradient(cx,cy-18,cx,cy+12,new int[]{mix(face,Color.WHITE,.35f),face,mix(face,Color.BLACK,.18f)},null,Shader.TileMode.CLAMP));c.drawOval(new RectF(cx-21,cy-19+offset,cx+21,cy+10+offset),p);p.setShader(null);
  p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1);p.setColor(Color.argb(190,255,255,255));c.drawArc(new RectF(cx-18,cy-17+offset,cx+18,cy+7+offset),190,145,false,p);p.setStyle(Paint.Style.FILL);
  c.save();c.translate(cx,cy-5+offset);c.scale(1,.72f);label(c,""+(char)('A'+index),-7,8,21,index==2||index==3||index==4?bg:white,true);c.restore();
  label(c,caption,x+58,699,17,white,false);
  if(strength>0&&!feedback.isHeld(index))postInvalidateDelayed(30);
 }
 private void footer(Canvas c){

  String[] captions=a.isGuide()?new String[]{a.categoryFocus?UiText.get("action.read_guide"):UiText.get("action.next_page"),UiText.get("action.categories"),"",UiText.get("action.previous_page"),UiText.get("action.next_page"),UiText.get("action.categories")}:a.searchOpen?new String[]{a.suggestionFocus?UiText.get("action.game_details"):UiText.get("action.letter"),a.suggestionFocus?UiText.get("action.keyboard"):UiText.get("action.results"),UiText.get("action.delete"),UiText.get("action.previous_filter"),UiText.get("action.next_filter"),UiText.get("action.categories")}:new String[]{a.isOptions()?UiText.get("action.open_options"):UiText.get("action.launch"),a.model.category().equals("Cerca un gioco")?UiText.get("action.edit_search"):UiText.get("action.categories"),UiText.get("nav.favorites"),UiText.get("action.previous_page"),UiText.get("action.next_page"),UiText.get("action.categories")};
  label(c,"",0,0,17,white,false);float[] widths=new float[6];float total=25*(a.isGuide()?4:5);
  for(int i=0;i<6;i++){if(a.isGuide()&&i==2)continue;p.setTypeface(UiText.systemFont(captions[i])?Typeface.DEFAULT:arcadeFace);p.setTextSize(UiText.systemFont(captions[i])?17:17*1.22f);widths[i]=58+p.measureText(captions[i]);total+=widths[i];}
  float start=(1280-total)/2,footerScale=Math.min(1f,1240f/total);c.save();c.translate(640,0);c.scale(footerScale,1);c.translate(-640,0);
  for(int i=0;i<6;i++){if(a.isGuide()&&i==2){buttonWidths[i]=0;continue;}buttonXs[i]=640+(start-640)*footerScale;buttonWidths[i]=widths[i]*footerScale;arcadeButton(c,i,start,captions[i]);start+=widths[i]+25;}c.restore();
 }
 private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
 private final LruCache<String,Bitmap> images=new LruCache<String,Bitmap>(10);
 private final int bg=Color.rgb(17,26,32),panel=Color.argb(212,28,41,49),nav=Color.rgb(23,36,44),white=Color.rgb(244,241,232),muted=Color.rgb(176,193,199),accent=Color.rgb(102,226,192),line=Color.rgb(53,70,78);
 MenuView(MainActivity activity){super(activity);a=activity;setFocusable(true);setFocusableInTouchMode(true);try{arcadeFace=Typeface.createFromAsset(activity.getAssets(),"theme/fonts/VT323-Regular.ttf");}catch(Exception e){arcadeFace=Typeface.create("monospace",Typeface.BOLD);}}
 void clearThumbnails(){images.evictAll();}
 private void box(Canvas c,float x,float y,float w,float h,int color){p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawRect(x,y,x+w,y+h,p);}
 private void textStyle(String s,float size,boolean bold){boolean retro=!keyboardText&&!UiText.systemFont(s);p.setTextSize(retro?size*1.22f:size);p.setTypeface(retro?arcadeFace:Typeface.create("sans-serif",bold?Typeface.BOLD:Typeface.NORMAL));}
 private void label(Canvas c,String s,float x,float y,float size,int color,boolean bold){p.setColor(color);textStyle(s,size,bold);if(!keyboardText)p.setShadowLayer(3,0,2,Color.BLACK);c.drawText(s,x,y,p);p.clearShadowLayer();}
 private String fit(String s,float width){return TextUtils.ellipsize(s,new android.text.TextPaint(p),width,TextUtils.TruncateAt.END).toString();}
 private void fitted(Canvas c,String s,float x,float y,float width,float size,int color,boolean bold){textStyle(s,size,bold);label(c,fit(s,width),x,y,size,color,bold);}
 private void title(Canvas c,String s,float x,float y,float width){
  textStyle(s,31,true);
  if(p.measureText(s)<=width){label(c,s,x,y,31,white,true);return;}
  int end=s.length();while(end>0&&p.measureText(s.substring(0,end))>width)end=s.offsetByCodePoints(end,-1);
  if(end==0)end=Character.charCount(s.codePointAt(0));
  int space=s.lastIndexOf(' ',end);if(space>0)end=space;
  label(c,s.substring(0,end),x,y,31,white,true);fitted(c,s.substring(end).trim(),x,y+36,width,29,white,true);
 }
 private void picture(Canvas c,String path,float x,float y,float w,float h){
  box(c,x,y,w,h,Color.rgb(7,12,16));if(path.isEmpty())return;
  Bitmap b=images.get(path);
  if(b==null){BitmapFactory.Options o=new BitmapFactory.Options();o.inJustDecodeBounds=true;BitmapFactory.decodeFile(path,o);o.inSampleSize=1;while(o.outWidth/o.inSampleSize>1024||o.outHeight/o.inSampleSize>768)o.inSampleSize*=2;o.inJustDecodeBounds=false;o.inPreferredConfig=Bitmap.Config.RGB_565;b=BitmapFactory.decodeFile(path,o);if(b!=null)images.put(path,b);}
  if(b!=null){float scale=Math.min(w/b.getWidth(),h/b.getHeight()),dw=b.getWidth()*scale,dh=b.getHeight()*scale;c.drawBitmap(b,null,new RectF(x+(w-dw)/2,y+(h-dh)/2,x+(w+dw)/2,y+(h+dh)/2),p);}
 }
 private void key(Canvas c,String letter,String text,float x){p.setColor(accent);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(2);c.drawCircle(x+14,692,14,p);p.setStyle(Paint.Style.FILL);label(c,letter,x+8,698,17,accent,true);label(c,text,x+37,699,18,white,false);}
 @Override protected void onDraw(Canvas raw){
  super.onDraw(raw);Canvas c=raw;int saved=c.save();c.scale(getWidth()/1280f,getHeight()/720f);c.drawColor(bg);background(c);
  logo(c);
  int top=MenuModel.categoryWindowStart(a.categoryIndex,11);
  for(int i=top;i<Math.min(top+11,MenuModel.CATEGORIES.length);i++){
   float y=149+(i-top)*44;boolean selected=i==a.categoryIndex;int color=selected?accent:nav;
   if(selected)glass(c,22,y,211,39,a.categoryFocus);
   navigationText=true;
   if(!platformMark(c,MenuModel.CATEGORIES[i],34,y+1)){String text=UiText.category(MenuModel.CATEGORIES[i]);textStyle(text,20,selected||i==1);String clipped=fit(text,190);textStyle(clipped,20,selected||i==1);float left=34+(190-p.measureText(clipped))/2;label(c,clipped,left,y+27,20,selected?white:muted,selected||i==1);}navigationText=false;
  }
  {boolean moreDown=top+11<MenuModel.CATEGORIES.length;p.setColor(muted);Path arrow=new Path();if(moreDown){arrow.moveTo(121,637);arrow.lineTo(141,637);arrow.lineTo(131,649);}else{arrow.moveTo(121,649);arrow.lineTo(141,649);arrow.lineTo(131,637);}arrow.close();c.drawPath(arrow,p);}
  boolean available=new java.io.File(MainActivity.ROOT).isDirectory();List<MenuModel.Game> games=available?a.model.visible():Collections.<MenuModel.Game>emptyList();
  label(c,UiText.category(a.model.category()),286,64,33,white,true);
  fitted(c,a.isOptions()?UiText.get("header.options"):a.isGuide()?UiText.get("header.guide"):a.model.category().equals("Cerca un gioco")?UiText.format("header.search",a.model.query().isEmpty()?UiText.get("header.any_name"):a.model.query(),a.model.scope().isEmpty()?UiText.get("header.all_platforms"):UiText.category(a.model.scope())):UiText.format("header.count",games.size()),288,104,650,19,muted,false);

  box(c,285,123,956,1,line);
  if(a.isGuide()){
   if(embeddedGuide==null)embeddedGuide=new GuideRenderer(getContext());
   label(c,UiText.format("guide.page",a.guidePage+1),288,145,21,muted,false);
   c.save();c.clipRect(275,157,1260,660);c.translate(288,76);c.scale(.74f,.74f);embeddedGuide.draw(c,a.guidePage);c.restore();
   footer(c);c.restoreToCount(saved);return;
  }
  if(a.isOptions()){
   label(c,UiText.get("settings.heading"),290,274,30,white,true);
   label(c,UiText.get("settings.open_hint"),290,320,22,muted,false);
   label(c,UiText.get("settings.category_hint"),290,364,20,muted,false);
  }
  else if(a.searchOpen){keyboardText=true;keyboard(c);keyboardText=false;}
  else if(games.isEmpty()){
   boolean pendingPS=a.model.category().equals("PlayStation");
   label(c,!available?UiText.get("empty.medium"):pendingPS?UiText.get("empty.ps"):UiText.get("empty.none"),290,274,30,white,true);
   label(c,!available?UiText.get("empty.check_medium"):pendingPS?UiText.get("empty.cd_review"):a.model.category().equals("Preferiti")?UiText.get("empty.favorite_hint"):UiText.get("empty.search_hint"),290,320,21,muted,false);
   if(pendingPS&&available)label(c,UiText.get("empty.ps_catalog"),290,363,19,muted,false);
  }else{
   MenuModel.Game g=a.model.selected();int image=g.screenshots.length==0?0:a.imageIndex%g.screenshots.length;
   picture(c,g.screenshots.length==0?"":g.screenshots[image],286,151,578,331);
   glass(c,280,495,590,165,false);
   title(c,g.title,287,524,576);
   label(c,g.platform+"  /  "+g.players+" "+UiText.get(g.players==1?"game.player":"game.players")+(a.model.favorites.contains(g.id)?"  /  "+UiText.get("game.favorite"):""),289,599,20,muted,false);
   if(g.screenshots.length>1){for(int i=0;i<g.screenshots.length;i++){box(c,287+i*108,610,100,47,i==image?accent:panel);picture(c,g.screenshots[i],290+i*108,613,94,41);}label(c,UiText.get("game.images"),511,639,18,muted,false);}
   else label(c,UiText.get("game.exit_hint"),289,639,16,muted,false);
   int first=a.model.index()/6*6;
   for(int n=0;n<6&&first+n<games.size();n++){
    MenuModel.Game row=games.get(first+n);float y=150+n*78;boolean selected=first+n==a.model.index();boolean focus=selected&&!a.categoryFocus;
    p.setColor(Color.argb(164,10,23,35));c.drawRoundRect(new RectF(890,y-4,1240,y+68),10,10,p);glass(c,890,y-4,350,72,focus);
    picture(c,row.screenshots.length==0?"":row.screenshots[0],896,y+2,83,58);
    fitted(c,row.title,992,y+25,232,21,white,true);label(c,row.platform,992,y+52,16,muted,false);
   }
   label(c,UiText.format("list.page",a.model.index()/6+1,(games.size()+5)/6),1046,645,18,muted,false);
  }
  footer(c);
  c.restoreToCount(saved);
 }
 private void keyboard(Canvas c){
  box(c,287,145,951,55,panel);fitted(c,a.model.query().isEmpty()?UiText.get("search.minimum"):a.model.query(),305,181,910,28,white,true);
  glass(c,282,358,960,62,false);
  if(!MenuModel.suggestionsReady(a.model.query())){
   label(c,UiText.get("search.ready_hint"),303,244,21,muted,false);
  }else if(a.model.visible().isEmpty()){
   label(c,UiText.get("search.none"),303,244,21,muted,false);
  }else{
   int top=a.suggestionFocus?Math.max(0,a.suggestionIndex-3):0;
   for(int n=0;n<4&&top+n<a.model.visible().size();n++){
    int index=top+n;MenuModel.Game g=a.model.visible().get(index);float y=211+n*37;boolean selected=a.suggestionFocus&&index==a.suggestionIndex;
    box(c,287,y,951,35,selected?accent:panel);fitted(c,g.title,303,y+25,660,21,selected?bg:white,true);fitted(c,g.platform,980,y+24,240,17,selected?bg:muted,false);
   }
   label(c,UiText.format("search.matches",a.model.visible().size()),289,378,18,muted,false);
  }
  fitted(c,UiText.format("search.platform",a.model.scope().isEmpty()?UiText.get("header.all_platforms"):UiText.category(a.model.scope())),289,410,940,20,accent,true);
  for(int i=0;i<40;i++){
   int row=i/10;float x=288+(i%10)*95,y=row==0?426:489+(row-1)*48;boolean selected=!a.suggestionFocus&&i==a.keyboardIndex;
   box(c,x,y,88,40,selected?accent:panel);String key=UiText.keyboard(MainActivity.KEYS[i]);float size=key.length()>1?14:22;label(c,"",0,0,size,white,true);label(c,key,x+(88-p.measureText(key))/2,y+28,size,selected?bg:white,true);
  }
  glass(c,282,630,960,29,false);
  label(c,UiText.get("search.help"),289,648,18,muted,false);
 }
 @Override public boolean onTouchEvent(MotionEvent e){
  float x=e.getX()*1280/getWidth(),y=e.getY()*720/getHeight();
  int[] physical={KeyEvent.KEYCODE_U,KeyEvent.KEYCODE_I,KeyEvent.KEYCODE_J,KeyEvent.KEYCODE_K,KeyEvent.KEYCODE_O,KeyEvent.KEYCODE_L};
  if(e.getAction()==MotionEvent.ACTION_DOWN&&y>=666){for(int i=0;i<6;i++)if(x>=buttonXs[i]&&x<=buttonXs[i]+buttonWidths[i]){touchCommand=i;a.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,physical[i]));break;}return true;}
  if((e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL)&&touchCommand>=0){a.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,physical[touchCommand]));touchCommand=-1;return true;}
  if(e.getAction()!=MotionEvent.ACTION_UP)return true;
  if(a.searchOpen){
   if(x>=287&&x<1238&&y>=211&&y<359&&a.hasSuggestions()){int top=a.suggestionFocus?Math.max(0,a.suggestionIndex-3):0;int index=top+(int)((y-211)/37);if(index<a.model.visible().size())a.chooseSuggestion(index);return true;}
   int row=y>=426&&y<466?0:y>=489&&y<629?1+(int)((y-489)/48):-1;
   if(x>=288&&x<1238&&row>=0&&row<4){int col=(int)((x-288)/95),k=row*10+col;a.keyboardIndex=k;a.suggestionFocus=false;a.editSearch(MainActivity.KEYS[k]);}return true;
  }
  if(x<250&&y>=149&&y<633){int top=MenuModel.categoryWindowStart(a.categoryIndex,11);int n=top+(int)((y-149)/44);a.categoryFocus=true;a.category(n);if(n==1)a.openSearch();return true;}
  if(a.isGuide()&&x>=275&&y>=157&&y<660){a.categoryFocus=false;a.guideTurn(x<760?-1:1);return true;}
  if(x>=890&&y>=146&&y<618){int n=a.model.index()/6*6+(int)((y-146)/78);if(n<a.model.visible().size()){boolean launch=a.model.index()==n&&!a.categoryFocus;a.model.select(n);a.categoryFocus=false;a.imageIndex=0;invalidate();if(launch)a.launchSelected();}return true;}
  if(x>=287&&x<=500&&y>=610&&y<=657){a.image(x<395?-1:1);return true;}
  return true;
 }
}
