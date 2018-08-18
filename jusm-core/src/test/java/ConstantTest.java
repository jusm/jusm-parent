
public class ConstantTest {
public static void main(String[] args) {
	t1();
	System.out.println("----------");
	t2();
}

public static void t1() {
	String a = "abc";
	String b = "abc";
	String c = "a" + "b" + "c";
	String d = "a" + "bc";
	String e = "ab" + "c";
	        
	System.out.println(a == b);
	System.out.println(a == c);
	System.out.println(a == d);
	System.out.println(a == e);
	System.out.println(c == d);
	System.out.println(c == e);
}
public static void t2() {
	String a = "一二三";
	String b = "一二三";
	String c = "一" + "二" + "三";
	String d = "一" + "二三";
	String e = "一二" + "三";
	        
	System.out.println(a == b);
	System.out.println(a == c);
	System.out.println(a == d);
	System.out.println(a == e);
	System.out.println(c == d);
	System.out.println(c == e);
}
}
