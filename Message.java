package za.ac.tut.message;

public class Message {
    private final String content;
    
    public Message(String content) {
        this.content = content != null ? content : "";
    }
    
    public String getContent() {
        return content;
    }
    
    public int getLength() {
        return content.length();
    }
}