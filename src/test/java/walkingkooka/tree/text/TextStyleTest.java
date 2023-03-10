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
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.tree.json.patch.PatchableTesting;

import java.math.MathContext;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleTest implements ClassTesting2<TextStyle>,
        HashCodeEqualsDefinedTesting2<TextStyle>,
        HasCssTesting,
        JsonNodeMarshallingTesting<TextStyle>,
        PatchableTesting<TextStyle>,
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
        this.checkEquals(copy, textStyle.value(), "value");
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
        this.checkEquals(TextNodeMap.class, textStyle.value().getClass(), () -> "" + textStyle);
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
        this.checkEquals(textStyleNode,
                properties.setChildren(children),
                () -> properties + " setChildren " + children);
    }

    // getOrFail.........................................................................................................

    @Test
    public void testGetOrFail() {
        assertThrows(TextStylePropertyValueException.class, () -> this.createObject().getOrFail(TextStylePropertyName.WIDTH));
    }

    // setOrRemove......................................................................................................

    @Test
    public void testSetOrRemoveNonNullValue() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
        this.checkEquals(
                style.set(TextStylePropertyName.COLOR, Color.WHITE),
                style.setOrRemove(TextStylePropertyName.COLOR, Color.WHITE)
        );
    }

    @Test
    public void testSetOrRemoveNullValue() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
        this.checkEquals(
                style.remove(TextStylePropertyName.BACKGROUND_COLOR),
                style.setOrRemove(TextStylePropertyName.BACKGROUND_COLOR, null)
        );
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
                .set(TextStylePropertyName.BORDER_SPACING, Length.pixel(5.0))
                .set(TextStylePropertyName.FONT_FAMILY, FontFamily.with("Times New Roman"))
                .set(TextStylePropertyName.FONT_KERNING, FontKerning.NORMAL)
                .set(TextStylePropertyName.FONT_SIZE, FontSize.with(10))
                .set(TextStylePropertyName.FONT_STRETCH, FontStretch.EXPANDED)
                .set(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC)
                .set(TextStylePropertyName.FONT_VARIANT, FontVariant.SMALL_CAPS)
                .set(TextStylePropertyName.HANGING_PUNCTUATION, HangingPunctuation.ALLOW_END)
                .set(TextStylePropertyName.HEIGHT, Length.pixel(15.0))
                .set(TextStylePropertyName.HYPHENS, Hyphens.MANUAL)
                .set(TextStylePropertyName.LETTER_SPACING, Length.pixel(2.5))
                .set(TextStylePropertyName.LIST_STYLE_POSITION, ListStylePosition.INSIDE)
                .set(TextStylePropertyName.LIST_STYLE_TYPE, ListStyleType.DECIMAL)
                .set(TextStylePropertyName.OPACITY, Opacity.TRANSPARENT)
                .set(TextStylePropertyName.OVERFLOW_X, Overflow.HIDDEN)
                .set(TextStylePropertyName.TEXT_ALIGN, TextAlign.CENTER)
                .set(TextStylePropertyName.TEXT_DECORATION_LINE, TextDecorationLine.LINE_THROUGH)
                .set(TextStylePropertyName.TEXT_DECORATION_STYLE, TextDecorationStyle.DASHED)
                .set(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.INTER_CHARACTER)
                .set(TextStylePropertyName.TEXT_TRANSFORM, TextTransform.CAPITALIZE)
                .set(TextStylePropertyName.TEXT_WRAPPING, TextWrapping.CLIP)
                .set(TextStylePropertyName.VERTICAL_ALIGN, VerticalAlign.BOTTOM)
                .set(TextStylePropertyName.VISIBILITY, Visibility.HIDDEN)
                .set(TextStylePropertyName.WORD_BREAK, WordBreak.NORMAL)
                .set(TextStylePropertyName.WORD_SPACING, Length.normal())
                .set(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD)
                .set(TextStylePropertyName.WRITING_MODE, WritingMode.VERTICAL_LR);

        this.marshallRoundTripTwiceAndCheck(style);
    }

    // patch............................................................................................................

    @Test
    public void testPatchJsonNull() {
        this.patchAndCheck(
                TextStyle.EMPTY,
                JsonNode.nullNode()
        );
    }

    @Test
    public void testPatchJsonNull2() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.BLACK),
                JsonNode.nullNode(),
                TextStyle.EMPTY
        );
    }

    @Test
    public void testPatchEmptyObject() {
        this.patchAndCheck(
                TextStyle.EMPTY,
                JsonNode.object()
        );
    }

    @Test
    public void testPatchEmptyObject2() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.BLACK),
                JsonNode.object()
        );
    }

    @Test
    public void testPatchRemoveUnknownProperty() {
        this.patchAndCheck(
                TextStyle.EMPTY,
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), JsonNode.nullNode())
        );
    }

    @Test
    public void testPatchRemoveUnknownProperty2() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), JsonNode.nullNode())
        );
    }

    @Test
    public void testPatchRemoveProperty() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.BACKGROUND_COLOR.value()), JsonNode.nullNode()),
                TextStyle.EMPTY
        );
    }

    @Test
    public void testPatchRemovePropertyEnum() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.TEXT_ALIGN, TextAlign.RIGHT),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.TEXT_ALIGN.value()), JsonNode.nullNode()),
                TextStyle.EMPTY
        );
    }

    @Test
    public void testPatchSetProperty() {
        this.patchAndCheck(
                TextStyle.EMPTY,
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), marshall(Color.BLACK)),
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.BLACK)
        );
    }

    @Test
    public void testPatchSetProperty2() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), marshall(Color.BLACK)),
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK)
                        .set(TextStylePropertyName.COLOR, Color.BLACK)
        );
    }

    @Test
    public void testPatchReplaceProperty() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.BLACK),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), marshall(Color.WHITE)),
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.WHITE)
        );
    }

    @Test
    public void testPatchSetReplaceAndRemove() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK)
                        .set(TextStylePropertyName.COLOR, Color.BLACK)
                        .set(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.BACKGROUND_COLOR.value()), marshall(Color.WHITE))
                        .set(JsonPropertyName.with(TextStylePropertyName.COLOR.value()), JsonNode.nullNode()),
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.WHITE)
                        .set(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD)
        );
    }

    private JsonNode marshall(final Object value) {
        return JsonNodeMarshallContexts.basic()
                .marshall(value);
    }

    // css..............................................................................................................

    @Test
    public void testCssEmpty() {
        this.hasCssAndCheck(
                TextStyle.EMPTY,
                "{}"
        );
    }

    @Test
    public void testCssStringValueWithoutSpaces() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.FONT_FAMILY,
                                FontFamily.with("Banana")
                        ),
                "{" + LineEnding.SYSTEM +
                        "  font-family: Banana;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssStringValueWithSpaces() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.FONT_FAMILY,
                                FontFamily.with("Times New Roman")
                        ),
                "{" + LineEnding.SYSTEM +
                        "  font-family: \"Times New Roman\";" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssBorderBottomStyleEnum() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                                BorderStyle.DOTTED
                        ),
                "{" + LineEnding.SYSTEM +
                        "  border-bottom-style: dotted;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssvBorderBottomWidth() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                                Length.pixel(12.5)
                        ),
                "{" + LineEnding.SYSTEM +
                        "  border-bottom-width: 12.5px;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssColor() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.COLOR,
                                Color.parse("#123456")
                        ),
                "{" + LineEnding.SYSTEM +
                        "  color: #123456;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssFontSize() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.FONT_SIZE,
                                FontSize.with(123)
                        ),
                "{" + LineEnding.SYSTEM +
                        "  font-size: 123;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }

    @Test
    public void testCssText() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.TEXT,
                                "has-no-spaces"
                        ),
                "{" + LineEnding.SYSTEM +
                        "  text: has-no-spaces;" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
    }


    @Test
    public void testCssTextIncludesSpacesRequiresQuotes() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.TEXT,
                                "has spaces"
                        ),
                "{" + LineEnding.SYSTEM +
                        "  text: \"has spaces\";" + LineEnding.SYSTEM +
                        "}" + LineEnding.SYSTEM
        );
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
    public void testUnmarshallEmptyJsonObject() {
        assertSame(
                TextStyle.EMPTY,
                TextStyle.unmarshall(
                        JsonNode.object(),
                        this.createPatchContext()
                )
        );
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
    public TextStyle createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    // PatchableTesting.................................................................................................

    @Override
    public TextStyle createPatchable() {
        return this.createObject();
    }

    @Override
    public JsonNode createPatch() {
        return JsonNode.object();
    }

    @Override
    public JsonNodeUnmarshallContext createPatchContext() {
        return JsonNodeUnmarshallContexts.basic(
                ExpressionNumberKind.BIG_DECIMAL,
                MathContext.UNLIMITED
        );
    }
}
