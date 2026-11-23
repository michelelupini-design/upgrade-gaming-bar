package com.upgrade.pandora;
public final class ArcadeFeedback {
 private int held=0;private final long[] released={-1,-1,-1,-1,-1,-1};
 public void press(int n){if(n>=0&&n<6)held|=1<<n;}
 public void release(int n,long now){if(n>=0&&n<6){held&=~(1<<n);released[n]=now;}}
 public boolean isHeld(int n){return n>=0&&n<6&&(held&(1<<n))!=0;}
 public float strength(int n,long now){if(n<0||n>=6)return 0;if((held&(1<<n))!=0)return 1;if(released[n]<0)return 0;return Math.max(0,1-(now-released[n])/160f);}
 public void clear(){held=0;for(int i=0;i<6;i++)released[i]=-1;}
}