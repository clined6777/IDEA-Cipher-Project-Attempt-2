import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Encryptor test = new Encryptor("My name is Donnell", "Thisisakeyyeah");
		test.encrypt();
		System.out.println(test.getCipherText());
		Decryptor test2 = new Decryptor(test.getCipherText(), test.getKey());
		test2.decrypt();
		System.out.println(test2.getPlainText());
	}

}
