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
        this.checkEquals(this.textStyle(TextStylePropertyName.BORDER_LEFT_COLOR, differentColor), different.textStyle());
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
        this.checkEquals(
            TextStyle.EMPTY.setValues(properties),
            different.textStyle()
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
        this.checkEquals(
            TextStyle.EMPTY.setValues(properties),
            different.textStyle()
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
        this.checkEquals(this.textStyle(TextStylePropertyName.BORDER_LEFT_STYLE, differentStyle), different.textStyle());
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

        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, differentStyle);
        this.checkEquals(
            TextStyle.EMPTY.setValues(properties),
            different.textStyle()
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

        properties.remove(TextStylePropertyName.BORDER_RIGHT_STYLE);
        this.checkEquals(
            TextStyle.EMPTY.setValues(properties),
            different.textStyle()
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            Border.with(BoxEdge.BOTTOM,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.BORDER_RIGHT_COLOR, Color.fromRgb(0x123456),
                        TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
                )
            ),
            "BOTTOM {border-right-color=#123456, border-right-style=DOTTED}"
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

    @Override
    public Class<Border> type() {
        return Border.class;
    }
}
