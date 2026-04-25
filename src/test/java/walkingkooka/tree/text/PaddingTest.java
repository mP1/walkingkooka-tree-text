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
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PaddingTest extends BorderMarginPaddingTestCase<Padding> {

    // isEmpty..........................................................................................................

    @Test
    public void testIsEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
            Padding.parse("1px"),
            false
        );
    }

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            Padding.parse(""),
            true
        );
    }

    // getProperty......................................................................................................

    @Test
    public void testGetPropertyMissing() {
        this.getPropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_LEFT
        );
    }

    @Test
    public void testGetPropertyPresent() {
        this.getPropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_TOP,
            Length.pixel(1.0)
        );
    }
    
    // setProperty......................................................................................................

    @Test
    public void testSetPropertyWithSame() {
        this.setPropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_TOP,
            Length.pixel(1.0)
        );
    }

    @Test
    public void testSetPropertyWithDifferent() {
        this.setPropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_BOTTOM,
            Length.pixel(2.0),
            Padding.parse("top: 1px; bottom: 2.0px;")
        );
    }

    // removeProperty...................................................................................................

    @Test
    public void testRemovePropertyMissingProperty() {
        this.removePropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_BOTTOM
        );
    }

    @Test
    public void testRemovePropertyExisting() {
        this.removePropertyAndCheck(
            Padding.parse("top: 1px; bottom: 2px;"),
            TextStylePropertyName.PADDING_BOTTOM,
            Padding.parse("top: 1px; ")
        );
    }
    
    // toString.........................................................................................................

    @Test
    public void testToStringWithAll() {
        this.toStringAndCheck(
            TextStyle.parse("padding-top: 11px; padding-right: 22px; padding-bottom: 33px; padding-left: 44px;")
                .padding(BoxEdge.ALL),
            "padding ALL 11px 22px 33px 44px"
        );
    }

    @Test
    public void testToStringWithBottom() {
        this.toStringAndCheck(
            TextStyle.EMPTY.setValues(
                Maps.of(
                    TextStylePropertyName.PADDING_BOTTOM, Length.pixel(12.5),
                    TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
            ).padding(BoxEdge.BOTTOM),
            "padding BOTTOM 12.5px"
        );
    }

    // text.............................................................................................................

    @Test
    public void testTextAllEmpty() {
        this.textAndCheck(
            Padding.with(
                BoxEdge.ALL,
                TextStyle.EMPTY
            ),
            ""
        );
    }

    @Test
    public void testTextLeftEmpty() {
        this.textAndCheck(
            Padding.with(
                BoxEdge.LEFT,
                TextStyle.EMPTY
            ),
            ""
        );
    }

    @Test
    public void testTextAllAndSameValues() {
        final Length<?> length = Length.pixel(2.0);

        this.textAndCheck(
            Padding.with(
                BoxEdge.ALL,
                TextStyle.EMPTY.set(
                    TextStylePropertyName.PADDING_TOP,
                    length
                ).set(
                    TextStylePropertyName.PADDING_RIGHT,
                    length
                ).set(
                    TextStylePropertyName.PADDING_BOTTOM,
                    length
                ).set(
                    TextStylePropertyName.PADDING_LEFT,
                    length
                )
            ),
            "2px"
        );
    }

    @Test
    public void testTextAllAndDifferentValues() {
        final String text = "1px 2px 3px 4px";

        this.textAndCheck(
            Padding.parse(text),
            text
        );
    }

    @Test
    public void testTextAllAndDifferentValuesSomeWithoutUnits() {
        final String text = "1px 2px 3 4";

        this.textAndCheck(
            Padding.parse(text),
            text
        );
    }

    @Test
    public void testTextLeft() {
        final String text = "1px";

        this.textAndCheck(
            Padding.parse(text),
            text
        );
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidPaddingTextStylePropertyFails() {
        this.parseStringFails(
            "top-invalid: 1px;",
            new UnknownTextStylePropertyNameException("top-invalid")
        );
    }

    @Test
    public void testParseMarginFails() {
        this.parseStringFails(
            "top: 1px; margin-top: 2px",
            InvalidTextStylePropertyNameException.padding(TextStylePropertyName.MARGIN_TOP)
        );
    }

    @Test
    public void testParseMarginFails2() {
        this.parseStringInvalidCharacterFails(
            "1px 2px $3.5",
            '$'
        );
    }

    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            TextStyle.EMPTY.padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithPrefix() {
        final String text = "padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-right: 4px;";

        this.parseStringAndCheck(
            text,
            TextStyle.parse(text)
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithoutPrefix() {
        final String text = "padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-right: 4px;";

        this.parseStringAndCheck(
            text.replace("padding-", ""),
            TextStyle.parse(text)
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithAndWithoutPrefix() {
        this.parseStringAndCheck(
            "padding-top: 1px; padding-right: 2px; bottom: 3px; right: 4px;",
            TextStyle.parse("padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-right: 4px;")
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseLengths() {
        this.parseStringAndCheck(
            "1px 2px 3px 4px",
            TextStyle.parse("padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-left: 4px;")
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseLengthsNegativeValues() {
        this.parseStringAndCheck(
            "-1px -2px -3px -4px",
            TextStyle.parse("padding-top: -1px; padding-right: -2px; padding-bottom: -3px; padding-left: -4px;")
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseLengthsWithoutUnits() {
        this.parseStringAndCheck(
            "1 2.5 3 4.5",
            TextStyle.parse("padding-top: 1; padding-right: 2.5; padding-bottom: 3; padding-left: 4.5;")
                .padding(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseLengthsWithoutUnitsNegativeValues() {
        this.parseStringAndCheck(
            "-1 -2.5 -3 -4.5",
            TextStyle.parse("padding-top: -1; padding-right: -2.5; padding-bottom: -3; padding-left: -4.5;")
                .padding(BoxEdge.ALL)
        );
    }

    @Override
    public Padding parseString(final String text) {
        return Padding.parse(text);
    }

    // setOrRemoveProperty..............................................................................................

    @Test
    public void testSetOrRemovePropertyWithInvalidFails() {
        assertThrows(
            InvalidTextStylePropertyNameException.class,
            () -> Padding.parse("top: 1px;")
                .setOrRemoveProperty(
                    TextStylePropertyName.MARGIN_BOTTOM,
                    Optional.of(
                        Length.pixel(222.0)
                    )
                )
        );
    }

    @Test
    public void testSetOrRemovePropertyWithSame() {
        this.setOrRemovePropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_TOP,
            Length.pixel(1.0)
        );
    }

    @Test
    public void testSetOrRemovePropertyWithReplaced() {
        this.setOrRemovePropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_TOP,
            Length.pixel(2.0),
            Padding.parse("top: 2px;")
        );
    }

    @Test
    public void testSetOrRemovePropertyWithAll() {
        this.setOrRemovePropertyAndCheck(
            Padding.parse("top: 1px;"),
            TextStylePropertyName.PADDING_BOTTOM,
            Length.pixel(2.0),
            Padding.parse("top: 1px; bottom: 2px;")
        );
    }

    // helpers..........................................................................................................

    @Override
    Padding createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.padding(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.paddingPropertyName();
    }

    // treePrint........................................................................................................

    @Test
    public void testTreePrintAll() {
        this.treePrintAndCheck(
            Padding.parse("top: 1px; right: 2px; bottom: 3px; left: 4px;"),
            "Padding\n" +
                "  ALL\n" +
                "    TextStyle\n" +
                "      padding-bottom=3px (walkingkooka.tree.text.PixelLength)\n" +
                "      padding-left=4px (walkingkooka.tree.text.PixelLength)\n" +
                "      padding-right=2px (walkingkooka.tree.text.PixelLength)\n" +
                "      padding-top=1px (walkingkooka.tree.text.PixelLength)\n"
        );
    }

    @Test
    public void testTreePrintTop() {
        this.treePrintAndCheck(
            Padding.parse("top: 99px;")
                .setEdge(BoxEdge.TOP),
            "Padding\n" +
                "  TOP\n" +
                "    TextStyle\n" +
                "      padding-top=99px (walkingkooka.tree.text.PixelLength)\n"
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshallAll() {
        final Padding padding = Padding.parse("top: 1px; left: 4px;");

        this.marshallAndCheck(
            padding,
            JsonNode.string(
                "ALL " + padding.text()
            )
        );
    }

    @Test
    public void testMarshallLeft() {
        final Padding padding = Padding.parse("left: 4px;")
            .setEdge(BoxEdge.LEFT);

        this.marshallAndCheck(
            padding,
            JsonNode.string(
                "LEFT " + padding.text()
            )
        );
    }

    @Override
    public Padding unmarshall(final JsonNode json,
                              final JsonNodeUnmarshallContext context) {
        return BorderMarginPadding.unmarshallPadding(
            json,
            context
        );
    }

    // class............................................................................................................

    @Override
    public Class<Padding> type() {
        return Padding.class;
    }
}
