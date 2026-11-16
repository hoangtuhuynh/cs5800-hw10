package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
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
        for (User user : List.of(alice, bob, charlie)) {
            server.registerUser(user);
            user.setChatServer(server);
        }
    }

    @Test
    void sendAndReceiveThroughMediator() {
        alice.sendMessage("hello bob", bob);
        assertEquals(1, alice.getChatHistory().size());
        assertEquals(1, bob.getChatHistory().size());
        assertEquals("hello bob", bob.getChatHistory().getLastMessage().getContent());
    }

    @Test
    void undoLastMessageUsesMemento() {
        alice.sendMessage("oops", bob);
        assertEquals("oops", alice.getLastMessageMemento().getContent());
        alice.undoLastMessage();
        assertTrue(alice.getChatHistory().isEmpty());
        assertTrue(bob.getChatHistory().isEmpty());
    }

    @Test
    void blockUserPreventsIncomingMessages() {
        bob.blockUser(alice);
        alice.sendMessage("are you there?", bob);
        assertEquals(1, alice.getChatHistory().size());
        assertTrue(bob.getChatHistory().isEmpty());
    }

    @Test
    void iteratorDelegatesToChatHistory() {
        alice.sendMessage("hi bob", bob);
        bob.sendMessage("hi alice", alice);
        charlie.sendMessage("hi alice", alice);

        Iterator<Message> withBob = alice.iterator(bob);
        List<Message> found = new ArrayList<>();
        while (withBob.hasNext()) {
            found.add(withBob.next());
        }

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(message -> message.involves(bob)));
        assertFalse(found.stream().anyMatch(message -> message.involves(charlie)
                && !message.involves(bob)));
    }

    @Test
    void rejectsBlankNameAndMissingServer() {
        assertThrows(IllegalArgumentException.class, () -> new User(" "));
        User dana = new User("Dana");
        assertThrows(IllegalStateException.class, () -> dana.sendMessage("hi", alice));
    }
}
