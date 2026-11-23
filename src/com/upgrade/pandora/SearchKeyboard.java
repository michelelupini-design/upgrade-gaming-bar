package com.upgrade.pandora;
public final class SearchKeyboard {
 public static final String[] KEYS=create();
 private static String[] create(){String chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";String[] keys=new String[40];for(int i=0;i<36;i++)keys[i]=chars.substring(i,i+1);keys[36]="SPAZIO";keys[37]="CANCELLA";keys[38]="PIATTAFORMA";keys[39]="INDIETRO";return keys;}
 public static int move(int index,int dx,int dy){int row=Math.max(0,Math.min(3,index/10+dy)),col=Math.max(0,Math.min(9,index%10+dx));return row*10+col;}
}