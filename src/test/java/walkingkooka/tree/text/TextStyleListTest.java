package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.HasCsvStringListTesting;
import walkingkooka.collect.list.HasTsvStringListTesting;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleListTest implements ImmutableListTesting<TextStyleList, TextStyle>,
    HasTextStyleTesting,
    HasCsvStringListTesting,
    HasTsvStringListTesting {

    // fromCsvStringList................................................................................................

    @Test
    public void testFromCsvStringListWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStyleList.fromCsvStringList(null)
        );
    }

    @Test
    public void testFromCsvListWithEmpty() {
        this.fromCsvStringListAndCheck(
            CsvStringList.EMPTY,
            TextStyleList.EMPTY
        );
    }

    @Test
    public void testFromCsvListWithNotEmpty() {
        this.fromCsvStringListAndCheck(
            CsvStringList.EMPTY.concat(TEXT_STYLE.csvStringList().text())
                .concat(DIFFERENT_TEXT_STYLE.csvStringList().text()),
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(DIFFERENT_TEXT_STYLE)
        );
    }

    @Test
    public void testFromCsvListWithNotEmptyAndNull() {
        this.fromCsvStringListAndCheck(
            CsvStringList.EMPTY.concat(TEXT_STYLE.csvStringList().text())
                .concat("")
                .concat(DIFFERENT_TEXT_STYLE.csvStringList().text()),
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(TextStyle.EMPTY)
                .concat(DIFFERENT_TEXT_STYLE)
        );
    }

    private void fromCsvStringListAndCheck(final CsvStringList csvStringList,
                                           final TextStyleList expected) {
        this.checkEquals(
            expected,
            TextStyleList.fromCsvStringList(csvStringList)
        );
    }

    // setElements......................................................................................................

    @Test
    public void testSetElementsWithDifferent() {
        this.setElementsAndCheck(
            new TextStyleList(
                Lists.of(TEXT_STYLE)
            ),
            Lists.of(DIFFERENT_TEXT_STYLE),
            new TextStyleList(
                Lists.of(DIFFERENT_TEXT_STYLE)
            )
        );
    }

    @Test
    public void testSetElementsWithDifferentEmpty() {
        this.setElementsAndCheck(
            new TextStyleList(
                Lists.of(TEXT_STYLE)
            ),
            Lists.empty(),
            TextStyleList.EMPTY
        );
    }

    @Override
    public TextStyleList createList() {
        return TextStyleList.EMPTY;
    }

    // HasCsvStringList.................................................................................................

    @Test
    public void testCsvStringListWithEmpty() {
        this.csvStringListAndCheck(
            TextStyleList.EMPTY,
            ""
        );
    }

    @Test
    public void testCsvStringListWithNotEmpty() {
        this.csvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color,black\",\"color,white\""
        );
    }

    @Test
    public void testCsvStringListWithNotEmptyIncludingNull() {
        this.csvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(null)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color,black\",,\"color,white\""
        );
    }

    // HasTsvStringList.................................................................................................

    @Test
    public void testTsvStringListWithEmpty() {
        this.tsvStringListAndCheck(
            TextStyleList.EMPTY,
            ""
        );
    }

    @Test
    public void testTsvStringListWithNotEmpty() {
        this.tsvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color\tblack\"\t\"color\twhite\""
        );
    }

    @Test
    public void testTsvStringListWithNotEmptyIncludingNull() {
        this.tsvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(null)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color\tblack\"\t\t\"color\twhite\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleList> type() {
        return TextStyleList.class;
    }
}
