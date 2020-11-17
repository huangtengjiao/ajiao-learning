import java.io.UnsupportedEncodingException;

public class BinaryToString {
	public static void main(String[] args) {
		String string = "1";
		byte[] b = null;
		try {
			b = string.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < b.length; i++) {
			System.out.print(Integer.toBinaryString(b[i] & 0xff));
		}
		//String fString = new String(b);
		//System.out.print("\n" + fString);
	}
}
