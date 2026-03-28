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

public final class MarginTest extends BorderMarginPaddingTestCase<Margin> {

    // setProperty......................................................................................................

    @Test
    public void testSetPropertyWithSame() {
        this.setPropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(1.0)
        );
    }

    @Test
    public void testSetPropertyWithDifferent() {
        this.setPropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_BOTTOM,
            Length.pixel(2.0),
            Margin.parse("top: 1px; bottom: 2.0px;")
        );
    }

    // removeProperty...................................................................................................

    @Test
    public void testRemovePropertyMissingProperty() {
        this.removePropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_BOTTOM
        );
    }

    @Test
    public void testRemovePropertyExisting() {
        this.removePropertyAndCheck(
            Margin.parse("top: 1px; bottom: 2px;"),
            TextStylePropertyName.MARGIN_BOTTOM,
            Margin.parse("top: 1px; ")
        );
    }
    
    // toString.........................................................................................................

    @Test
    public void testToStringWithAll() {
        this.toStringAndCheck(
            TextStyle.parse("margin-top: 11px; margin-right: 22px; margin-bottom: 33px; margin-left: 44px;")
                .margin(BoxEdge.ALL),
            "margin ALL 11px 22px 33px 44px"
        );
    }
    
    @Test
    public void testToStringBottom() {
        this.toStringAndCheck(
            Margin.with(
                BoxEdge.BOTTOM,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.MARGIN_BOTTOM, Length.pixel(12.5)
                    )
                )
            ),
            "margin BOTTOM 12.5px"
        );
    }

    // text.............................................................................................................

    @Test
    public void testTextAllAndSameValues() {
        this.textAndCheck(
            TextStyle.EMPTY.setMargin(
                Optional.of(
                    Length.pixel(2.0)
                )
            ).margin(BoxEdge.ALL),
            "2px"
        );
    }

    @Test
    public void testTextAllAndDifferentValues() {
        this.textAndCheck(
            Margin.parse("top: 1px; right: 2px; bottom: 3px; left: 4px;"),
            "1px 2px 3px 4px"
        );
    }

    @Test
    public void testTextLeft() {
        this.textAndCheck(
            Margin.parse("left: 1px")
                .setEdge(BoxEdge.LEFT),
            "1px"
        );
    }

    // helpers..........................................................................................................

    @Override
    Margin createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.margin(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.marginPropertyName();
    }

    // setOrRemoveProperty......................................................................................................

    @Test
    public void testSetOrRemovePropertyWithInvalidFails() {
        assertThrows(
            InvalidTextStylePropertyNameException.class,
            () -> Margin.parse("top: 1px;")
                .setEdge(BoxEdge.TOP)
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
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(1.0)
        );
    }

    @Test
    public void testSetOrRemovePropertyWithReplaced() {
        this.setOrRemovePropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(2.0),
            Margin.parse("top: 2px;")
        );
    }

    @Test
    public void testSetOrRemovePropertyRemoved() {
        this.setOrRemovePropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_TOP,
            Margin.parse("")
        );
    }

    @Test
    public void testSetOrRemovePropertyWhenAll() {
        this.setOrRemovePropertyAndCheck(
            Margin.parse("top: 1px;"),
            TextStylePropertyName.MARGIN_BOTTOM,
            Length.pixel(2.0),
            Margin.parse("top: 1px; bottom: 2px;")
        );
    }
    
    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidMarginTextStylePropertyFails() {
        this.parseStringFails(
            "top-invalid: 1px;",
            new UnknownTextStylePropertyNameException("top-invalid")
        );
    }

    @Test
    public void testParsePaddingPropertyFails() {
        this.parseStringFails(
            "top: 1px; padding-top: 2px",
            InvalidTextStylePropertyNameException.margin(TextStylePropertyName.PADDING_TOP)
        );
    }
    
    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            TextStyle.EMPTY.margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithPrefix() {
        final String text = "margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;";

        this.parseStringAndCheck(
            text,
            TextStyle.parse(text)
                .margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithoutPrefix() {
        final String text = "margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;";

        this.parseStringAndCheck(
            text.replace("margin-", ""),
            TextStyle.parse(text)
                .margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithAndWithoutPrefix() {
        this.parseStringAndCheck(
            "margin-top: 1px; margin-right: 2px; bottom: 3px; right: 4px;",
            TextStyle.parse("margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;")
                .margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseLengths() {
        this.parseStringAndCheck(
            "1px 2px 3px 4px",
            TextStyle.parse("margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-left: 4px;")
                .margin(BoxEdge.ALL)
        );
    }

    @Override
    public Margin parseString(final String text) {
        return Margin.parse(text);
    }

    // valueAsText......................................................................................................

    @Test
    public void testValuesAsTextWithEmptyAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.margin(BoxEdge.ALL),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithEmptyAndTop() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.margin(BoxEdge.TOP),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAndTop() {
        this.valuesAsTextAndCheck(
            BoxEdge.TOP.parseMargin("1px"),
            "1px"
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAndAll() {
        this.valuesAsTextAndCheck(
            BoxEdge.ALL.parseMargin("1px"),
            "1px"
        );
    }

    @Test
    public void testValuesAsTextWithNotEmptyAllDifferentAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.parse("margin-top: 1px; margin-bottom: 2px; margin-left: 3px; margin-bottom: 4px;")
                .margin(BoxEdge.ALL),
            ""
        );
    }

    // treePrint........................................................................................................

    @Test
    public void testTreePrintAll() {
        this.treePrintAndCheck(
            Margin.parse("top: 1px; right: 2px; bottom: 3px; left: 4px"),
            "Margin\n" +
                "  ALL\n" +
                "    TextStyle\n" +
                "      margin-bottom=3px (walkingkooka.tree.text.PixelLength)\n" +
                "      margin-left=4px (walkingkooka.tree.text.PixelLength)\n" +
                "      margin-right=2px (walkingkooka.tree.text.PixelLength)\n" +
                "      margin-top=1px (walkingkooka.tree.text.PixelLength)\n"
        );
    }

    @Test
    public void testTreePrintTop() {
        this.treePrintAndCheck(
            Margin.parse("top: 99px")
                .setEdge(BoxEdge.TOP),
            "Margin\n" +
                "  TOP\n" +
                "    TextStyle\n" +
                "      margin-top=99px (walkingkooka.tree.text.PixelLength)\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<Margin> type() {
        return Margin.class;
    }
}
