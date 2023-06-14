package fr.utc.sr03.chat.websocket;

public class ChatMessage {
    private String email;
    private String content;
    private long timestamp;

    public ChatMessage(String email, String content, long timestamp) {
        this.email = email;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
