package cn.larry.regexp;

/**
 * Created by larryfu on 16-1-31.
 */
public interface Escape_Characters {
    char ESCAPE_LEFT_BARCKET = Character.MAX_VALUE - 1;     //(
    char ESCAPE_RIGHT_BARCKET = Character.MAX_VALUE - 2;    //)
    char ESCAPE_START = Character.MAX_VALUE - 3;            //^
    char ESCAPE_END = Character.MAX_VALUE - 4;              //$
    char ESCAPE_NUMBER = Character.MAX_VALUE - 5;           //\d
    char ESCAPE_NOT_NUMBER = Character.MAX_VALUE - 6;       //\D
    char ESCAPE_WORD = Character.MAX_VALUE - 7;             //\w
    char ESCAPE_NOT_WORD = Character.MAX_VALUE - 8;         // \W
    char ESCAPE_SPACE = Character.MAX_VALUE - 9;            // \s
    char ESCAPE_TABLE = Character.MAX_VALUE - 10;           // \t
    char ESCAPE_STAR = Character.MAX_VALUE - 11;            // \*
    char ESCAPE_ADD = Character.MAX_VALUE - 12;             // \+
    char ESCAPE_OR = Character.MAX_VALUE - 13;              // \|
    char ESCAPE_ESCAPE = Character.MAX_VALUE - 14;          //\\
    char ESCAPE_ANTI_SPACE = Character.MAX_VALUE - 9;       // \/
}
