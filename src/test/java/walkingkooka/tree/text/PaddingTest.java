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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PaddingTest extends BorderMarginPaddingTestCase<Padding> {

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
            TextStyle.parse("padding-left: 11px; padding-top: 22px; padding-right: 33px; padding-bottom: 44px;")
                .padding(BoxEdge.ALL),
            "padding ALL bottom: 44px; left: 11px; right: 33px; top: 22px;"
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
            "padding BOTTOM bottom: 12.5px;"
        );
    }

    // text.............................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
            TextStyle.EMPTY.setPadding(
                Optional.of(
                    Length.pixel(2.0)
                )
            ).padding(BoxEdge.ALL),
            "bottom: 2px; left: 2px; right: 2px; top: 2px;"
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

    // valueAsText......................................................................................................

    @Test
    public void testValuesAsTextWithEmptyAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.padding(BoxEdge.ALL),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithEmptyAndTop() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.padding(BoxEdge.TOP),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAndTop() {
        this.valuesAsTextAndCheck(
            BoxEdge.TOP.parsePadding("1px"),
            "1px"
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAndAll() {
        this.valuesAsTextAndCheck(
            BoxEdge.ALL.parsePadding("1px"),
            "1px"
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAllDifferentAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.parse("padding-top: 1px; padding-bottom: 2px; padding-left: 3px; padding-bottom: 4px;")
                .padding(BoxEdge.ALL),
            ""
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

    // class............................................................................................................

    @Override
    public Class<Padding> type() {
        return Padding.class;
    }
}
