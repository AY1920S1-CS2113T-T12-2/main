package duke.model;

import duke.exception.DukeException;
import duke.model.payment.Payment;
import duke.model.payment.PaymentList;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The API of the Model component.
 */
public interface Model {

    //******************************** ExpenseList operations

    public void addExpense(Expense expense);

    public void deleteExpense(int index) throws DukeException;

    public void clearExpense();

    public void filterExpense(String filterCriteria) throws DukeException;

    public void sortExpense(String sortCriteria) throws DukeException;

    public void viewExpense(String viewScope, int previous) throws DukeException;

    public ObservableList<Expense> getExpenseExternalList();

    public ExpenseList getExpenseList();

    public StringProperty getExpenseListTotalString();

    public StringProperty getSortCriteriaString();

    public StringProperty getViewCriteriaString();

    public StringProperty getFilterCriteriaString();

    public String getMonthlyBudgetString();

    public void setMonthlyBudget(BigDecimal monthlyBudget);

    public void setCategoryBudget(String category, BigDecimal budgetBD);

    public BigDecimal getRemaining(BigDecimal total);

    public Map<String, BigDecimal> getBudgetCategory();

    public Budget getBudget();

    public  ObservableList<String> getBudgetObservableList();
    //******************************** Operations for other data....
    //******************************** For example, operations of monthly income list.
    // todo: add other data operations

    //PlanBot
    public ObservableList<PlanBot.PlanDialog> getDialogObservableList();
    public void processPlanInput(String input) throws DukeException;
    public Map<String, String> getKnownPlanAttributes();
    public PlanQuestionBank.PlanRecommendation getRecommendedBudgetPlan();


    //************************************************************
    // Pending Payments operations

    public void addPayment(Payment payment);

    public void setPayment(int index, Payment editedPayment) throws DukeException;

    public void removePayment(int index) throws DukeException;

    public void setPaymentSortCriteria(String sortCriteria) throws DukeException;

    public void setAllPredicate();

    public void setMonthPredicate();

    public void setWeekPredicate();

    public void setOutOfDatePredicate();

    public void setSearchKeyword(String keyword);

    public Payment getPayment(int index) throws DukeException;

    public FilteredList<Payment> getFilteredPaymentList();

    public FilteredList<Payment> getSearchResult();

    public PaymentList getPaymentList();

    //******************************** IncomeList operations

    public void addIncome(Income income);

    public void deleteIncome(int index) throws DukeException;

    public void clearIncome();

    public void filterIncome(String filterCriteria) throws DukeException;

    public void sortIncome(String sortCriteria) throws DukeException;

    public void viewIncome(String viewScope, int previous) throws DukeException;

    public ObservableList<Income> getIncomeExternalList();

    public IncomeList getIncomeList();

}
