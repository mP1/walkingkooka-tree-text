package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.HasCsvStringListTesting;
import walkingkooka.collect.list.HasTsvStringListTesting;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleListTest implements ImmutableListTesting<TextStyleList, TextStyle>,
    HasTextStyleTesting,
    HasCsvStringListTesting,
    HasTsvStringListTesting {

    // fromStringLikeList................................................................................................

    @Test
    public void testFromStringLikeListWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStyleList.fromStringLikeList(null)
        );
    }

    @Test
    public void testFromStringLikeListWithEmpty() {
        this.fromStringLikeListAndCheck(
            CsvStringList.EMPTY,
            TextStyleList.EMPTY
        );
    }

    @Test
    public void testFromStringLikeListWithNotEmpty() {
        this.fromStringLikeListAndCheck(
            CsvStringList.EMPTY.concat(TEXT_STYLE.text())
                .concat(DIFFERENT_TEXT_STYLE.text()),
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(DIFFERENT_TEXT_STYLE)
        );
    }

    @Test
    public void testFromStringLikeListWithNotEmptyAndEmptyTextStyle() {
        this.fromStringLikeListAndCheck(
            CsvStringList.EMPTY.concat(TEXT_STYLE.text())
                .concat("")
                .concat(DIFFERENT_TEXT_STYLE.text()),
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(TextStyle.EMPTY)
                .concat(DIFFERENT_TEXT_STYLE)
        );
    }

    private void fromStringLikeListAndCheck(final CsvStringList csvStringList,
                                            final TextStyleList expected) {
        this.checkEquals(
            expected,
            TextStyleList.fromStringLikeList(csvStringList)
        );
    }
    
    // concat...........................................................................................................

    @Test
    public void testConcatWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStyleList.EMPTY.concat(null)
        );
    }

    // setElements......................................................................................................

    @Test
    public void testSetElementsWithNullElementFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStyleList.EMPTY.setElements(
                Arrays.asList((TextStyle)null)
            )
        );
    }

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
            "\"color: black\",\"color: white\""
        );
    }

    @Test
    public void testCsvStringListWithNotEmptyIncludingEmptyTextStyle() {
        this.csvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(TextStyle.EMPTY)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color: black\",,\"color: white\""
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
            "\"color: black\"\t\"color: white\""
        );
    }

    @Test
    public void testTsvStringListWithNotEmptyIncludingEmptyTextStyle() {
        this.tsvStringListAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(TextStyle.EMPTY)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color: black\"\t\t\"color: white\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleList> type() {
        return TextStyleList.class;
    }
}
