package cn.larry.regexp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larryfu on 16-2-20.
 */
public class Regexp {
    private RegNFA nfa;

    public Regexp(String regexp) {
        this.nfa = new RegNFA(regexp);
    }

    public List<String> match(String txt) {
        List<String> match = new ArrayList<>();
        for (int i = 0; i < txt.length(); i++) {
            boolean alreadyAccept = nfa.accept();
            boolean live = nfa.input(txt.charAt(i), txt, i);
            if (!live) {
                if (alreadyAccept)
                    match.add(nfa.getCurrentMatch());
                nfa.reset();
            }
        }
        if (nfa.accept())
            match.add(nfa.getCurrentMatch());
        nfa.reset();
        return match;
    }

    public static void main(String[] args) {


        String expression = "(at|is)";
        RegNFA nfa = new RegNFA(expression);
        System.out.println(nfa.recognizes("at"));

        Regexp regexp1 = new Regexp(expression);
        System.out.println(regexp1.match("that is at"));
    }

}
