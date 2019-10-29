package duke.logic.command;

import duke.exception.DukeException;
import duke.logic.CommandParams;
import duke.logic.CommandResult;
import duke.model.Expense;
import duke.model.Model;
import duke.storage.Storage;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfirmTentativeCommand extends Command {
    /**
     * Creates a new command object, with its name, description, usage and secondary parameters.
     *
     * @param name            the name of the command to create.
     * @param description     the description of the command to create.
     * @param usage           the usage of the command to create.
     * @param secondaryParams the secondary parameters of the command to create.
     */
    private static final String name = "confirm";
    private static final String description = "confirm a tentative Expense";
    private static final String usage = "confirms $index, if it is a tentative task";
    private static final String COMPLETE_MESSAGE = "Confirmed the tentative expense!";

    private enum SecondaryParam {
        ;

        private String name;
        private String description;

        SecondaryParam(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public ConfirmTentativeCommand() {
        super(name, description, usage, Stream.of(SecondaryParam.values())
                .collect(Collectors.toMap(s -> s.name, s -> s.description)));
    }


    @Override
    public CommandResult execute(CommandParams commandParams, Model model, Storage storage) throws DukeException {
        try {
            int index = Integer.parseInt(commandParams.getMainParam());
            Expense expense = model.getExpenseExternalList().get(index - 1);
            if (expense.isTentative()) {
                model.deleteExpense(index);
                expense.setTentative(false);
                model.addExpense(expense);
            } else {
                throw new DukeException(index + " is not a tentative task!");
            }
        } catch (NumberFormatException e) {
            throw new DukeException("Please enter a number!");
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("The index you've entered is out of range!");
        }
        return new CommandResult(COMPLETE_MESSAGE, CommandResult.DisplayedPane.EXPENSE);
    }

}
