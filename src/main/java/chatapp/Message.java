package chatapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Message {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final User sender;
    private final List<User> recipients;
    private final LocalDateTime timestamp;
    private final String content;

    public Message(User sender, List<User> recipients, String content) {
        this(sender, recipients, content, LocalDateTime.now());
    }

    public Message(User sender, List<User> recipients, String content, LocalDateTime timestamp) {
        if (sender == null) {
            throw new IllegalArgumentException("sender cannot be null");
        }
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("recipients cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        this.sender = sender;
        this.recipients = List.copyOf(recipients);
        this.content = content;
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public boolean involves(User user) {
        if (user == null) {
            return false;
        }
        return sender.equals(user) || recipients.contains(user);
    }

    public MessageMemento createMemento() {
        return new MessageMemento(content, timestamp);
    }

    public boolean matchesMemento(MessageMemento memento) {
        return memento != null
                && Objects.equals(content, memento.getContent())
                && Objects.equals(timestamp, memento.getTimestamp());
    }

    @Override
    public String toString() {
        String recipientNames = recipients.stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));
        return String.format("[%s] %s -> %s: %s",
                timestamp.format(FORMATTER),
                sender.getName(),
                recipientNames,
                content);
    }

}
