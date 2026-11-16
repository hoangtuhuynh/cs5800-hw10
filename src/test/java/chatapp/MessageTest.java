package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class MessageTest {
    private final User alice = new User("Alice");
    private final User bob = new User("Bob");
    private final User charlie = new User("Charlie");

    @Test
    void storesSenderRecipientsContentAndTimestamp() {
        LocalDateTime time = LocalDateTime.of(2026, 11, 15, 10, 0);
        Message message = new Message(alice, List.of(bob, charlie), "hello", time);

        assertEquals(alice, message.getSender());
        assertEquals(List.of(bob, charlie), message.getRecipients());
        assertEquals("hello", message.getContent());
        assertEquals(time, message.getTimestamp());
    }

    @Test
    void involvesSenderOrRecipient() {
        Message message = new Message(alice, List.of(bob), "hi");
        assertTrue(message.involves(alice));
        assertTrue(message.involves(bob));
        assertFalse(message.involves(charlie));
    }

    @Test
    void createMementoCapturesContentAndTimestamp() {
        LocalDateTime time = LocalDateTime.of(2026, 11, 15, 11, 30);
        Message message = new Message(alice, List.of(bob), "snapshot", time);
        MessageMemento memento = message.createMemento();

        assertEquals("snapshot", memento.getContent());
        assertEquals(time, memento.getTimestamp());
        assertTrue(message.matchesMemento(memento));
    }

    @Test
    void rejectsInvalidConstruction() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(null, List.of(bob), "x"));
        assertThrows(IllegalArgumentException.class,
                () -> new Message(alice, List.of(), "x"));
        assertThrows(IllegalArgumentException.class,
                () -> new Message(alice, List.of(bob), null));
        assertThrows(IllegalArgumentException.class,
                () -> new Message(alice, List.of(bob), "x", null));
    }
}
