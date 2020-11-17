import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String str = "For my money, the important thing " + "about the meeting was bridge-building";

		long l = System.currentTimeMillis();
		int nums = 10000;
		for (int i = 0; i < nums; i++) {
			str.matches("the");
		}

		System.out.println((System.currentTimeMillis() - l));

		long length = str.getBytes().length * nums;
		System.out.println(length/1024 +"K");
	}
}
