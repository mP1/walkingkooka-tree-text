package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.HasCsvStringListTesting;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;

public final class TextStyleListTest implements ImmutableListTesting<TextStyleList, TextStyle>,
    HasTextStyleTesting,
    HasCsvStringListTesting {
    
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

    // class............................................................................................................

    @Override
    public Class<TextStyleList> type() {
        return TextStyleList.class;
    }
}
