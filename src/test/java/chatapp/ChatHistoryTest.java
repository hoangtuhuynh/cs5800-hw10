package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatHistoryTest {
    private ChatHistory history;
    private User alice;
    private User bob;
    private User charlie;
    private Message aliceToBob;
    private Message bobToAlice;
    private Message aliceToCharlie;

    @BeforeEach
    void setUp() {
        history = new ChatHistory();
        alice = new User("Alice");
        bob = new User("Bob");
        charlie = new User("Charlie");
        aliceToBob = new Message(alice, List.of(bob), "hi bob");
        bobToAlice = new Message(bob, List.of(alice), "hi alice");
        aliceToCharlie = new Message(alice, List.of(charlie), "hi charlie");
    }

    @Test
    void addsMessagesAndReturnsLast() {
        assertNull(history.getLastMessage());
        history.addMessage(aliceToBob);
        history.addMessage(bobToAlice);
        assertEquals(bobToAlice, history.getLastMessage());
        assertEquals(2, history.size());
    }

    @Test
    void removesMessageMatchingMemento() {
        history.addMessage(aliceToBob);
        history.addMessage(bobToAlice);
        assertTrue(history.removeMessageMatching(aliceToBob.createMemento()));
        assertEquals(List.of(bobToAlice), history.getMessages());
        assertFalse(history.removeMessageMatching(aliceToBob.createMemento()));
    }

    @Test
    void iteratorFiltersByUser() {
        history.addMessage(aliceToBob);
        history.addMessage(bobToAlice);
        history.addMessage(aliceToCharlie);

        Iterator<Message> withBob = history.iterator(bob);
        List<Message> found = new ArrayList<>();
        while (withBob.hasNext()) {
            found.add(withBob.next());
        }

        assertEquals(List.of(aliceToBob, bobToAlice), found);
    }

    @Test
    void rejectsNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> history.addMessage(null));
    }
}
