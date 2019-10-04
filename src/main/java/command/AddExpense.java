package command;


import dukeobjects.Expense;
import dukeobjects.ExpenseList;
import exception.DukeException;
import parser.CommandParams;
import storage.Storage;
import ui.Ui;

/**
 * Represents a specified command as AddCommand by extending the {@code Command} class.
 * Adds various specified type of expensesList into the ExpenseList. e.g event
 * Responses with the result.
 */
public class AddExpense extends Command {
    /**
     * Creates a new command object, with its name, description, usage and secondary parameters.
     */
    public AddExpense() {
        super(null, null, null, null);
    }

    @Override
    public void execute(CommandParams commandParams, ExpenseList expensesList, Ui ui, Storage storage) {
        String expenseAmount = commandParams.getMainParam();
        if (expenseAmount == null) {
            throw new DukeException("Empty amount!");
        }

        String expenseDescription = commandParams.getParam("d");
        if (expenseDescription == null) {
            throw new DukeException("Empty description!");
        }
        Expense expense = new Expense(expenseAmount, expenseDescription);
        expensesList.add(expense);
        ui.println("Ok! Added " + expense.toString());
    }

}
