import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Encryptor implements Ciphering{
	
	private String plainText, key;
	private byte[] cipherText, byteKey, byteText;
	private short[] subKeys;
	
	public Encryptor(String text, String keySet) {
		plainText = text;
		key = keySet;
		byteKey = keySet.getBytes();
		subKeys = new short[52];
		byte[] tempKey = byteKey;
		for (int k = 0; k < 8; k++) {
			subKeys[8 * k] = (short) (byteKey[8 * k] << 8 + (int) byteKey[8 * k + 1]);
			subKeys[8 * k + 1] = (short) (byteKey[8 * k + 2] << 8 + (int) byteKey[8 * k + 3]);
			subKeys[8 * k + 2] = (short) (byteKey[8 * k + 4] << 8 + (int) byteKey[8 * k + 5]);
			subKeys[8 * k + 3] = (short) (byteKey[8 * k + 6] << 8 + (int) byteKey[8 * k + 7]);
			subKeys[8 * k + 4] = (short) (byteKey[8 * k + 8] << 8 + (int) byteKey[8 * k + 9]);
			subKeys[8 * k + 5] = (short) (byteKey[8 * k + 10] << 8 + (int) byteKey[8 * k + 11]);
			subKeys[8 * k + 6] = (short) (byteKey[8 * k + 12] << 8 + (int) byteKey[8 * k + 13]);
			subKeys[8 * k + 7] = (short) (byteKey[8 * k + 14] << 8 + (int) byteKey[8 * k + 15]);
			for (int g = 0; g < byteKey.length; g++)
				tempKey[g] = (byte) (byteKey[(g + 3) % byteKey.length] << 1 + byteKey[(g + 4) % byteKey.length] >> 7);
			byteKey = tempKey;
		}
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
			subKeys = new short[52];
			byte[] tempKey = byteKey;
			for (int k = 0; k < 8; k++) {
				subKeys[8 * k] = (short) (byteKey[8 * k] << 8 + (int) byteKey[8 * k + 1]);
				subKeys[8 * k + 1] = (short) (byteKey[8 * k + 2] << 8 + (int) byteKey[8 * k + 3]);
				subKeys[8 * k + 2] = (short) (byteKey[8 * k + 4] << 8 + (int) byteKey[8 * k + 5]);
				subKeys[8 * k + 3] = (short) (byteKey[8 * k + 6] << 8 + (int) byteKey[8 * k + 7]);
				subKeys[8 * k + 4] = (short) (byteKey[8 * k + 8] << 8 + (int) byteKey[8 * k + 9]);
				subKeys[8 * k + 5] = (short) (byteKey[8 * k + 10] << 8 + (int) byteKey[8 * k + 11]);
				subKeys[8 * k + 6] = (short) (byteKey[8 * k + 12] << 8 + (int) byteKey[8 * k + 13]);
				subKeys[8 * k + 7] = (short) (byteKey[8 * k + 14] << 8 + (int) byteKey[8 * k + 15]);
				for (int g = 0; g < byteKey.length; g++)
					tempKey[g] = (byte) ((byteKey[(g + 3) % byteKey.length] << 1) % 256 + byteKey[(g + 4) % byteKey.length] >> 7);
				byteKey = tempKey;
			}
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
		else {
			for (int i = 0; i < byteText.length / 8; i++) {
				for (int j = 0; j < 8; j++) {
					short s1 = (short) (byteText[6 * i] << 8 + (int) byteText[6 * i + 1]);
					short s2 = (short) (byteText[6 * i + 2] << 8 + (int) byteText[6 * i + 3]);
					short s3 = (short) (byteText[6 * i + 4] << 8 + (int) byteText[6 * i + 5]);
					short s4 = (short) (byteText[6 * i + 6] << 8 + (int) byteText[6 * i + 7]);
					
				}
			}
		}
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
