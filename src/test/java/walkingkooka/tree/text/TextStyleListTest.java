package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;

public final class TextStyleListTest implements ImmutableListTesting<TextStyleList, TextStyle>,
    HasTextStyleTesting {
    
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

    // class............................................................................................................

    @Override
    public Class<TextStyleList> type() {
        return TextStyleList.class;
    }
}
