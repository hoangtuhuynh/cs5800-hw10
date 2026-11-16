package chatapp;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class User implements IterableByUser {
    private final String name;
    private final ChatHistory chatHistory = new ChatHistory();
    private ChatServer chatServer;
    private MessageMemento lastMessageMemento;

    public User(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ChatHistory getChatHistory() {
        return chatHistory;
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public ChatServer getChatServer() {
        return chatServer;
    }

    public void sendMessage(String content, User... recipients) {
        requireServer();
        if (recipients == null || recipients.length == 0) {
            throw new IllegalArgumentException("recipients cannot be null or empty");
        }
        chatServer.sendMessage(this, Arrays.asList(recipients), content);
    }

    public void receiveMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        chatHistory.addMessage(message);
    }

    public void storeSentMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        lastMessageMemento = message.createMemento();
        chatHistory.addMessage(message);
    }

    public void undoLastMessage() {
        requireServer();
        chatServer.undoLastMessage(this);
    }

    public void blockUser(User user) {
        requireServer();
        chatServer.blockUser(this, user);
    }

    public void unblockUser(User user) {
        requireServer();
        chatServer.unblockUser(this, user);
    }

    public MessageMemento getLastMessageMemento() {
        return lastMessageMemento;
    }

    public void clearLastMessageMemento() {
        lastMessageMemento = null;
    }

    public void printChatHistory() {
        System.out.println("--- Chat history for " + name + " ---");
        if (chatHistory.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (Message message : chatHistory.getMessages()) {
            System.out.println(message);
        }
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return chatHistory.iterator(userToSearchWith);
    }

    private void requireServer() {
        if (chatServer == null) {
            throw new IllegalStateException(name + " is not connected to a chat server");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User that)) {
            return false;
        }
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
