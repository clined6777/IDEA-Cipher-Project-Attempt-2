
public interface Ciphering {
	public void setKey(String keySet);
	public String getKey();
	public String getPlainText();
	public byte[] getCipherText();
}
