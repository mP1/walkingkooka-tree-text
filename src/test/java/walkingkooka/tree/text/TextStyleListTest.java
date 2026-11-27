/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.HasCsvStringListTsvStringListTesting2;
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallerTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleListTest implements ImmutableListTesting<TextStyleList, TextStyle>,
    HasTextStyleTesting,
    HasCsvStringListTsvStringListTesting2<TextStyleList>,
    JsonNodeMarshallerTesting<TextStyleList>,
    ThrowableTesting {

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
        this.getMessageAndCheck(
            assertThrows(
                NullPointerException.class,
                () -> TextStyleList.EMPTY.setElements(
                    Arrays.asList((TextStyle) null)
                )
            ),
            "Includes null TextStyle"
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

    // subList..........................................................................................................

    private final static TextStyle TEXT_STYLE1 = TEXT_STYLE;

    private final static TextStyle TEXT_STYLE2 = DIFFERENT_TEXT_STYLE;

    private final static TextStyle TEXT_STYLE3 = TextStyle.EMPTY.set(
        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
        Length.pixel(333.0)
    );

    private final static TextStyle TEXT_STYLE4 = TextStyle.EMPTY.set(
        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
        Length.pixel(444.0)
    );

    private final static TextStyle TEXT_STYLE5 = TextStyle.EMPTY.set(
        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
        Length.pixel(555.0)
    );

    private final static List<TextStyle> ELEMENTS = Lists.of(
        TEXT_STYLE1,
        TEXT_STYLE2,
        TEXT_STYLE3,
        TEXT_STYLE4,
        TEXT_STYLE5
    );

    @Test
    public void testSubList() {
        this.subListAndCheck(
            new TextStyleList(ELEMENTS),
            0,
            2,
            new TextStyleList(
                Lists.of(
                    TEXT_STYLE1,
                    TEXT_STYLE2
                )
            )
        );
    }

    @Test
    public void testSubList2() {
        this.subListAndCheck(
            new TextStyleList(ELEMENTS),
            2,
            4,
            new TextStyleList(
                Lists.of(
                    TEXT_STYLE3,
                    TEXT_STYLE4
                )
            )
        );
    }

    @Test
    public void testSubListSubList() {
        this.subListAndCheck(
            new TextStyleList(ELEMENTS)
                .subList(
                    1,
                    4
                ), // 2, 3, 4
            1,
            3,
            new TextStyleList(
                Lists.of(
                    TEXT_STYLE3,
                    TEXT_STYLE4
                )
            )
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

    @Override
    public TextStyleList createHasCsvStringListTsvStringList() {
        return this.createList();
    }

    // json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            TextStyleList.EMPTY.concat(TEXT_STYLE)
                .concat(DIFFERENT_TEXT_STYLE),
            "\"color: black,color: white\""
        );
    }

    @Override
    public TextStyleList unmarshall(final JsonNode jsonNode,
                                    final JsonNodeUnmarshallContext context) {
        return TextStyleList.unmarshall(
            jsonNode,
            context
        );
    }

    @Override
    public TextStyleList createJsonNodeMarshallingValue() {
        return this.createList();
    }
    
    // class............................................................................................................

    @Override
    public Class<TextStyleList> type() {
        return TextStyleList.class;
    }
}
