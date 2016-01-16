package cn.larry.demo;

/**
 * Created by larryfu on 2016/1/9.
 *
 * @author larryfu
 */
public class Sup {
    byte tag;
    String name = "larry";
    static String str = "abc";

    String say() {
        return "sup";
    }

    public static void main(String[] args) {
        //Sup sup = new Sup();
//        System.out.println(sup.tag);
//        System.out.println(sup.say());
        Person p1 = new Person();
        Person p2 = new Person();
        String name = new String("larry");
        name = name.intern();
        System.out.println(Sup.class.getClassLoader());
        String s = "abc";

        boolean bl = p1.name == p2.name;
        System.out.println(s==Person.str);
//        System.out.println(bl);
//        System.out.println(p1.name == name);
    }
}

class Sub extends Sup {
    byte tag = 1;

    String say() {
        return "sub";
    }

}