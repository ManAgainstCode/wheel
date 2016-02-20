package cn.larry.regexp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larryfu on 16-2-6.
 */
public enum EscapeCharacter {
    LEFT_BARCKET(1, "\\(") {
        @Override
        public boolean match(char c) {
            return c == '(';
        }
    },
    RIGHT_BARCKET(2, "\\)") {
        @Override
        public boolean match(char c) {
            return c == ')';
        }
    },
    STAR(3, "\\*") {
        @Override
        public boolean match(char c) {
            return c == '*';
        }
    },
    ADD(4, "\\+") {
        @Override
        public boolean match(char c) {
            return c == '+';
        }
    },
    OR(5, "\\|") {
        @Override
        public boolean match(char c) {
            return c == '|';
        }
    },
    DOT(6, "\\.") {
        @Override
        public boolean match(char c) {
            return c == '.';
        }
    },
    START(7, "\\^") {
        @Override
        public boolean match(char c) {
            return c == '^';
        }
    },
    END(8, "\\$") {
        @Override
        public boolean match(char c) {
            return c == '$';
        }
    };
//    },
//    NUMBER(9, "\\d"),
//    NOT_NUMBER(10, "\\D"),
//    WORD(11, "\\w"),
//    NOT_WORD(12, "\\W"),
//    SPACE(13, "\\s"),
//    TABLE(14, "\\T");

    private char code;
    private String expression;

    EscapeCharacter(int index, String expression) {
        this.code = (char) (Character.MAX_VALUE - index);
        this.expression = expression;
    }

    public char getCode() {
        return code;
    }

    public abstract boolean match(char c);

    public String getExpression() {
        return expression;
    }

    private Map<Character, String> getMap() {
        Map<Character, String> valueMap = new HashMap<>();
        for (EscapeCharacter ec : EscapeCharacter.values())
            valueMap.put(ec.getCode(), ec.getExpression());

        return valueMap;
    }
}
