package com.upgrade.pandora;
/** Codes shared by both physical panels and the keyboard, only within our menus. */
public final class MenuActions {
 public static boolean isBack(int key){return key==40||key==147||key==100||key==37||key==149||key==97||key==4||key==111;}
 public static int optionAction(int key){
 if(isBack(key)||key==39||key==146)return 1;
 if(key==51||key==19)return 2;
 if(key==47||key==20)return 3;
 if(key==49||key==148||key==9||key==11||key==96||key==108||key==66||key==23)return 0;
 return -1;
 }
 /** 0 categories, 1 search keyboard, 2 search results. F has one consistent destination. */
 public static int backDestination(int key,boolean search,boolean keyboard,boolean suggestions){
  if(key==40||key==147||key==100||!search)return 0;
  return keyboard?(suggestions?1:2):1;
 }
}
