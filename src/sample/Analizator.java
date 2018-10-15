package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Miha on 26/11/2017.
 */
public class Analizator {
    public HashMap<Character, Integer> analizirajDatoteko(File file) throws FileNotFoundException {
        HashMap<Character, Integer> mapa = new HashMap<>();

        Scanner scannerSifrirano = new Scanner(file);
        StringBuilder stringBuffer = new StringBuilder();

        while(scannerSifrirano.hasNextLine()){
            stringBuffer.append(scannerSifrirano.nextLine()).append(" ");
        }
        String besediloSifrirano = stringBuffer.toString();

        for(int i = 0; i < besediloSifrirano.length(); i++){
            char c = besediloSifrirano.charAt(i);
            Integer val = mapa.get(c);
            if(val != null){
                mapa.put(c, val + 1);
            }else{
                mapa.put(c, 1);
            }
        }

        return mapa;
    }

    public String zamenjajCrki(String str, char a, char b){
        //System.out.println("Original: "+str);
        /*return str
                .replace("$"+Character.toString(b), "$*")
                .replace("$"+a, Character.toString(b))
                .replace("$*", Character.toString(a));*/
        return str.replace("$"+Character.toString(a), Character.toString(b));
    }

    /*public Multiset<Character> analizirajDatoteko(File file ) throws FileNotFoundException {
        Multiset<Character> chars = HashMultiset.create();

        Scanner scannerSifrirano = new Scanner(file);
        StringBuilder stringBuffer = new StringBuilder();

        while(scannerSifrirano.hasNextLine()){
            stringBuffer.append(scannerSifrirano.nextLine()).append(" ");
        }
        String besediloSifrirano = stringBuffer.toString();

        for(int i = 0; i < besediloSifrirano.length(); i++){
            chars.add(besediloSifrirano.charAt(i));
        }


        return chars;
    }*/
}
