package duke;

import duke.commons.LogsCenter;
import duke.logic.Logic;
import duke.logic.LogicManager;
import duke.model.DukePP;
import duke.model.Model;
import duke.storage.BudgetStorage;
import duke.storage.ExpenseListStorage;
import duke.storage.ExpenseListStorageManager;
import duke.storage.PlanAttributesStorage;
import duke.storage.PlanAttributesStorageManager;
import duke.storage.Storage;
import duke.storage.StorageManager;
import duke.storage.payment.PaymentListStorage;
import duke.storage.payment.PaymentListStorageManager;
import duke.ui.Ui;
import duke.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * Bridge between duke and MainWindow.
 */
public class Main extends Application {

    private static final Logger logger = LogsCenter.getLogger(Main.class);

    private Ui ui;
    private Logic logic;
    private Model model;
    private Storage storage;

    @Override
    public void init() throws Exception {
        super.init();

        ExpenseListStorage expenseListStorage = new ExpenseListStorageManager();
        PlanAttributesStorage planAttributesStorage = new PlanAttributesStorageManager();
        BudgetStorage budgetStorage = new BudgetStorage();
        PaymentListStorage paymentListStorage = new PaymentListStorageManager();
        logger.info("PaymentListStorage is set");
        storage = new StorageManager(expenseListStorage, planAttributesStorage, budgetStorage, paymentListStorage);
        logger.info("Storage is set");
        model = new DukePP(storage.loadExpenseList(), storage.loadPlanAttributes(), storage.loadBudget(), storage.loadPaymentList());

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
        logger.info("Initialized the app");
    }


    /**
     * Starts Duke with MainWindow.
     *
     * @param primaryStage The main GUI of Duke
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        ui.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
