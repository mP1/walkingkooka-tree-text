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
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyle textStyle = TextStyle.EMPTY.setValues(map);
        this.checkEquals(
                TextNodeMap.class,
                textStyle.value().getClass(),
                () -> "" + textStyle
        );
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
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createObject()
                        .getOrFail(TextStylePropertyName.WIDTH)
        );
    }

    @Test
    public void testGetOrFailAll() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.createObject()
                        .getOrFail(TextStylePropertyName.ALL)
        );
    }

    // setOrRemove......................................................................................................

    @Test
    public void testSetOrRemoveNonNullValue() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
        this.setOrRemoveAndCheck(
                style,
                TextStylePropertyName.COLOR,
                Color.WHITE,
                style.set(TextStylePropertyName.COLOR, Color.WHITE)
        );
    }

    @Test
    public void testSetOrRemoveAllNullValue() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
        this.setOrRemoveAndCheck(
                style,
                TextStylePropertyName.BACKGROUND_COLOR,
                null,
                style.remove(TextStylePropertyName.BACKGROUND_COLOR)
        );
    }

    @Test
    public void testSetOrRemoveNullValue() {
        final TextStyle style = TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
        this.setOrRemoveAndCheck(
                style,
                TextStylePropertyName.BACKGROUND_COLOR,
                null,
                style.remove(TextStylePropertyName.BACKGROUND_COLOR)
        );
    }

    private <T> void setOrRemoveAndCheck(final TextStyle style,
                                         final TextStylePropertyName<T> name,
                                         final T value,
                                         final TextStyle expected) {
        this.checkEquals(
                expected,
                style.setOrRemove(name, value),
                style + " setOrRemove " + name + " " + value
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
    public void testPatchSetAllWithNonNullFails() {
        final UnsupportedOperationException thrown = assertThrows(
                UnsupportedOperationException.class,
                () -> TextStyle.EMPTY.patch(
                        JsonNode.object()
                                .set(
                                        JsonPropertyName.with(TextStylePropertyName.ALL.value()),
                                        JsonNode.string("This must fail!")
                                ),
                        this.createPatchContext()
                )
        );
        this.checkEquals(
                "Unmarshalling \"*\" with \"This must fail!\" is not supported",
                thrown.getMessage(),
                "message"
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
    public void testPatchRemovePropertyAll() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.ALL.value()), JsonNode.nullNode()),
                TextStyle.EMPTY
        );
    }

    @Test
    public void testPatchRemovePropertyAllManyValues() {
        this.patchAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK)
                        .set(TextStylePropertyName.COLOR, Color.WHITE),
                JsonNode.object()
                        .set(JsonPropertyName.with(TextStylePropertyName.ALL.value()), JsonNode.nullNode()),
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
                ""
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
                "font-family: Banana;"
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
                "font-family: \"Times New Roman\";"
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
                "border-bottom-style: dotted;"
        );
    }

    @Test
    public void testCssBorderBottomWidth() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                                Length.pixel(12.5)
                        ),
                "border-bottom-width: 12.5px;"
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
                "color: rgb(18, 52, 86);"
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
                "font-size: 123;"
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
                "text: has-no-spaces;"
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
                "text: \"has spaces\";"
        );
    }

    @Test
    public void testCssTextSeveralProperties() {
        this.hasCssAndCheck(
                TextStyle.EMPTY
                        .set(
                                TextStylePropertyName.BORDER_TOP_COLOR,
                                Color.parse("#123456")
                        ).set(
                                TextStylePropertyName.BORDER_TOP_STYLE,
                                BorderStyle.DOTTED
                        ).set(
                                TextStylePropertyName.BORDER_TOP_WIDTH,
                                Length.pixel(123.0)
                        ),
                "border-top-color: rgb(18, 52, 86); border-top-style: dotted; border-top-width: 123px;"
        );
    }

    @Test
    public void testCssCached() {
        final TextStyle style = TextStyle.EMPTY
                .set(
                        TextStylePropertyName.TEXT,
                        "has-no-spaces"
                );
        assertSame(
                style.css(),
                style.css()
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(
                TextStyle.EMPTY.setValues(map),
                map.toString()
        );
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
        return TextStyle.EMPTY.setValues(
                Maps.of(
                        this.property1(), this.value1(),
                        this.property2(), this.value2()
                )
        );
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
