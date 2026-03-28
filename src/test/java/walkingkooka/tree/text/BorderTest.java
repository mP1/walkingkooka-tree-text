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
import walkingkooka.color.Color;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BorderTest extends BorderMarginPaddingTestCase<Border> {

    // rgb............................................................................................................

    @Test
    public void testColor() {
        final Color color = Color.parse("red");
        final BoxEdge edge = BoxEdge.BOTTOM;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_BOTTOM_COLOR, color));
        this.checkEquals(Optional.of(color), border.color(), "rgb");
    }

    @Test
    public void testSetColorSame() {
        final Color color = Color.parse("blue");
        final BoxEdge edge = BoxEdge.TOP;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_TOP_COLOR, color));
        assertSame(border, border.setColor(Optional.of(color)));
    }

    @Test
    public void testSetColorDifferent() {
        final Color color = Color.parse("lime");
        final BoxEdge edge = BoxEdge.LEFT;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_LEFT_COLOR, color));

        final Color differentColor = Color.parse("yellow");
        final Border different = border.setColor(Optional.of(differentColor));

        assertNotSame(border, different);
        this.textStyleAndCheck(
            different,
            this.textStyle(TextStylePropertyName.BORDER_LEFT_COLOR, differentColor)
        );
    }

    @Test
    public void testSetColorDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseRgb("aqua"));

        final BoxEdge edge = BoxEdge.RIGHT;
        final Border border = edge.border(
            TextStyle.EMPTY.setValues(properties)
        );

        final Color differentColor = Color.parse("yellow");
        final Border different = border.setColor(Optional.of(differentColor));

        assertNotSame(border, different);

        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, differentColor);
        this.textStyleAndCheck(
            different,
            TextStyle.EMPTY.setValues(
                Maps.of(
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    differentColor
                )
            )
        );
    }

    @Test
    public void testSetColorRemoved() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseRgb("aqua"));

        final BoxEdge edge = BoxEdge.RIGHT;
        final Border border = edge.border(
            TextStyle.EMPTY.setValues(properties)
        );

        final Border different = border.setColor(Optional.empty());

        assertNotSame(border, different);

        properties.remove(TextStylePropertyName.BORDER_RIGHT_COLOR);

        this.textStyleAndCheck(
            different,
            TextStyle.EMPTY
        );
    }

    // style............................................................................................................

    @Test
    public void testStyle() {
        final BorderStyle style = BorderStyle.DOTTED;
        final BoxEdge edge = BoxEdge.BOTTOM;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_BOTTOM_STYLE, style));

        this.checkEquals(Optional.of(style), border.style(), "style");
    }

    @Test
    public void testSetStyleSame() {
        final BorderStyle style = BorderStyle.DASHED;
        final BoxEdge edge = BoxEdge.TOP;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_TOP_STYLE, style));
        assertSame(border, border.setStyle(Optional.of(style)));
    }

    @Test
    public void testSetStyleDifferent() {
        final BorderStyle style = BorderStyle.HIDDEN;
        final BoxEdge edge = BoxEdge.LEFT;
        final Border border = edge.border(this.textStyle(TextStylePropertyName.BORDER_LEFT_STYLE, style));

        final BorderStyle differentStyle = BorderStyle.GROOVE;
        final Border different = border.setStyle(Optional.of(differentStyle));

        assertNotSame(border, different);
        this.textStyleAndCheck(
            different,
            this.textStyle(
                TextStylePropertyName.BORDER_LEFT_STYLE,
                differentStyle
            )
        );
    }

    @Test
    public void testSetStyleDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.OUTSET);

        final BoxEdge edge = BoxEdge.RIGHT;
        final Border border = edge.border(
            TextStyle.EMPTY.setValues(properties)
        );

        final BorderStyle differentStyle = BorderStyle.INSET;
        final Border different = border.setStyle(Optional.of(differentStyle));

        assertNotSame(border, different);

        this.textStyleAndCheck(
            different,
            TextStyle.EMPTY.setValues(
                Maps.of(
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    differentStyle
                )
            )
        );
    }

    @Test
    public void testSetStyleRemoved() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOUBLE);

        final BoxEdge edge = BoxEdge.RIGHT;
        final Border border = edge.border(
            TextStyle.EMPTY.setValues(properties)
        );

        final Border different = border.setStyle(Optional.empty());

        assertNotSame(border, different);

        this.textStyleAndCheck(
            different,
            TextStyle.EMPTY
        );
    }

    // setProperty......................................................................................................

    @Test
    public void testSetPropertyWithSame() {
        this.setPropertyAndCheck(
            Border.parse("top-color: black;"),
            TextStylePropertyName.BORDER_TOP_COLOR,
            Color.BLACK
        );
    }

    @Test
    public void testSetPropertyWithDifferent() {
        this.setPropertyAndCheck(
            Border.parse("top-color: black;"),
            TextStylePropertyName.BORDER_TOP_STYLE,
            BorderStyle.DASHED,
            Border.parse("top-color: black; top-style: dashed;")
        );
    }

    // removeProperty...................................................................................................

    @Test
    public void testRemovePropertyWithMissing() {
        this.removePropertyAndCheck(
            Border.parse("top-color: black;"),
            TextStylePropertyName.BORDER_TOP_STYLE
        );
    }

    @Test
    public void testRemovePropertyExisting() {
        this.removePropertyAndCheck(
            Border.parse("top-color: black; top-style: dashed;"),
            TextStylePropertyName.BORDER_TOP_STYLE,
            Border.parse("top-color: black;")
        );
    }

    // setOrRemoveProperty..............................................................................................

    @Test
    public void testSetOrRemovePropertyWithInvalidFails() {
        assertThrows(
            InvalidTextStylePropertyNameException.class,
            () -> Border.parse("top-color: black; top-style: solid; top-width: 1px;")
                .setEdge(BoxEdge.TOP)
                .setOrRemoveProperty(
                    TextStylePropertyName.BORDER_BOTTOM_COLOR,
                    Optional.of(Color.WHITE)
                )
        );
    }

    @Test
    public void testSetOrRemovePropertyWithSame() {
        this.setOrRemovePropertyAndCheck(
            Border.parse("top-color: black; top-style: solid; top-width: 1px;"),
            TextStylePropertyName.BORDER_TOP_COLOR,
            Color.BLACK
        );
    }

    @Test
    public void testSetOrRemovePropertyWithReplaced() {
        this.setOrRemovePropertyAndCheck(
            Border.parse("top-color: black; top-style: solid; top-width: 1px;"),
            TextStylePropertyName.BORDER_TOP_COLOR,
            Color.WHITE,
            Border.parse("top-color: white; top-style: solid; top-width: 1px;")
        );
    }

    @Test
    public void testSetOrRemovePropertyRemoved() {
        this.setOrRemovePropertyAndCheck(
            Border.parse("top-color: black; top-style: solid; top-width: 1px;"),
            TextStylePropertyName.BORDER_TOP_COLOR,
            Border.parse("top-style: solid; top-width: 1px;")
        );
    }

    @Test
    public void testSetOrRemovePropertyWithEdgeAll() {
        this.setOrRemovePropertyAndCheck(
            Border.parse("top-color: black; top-style: solid; top-width: 1px;"),
            TextStylePropertyName.BORDER_BOTTOM_COLOR,
            Color.WHITE,
            Border.parse("top-color: black; top-style: solid; top-width: 1px; bottom-color: white")
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithBottom() {
        this.toStringAndCheck(
            Border.with(BoxEdge.BOTTOM,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.BORDER_RIGHT_COLOR, Color.fromRgb(0x123456),
                        TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
                )
            ),
            "border BOTTOM right-color: #123456; right-style: dotted;"
        );
    }

    @Test
    public void testToStringWithTop() {
        this.toStringAndCheck(
            Border.with(BoxEdge.TOP,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.BORDER_RIGHT_COLOR, Color.fromRgb(0x123456),
                        TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
                )
            ),
            "border TOP right-color: #123456; right-style: dotted;"
        );
    }

    @Test
    public void testToStringWithAll() {
        this.toStringAndCheck(
            TextStyle.EMPTY.setBorder(
                Color.BLACK,
                BorderStyle.DASHED,
                Length.pixel(2.0)
            ).border(BoxEdge.ALL),
            "border ALL bottom-color: black; bottom-style: dashed; bottom-width: 2px; left-color: black; left-style: dashed; left-width: 2px; right-color: black; right-style: dashed; right-width: 2px; top-color: black; top-style: dashed; top-width: 2px;"
        );
    }

    // text.............................................................................................................

    @Test
    public void testTextWithAll() {
        this.textAndCheck(
            TextStyle.EMPTY.setBorder(
                Color.BLACK,
                BorderStyle.DASHED,
                Length.pixel(2.0)
            ).border(BoxEdge.ALL),
            "bottom-color: black; bottom-style: dashed; bottom-width: 2px; left-color: black; left-style: dashed; left-width: 2px; right-color: black; right-style: dashed; right-width: 2px; top-color: black; top-style: dashed; top-width: 2px;"
        );
    }

    @Test
    public void testTextWithTop() {
        this.textAndCheck(
            TextStyle.EMPTY.setBorder(
                Color.BLACK,
                BorderStyle.DASHED,
                Length.pixel(2.0)
            ).border(BoxEdge.TOP),
            "top-color: black; top-style: dashed; top-width: 2px;"
        );
    }

    // helpers..........................................................................................................

    @Override
    Border createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.border(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.borderWidthPropertyName();
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            TextStyle.EMPTY.border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseInvalidBorderTextStylePropertyFails() {
        this.parseStringFails(
            "top-invalid: 1px;",
            new UnknownTextStylePropertyNameException("top-invalid")
        );
    }

    @Test
    public void testParsePaddingPropertyFails() {
        this.parseStringFails(
            "top-width: 1px; padding-top: 2px",
            InvalidTextStylePropertyNameException.border(TextStylePropertyName.PADDING_TOP)
        );
    }

    @Test
    public void testParsePropertyNamesWithPrefix() {
        final String text = "border-top-color: BLACK; border-right-style: solid; border-bottom-width: 1px; border-left-color: WHITE;";

        this.parseStringAndCheck(
            text,
            TextStyle.parse(text)
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParsePropertyNamesWithoutPrefix() {
        this.parseStringAndCheck(
            "top-color: BLACK; right-style: solid; bottom-width: 1px; left-color: WHITE;",
            TextStyle.parse("border-top-color: BLACK; border-right-style: solid; border-bottom-width: 1px; border-left-color: WHITE;")
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithAndPropertyNamesWithoutPrefix() {
        this.parseStringAndCheck(
            "border-top-color: BLACK; right-style: solid; bottom-width: 1px; left-color: WHITE;",
            TextStyle.parse("border-top-color: BLACK; border-right-style: solid; border-bottom-width: 1px; border-left-color: WHITE;")
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithoutPropertyNames() {
        this.parseStringAndCheck(
            "BLACK, WHITE, RED, GREEN",
            TextStyle.parse("border-top-color: BLACK; border-right-color: WHITE; border-bottom-color: RED; border-left-color: GREEN;")
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithoutPropertyNames2() {
        this.parseStringAndCheck(
            "BLACK solid 1px, WHITE, RED, GREEN",
            TextStyle.parse("border-top-color: BLACK; border-top-style: solid; border-top-width: 1px; border-right-color: WHITE; border-bottom-color: RED; border-left-color: GREEN;")
                .border(BoxEdge.ALL)
        );
    }

    @Override
    public Border parseString(final String text) {
        return Border.parse(text);
    }

    // valueAsText......................................................................................................

    @Test
    public void testValuesAsTextWithEmptyAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.border(BoxEdge.ALL),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithEmptyAndTop() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.border(BoxEdge.TOP),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithSameAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.setBorder(
                Color.BLACK,
                BorderStyle.DASHED,
                Length.pixel(1.0)
            ).border(BoxEdge.ALL),
            "black DASHED 1px"
        );
    }

    @Test
    public void testValuesAsTextWithSomeDifferentAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.parse(
                "border-top-color: BLACK; border-top-style: SOLID; border-top-width: 1px; border-bottom-color: BLACK; border-bottom-style: DASHED; border-bottom-width: 2px;" +
                    "border-left-color: BLACK; border-left-style: SOLID; border-left-width: 3px; border-right-color: BLACK; border-right-style: DOTTED; border-right-width: 4px;"
            ).border(BoxEdge.ALL),
            "black"
        );
    }

    @Test
    public void testValuesAsTextWithAllDifferentAndAll() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.parse(
                "border-top-color: BLACK; border-top-style: SOLID; border-top-width: 1px; border-bottom-color: WHITE; border-bottom-style: DASHED; border-bottom-width: 2px;" +
                    "border-left-color: RED; border-left-style: SOLID; border-left-width: 3px; border-right-color: GREEN; border-right-style: DOTTED; border-right-width: 4px;"
            ).border(BoxEdge.ALL),
            ""
        );
    }

    @Test
    public void testValuesAsTextWithSameAndLeft() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.setBorder(
                Color.BLACK,
                BorderStyle.DASHED,
                Length.pixel(1.0)
            ).border(BoxEdge.LEFT),
            "black DASHED 1px"
        );
    }

    @Test
    public void testValuesAsTextWithIncompleteAndLeft() {
        this.valuesAsTextAndCheck(
            TextStyle.EMPTY.setBorder(
                    Color.BLACK,
                    BorderStyle.DASHED,
                    Length.pixel(1.0)
                ).border(BoxEdge.LEFT)
                .setOrRemoveProperty(
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    Optional.empty()
                ),
            "DASHED 1px"
        );
    }

    // treePrint........................................................................................................

    @Test
    public void testTreePrintAll() {
        this.treePrintAndCheck(
            Border.parse("top-color: black; right-color: red; right-style: SOLID; right-width: 1px; bottom-color: green"),
            "Border\n" +
                "  ALL\n" +
                "    TextStyle\n" +
                "      border-bottom-color=green (walkingkooka.color.OpaqueRgbColor)\n" +
                "      border-right-color=red (walkingkooka.color.OpaqueRgbColor)\n" +
                "      border-right-style=SOLID (walkingkooka.tree.text.BorderStyle)\n" +
                "      border-right-width=1px (walkingkooka.tree.text.PixelLength)\n" +
                "      border-top-color=black (walkingkooka.color.OpaqueRgbColor)\n"
        );
    }

    @Test
    public void testTreePrintTop() {
        this.treePrintAndCheck(
            Border.parse("top-color: black;")
                .setEdge(BoxEdge.TOP),
            "Border\n" +
                "  TOP\n" +
                "    TextStyle\n" +
                "      border-top-color=black (walkingkooka.color.OpaqueRgbColor)\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<Border> type() {
        return Border.class;
    }
}
