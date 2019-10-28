package duke.logic.util;

import duke.commons.LogsCenter;
import duke.logic.command.AddExpenseCommand;
import duke.logic.command.Command;
import duke.logic.command.ConfirmTentativeCommand;
import duke.logic.command.DeleteExpenseCommand;
import duke.logic.command.ExitCommand;
import duke.logic.command.FilterExpenseCommand;
import duke.logic.command.GoToCommand;
import duke.logic.command.PlanBotCommand;
import duke.logic.command.SortExpenseCommand;
import duke.logic.command.ViewExpenseCommand;
import duke.logic.command.payment.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a auto-complete to what the user has typed in userInput when TAB key is pressed.
 * It can complete a commandName or iterate through all suitable commandNames.
 * It can also produce a parameter name, complete a parameter name and iterate through all suitable parameter names.
 * If the given input ends with space, autoCompleter will produce a parameter name.
 * If the given input doesn't end with space and is different from last complement,
 * autoCompleter will complete the last token, which can be a commandName or a parameter.
 * Instead, if the given input is same as last complement,
 * autoCompleter will iterate through other suitable complements in the list.
 *
 * The mechanism of complement and iteration is firstly getting a suitable token(complement),
 * which can be either a complete commandName or parameter, or just original last token from input
 * in case that no suitable complements can be applied to current input.
 * Then auto-completer replaces the last token of original input with this suitable token,
 * and lastly returns the modified input to userInput TextField.
 */
public class AutoCompleter {

    private static final Logger logger = LogsCenter.getLogger(AutoCompleter.class);

    /** Three keywords used to decide the purpose of complement **/
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    private static final String PARAMETER_INDICATOR = "/";

    /** The list storing all commands' names **/
    private final List<String> allCommandNames;

    /** CommandNames mapped to their respective secondaryParams' names **/
    private final HashMap<String, List<String>> allSecondaryParams;

    /** The most recent complement provided by auto-completer **/
    private String lastComplement;

    /** The purpose of complement **/
    private Purpose purpose;

    /** The content inside the userInput TextField when TAB key is pressed **/
    private String fromInput;

    /** Number of tokens of {@code fromInput} **/
    private int numberOfTokens;

    /** The starting index of the last token in {@code fromInput} **/
    private int startIndexOfLastToken;

    /** All suitable tokens that can replace the last token of {@code fromInput} **/
    private List<String> complementList;

    /** The index used to iterate through {@code complementList} **/
    private int iteratingIndex;

    /** A supplier that supplies streams of all command classes **/
    private static final Supplier<Stream<Command>> COMMANDS = () -> Stream.of(
            new AddExpenseCommand(),
            new DeleteExpenseCommand(),
            new ConfirmTentativeCommand(),
            new ExitCommand(),
            new FilterExpenseCommand(),
            new SortExpenseCommand(),
            new ViewExpenseCommand(),
            new GoToCommand(),
            new PlanBotCommand(),
            new AddPaymentCommand(),
            new ChangePaymentCommand(),
            new DeletePaymentCommand(),
            new FilterPaymentCommand(),
            new SearchPaymentCommand(),
            new SortPaymentCommand()
    );

    /**
     * Purposes of complement.
     */
    private enum Purpose {
        COMPLETE_COMMAND_NAME,
        PRODUCE_PARAMETER,
        COMPLETE_PARAMETER,
        ITERATE,
        NOT_DOABLE;
    }

    /**
     * Constructs a auto-completer.
     * All commandNames are stored in {@code allCommandNames}.
     * All secondaryParams of each command are stored in {@code allSecondaryParams}.
     * The {@code complementList} is initialized as an empty ArrayList.
     * The {@code lastComplement} is initialized as an empty String.
     */
    public AutoCompleter() {
        allCommandNames = COMMANDS.get().map(Command::getName).collect(Collectors.toList());

        allSecondaryParams = new HashMap<String, List<String>>();
        COMMANDS.get().forEach(c -> {
            Set<String> secondaryParamSet = c.getSecondaryParams().keySet();
            List<String> secondaryParamList = new ArrayList<String>(secondaryParamSet);
            allSecondaryParams.put(c.getName(), secondaryParamList);
        });

        complementList = new ArrayList<String>();
        lastComplement = EMPTY_STRING;
    }

    /**
     * Receives the content in userInput TextField when TAB key is pressed.
     * Stores it in {@code fromInput}.
     *
     * @param fromInput The content in userInput TextField.
     */
    public void receiveText(String fromInput) {
        this.fromInput = fromInput;
        logger.info("start receiving Text");
        getPurpose();
    }

    /**
     * Replaces the last token of {@code fromInput} with complement token
     * and stores this modified String in {@code lastComplement}.
     * This modified String is full complement and will be set as text in userInput TextField.
     *
     * i.e full complement = fromInput - last token of fromInput + complement token.
     *
     * @return Full complement going to be set in userInput TextField
     */
    public String getFullComplement() {
        String complement = getComplement();
        lastComplement = fromInput.substring(0, startIndexOfLastToken) + complement;
        return lastComplement;
    }

    /**
     * Tests whether the given input is same as the most recent complement
     * This is a criteria to help decide purpose.
     *
     * @return True if {@code fromInput} is same as {@code lastComplement} and false otherwise
     */
    private boolean isSameAsLastComplement() {
        return fromInput.equals(lastComplement);
    }

    /**
     * Tests whether the given input is empty.
     * This is a criteria to help decide purpose.
     *
     * @return True if {@code fromInput} is an empty String and false otherwise.
     */
    private boolean isEmpty() {
        return fromInput.trim().equals(EMPTY_STRING);
    }

    /**
     * Tests whether the given input ends with a space.
     * This is a criteria to help decide purpose.
     *
     * @return True if {@code fromInput} ends with space and false otherwise.
     */
    private boolean endsWithSpace() {
        return fromInput.endsWith(SPACE);
    }

    /**
     * Tests whether the last token of given input starts with {@code PARAMETER_INDICATOR}.
     * This is a criteria to help decide purpose.
     *
     * @return True if the last token starts with {@code PARAMETER_INDICATOR} and false otherwise.
     */
    private boolean inUncompletedParameter() {
        return getLastToken().startsWith(PARAMETER_INDICATOR);
    }

    /**
     * Gets the first token of the given input.
     * Updates {@code numberOfTokens} after the given input is tokenized.
     *
     * @return The first token of {@code fromInput}
     */
    private String getCommandName() {
        List<String> tokens = Arrays.asList(fromInput.split(SPACE));
        tokens = tokens.stream().filter(s -> !s.equals(EMPTY_STRING)).collect(Collectors.toList());
        tokens = tokens.stream().map(String::trim).collect(Collectors.toList());
        numberOfTokens = tokens.size();

        return tokens.get(0);
    }

    /**
     * Tests whether the first token of given input is a valid commandName.
     * This is a criteria to help decide purpose.
     *
     * @return True if the first token of {@code fromInput} is a valid commandName and false otherwise.
     */
    private boolean hasValidCommandName() {
        return allCommandNames.contains(getCommandName());
    }

    /**
     * Gets the last token of given input.
     * Updates {@code startIndexOfLastToken}.
     *
     * @return The last token of {@code fromInput}.
     */
    private String getLastToken() {
        int index = fromInput.length() - 1;
        while (index >= 0 && SPACE.charAt(0) != fromInput.charAt(index)) {
            index -= 1;
        }
        startIndexOfLastToken = index + 1;
        return fromInput.substring(startIndexOfLastToken);
    }

    /**
     * Decides the {@code purpose} with values of various criteria.
     */
    private void getPurpose() {
        if (isEmpty()) {
            purpose = Purpose.NOT_DOABLE;

        } else if (isSameAsLastComplement()) {
            purpose = Purpose.ITERATE;

        } else if (!hasValidCommandName()) {
            if (numberOfTokens > 1 || endsWithSpace()) {
                purpose = Purpose.NOT_DOABLE;

            } else {
                purpose = Purpose.COMPLETE_COMMAND_NAME;

            }

        } else if (endsWithSpace()) {
            purpose = Purpose.PRODUCE_PARAMETER;

        } else if (inUncompletedParameter()) {
            purpose = Purpose.COMPLETE_PARAMETER;

        } else if (numberOfTokens == 1) {
            purpose = Purpose.COMPLETE_COMMAND_NAME;

        } else {
            purpose = Purpose.NOT_DOABLE;

        }
        logger.info("purpose decided.");
    }

    /**
     * Fills the {@code complementList} with all complete versions of current commandName.
     * This is called when {@code purpose} is {@code COMPLETE_COMMAND_NAME}.
     */
    private void completeCommandNameComplements() {
        String unCompletedCommandName = getLastToken();
        complementList = allCommandNames.stream()
                .filter(s -> s.startsWith(unCompletedCommandName)).collect(Collectors.toList());
    }

    /**
     * Fills the {@code complementList} with all complete versions of current secondaryParameter.
     * This is called when {@code purpose} is {@code COMPLETE_PARAMETER}.
     */
    private void completeParameterComplements() {
        String unCompletedParameter = getLastToken().substring(1);
        List<String> usableParameters = allSecondaryParams.get(getCommandName());
        List<String> options = usableParameters.stream()
                .filter(s -> s.startsWith(unCompletedParameter)).collect(Collectors.toList());
        logger.info("options for complementList lengths " + options.size());
        complementList = options.stream().map(s -> PARAMETER_INDICATOR + s).collect(Collectors.toList());
    }

    /**
     * Fills the {@code complementList} with all secondaryParameters belonging to commandName.
     * This is called when {@code purpose} is {@code PRODUCE_PARAMETER}.
     */
    private void produceParameterComplements() {
        String empty = getLastToken(); // updates the starting index of last token.
        List<String> options = allSecondaryParams.get(getCommandName());
        complementList = options.stream().map(s -> PARAMETER_INDICATOR + s).collect(Collectors.toList());
    }

    /**
     * Iterates the index of {@code complementList} by adding it by one.
     * This is called when {@code purpose} is {@code ITERATE}.
     */
    private void iterateIndex() {
        iteratingIndex += 1;
        if(iteratingIndex >= complementList.size()) {
            iteratingIndex = 0;
        }
    }

    /**
     * Gets the complement token.
     * If the {@code purpose} is not {@code ITERATE},
     * it firstly generates {@code complementList} containing all suitable complements(tokens)
     * and chooses the first element of list as complement token.
     * If the {@code purpose} is {@code ITERATE},
     * it adds the {@code iteratingIndex} by one and chooses the corresponding element
     * in existing {@code complementList} as complement token.
     *
     * @return The complement token
     */
    private String getComplement() {
        switch (purpose) {
        case COMPLETE_COMMAND_NAME:
            completeCommandNameComplements();
            iteratingIndex = 0;
            break;

        case PRODUCE_PARAMETER:
            produceParameterComplements();
            iteratingIndex = 0;
            break;

        case COMPLETE_PARAMETER:
            completeParameterComplements();
            iteratingIndex = 0;
            break;

        case ITERATE:
            iterateIndex();
            break;

        case NOT_DOABLE:
            complementList.clear();
            break;
        }

        if (complementList.isEmpty()) {
            return getLastToken();
        } else {
            return complementList.get(iteratingIndex);
        }
    }

}
