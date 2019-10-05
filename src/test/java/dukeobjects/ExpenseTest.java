package dukeobjects;

import exception.DukeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ExpenseTest {
    private static final double DEFAULT_AMOUNT = 0;
    private static final String DEFAULT_DESCRIPTION = "";
    private static final boolean DEFAULT_TENTATIVE = false;

    private static final double TEST_AMOUNT = 1.23;
    private static final String TEST_DESCRIPTION = "test description";
    private static final boolean TEST_TENTATIVE = true;
    private static final String[] TEST_TAGS = {"tag1", "tag2", "tag3"};
    private static final String[] TEST_FLIP_TAGS = {"tag1", "tag2", "tag4"};
    private static final String[] TEST_FLIPPED_TAGS = {"tag3", "tag4"};

    private static final String INVALID_STORAGE_STRING = "tags:tag1 tag2 tag3\n"
            + "amount:1.23\n"
            + "d:1\n"
            + "t:2";

    private static final String ACTUAL_TO_STRING = "$1.23 test description (tentative) Tags: tag1 tag2 tag3";
    private static final String ACTUAL_TO_STORAGE_STRING = "tags:tag1 tag2 tag3\n"
            + "amount:1.23\n"
            + "description:test description\n"
            + "isTentative:true";

    @Test
    public void testDefaults() {
        Expense testExpense = new Expense.Builder().build();
        assertEquals(testExpense.getAmount(), DEFAULT_AMOUNT);
        assertEquals(testExpense.getDescription(), DEFAULT_DESCRIPTION);
        assertEquals(testExpense.isTentative(), DEFAULT_TENTATIVE);
        assertTrue(testExpense.getTags().isEmpty());
    }

    @Test
    public void testBuilderFromExpense() {
        Expense testExpense = new Expense.Builder()
                .setAmount(TEST_AMOUNT)
                .setDescription(TEST_DESCRIPTION)
                .setTentative(TEST_TENTATIVE)
                .invertTags(TEST_TAGS)
                .build();
        Expense testExpenseTwo = new Expense.Builder(testExpense).build();
        assertEquals(testExpense.getAmount(), testExpenseTwo.getAmount());
        assertEquals(testExpense.getDescription(), testExpenseTwo.getDescription());
        assertEquals(testExpense.getTags(), testExpenseTwo.getTags());
        assertEquals(testExpense.getAmount(), testExpenseTwo.getAmount());
    }

    @Test
    public void testAmount() {
        Expense testExpense = new Expense.Builder().setAmount(TEST_AMOUNT).build();
        assertEquals(testExpense.getAmount(), TEST_AMOUNT);
    }

    @Test
    public void testDescription() {
        Expense testExpense = new Expense.Builder().setDescription(TEST_DESCRIPTION).build();
        assertEquals(testExpense.getDescription(), TEST_DESCRIPTION);
    }

    @Test
    public void testIsTentative() {
        Expense testExpense = new Expense.Builder().setTentative(TEST_TENTATIVE).build();
        assertEquals(testExpense.isTentative(), TEST_TENTATIVE);
    }

    @Test
    public void testTags() {
        Expense testExpense = new Expense.Builder().invertTags(TEST_TAGS).build();
        assertEquals(testExpense.getTags(), Set.of(TEST_TAGS));
        testExpense = new Expense.Builder().invertTags(TEST_TAGS).invertTags(TEST_FLIP_TAGS).build();
        assertEquals(testExpense.getTags(), Set.of(TEST_FLIPPED_TAGS));
    }

    @Test
    public void testToString() {
        assertEquals(new Expense.Builder()
                        .setAmount(TEST_AMOUNT)
                        .setDescription(TEST_DESCRIPTION)
                        .setTentative(TEST_TENTATIVE)
                        .invertTags(TEST_TAGS)
                        .build()
                        .toString(),
                ACTUAL_TO_STRING);
    }

    @Test
    public void testToStorageString() {
        String storageString = new Expense.Builder()
                        .setAmount(TEST_AMOUNT)
                        .setDescription(TEST_DESCRIPTION)
                        .setTentative(TEST_TENTATIVE)
                        .invertTags(TEST_TAGS)
                        .build()
                        .toStorageString();
        assertEquals(storageString, ACTUAL_TO_STORAGE_STRING);
        Expense testExpense = new Expense.Builder(storageString).build();
        assertEquals(testExpense.getAmount(), TEST_AMOUNT);
        assertEquals(testExpense.getDescription(), TEST_DESCRIPTION);
        assertEquals(testExpense.isTentative(), TEST_TENTATIVE);
        assertEquals(testExpense.getTags(), Set.of(TEST_TAGS));
    }

    @Test
    public void testInvalidStorageString() {
        try {
            new Expense.Builder(INVALID_STORAGE_STRING);
            fail();
        } catch (DukeException e) {
            // Success
            // todo: I would check the message here if the message was constant
            e.printStackTrace();
        }
    }
}
