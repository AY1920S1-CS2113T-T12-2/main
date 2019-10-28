package duke.storage;

import duke.Main;
import duke.commons.LogsCenter;
import duke.exception.DukeException;
import duke.model.Budget;
import duke.model.ExpenseList;
import duke.model.payment.PaymentList;
import duke.storage.payment.PaymentListStorage;
import duke.model.IncomeList;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);

    private ExpenseListStorage expenseListStorage;
    private PlanAttributesStorage planAttributesStorage;
    private IncomeListStorage incomeListStorage;
    private BudgetStorage budgetStorage;
    private PaymentListStorage paymentListStorage;

    /**
     * Constructor for StorageManager
     *
     * @param expenseListStorage    Storage for expense List
     * @param planAttributesStorage Storage for PlanAttributes from PlanBot
     * @param incomeListStorage     Storage for income
     * @param budgetStorage         Storage for budget
     * @param paymentListStorage    Storage for payment
     */
    public StorageManager(ExpenseListStorage expenseListStorage,
                          PlanAttributesStorage planAttributesStorage,
                          IncomeListStorage incomeListStorage,
                          BudgetStorage budgetStorage,
                          PaymentListStorage paymentListStorage) {

        this.expenseListStorage = expenseListStorage;
        this.planAttributesStorage = planAttributesStorage;
        this.incomeListStorage = incomeListStorage;
        this.budgetStorage = budgetStorage;
        this.paymentListStorage = paymentListStorage;
    }

    @Override
    public void saveExpenseList(ExpenseList expenseList) throws DukeException {
        expenseListStorage.saveExpenseList(expenseList);
    }

    @Override
    public ExpenseList loadExpenseList() throws DukeException {
        return expenseListStorage.loadExpenseList();
    }

    @Override
    public void savePlanAttributes(Map<String, String> attributes) throws DukeException {
        planAttributesStorage.savePlanAttributes(attributes);
    }

    @Override
    public Map<String, String> loadPlanAttributes() {
        return planAttributesStorage.loadAttributes();
    }

    @Override
    public void saveIncomeList(IncomeList incomeList) throws DukeException {
        incomeListStorage.saveIncomeList(incomeList);
    }

    @Override
    public IncomeList loadIncomeList() throws DukeException {
        return incomeListStorage.loadIncomeList();
    }

    public Budget loadBudget() throws IOException, DukeException {
        return budgetStorage.loadBudget();
    }

    @Override
    public void saveBudget(Budget budget) throws DukeException {
        budgetStorage.saveBudget(budget);
    }

    @Override
    public Optional<PaymentList> loadPaymentList() throws DukeException {
        logger.info("start loading paymentList");
        return paymentListStorage.readPaymentList();
    }

    @Override
    public void savePaymentList(PaymentList paymentList) throws IOException {
        paymentListStorage.savePaymentList(paymentList);
    }

}
