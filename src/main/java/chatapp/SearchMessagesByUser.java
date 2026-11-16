package chatapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchMessagesByUser implements Iterator<Message> {
    private final List<Message> matchingMessages;
    private int index;

    public SearchMessagesByUser(List<Message> messages, User userToSearchWith) {
        if (messages == null) {
            throw new IllegalArgumentException("messages cannot be null");
        }
        if (userToSearchWith == null) {
            throw new IllegalArgumentException("userToSearchWith cannot be null");
        }
        matchingMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.involves(userToSearchWith)) {
                matchingMessages.add(message);
            }
        }
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < matchingMessages.size();
    }

    @Override
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more messages for this user");
        }
        return matchingMessages.get(index++);
    }
}
