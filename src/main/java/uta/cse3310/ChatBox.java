package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

public class ChatBox {
    private List<String> localMessages;  // Messages specific to the user
    private List<String> globalMessages; // Messages visible to all users

    public ChatBox() {
        this.localMessages = new ArrayList<>();
        this.globalMessages = new ArrayList<>();
    }

    public void addLocalMessage(String message) {
        if (checkLanguage(message)) {
            localMessages.add(message);
        } else {
            System.out.println("Inappropriate language detected, message not added.");
        }
    }

    public void addGlobalMessage(String message) {
        if (checkLanguage(message)) {
            globalMessages.add(message);
        } else {
            System.out.println("Inappropriate language detected, message not added.");
        }
    }

    public List<String> getLocalMessages() {
        return localMessages;
    }

    public List<String> getGlobalMessages() {
        return globalMessages;
    }

    // Check if a message contains inappropriate language
    public boolean checkLanguage(String line) {
        // Example: Simple language filter
        String lowerCaseLine = line.toLowerCase();
        return !(lowerCaseLine.contains("badword1") || lowerCaseLine.contains("badword2"));
    }
}
