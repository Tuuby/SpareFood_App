package de.thb.sparefood_app.ui.chat;

public class Message {
    private String bearerToken;
    private String recipient;
    private String content;

    public Message(String bearerToken, String recipient, String content) {
        this.bearerToken = bearerToken;
        this.recipient = recipient;
        this.content = content;
    }

    public Message(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}