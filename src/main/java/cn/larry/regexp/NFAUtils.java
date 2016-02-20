package cn.larry.regexp;

import java.util.*;

/**
 * Created by larryfu on 16-2-20.
 */
public class NFAUtils {

    private NFAUtils(){}

     static boolean reconizeStartEnd(char c, String text, int index) {
        return c == '^' && index == 0 || c == '$' && index == text.length() - 1;
    }

    static boolean equal(char expChar, char realChar) {
        if (expChar == realChar)
            return true;
        for (EscapeCharacter ec : EscapeCharacter.values())
            if (expChar == ec.getCode())
                return ec.match(realChar);
        return false;
    }

    /**
     * 将str中的所有origin子串替换为target
     *
     * @param str
     * @param origin
     * @param target
     * @return
     */

    public static String replaceAll(String str, String origin, String target) {
        if (str == null || origin == null || target == null)
            throw new IllegalArgumentException("parameter can not be null");
        if (origin.length() > str.length() || origin.length() == 0)
            return str;
        StringBuilder sb = new StringBuilder();
        int strLen = str.length(), originLen = origin.length();
        for (int i = 0; i < strLen; i++) {
            int j = 0;
            for (; j < originLen && i + j < strLen; j++)
                if (str.charAt(i + j) != origin.charAt(j))
                    break;
            if (j < originLen)
                sb.append(str.charAt(i));
            else {
                sb.append(target);
                i += originLen - 1;
            }
        }
        return sb.toString();
    }

    /**
     * 处理次数匹配
     *
     * @param re
     * @return
     */
    static char[] processTimes(char[] re) {
        int lp = 0, end = 0;
        Stack<Integer> stack = new Stack<>();
        Times time = null;
        char[] repeat = null;
        for (int i = 0; i < re.length; i++) {
            lp = i;
            if (re[i] == '|' || re[i] == '(')
                stack.push(i);
            if (re[i] == ')') {
                lp = stack.pop();
                while (re[lp] == '|')
                    lp = stack.pop();
            }
            if (i < re.length - 1 && re[i + 1] == '{') {
                if (re[i + 3] == '}') {
                    time = new Times(re[i + 2] - '0');
                    end = i + 4;
                } else if (re[i + 5] == '}') {
                    time = new Times(re[i + 2] - '0', re[i + 4] - '0');
                    end = i + 6;
                } else throw new IllegalArgumentException();
                repeat = new char[i + 1 - lp];
                System.arraycopy(re, lp, repeat, 0, repeat.length);
                break;
            }
        }
        if (time == null)
            return re;
        char[] prefix = new char[lp];
        System.arraycopy(re, 0, prefix, 0, prefix.length);
        char[] suffix = new char[re.length - end];
        System.arraycopy(re, end, suffix, 0, suffix.length);
        List<char[]> middle = new ArrayList<>();
        for (int i = 0; i < time.min; i++)
            middle.add(repeat);
        char[] exists = new StringBuilder().append(repeat).append('?').toString().toCharArray();
        for (int j = 0; j < time.max - time.min; j++)
            middle.add(exists);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        middle.forEach(sb::append);
        sb.append(suffix);
        return processTimes(sb.toString().toCharArray());
    }

    static String processEscape(String regexp) {
        for (EscapeCharacter ec : EscapeCharacter.values())
            regexp = replaceAll(regexp, ec.getExpression(), String.valueOf(ec.getCode()));
        return regexp;
    }

    /**
     * 将+号转换为*号
     *
     * @param re
     * @return
     */
     static char[] processPlus(char[] re) {
        Stack<Integer> stack = new Stack<>();
        int lp;
        Map<Integer, char[]> plusChars = new TreeMap<>();
        for (int i = 0; i < re.length; i++) {
            lp = i;
            if (re[i] == '|' || re[i] == '(')
                stack.push(i);
            if (re[i] == ')') {
                lp = stack.pop();
                while (re[lp] == '|')
                    lp = stack.pop();
            }
            if (i < re.length - 1 && re[i + 1] == '+') {
                char[] repeat = new char[i + 1 - lp];
                System.arraycopy(re, lp, repeat, 0, repeat.length);
                plusChars.put(lp, repeat);
                re[i + 1] = '*';
                break;
            }
        }
        int index = 0;
        List<char[]> clist = new ArrayList<>();
        if (plusChars.size() == 0)
            return re;
        for (Map.Entry<Integer, char[]> entry : plusChars.entrySet()) {
            char[] cs = new char[entry.getKey() - index];
            System.arraycopy(re, index, cs, 0, cs.length);
            clist.add(cs);
            clist.add(entry.getValue());
            index = entry.getKey();
        }
        char[] chars = new char[re.length - index];
        System.arraycopy(re, index, chars, 0, chars.length);
        clist.add(chars);
        StringBuilder sb = new StringBuilder();
        clist.forEach(sb::append);
        return processPlus(sb.toString().toCharArray());
    }



}
