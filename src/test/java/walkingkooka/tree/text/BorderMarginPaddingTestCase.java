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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BorderMarginPaddingTestCase<T extends BorderMarginPadding> implements ClassTesting2<T>,
        HashCodeEqualsDefinedTesting2<T>,
        ToStringTesting<T> {

    BorderMarginPaddingTestCase() {
        super();
    }

    @Test
    public final void testWithNullTextStyleFails() {
        assertThrows(NullPointerException.class, () -> this.createBorderMarginPadding(BoxEdge.RIGHT, null));
    }

    @Test
    public final void testWithEmptyTextStyle() {
        final TextStyle textStyle = TextStyle.EMPTY;

        for (BoxEdge edge : BoxEdge.values()) {
            final T borderMarginPadding = this.createBorderMarginPadding(edge, textStyle);
            this.check(borderMarginPadding, edge, textStyle);
            assertSame(borderMarginPadding, this.createBorderMarginPadding(edge, textStyle));
        }
    }

    @Test
    public final void testWithNonEmptyTextStyle() {
        final TextStyle textStyle = this.textStyle();

        for (BoxEdge edge : BoxEdge.values()) {
            this.check(this.createBorderMarginPadding(edge, textStyle), edge, textStyle);
        }
    }

    // edge........................................................................................................

    @Test
    public final void testEdge() {
        final TextStyle textStyle = this.textStyle();

        for (BoxEdge edge : BoxEdge.values()) {
            this.checkEquals(edge, this.createBorderMarginPadding(edge, textStyle).edge(), "edge");
        }
    }

    @Test
    public final void testSetEdgeSame() {
        final TextStyle textStyle = this.textStyle();

        for (BoxEdge edge : BoxEdge.values()) {
            final T borderMarginPadding = this.createBorderMarginPadding(edge, textStyle);
            assertSame(borderMarginPadding, borderMarginPadding.setEdge(edge));
        }
    }

    @Test
    public final void testSetEdgeDifferent() {
        final TextStyle textStyle = this.textStyle();

        final T borderMarginPadding = this.createBorderMarginPadding(BoxEdge.LEFT, textStyle);
        final BorderMarginPadding different = borderMarginPadding.setEdge(BoxEdge.RIGHT);
        assertNotSame(borderMarginPadding, different);
        this.check(different, BoxEdge.RIGHT, textStyle);
    }

    // width............................................................................................................

    @Test
    public final void testWidth() {
        final Length<?> width = Length.pixel(2.5);
        final BoxEdge edge = BoxEdge.BOTTOM;
        final T borderMarginPadding = this.createBorderMarginPadding(edge, this.textStyle(this.widthPropertyName(edge), width));
        this.checkEquals(Optional.of(width), borderMarginPadding.width(), "width");
    }

    @Test
    public final void testSetWidthSame() {
        final Length<?> width = Length.pixel(2.5);
        final BoxEdge edge = BoxEdge.TOP;
        final T borderMarginPadding = this.createBorderMarginPadding(edge, this.textStyle(this.widthPropertyName(edge), width));
        assertSame(borderMarginPadding, borderMarginPadding.setWidth(Optional.of(width)));
    }

    @Test
    public final void testSetWidthDifferent() {
        final Length<?> width = Length.pixel(2.5);
        final BoxEdge edge = BoxEdge.LEFT;
        final TextStylePropertyName<Length<?>> propertyName = this.widthPropertyName(edge);
        final T borderMarginPadding = this.createBorderMarginPadding(edge, this.textStyle(propertyName, width));

        final Length<?> differentWidth = Length.pixel(99.0);
        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.of(differentWidth));

        assertNotSame(borderMarginPadding, different);
        this.checkEquals(this.textStyle(propertyName, differentWidth), different.textStyle());
    }

    @Test
    public final void testSetWidthDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#333"));

        final BoxEdge edge = BoxEdge.RIGHT;
        final T borderMarginPadding = this.createBorderMarginPadding(edge, TextStyle.with(properties));

        final Length<?> differentWidth = Length.pixel(99.0);
        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.of(differentWidth));

        assertNotSame(borderMarginPadding, different);

        properties.put(this.widthPropertyName(edge), differentWidth);
        this.checkEquals(TextStyle.with(properties), different.textStyle());
    }

    @Test
    public final void testSetWidthRemoved() {
        final BoxEdge edge = BoxEdge.RIGHT;

        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.COLOR, Color.parseRgb("#444"));
        properties.put(this.widthPropertyName(edge), Length.pixel(1.5));

        final T borderMarginPadding = this.createBorderMarginPadding(edge, TextStyle.with(properties));

        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.empty());

        assertNotSame(borderMarginPadding, different);

        properties.remove(this.widthPropertyName(edge));
        this.checkEquals(TextStyle.with(properties), different.textStyle());
    }

    // equals...........................................................................................................

    @Test
    public final void testDifferentDirection() {
        final TextStyle textStyle = this.textStyle();
        this.checkNotEquals(this.createBorderMarginPadding(BoxEdge.LEFT, textStyle),
                this.createBorderMarginPadding(BoxEdge.RIGHT, textStyle));
    }

    @Test
    public final void testDifferentTextStyle() {
        final BoxEdge edge = BoxEdge.RIGHT;
        this.checkNotEquals(this.createBorderMarginPadding(edge, TextStyle.EMPTY),
                this.createBorderMarginPadding(edge, this.textStyle()));
    }

    // helpers..........................................................................................................

    abstract T createBorderMarginPadding(final BoxEdge edge,
                                         final TextStyle textStyle);

    abstract TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge);

    private TextStyle textStyle() {
        return this.textStyle(TextStylePropertyName.COLOR, Color.fromArgb(0x123456));
    }

    final <TT> TextStyle textStyle(final TextStylePropertyName<TT> propertyName, final TT value) {
        return TextStyle.with(Maps.of(propertyName, value));
    }

    final void check(final BorderMarginPadding borderMarginPadding,
                     final BoxEdge edge,
                     final TextStyle textStyle) {
        this.checkEquals(edge, borderMarginPadding.edge(), "edge");
        this.checkEquals(textStyle, borderMarginPadding.textStyle(), "textStyle");
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefined............................................................................................

    @Override
    public final T createObject() {
        return this.createBorderMarginPadding(BoxEdge.LEFT, this.textStyle());
    }
}
