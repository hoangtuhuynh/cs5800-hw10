package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatServerTest {
    private ChatServer server;
    private User alice;
    private User bob;
    private User charlie;

    @BeforeEach
    void setUp() {
        server = new ChatServer();
        alice = new User("Alice");
        bob = new User("Bob");
        charlie = new User("Charlie");
        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);
        alice.setChatServer(server);
        bob.setChatServer(server);
        charlie.setChatServer(server);
    }

    @Test
    void registerAndUnregisterUsers() {
        User dana = new User("Dana");
        assertFalse(server.isRegistered(dana));
        server.registerUser(dana);
        assertTrue(server.isRegistered(dana));
        server.unregisterUser(dana);
        assertFalse(server.isRegistered(dana));
    }

    @Test
    void sendMessageDeliversToAllRecipients() {
        server.sendMessage(alice, List.of(bob, charlie), "group hello");

        assertEquals(1, alice.getChatHistory().size());
        assertEquals(1, bob.getChatHistory().size());
        assertEquals(1, charlie.getChatHistory().size());
        assertEquals("group hello", bob.getChatHistory().getLastMessage().getContent());
    }

    @Test
    void blockedRecipientDoesNotReceiveMessage() {
        server.blockUser(charlie, bob);
        server.sendMessage(bob, List.of(charlie), "blocked?");

        assertEquals(1, bob.getChatHistory().size());
        assertTrue(charlie.getChatHistory().isEmpty());
        assertTrue(server.isBlocked(charlie, bob));
    }

    @Test
    void undoRemovesMessageFromSenderAndRecipients() {
        server.sendMessage(alice, List.of(bob, charlie), "take this back");
        server.undoLastMessage(alice);

        assertTrue(alice.getChatHistory().isEmpty());
        assertTrue(bob.getChatHistory().isEmpty());
        assertTrue(charlie.getChatHistory().isEmpty());
        assertEquals(null, alice.getLastMessageMemento());
    }

    @Test
    void undoOnlyAffectsLastSentEvenIfLaterMessagesWereReceived() {
        server.sendMessage(alice, List.of(bob), "first");
        server.sendMessage(bob, List.of(alice), "reply");
        server.sendMessage(alice, List.of(bob), "second");
        server.sendMessage(bob, List.of(alice), "another reply");

        server.undoLastMessage(alice);

        assertEquals(3, alice.getChatHistory().size());
        assertEquals("another reply", alice.getChatHistory().getLastMessage().getContent());
        assertEquals(3, bob.getChatHistory().size());
        assertFalse(bob.getChatHistory().getMessages().stream()
                .anyMatch(message -> message.getContent().equals("second")));
    }

    @Test
    void rejectsUnregisteredSender() {
        User outsider = new User("Outsider");
        outsider.setChatServer(server);
        assertThrows(IllegalArgumentException.class,
                () -> server.sendMessage(outsider, List.of(alice), "hi"));
    }
}
