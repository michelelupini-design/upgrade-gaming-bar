package com.upgrade.pandora;

import java.util.*;
import java.text.Normalizer;
import java.util.regex.Pattern;

public final class MenuModel {
    private static final Pattern MARKS=Pattern.compile("\\p{M}+");
    private static final Pattern NON_ALNUM=Pattern.compile("[^\\p{L}\\p{Nd}]+");
    private static final Pattern PARENTHESES=Pattern.compile("\\([^)]*\\)");
    public static final String IMPORTS="Import e altre lingue";
    public static final String[] CATEGORIES = {"Tutti", "Cerca un gioco", "Preferiti", "Guida ai comandi", "Arcade", "NES / Famicom", "Super Nintendo", "Mega Drive", "Game Boy / Color", "Game Boy Advance", "PC Engine", "PlayStation", "Classici", "Azione", "Sparatutto", "Rompicapo", "Sport", IMPORTS, "Opzioni console"};
    public static int categoryWindowStart(int selected,int visible){return Math.max(0,Math.min(selected-visible+1,CATEGORIES.length-visible));}
    public static final class Game {
        public final String id, title, category, path, core, thumbnail;
        public final int players;
        public final String platform;
        private final String alphabeticKey, searchKey;
        public String[] screenshots;
        public boolean mainCatalog=true;
        public Game(String id, String title, String category, int players, String path, String core, String thumbnail) {
            this.screenshots=thumbnail.isEmpty()?new String[0]:new String[]{thumbnail};
            this.id=id; this.title=title; this.category=category; this.players=players;
            this.path=path; this.core=core; this.thumbnail=thumbnail;
            this.alphabeticKey=sortKey(title);
            this.searchKey=normalize(PARENTHESES.matcher(title).replaceAll(""));
            this.platform=core.startsWith("mame")?"Arcade":core.startsWith("quicknes")||core.startsWith("fceumm")?"NES / Famicom":core.startsWith("genesis_plus_gx")?"Mega Drive":core.startsWith("snes9x")?"Super Nintendo":core.startsWith("gambatte")?"Game Boy / Color":core.startsWith("gpsp")||core.startsWith("mgba")?"Game Boy Advance":core.startsWith("mednafen_pce")?"PC Engine":core.startsWith("mupen64plus")?"Nintendo 64":core.startsWith("pcsx_rearmed")?"PlayStation":"";
        }
    }
    private final List<Game> all;
    public final Set<String> favorites;
    private final List<Game> filtered = new ArrayList<Game>();
    private String category="Tutti",query="",scope="";
    private int selection=0;
    public MenuModel(List<Game> games, Set<String> favorites) { this.all=new ArrayList<Game>(games); this.favorites=favorites;
        Collections.sort(this.all,new Comparator<Game>(){public int compare(Game a,Game b){int c=a.alphabeticKey.compareTo(b.alphabeticKey);return c!=0?c:a.id.compareTo(b.id);}});
        filter("Tutti"); }
    public List<Game> visible() { return Collections.unmodifiableList(filtered); }
    public Game selected() { return filtered.isEmpty() ? null : filtered.get(selection); }
    public int index() { return selection; }
    public String category() { return category; }
    public void select(int n) { selection=Math.max(0,Math.min(n,filtered.size()-1)); }
    public static String normalize(String s){return NON_ALNUM.matcher(MARKS.matcher(Normalizer.normalize(s,Normalizer.Form.NFD)).replaceAll("").toLowerCase(Locale.ROOT)).replaceAll("");}
    private static String sortKey(String s){return normalize(s.toLowerCase(Locale.ROOT).startsWith("the ")?s.substring(4):s);}
    public static boolean suggestionsReady(String text){String normalized=normalize(text);return normalized.codePointCount(0,normalized.length())>=3;}
    public String query(){return query;}
    public String scope(){return scope;}
    public List<String> availablePlatforms(){TreeSet<String> p=new TreeSet<String>();for(Game g:all)if(!g.platform.isEmpty())p.add(g.platform);ArrayList<String> result=new ArrayList<String>(p);result.add(IMPORTS);return result;}
    public void search(String text,String platform){category="Cerca un gioco";query=text.length()>64?text.substring(0,64):text;scope=platform;rebuild();}
    public void filter(String next){category=next;query="";scope="";rebuild();}
    private void rebuild(){
        filtered.clear();String needle=normalize(query);
        for(Game g:all){
            boolean importView=category.equals(IMPORTS)||(category.equals("Cerca un gioco")&&scope.equals(IMPORTS));
            if(importView==g.mainCatalog)continue;
            boolean match=category.equals(IMPORTS)||category.equals("Cerca un gioco")?(scope.isEmpty()||scope.equals(IMPORTS)||scope.equals(g.platform))&&g.searchKey.contains(needle):category.equals("Tutti")||category.equals(g.category)||category.equals(g.platform)||(category.equals("Preferiti")&&favorites.contains(g.id));
            if(match)filtered.add(g);
        }
        selection=0;
    }
    public void move(int dx,int dy,int columns) { select(selection+dx+dy*columns); }
    public void toggleFavorite() {
        Game g=selected(); if(g==null)return;
        if(!favorites.remove(g.id))favorites.add(g.id);
        if(category.equals("Preferiti"))filter(category);
    }
}
