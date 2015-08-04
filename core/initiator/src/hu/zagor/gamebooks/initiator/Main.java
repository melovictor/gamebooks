package hu.zagor.gamebooks.initiator;

import hu.zagor.gamebooks.initiator.book.CreateBookProject;

public class Main {

    public static void main(final String[] args) {
        final Console console = Console.getConsole();
        console.println("What would you like to create?");
        console.println("1: collector project");
        console.println("2: book project");

        // new CreateCollectorProject().create();
        new CreateBookProject().create();
    }
}
