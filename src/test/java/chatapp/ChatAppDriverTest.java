package chatapp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatAppDriverTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void driverDemonstratesMediatorMementoAndIterator() {
        ChatAppDriver.main(new String[0]);
        String printed = output.toString();

        assertTrue(printed.contains("Sending messages through the mediator"));
        assertTrue(printed.contains("Alice undoes her last message"));
        assertTrue(printed.contains("Charlie blocks Bob"));
        assertTrue(printed.contains("Iterate Alice's history for messages involving Bob"));
        assertTrue(printed.contains("Alice -> Bob: Hey Bob, want to grab lunch?"));
        assertTrue(printed.contains("Charlie -> Alice: Alice, Bob is blocked on my side."));
        assertTrue(printed.contains("Bob -> Charlie: Charlie, can you review my PR?"));
    }
}
