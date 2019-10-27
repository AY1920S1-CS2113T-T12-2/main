package duke.logic;

import duke.exception.DukeException;
import duke.logic.command.Command;
import duke.logic.command.PlanBotCommand;
import duke.model.*;
import duke.storage.Storage;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class LogicManager implements Logic {

    private Model model;
    private Storage storage;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(String userInput) throws DukeException {
        CommandResult commandResult;
        CommandParams commandParams = new CommandParams(userInput);
        Command command = commandParams.getCommand();
        commandResult = command.execute(commandParams, model, storage);

        return commandResult;
    }

    @Override
    public ObservableList<Expense> getExternalExpenseList() {
        return model.getExpenseExternalList();
    }

    @Override
    public ObservableList<PlanBot.PlanDialog> getDialogObservableList() {
        return model.getDialogObservableList();
    }
    @Override
    public BigDecimal getTagAmount(String tag) {
        return model.getExpenseList().getTagAmount(tag);
    }

    @Override
    public ObservableList<Income> getExternalIncomeList() {
        return model.getIncomeExternalList();
    }

    @Override
    public ObservableList<String> getBudgetObservableList() {
        return model.getBudgetObservableList();
    }

}
