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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BorderTest extends BorderMarginPaddingTestCase<Border> {

    // isEmpty..........................................................................................................

    @Test
    public void testIsEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
            Border.parse("1px"),
            false
        );
    }

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            Border.parse(""),
            true
        );
    }

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

    // getProperty......................................................................................................

    @Test
    public void testGetPropertyMissing() {
        this.getPropertyAndCheck(
            Border.parse("top-color: black;"),
            TextStylePropertyName.BORDER_TOP_STYLE
        );
    }

    @Test
    public void testGetPropertyPresent() {
        this.getPropertyAndCheck(
            Border.parse("top-color: black;"),
            TextStylePropertyName.BORDER_TOP_COLOR,
            Color.BLACK
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

    @Test
    public void testRemovePropertyExistingBorder() {
        this.removePropertyAndCheck(
            Border.parse("top-color: black; top-style: dashed;"),
            TextStylePropertyName.BORDER,
            Border.parse("")
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
            Border.with(
                BoxEdge.BOTTOM,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.BORDER_BOTTOM_COLOR, Color.fromRgb(0x123456),
                        TextStylePropertyName.BORDER_BOTTOM_STYLE, BorderStyle.DOTTED)
                )
            ),
            "border BOTTOM #123456 DOTTED"
        );
    }

    @Test
    public void testToStringWithRight() {
        this.toStringAndCheck(
            Border.with(
                BoxEdge.RIGHT,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.BORDER_RIGHT_COLOR, Color.fromRgb(0x123456),
                        TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
                )
            ),
            "border RIGHT #123456 DOTTED"
        );
    }

    @Test
    public void testToStringWithAll() {
        final String text = "black DASHED 2px";

        this.toStringAndCheck(
            Border.parse(text),
            "border ALL " + text
        );
    }

    // text.............................................................................................................

    @Test
    public void testTextWithAllAndEmpty() {
        this.textAndCheck(
            TextStyle.EMPTY.border(BoxEdge.ALL),
            ""
        );
    }

    @Test
    public void testTextWithTopAndEmpty() {
        this.textAndCheck(
            TextStyle.EMPTY.border(BoxEdge.TOP),
            ""
        );
    }

    @Test
    public void testTextWithAllDifferent() {
        this.textAndCheck(
            Border.parse("BLACK SOLID 1px, WHITE DOTTED 2px, RED DASHED 3px, GREEN DOTTED 4px"),
            "black SOLID 1px, white DOTTED 2px, red DASHED 3px, green DOTTED 4px"
        );
    }

    @Test
    public void testTextWithAllSame() {
        this.textAndCheck(
            Border.parse("BLACK SOLID 1px"),
            "black SOLID 1px"
        );
    }

    @Test
    public void testTextWithTop() {
        this.textAndCheck(
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.DASHED),
                Optional.of(
                    Length.pixel(2.0)
                )
            ),
            "black DASHED 2px"
        );
    }

    @Test
    public void testTextWithRight() {
        this.textAndCheck(
            BoxEdge.RIGHT.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.DASHED),
                Optional.of(
                    Length.pixel(2.0)
                )
            ),
            "black DASHED 2px"
        );
    }

    // setEdge..........................................................................................................

    @Test
    public void testSetEdgeFromAllToLeft() {
        this.setEdgeAndCheck(
            Border.parse("left-width: 1px; left-color: black; left-style: SOLID; right-width: 2px; right-color: white; right-style: DASHED; top-width: 3px; bottom-width: 4px;"),
            BoxEdge.LEFT,
            BoxEdge.LEFT.setBorder(
                Optional.of(
                    Color.BLACK
                ),
                Optional.of(
                    BorderStyle.SOLID
                ),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testSetEdgeFromAllToTop() {
        this.setEdgeAndCheck(
            Border.parse("left-width: 1px; left-color: black; left-style: SOLID; right-width: 2px; right-color: white; right-style: DASHED; top-width: 3px; bottom-width: 4px;"),
            BoxEdge.TOP,
            BoxEdge.TOP.setBorder(
                Optional.empty(),
                Optional.empty(),
                Optional.of(
                    Length.pixel(3.0)
                )
            )
        );
    }

    @Test
    public void testSetEdgeFromLeftToAll() {
        this.setEdgeAndCheck(
            BoxEdge.LEFT.parseBorder("black SOLID 1px"),
            BoxEdge.ALL,
            BoxEdge.ALL.parseBorder("left-color: black; left-style: SOLID; left-width: 1px;")
        );
    }

    @Test
    public void testSetEdgeFromRightToAll() {
        this.setEdgeAndCheck(
            BoxEdge.RIGHT.parseBorder("black SOLID 1px"),
            BoxEdge.ALL,
            BoxEdge.ALL.parseBorder("right-color: black; right-style: SOLID; right-width: 1px;")
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
    public void testParseWithoutPropertyNames1Group() {
        this.parseStringAndCheck(
            "BLACK",
            TextStyle.parse("border-top-color: BLACK; border-right-color: BLACK; border-bottom-color: BLACK; border-left-color: BLACK;")
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithoutPropertyNames2GroupsFails() {
        this.parseStringFails(
            "BLACK, WHITE",
            new IllegalArgumentException("Missing bottom and left")
        );
    }

    @Test
    public void testParseWithoutPropertyNamesThreeGroupsFails() {
        this.parseStringFails(
            "BLACK, WHITE, RED",
            new IllegalArgumentException("Missing left")
        );
    }

    @Test
    public void testParseWithoutPropertyNamesFourGroups() {
        this.parseStringAndCheck(
            "BLACK, WHITE, RED, GREEN",
            TextStyle.parse("border-top-color: BLACK; border-right-color: WHITE; border-bottom-color: RED; border-left-color: GREEN;")
                .border(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithoutPropertyNamesFourGroups2() {
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

    // json.............................................................................................................

    @Test
    public void testMarshallAll() {
        final Border border = Border.parse("top-color: black; right-style: SOLID; bottom-width: 1px;");

        this.marshallAndCheck(
            border,
            JsonNode.string(
                "ALL " + border.text()
            )
        );
    }

    @Test
    public void testMarshallLeft() {
        final Border border = BoxEdge.LEFT.parseBorder("BLACK SOLID");

        this.marshallAndCheck(
            border,
            JsonNode.string(
                "LEFT " + border.text()
            )
        );
    }

    @Override
    public Border unmarshall(final JsonNode json,
                             final JsonNodeUnmarshallContext context) {
        return BorderMarginPadding.unmarshallBorder(
            json,
            context
        );
    }

    // class............................................................................................................

    @Override
    public Class<Border> type() {
        return Border.class;
    }
}
