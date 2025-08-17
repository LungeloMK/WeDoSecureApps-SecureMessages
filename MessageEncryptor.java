package za.ac.tut.encryption;
public class MessageEncryptor {
    private static final int SHIFT = 3; 
    
    public String encrypt(String plainText) {
        if (plainText == null) {
            return "";
        }
        char[] chars = plainText.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                char base = Character.isLowerCase(chars[i]) ? 'a' : 'A';
                chars[i] = (char)(base + (chars[i] - base + SHIFT) % 26);
            }
        }
        return new String(chars);
    }
    public String decrypt(String cipherText) {
        if (cipherText == null) {
            return "";
        }
        char[] chars = cipherText.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                char base = Character.isLowerCase(chars[i]) ? 'a' : 'A';
                chars[i] = (char)(base + (chars[i] - base - SHIFT + 26) % 26);
            }
        }
        return new String(chars);
    }
}