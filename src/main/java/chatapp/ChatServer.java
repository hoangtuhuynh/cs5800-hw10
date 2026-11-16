package chatapp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Set<String>> blockedUsers = new HashMap<>();

    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        users.put(user.getName(), user);
        blockedUsers.putIfAbsent(user.getName(), new HashSet<>());
    }

    public void unregisterUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        users.remove(user.getName());
        blockedUsers.remove(user.getName());
        for (Set<String> blocked : blockedUsers.values()) {
            blocked.remove(user.getName());
        }
    }

    public boolean isRegistered(User user) {
        return user != null && users.containsKey(user.getName());
    }

    public void blockUser(User blocker, User blocked) {
        requireRegistered(blocker);
        requireRegistered(blocked);
        if (blocker.equals(blocked)) {
            throw new IllegalArgumentException("a user cannot block themselves");
        }
        blockedUsers.get(blocker.getName()).add(blocked.getName());
    }

    public void unblockUser(User blocker, User blocked) {
        requireRegistered(blocker);
        if (blocked == null) {
            throw new IllegalArgumentException("blocked user cannot be null");
        }
        blockedUsers.get(blocker.getName()).remove(blocked.getName());
    }

    public boolean isBlocked(User recipient, User sender) {
        if (recipient == null || sender == null) {
            return false;
        }
        Set<String> blocked = blockedUsers.get(recipient.getName());
        return blocked != null && blocked.contains(sender.getName());
    }

    public void sendMessage(User sender, List<User> recipients, String content) {
        requireRegistered(sender);
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("recipients cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }

        for (User recipient : recipients) {
            requireRegistered(recipient);
            if (recipient.equals(sender)) {
                throw new IllegalArgumentException("cannot send a message to yourself");
            }
        }

        Message message = new Message(sender, recipients, content);
        sender.storeSentMessage(message);

        for (User recipient : recipients) {
            if (!isBlocked(recipient, sender)) {
                recipient.receiveMessage(message);
            }
        }
    }

    public void undoLastMessage(User sender) {
        requireRegistered(sender);
        MessageMemento memento = sender.getLastMessageMemento();
        if (memento == null) {
            return;
        }

        Message lastSent = sender.getChatHistory().getLastMessage();
        if (lastSent == null || !lastSent.matchesMemento(memento) || !lastSent.getSender().equals(sender)) {
            // Find the last message this user actually sent
            lastSent = null;
            List<Message> history = sender.getChatHistory().getMessages();
            for (int i = history.size() - 1; i >= 0; i--) {
                Message candidate = history.get(i);
                if (candidate.getSender().equals(sender) && candidate.matchesMemento(memento)) {
                    lastSent = candidate;
                    break;
                }
            }
        }

        if (lastSent == null) {
            sender.clearLastMessageMemento();
            return;
        }

        sender.getChatHistory().removeMessageMatching(memento);
        for (User recipient : lastSent.getRecipients()) {
            if (isRegistered(recipient)) {
                recipient.getChatHistory().removeMessageMatching(memento);
            }
        }
        sender.clearLastMessageMemento();
    }

    private void requireRegistered(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (!users.containsKey(user.getName())) {
            throw new IllegalArgumentException(user.getName() + " is not registered");
        }
    }
}
