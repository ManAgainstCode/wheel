package cn.larry.regexp;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则表达式
 * <p/>
 * Created by larryfu on 16-2-20.
 */
public class Regexp {

    private RegNFA nfa;

    public Regexp(String regexp) {
        this.nfa = new RegNFA(regexp);
    }

    public boolean recognizes(String str) {
        return nfa.recognizes(str);
    }

    /**
     * 从给定字符串txt中查找符合正则表达式regexp的子串,
     * 逻辑与字符串的子串查找算法一致，可以参考其算法优化
     *
     * @param txt
     * @return
     */

    public List<String> match(String txt) {
        List<String> match = new ArrayList<>();
        for (int i = 0; i < txt.length(); i++) {
            boolean alreadyAccept = nfa.accept(); //是否已识别成功
            boolean live = nfa.input(txt.charAt(i), txt, i);
            if (!live) { //i处字符导致nfa死亡
                if (alreadyAccept) {  //前面的字符已识别成功，将成功识别的字符串加入识别中
                    String current = nfa.getCurrentMatch();
                    if (!current.isEmpty())
                        match.add(current);
                } else i -= nfa.matchLen();  //与字符串的查找子串算法相同，若不匹配则回退已匹配的字符数
                nfa.reset();
            }
        }
        if (nfa.accept())
            match.add(nfa.getCurrentMatch());
        nfa.reset();
        return match;
    }

    public static void main(String[] args) {
        String expression = "(at|is) a";
        // RegNFA nfa = new RegNFA(expression);
        Regexp regexp1 = new Regexp(expression);
        System.out.println(regexp1.recognizes("is a"));
        System.out.println(regexp1.match("that is a"));
    }

}
