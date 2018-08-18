
public class RuntimeConstantPollOOM {
	public static void main(String[] args) {
		String str2 = new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern() == str2);
		String str1 = new StringBuilder("计算机6").append("软件").toString();
		System.out.println(str1.intern() == str1);
	}
}
