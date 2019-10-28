package duke.logic.command.payment;

import duke.exception.DukeException;
import duke.logic.CommandParams;
import duke.logic.CommandResult;
import duke.logic.command.Command;
import duke.model.Model;
import duke.storage.Storage;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortPaymentCommand extends Command {

    private static final String name = "sortPayment";
    private static final String description = "Sort Payments with given criteria";
    private static final String usage = "sortPayment $sortCriteria";

    private static final String COMPLETE_MESSAGE = "Payments are sorted!";

    private enum SecondaryParam {
        ;

        private String name;
        private String description;

        SecondaryParam(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    /**
     * Constructs a {@code SortPaymentCommand} object
     * given the sort criteria of payments.
     * Sort criteria include time, amount and priority.
     */
    public SortPaymentCommand() {
        super(name, description, usage, Stream.of(SecondaryParam.values())
                .collect(Collectors.toMap(s -> s.name, s -> s.description)));
    }

    @Override
    public CommandResult execute(CommandParams commandParams, Model model, Storage storage) throws DukeException {
        if (!commandParams.containsMainParam()) {
            throw new DukeException(String.format(DukeException.MESSAGE_COMMAND_PARAM_MISSING, "sortCriteria"));
        }

        model.setPaymentSortCriteria(commandParams.getMainParam());

        return new CommandResult(COMPLETE_MESSAGE, CommandResult.DisplayedPane.PAYMENT);
    }

}
