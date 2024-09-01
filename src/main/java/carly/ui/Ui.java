package carly.ui;

import carly.exception.CarlyException;

import java.util.Scanner;

public class Ui {
    private final String username;

    public Ui(String username) {
        this.username = username;
    }

    /** Displays a welcome message to the user with the current username. */
    public void welcomeMsg() {
        System.out.println("Hey " + username + "! I'm Carly.\nWhat can I do for you?");
    }

    /** Displays a farewell message to the user with the current username. */
    public void byeMsg() {
        System.out.println("Bye " + username + ". I'll see you next time!");
    }

    public String ReadInput(Scanner scan) throws CarlyException {
        if (scan.hasNextLine()) {
            return scan.nextLine();
        } else {
            throw new CarlyException("");
        }
    }
}

