package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MessageMementoTest {
    @Test
    void storesContentAndTimestamp() {
        LocalDateTime time = LocalDateTime.of(2026, 1, 1, 12, 0);
        MessageMemento memento = new MessageMemento("undo me", time);

        assertEquals("undo me", memento.getContent());
        assertEquals(time, memento.getTimestamp());
    }

    @Test
    void equalsByContentAndTimestamp() {
        LocalDateTime time = LocalDateTime.of(2026, 1, 1, 12, 0);
        MessageMemento first = new MessageMemento("same", time);
        MessageMemento second = new MessageMemento("same", time);
        MessageMemento different = new MessageMemento("other", time);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, different);
    }

    @Test
    void rejectsNullFields() {
        LocalDateTime time = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () -> new MessageMemento(null, time));
        assertThrows(IllegalArgumentException.class, () -> new MessageMemento("x", null));
    }
}
