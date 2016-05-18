import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class Decryptor implements Ciphering{
	private String cipherText, plainText, key;
	private byte[] byteText, byteKey;
	private short[] subKeys;
	
	public Decryptor (String text, String keySet) {
		plainText = "";
		key = keySet;
		cipherText = text;
		byteKey = keySet.getBytes();
		byteKey = Arrays.copyOf(byteKey, 16);
		subKeys = new short[52];
		byte[] tempKey = byteKey;
		for (int k = 0; k < 6; k++) {
			subKeys[8 * k] = (short) (byteKey[0] << 8 + (int) byteKey[1]);
			subKeys[8 * k + 1] = (short) ((byteKey[2] << 8) + (int) byteKey[3]);
			subKeys[8 * k + 2] = (short) ((byteKey[4] << 8) + (int) byteKey[5]);
			subKeys[8 * k + 3] = (short) ((byteKey[6] << 8) + (int) byteKey[7]);
			subKeys[8 * k + 4] = (short) ((byteKey[8] << 8) + (int) byteKey[9]);
			subKeys[8 * k + 5] = (short) ((byteKey[10] << 8) + (int) byteKey[11]);
			subKeys[8 * k + 6] = (short) ((byteKey[12] << 8) + (int) byteKey[13]);
			subKeys[8 * k + 7] = (short) ((byteKey[14] << 8) + (int) byteKey[15]);
			for (int g = 0; g < byteKey.length; g++)
				tempKey[g] = (byte) (((byteKey[(g + 3) % byteKey.length]) << 1) + ((byteKey[(g + 4) % byteKey.length]) >> 7));
			byteKey = tempKey;
		}
		for (int k = 0; k < 4; k++)
			subKeys[48 + k] = (short) ((byteKey[2 * k] << 8) + (int) (byteKey[2 * k + 1]));
		byte[] tempByte = text.getBytes();
		if (tempByte.length % 8 != 0) {
			int spaces = 8 - (tempByte.length % 8);
			byteText = new byte[tempByte.length + spaces];
			for (int i = 0; i < tempByte.length; i++)
				byteText[i] = tempByte[i];
			for (int j = tempByte.length; j < byteText.length; j++) {
				byteText[j] = 32;
			}
		}
		else
			byteText = text.getBytes();
	}
	
	public Decryptor (File fileText) {
		try {
			plainText = "";
			Scanner file = new Scanner(fileText);
			key = file.nextLine();
			while (file.hasNextLine()) {
				cipherText += file.nextLine();
			}
			byteKey = key.getBytes();
			subKeys = new short[52];
			byte[] tempKey = byteKey;
			for (int k = 0; k < 6; k++) {
				subKeys[8 * k] = (short) (byteKey[0] << 8 + (int) byteKey[1]);
				subKeys[8 * k + 1] = (short) (byteKey[2] << 8 + (int) byteKey[3]);
				subKeys[8 * k + 2] = (short) (byteKey[4] << 8 + (int) byteKey[5]);
				subKeys[8 * k + 3] = (short) (byteKey[6] << 8 + (int) byteKey[7]);
				subKeys[8 * k + 4] = (short) (byteKey[8] << 8 + (int) byteKey[9]);
				subKeys[8 * k + 5] = (short) (byteKey[10] << 8 + (int) byteKey[11]);
				subKeys[8 * k + 6] = (short) (byteKey[12] << 8 + (int) byteKey[13]);
				subKeys[8 * k + 7] = (short) (byteKey[14] << 8 + (int) byteKey[15]);
				for (int g = 0; g < byteKey.length; g++)
					tempKey[g] = (byte) ((byteKey[(g + 3) % byteKey.length] << 1) % 256 + byteKey[(g + 4) % byteKey.length] >> 7);
				byteKey = tempKey;
			}
			for (int k = 0; k < 4; k++)
				subKeys[48 + k] = (short) (byteKey[2 * k] << 8 + (int) byteKey[2 * k + 1]);
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
	
	public void decrypt() {
		if (key.length() == 8)
			System.out.print("You cannot encrypt with this key. Please enter a key with 8 characters.");
		else {
			System.out.println(byteKey.length);
			System.out.println(Arrays.toString(subKeys));
			System.out.println(Arrays.toString(byteText));
			short[] shortToHex = new short[byteText.length / 2];
			for (int i = 0; i < byteText.length / 8; i++) {
				short s1 = (short) ((byteText[8 * i] << 8) + (int) byteText[8 * i + 1]);
				short s2 = (short) ((byteText[8 * i + 2] << 8) + (int) byteText[8 * i + 3]);
				short s3 = (short) ((byteText[8 * i + 4] << 8) + (int) byteText[8 * i + 5]);
				short s4 = (short) ((byteText[8 * i + 6] << 8) + (int) byteText[8 * i + 7]);
				for (int j = 0; j < 8; j++) {
					if (i % 2 == 1) {
						short multMod1 = 0;
						short multMod2 = 0;
						if (subKeys[51 - 6 * j] == 0)
							multMod1 = multMod(s1, 0);
						else
							multMod1 = multMod(s1, subKeys[51 - 6 * j]);
						short addMod1 = addMod(s2, (short) -subKeys[50 - 6 * j]);
						short addMod2 = addMod(s3, (short) -subKeys[49 - 6 * j]);
						if (subKeys[48 - 6 * j] == 0)
							multMod2 = multMod(s4, 0);
						else
							multMod2 = multMod(s4, subKeys[48 - 6 * j]);
						short eXOR1 = eXOR(multMod1, addMod2);
						short eXOR2 = eXOR(multMod2, addMod1);
						short multMod3 = multMod(eXOR1, subKeys[47 - 6 * j]);
						short addMod3 = addMod(multMod3, eXOR2);
						short multMod4 = multMod(addMod3, subKeys[46 - 6 * j]);
						short addMod4 = addMod(multMod3, multMod4);
						short eXOR3 = eXOR(multMod1, multMod4);
						short eXOR4 = eXOR(addMod2, multMod4);
						short eXOR5 = eXOR(addMod1, addMod4);
						short eXOR6 = eXOR(multMod2, addMod4);
						s1 = eXOR3;
						s2 = eXOR4;
						s3 = eXOR5;
						s4 = eXOR6;
					}
				}
				short temp = s3;
				s3 = s2;
				s2 = temp;
				short halfMult1 = multMod(s1, subKeys[3]);
				short halfAdd1 = addMod(s2, subKeys[2]);
				short halfAdd2 = addMod(s3, subKeys[1]);
				short halfMult2 = multMod(s4, subKeys[0]);
				shortToHex[4 * i] = halfMult1;
				shortToHex[4 * i + 1] = halfAdd1;
				shortToHex[4 * i + 2] = halfAdd2;
				shortToHex[4 * i + 3] = halfMult2;
			}
			for (int i = 0; i < shortToHex.length; i++) {
				Short temp = shortToHex[i];
				plainText += (char)(temp & 0xFF);
			}
		}
	}
	
	public short eXOR(short sh1, short sh2) {
		return (short) (sh1 ^ sh2);
	}
	
	public short addMod(short sh1, short sh2) {
		return (short) (sh1 + sh2);
	}
	
	public short multMod(double sh1, double sh2) {
		double output = 0;
		if (sh1 == 0)
			sh1 = 1<<16;
		if (sh2 == 0)
			sh2 = 1<<16;
		output = (sh1 * sh2) % ((1<<16)+1);
		if (output == 1<<16)
			output = 0;		
		return (short) output;
	}
	
	public void setKey(String keySet) {
		key = keySet;
		byteKey = keySet.getBytes();
		byte[] tempKey = byteKey;
		for (int k = 0; k < 6; k++) {
			subKeys[8 * k] = (short) (byteKey[0] << 8 + (int) byteKey[1]);
			subKeys[8 * k + 1] = (short) (byteKey[2] << 8 + (int) byteKey[3]);
			subKeys[8 * k + 2] = (short) (byteKey[4] << 8 + (int) byteKey[5]);
			subKeys[8 * k + 3] = (short) (byteKey[6] << 8 + (int) byteKey[7]);
			subKeys[8 * k + 4] = (short) (byteKey[8] << 8 + (int) byteKey[9]);
			subKeys[8 * k + 5] = (short) (byteKey[10] << 8 + (int) byteKey[11]);
			subKeys[8 * k + 6] = (short) (byteKey[12] << 8 + (int) byteKey[13]);
			subKeys[8 * k + 7] = (short) (byteKey[14] << 8 + (int) byteKey[15]);
			for (int g = 0; g < byteKey.length; g++)
				tempKey[g] = (byte) ((byteKey[(g + 3) % byteKey.length] << 1) % 256 + byteKey[(g + 4) % byteKey.length] >> 7);
			byteKey = tempKey;
		}
		for (int k = 0; k < 4; k++)
			subKeys[48 + k] = (short) (byteKey[2 * k] << 8 + (int) byteKey[2 * k + 1]);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getPlainText() {
		return plainText;
	}
	
	public String getCipherText() {
		System.out.println(Arrays.toString(cipherText.getBytes()));
		return cipherText;
	}

}
