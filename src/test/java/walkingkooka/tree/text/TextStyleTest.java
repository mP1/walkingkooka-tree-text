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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumberContexts;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleTest implements ClassTesting2<TextStyle>,
        HashCodeEqualsDefinedTesting2<TextStyle>,
        JsonNodeMarshallingTesting<TextStyle>,
        ToStringTesting<TextStyle> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> TextStyle.with(null));
    }

    @Test
    public void testWithInvalidPropertyFails() {
        assertThrows(TextStylePropertyValueException.class, () -> TextStyle.with(Maps.of(TextStylePropertyName.WORD_BREAK, null)));
    }

    @Test
    public void testWithTextStyleMap() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        final TextNodeMap textStyleMap = TextNodeMap.with(map);

        final TextStyle textStyle = TextStyle.with(textStyleMap);
        assertSame(textStyleMap, textStyle.value(), "value");
    }

    @Test
    public void testWithMapCopied() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final Map<TextStylePropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(map);

        final TextStyle textStyle = TextStyle.with(map);

        map.clear();
        assertEquals(copy, textStyle.value(), "value");
    }

    @Test
    public void testEmpty() {
        assertSame(TextNodeMap.EMPTY, TextNodeMap.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyle textStyle = TextStyle.with(map);
        assertEquals(TextNodeMap.class, textStyle.value().getClass(), () -> "" + textStyle);
    }

    // setChildren......................................................................................................

    @Test
    public void testSetChildrenNullFails() {
        assertThrows(NullPointerException.class, () -> TextStyle.EMPTY.setChildren(null));
    }

    @Test
    public void testSetChildrenEmptyAndNoProperties() {
        final List<TextNode> children = TextStyleNode.NO_CHILDREN;

        this.setChildrenAndCheck(TextStyle.EMPTY,
                children,
                TextStyleNode.with(children, TextNodeMap.EMPTY));
    }

    @Test
    public void testSetChildrenEmpty() {
        final TextStyle textStyle = this.textStyle();
        final List<TextNode> children = TextStyleNode.NO_CHILDREN;

        this.setChildrenAndCheck(textStyle,
                children,
                TextNode.style(children).setAttributes(textStyle.value()));
    }

    @Test
    public void testSetChildrenAndNoProperties() {
        final List<TextNode> children = this.children();

        this.setChildrenAndCheck(TextStyle.EMPTY,
                children,
                TextStyleNode.with(children, TextNodeMap.EMPTY));
    }

    @Test
    public void testSetChildrenAndProperties() {
        final TextStyle textStyle = this.textStyle();
        final List<TextNode> children = this.children();

        this.setChildrenAndCheck(textStyle,
                children,
                TextNode.style(children).setAttributes(textStyle.value()));
    }

    private List<TextNode> children() {
        return Lists.of(TextNode.text("text-1a"), TextNode.text("text-1b"));
    }

    private void setChildrenAndCheck(final TextStyle properties,
                                     final List<TextNode> children,
                                     final TextNode textStyleNode) {
        assertEquals(textStyleNode,
                properties.setChildren(children),
                () -> properties + " setChildren " + children);
    }

    // getOrFail.........................................................................................................

    @Test
    public void testGetOrFail() {
        assertThrows(TextStylePropertyValueException.class, () -> this.createObject().getOrFail(TextStylePropertyName.WIDTH));
    }

    // json.............................................................................................................

    @Test
    public void testJsonRoundtripAllPropertyTypes() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.parse("#123456"))
                .set(TextStylePropertyName.BORDER_BOTTOM_COLOR, Color.parse("#222222"))
                .set(TextStylePropertyName.BORDER_BOTTOM_STYLE, BorderStyle.DASHED)
                .set(TextStylePropertyName.BORDER_BOTTOM_WIDTH, Length.pixel(1.0))
                .set(TextStylePropertyName.BORDER_COLLAPSE, BorderCollapse.COLLAPSE)
                .set(TextStylePropertyName.BORDER_SPACING, BorderSpacing.with(Length.pixel(5.0)))
                .set(TextStylePropertyName.FONT_FAMILY, FontFamily.with("Times New Roman"))
                .set(TextStylePropertyName.FONT_KERNING, FontKerning.NORMAL)
                .set(TextStylePropertyName.FONT_SIZE, FontSize.with(10))
                .set(TextStylePropertyName.FONT_STRETCH, FontStretch.EXPANDED)
                .set(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC)
                .set(TextStylePropertyName.FONT_VARIANT, FontVariant.SMALL_CAPS)
                .set(TextStylePropertyName.HANGING_PUNCTUATION, HangingPunctuation.ALLOW_END)
                .set(TextStylePropertyName.HEIGHT, Length.pixel(15.0))
                .set(TextStylePropertyName.HYPHENS, Hyphens.MANUAL)
                .set(TextStylePropertyName.LETTER_SPACING, LetterSpacing.parse("2.5px"))
                .set(TextStylePropertyName.LIST_STYLE_POSITION, ListStylePosition.INSIDE)
                .set(TextStylePropertyName.LIST_STYLE_TYPE, ListStyleType.DECIMAL)
                .set(TextStylePropertyName.OPACITY, Opacity.TRANSPARENT)
                .set(TextStylePropertyName.OVERFLOW_X, Overflow.HIDDEN)
                .set(TextStylePropertyName.TEXT_ALIGN, TextAlign.CENTER)
                .set(TextStylePropertyName.TEXT_DECORATION, TextDecoration.LINE_THROUGH)
                .set(TextStylePropertyName.TEXT_DECORATION_STYLE, TextDecorationStyle.DASHED)
                .set(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.INTER_CHARACTER)
                .set(TextStylePropertyName.TEXT_TRANSFORM, TextTransform.CAPITALIZE)
                .set(TextStylePropertyName.TEXT_WRAPPING, TextWrapping.CLIP)
                .set(TextStylePropertyName.VERTICAL_ALIGN, VerticalAlign.BOTTOM)
                .set(TextStylePropertyName.VISIBILITY, Visibility.HIDDEN)
                .set(TextStylePropertyName.WORD_BREAK, WordBreak.NORMAL)
                .set(TextStylePropertyName.WORD_SPACING, WordSpacing.with(Length.normal()))
                .set(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD)
                .set(TextStylePropertyName.WRITING_MODE, WritingMode.VERTICAL_LR);

        this.marshallRoundTripTwiceAndCheck(style);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(TextStyle.with(map), map.toString());
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(TextStyle.EMPTY, TextStyle.unmarshall(JsonNode.object(), JsonNodeUnmarshallContexts.basic(ExpressionNumberContexts.fake())));
    }

    @Override
    public TextStyle createObject() {
        return this.textStyle();
    }

    private TextStyle textStyle() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.ordered();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        return TextStyle.with(map);
    }

    private TextStylePropertyName<?> property1() {
        return TextStylePropertyName.WORD_WRAP;
    }

    private Object value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextStylePropertyName<?> property2() {
        return TextStylePropertyName.FONT_FAMILY;
    }

    private Object value2() {
        return FontFamily.with("Times News Roman");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextStyle> type() {
        return TextStyle.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public TextStyle unmarshall(final JsonNode from,
                                final JsonNodeUnmarshallContext context) {
        return TextStyle.unmarshall(from, context);
    }

    @Override
    public TextStyle createJsonNodeMappingValue() {
        return this.createObject();
    }
}
