package chatapp;

import java.util.Iterator;

public class ChatAppDriver {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        alice.setChatServer(server);
        bob.setChatServer(server);
        charlie.setChatServer(server);

        System.out.println("=== Sending messages through the mediator ===");
        alice.sendMessage("Hey Bob, want to grab lunch?", bob);
        bob.sendMessage("Sure Alice! Noon works for me.", alice);
        alice.sendMessage("Team update: demo is Friday.", bob, charlie);
        charlie.sendMessage("Thanks Alice, I'll prepare slides.", alice);
        bob.sendMessage("Charlie, can you review my PR?", charlie);

        System.out.println("\n=== Chat histories after sending ===");
        alice.printChatHistory();
        System.out.println();
        bob.printChatHistory();
        System.out.println();
        charlie.printChatHistory();

        System.out.println("\n=== Alice undoes her last message (team update) ===");
        alice.undoLastMessage();
        alice.printChatHistory();
        System.out.println();
        bob.printChatHistory();
        System.out.println();
        charlie.printChatHistory();

        System.out.println("\n=== Charlie blocks Bob, then Bob messages Charlie ===");
        charlie.blockUser(bob);
        bob.sendMessage("Charlie, did you see my PR comment?", charlie);
        charlie.sendMessage("Alice, Bob is blocked on my side.", alice);

        System.out.println("\nCharlie's history (Bob's blocked message should be missing):");
        charlie.printChatHistory();
        System.out.println("\nBob still has his outgoing message:");
        bob.printChatHistory();

        System.out.println("\n=== Iterate Alice's history for messages involving Bob ===");
        Iterator<Message> withBob = alice.iterator(bob);
        while (withBob.hasNext()) {
            System.out.println(withBob.next());
        }

        System.out.println("\n=== Iterate Charlie's history for messages involving Alice ===");
        Iterator<Message> withAlice = charlie.iterator(alice);
        while (withAlice.hasNext()) {
            System.out.println(withAlice.next());
        }
    }
}
