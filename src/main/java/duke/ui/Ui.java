package duke.ui;

import duke.list.Expense;
import duke.list.ExpenseList;
import duke.exception.DukeException;


import java.util.Scanner;

/**
 * Represents the User Interface of duke.Duke, and
 * manages both input and output operations.
 */
public class Ui {
    private Scanner dukeIn;

    /**
     * Constructs an Ui object.
     */
    public Ui() {
        dukeIn = new Scanner(System.in);
    }

    /**
     * Shows welcome message to the user when duke.Duke starts.
     */
    public void showWelcome() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("I am duke.Duke. What can I do for you?");
    }

    /**
     * Reads one line of user's commands.
     *
     * @return User's duke.command in {@code String} type.
     */
    public String readCommand() {
        return dukeIn.nextLine();
    }

    /**
     * Replaces the {@code System.out.println} method.
     *
     * @param s The string to be printed.
     */
    public void println(String s) {
        System.out.println(s);
    }

    /**
     * Prints the message of the duke.exception.
     *
     * @param e the {@code DukeException} whose message will be printed.
     */
    public void showError(DukeException e) {
        System.out.println(e.getMessage());
    }

    /**
     * Prints the {@code ExpenseList} given.
     *
     * @param ExpenseList {@code ExpenseList} that we want to be printed
     */
    public void printExpenseList(ExpenseList ExpenseList) {
        if (ExpenseList.getSize() > 0) {
            int count = 1;
            for (Expense expense : ExpenseList.getExpenseList()) {
                println(expense.toString());
                count++;
            }
        }
    }
}