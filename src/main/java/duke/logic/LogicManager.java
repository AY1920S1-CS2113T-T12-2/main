package duke.logic;

import duke.exception.DukeException;
import duke.logic.command.Command;
import duke.model.Expense;
import duke.model.Income;
import duke.model.Model;
import duke.model.PlanBot;
import duke.model.payment.Payment;
import duke.model.payment.PaymentList;
import duke.storage.Storage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {

    private Model model;
    private Storage storage;

    /**
     * Constructs a {@code LogicManager} with model and storage.
     *
     * @param model model of Duke++.
     * @param storage storage of Duke++.
     */
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

    @Override
    public BigDecimal getMonthlyBudget() {
        return model.getMonthlyBudget();
    }

    @Override
    public BigDecimal getTotalAmount() {
        return model.getTotalAmount();
    }

    @Override
    public BigDecimal getRemaining(BigDecimal total) {
        return model.getRemaining(total);
    }

    @Override
    public Map<Integer, String> getBudgetViewCategory() {
        return model.getBudgetViewCategory();
    }

    @Override
    public BigDecimal getBudgetTag(String category) {
        return model.getBudgetTag(category);
    }

    public ObservableList<Payment> getUnmodifiableFilteredPaymentList() {
        return model.getUnmodifiableFilteredPaymentList();
    }

    @Override
    public ObjectProperty<PaymentList.SortingCriteria> getPaymentSortingCriteria() {
        return model.getPaymentSortingCriteria();
    }

    @Override
    public ObjectProperty<Predicate> getPaymentPredicate() {
        return model.getPaymentPredicate();
    }

    @Override
    public StringProperty getExpenseListTotalString() {
        return model.getExpenseListTotalString();
    }

    @Override
    public StringProperty getSortCriteriaString() {
        return model.getSortCriteriaString();
    }

    @Override
    public StringProperty getViewCriteriaString() {
        return model.getViewCriteriaString();
    }

    @Override
    public StringProperty getFilterCriteriaString() {
        return model.getFilterCriteriaString();
    }

}
