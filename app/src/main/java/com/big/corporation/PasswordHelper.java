package com.big.corporation;

import java.util.Hashtable;
import java.util.Map;

public class PasswordHelper {

    private Map<Character, Character> map = new Hashtable<Character, Character>();

    public PasswordHelper(String[] stringArray){

        for (String s : stringArray) {

            String pair[] = s.split(":");
            map.put(pair[0].charAt(0), pair[1].charAt(0));
        }
    }

    public String convert(CharSequence source){

        StringBuilder result = new StringBuilder();
        char rus_letter;
        char latin_letter;

        for(int i=0; i<source.length(); i++){

            try{
                rus_letter = source.charAt(i);
                latin_letter = map.get(rus_letter);
                result.append(Character.isUpperCase(rus_letter)?
                        Character.toUpperCase(latin_letter) : latin_letter);
            }
            catch (NullPointerException e){
                result.append(source.charAt(i));
            }

        }

        return result.toString();
    }

    public String generatePassword(int length, boolean isCaps, boolean isDigits, boolean isSymbols){
        StringBuilder password = new StringBuilder();

        for(int i=0; i<password.length(); i++){
            password.append("A");
        }

        return password.toString();
    }

    public int getPasswordStrengthLevel(String password){

        if (password.isEmpty()) {
            return 0;
        }

        int strength = 0;
        int pswLength = password.length();

        if(pswLength > 0 && pswLength <= 3){
            strength = 1;
        }
        else if(pswLength >= 4 && pswLength <= 6){
            strength = 2;
        }
        else if(pswLength >= 7){
            strength = 3;
        }

        return strength;
    }
}
