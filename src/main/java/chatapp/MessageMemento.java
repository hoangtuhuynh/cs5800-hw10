package chatapp;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageMemento {
    private final String content;
    private final LocalDateTime timestamp;

    public MessageMemento(String content, LocalDateTime timestamp) {
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MessageMemento that)) {
            return false;
        }
        return Objects.equals(content, that.content)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, timestamp);
    }

    @Override
    public String toString() {
        return "MessageMemento{content='" + content + "', timestamp=" + timestamp + '}';
    }
}
