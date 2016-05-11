import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Encryptor implements Ciphering{
	
	private String plainText, key;
	private byte[] cipherText, byteKey, byteText;
	
	public Encryptor(String text, String keySet) {
		plainText = text;
		key = keySet;
		byteKey = keySet.getBytes();
		byte[] tempByte = text.getBytes();
		if (tempByte.length % 8 != 0) {
			int spaces = 8 - (tempByte.length % 8);
			byteText = new byte[tempByte.length + spaces];
			for (int i = 0; i < tempByte.length; i++)
				byteText[i] = tempByte[i];
			for (int j = tempByte.length; j < byteText.length; j += 2) {
				byteText[j] = 3;
				byteText[j + 1] = 2;
			}
		}
		else
			byteText = text.getBytes();
	}
	
	public Encryptor(File fileText) {
		try {
			Scanner file = new Scanner(fileText);
			key = file.nextLine();
			while (file.hasNextLine()) {
				plainText += file.nextLine();
			}
			byteKey = key.getBytes();
			byte[] tempByte = plainText.getBytes();
			if (tempByte.length % 8 != 0) {
				int spaces = 8 - (tempByte.length % 8);
				byteText = new byte[tempByte.length + spaces];
				for (int i = 0; i < tempByte.length; i++)
					byteText[i] = tempByte[i];
				for (int j = tempByte.length; j < byteText.length; j += 2) {
					byteText[j] = 3;
					byteText[j + 1] = 2;
				}
			}
			else
				byteText = plainText.getBytes();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void encrypt() {
		if (byteKey.length != 16)
			System.out.print("You cannot encrypt with this key. Please enter a key with 8 characters.");
	}
	
	public void setKey(String keySet) {
		key = keySet;
		byteKey = keySet.getBytes();
	}
	
	public String getKey() {
		return key;
	}
	
	public String getPlainText() {
		return plainText;
	}
	
	public byte[] getCipherText() {
		return cipherText;
	}
}
