package chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class SearchMessagesByUserTest {
    @Test
    void iteratesOnlyMessagesInvolvingGivenUser() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        Message withBob = new Message(alice, List.of(bob), "to bob");
        Message withCharlie = new Message(alice, List.of(charlie), "to charlie");
        Message fromBob = new Message(bob, List.of(alice), "from bob");

        SearchMessagesByUser iterator =
                new SearchMessagesByUser(List.of(withBob, withCharlie, fromBob), bob);

        assertTrue(iterator.hasNext());
        assertEquals(withBob, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(fromBob, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void rejectsNullArguments() {
        User bob = new User("Bob");
        assertThrows(IllegalArgumentException.class,
                () -> new SearchMessagesByUser(null, bob));
        assertThrows(IllegalArgumentException.class,
                () -> new SearchMessagesByUser(List.of(), null));
    }
}
