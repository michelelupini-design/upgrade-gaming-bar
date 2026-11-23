package com.upgrade.pandora;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import java.io.*;

/** Original project artwork; no vendor skin or logo is loaded. */
public final class ThemeArt {
 private ThemeArt(){}
 public static Drawable background(Context context,final int quadrant){
  Bitmap image=null;
  try{InputStream in=context.getAssets().open("theme/background-atlas.png");try{BitmapFactory.Options options=new BitmapFactory.Options();options.inPreferredConfig=Bitmap.Config.RGB_565;image=BitmapFactory.decodeStream(in,null,options);}finally{in.close();}}catch(IOException ignored){}
  final Bitmap bitmap=image;
  return new Drawable(){private final Paint paint=new Paint(Paint.FILTER_BITMAP_FLAG);
   public void draw(Canvas c){c.drawColor(Color.rgb(8,19,34));if(bitmap!=null){int w=bitmap.getWidth()/2,h=bitmap.getHeight()/2;c.drawBitmap(bitmap,new Rect((quadrant%2)*w,(quadrant/2)*h,(quadrant%2+1)*w,(quadrant/2+1)*h),getBounds(),paint);}}
   public void setAlpha(int alpha){paint.setAlpha(alpha);}public void setColorFilter(ColorFilter filter){paint.setColorFilter(filter);}public int getOpacity(){return PixelFormat.OPAQUE;}
  };
 }
}
