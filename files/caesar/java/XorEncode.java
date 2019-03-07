import java.util.*;

class XorEncode {
	public static void main(String[] args) {
		String kulcs = "";

		if(args.length > 0) {
			kulcs = args[0];
		} else {
			System.out.println("Kulcs nelkul nem titkositok!");
			System.out.println("Hasznalat: java XorEncode.java [kulcs]");
			System.exit(-1);
		}

		Scanner sc = new Scanner(System.in);
		String str = "";

		while(sc.hasNext()) {
			str = sc.next();
			System.out.println(xor(kulcs, str));
		}
	}

	public static String xor(String kulcs, String s) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < s.length(); i++) {
			sb.append((char)(s.charAt(i) ^ kulcs.charAt(i % kulcs.length())));
		}

		return sb.toString();
	}
}
