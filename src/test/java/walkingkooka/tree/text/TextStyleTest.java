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
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;
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
    HasTextTesting,
    HasTextStyleTesting,
    ParseStringTesting<TextStyle>,
    JsonNodeMarshallingTesting<TextStyle>,
    PatchableTesting<TextStyle>,
    ToStringTesting<TextStyle> {

    // isStyleClass.....................................................................................................

    @Test
    public void testIsStyleClassWithNull() {
        this.isStyleClassAndCheck(
            null,
            false
        );
    }

    @Test
    public void testIsStyleClassWithTextStyle() {
        this.isStyleClassAndCheck(
            TextStyle.class,
            true
        );
    }

    @Test
    public void testIsStyleClassWithTextStyleEmpty() {
        this.isStyleClassAndCheck(
            TextStyle.EMPTY.getClass(),
            true
        );
    }

    @Test
    public void testIsStyleClassWithTextStyleNotEmpty() {
        this.isStyleClassAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ).getClass(),
            true
        );
    }

    private void isStyleClassAndCheck(final Class<?> klass,
                                      final boolean expected) {
        this.checkEquals(
            expected,
            TextStyle.isStyleClass(klass),
            () -> null != klass ? klass.getName() : null
        );
    }

    // value............................................................................................................

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyle textStyle = TextStyle.EMPTY.setValues(map);
        this.checkEquals(
            TextStylePropertiesMap.class,
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
            TextStyleNode.with(children, TextStylePropertiesMap.EMPTY));
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
            TextStyleNode.with(children, TextStylePropertiesMap.EMPTY));
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
    public void testGetOrFailWildcard() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createObject()
                .getOrFail(TextStylePropertyName.WILDCARD)
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
            .set(TextStylePropertyName.OVERFLOW_WRAP, OverflowWrap.NORMAL)
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
    public void testPatchSetWildcardWithNonNullFails() {
        final UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            () -> TextStyle.EMPTY.patch(
                JsonNode.object()
                    .set(
                        JsonPropertyName.with(TextStylePropertyName.WILDCARD.value()),
                        "This must fail!"
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
    public void testPatchBorderColor() {
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        final Color color = Color.WHITE;

        this.patchAndCheck(
            style,
            TextStylePropertyName.BORDER_COLOR.stylePatch(color),
            style.setValues(
                Maps.of(
                    TextStylePropertyName.BORDER_TOP_COLOR, color,
                    TextStylePropertyName.BORDER_LEFT_COLOR, color,
                    TextStylePropertyName.BORDER_RIGHT_COLOR, color,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR, color
                )
            )
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
    public void testPatchRemovePropertyWildcard() {
        this.patchAndCheck(
            TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK),
            JsonNode.object()
                .set(
                    JsonPropertyName.with(
                        TextStylePropertyName.WILDCARD.value()
                    ),
                    JsonNode.nullNode()
                ),
            TextStyle.EMPTY
        );
    }

    @Test
    public void testPatchRemovePropertyWildcardManyValues() {
        this.patchAndCheck(
            TextStyle.EMPTY
                .set(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK)
                .set(TextStylePropertyName.COLOR, Color.WHITE),
            JsonNode.object()
                .set(
                    JsonPropertyName.with(
                        TextStylePropertyName.WILDCARD.value())
                    , JsonNode.nullNode()
                ),
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
            (String cc) -> {
                throw new UnsupportedOperationException();
            },
            (String lt) -> {
                throw new UnsupportedOperationException();
            },
            ExpressionNumberKind.BIG_DECIMAL,
            MathContext.UNLIMITED
        );
    }

    private JsonNode marshall(final Object value) {
        return JsonNodeMarshallContexts.basic()
            .marshall(value);
    }

    // text..............................................................................................................

    @Test
    public void testTextEmpty() {
        this.textAndCheck(
            TextStyle.EMPTY,
            ""
        );
    }

    @Test
    public void testTextStringValueWithoutSpaces() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.FONT_FAMILY,
                    FontFamily.with("Banana")
                ),
            "font-family: Banana;"
        );
    }

    @Test
    public void testTextStringValueWithSpaces() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.FONT_FAMILY,
                    FontFamily.with("Times New Roman")
                ),
            "font-family: \"Times New Roman\";"
        );
    }

    @Test
    public void testTextBorderBottomStyleEnum() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.BORDER_BOTTOM_STYLE,
                    BorderStyle.DOTTED
                ),
            "border-bottom-style: dotted;"
        );
    }

    @Test
    public void testTextBorderBottomWidth() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                    Length.pixel(12.5)
                ),
            "border-bottom-width: 12.5px;"
        );
    }

    @Test
    public void testTextBorderColor() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.BORDER_COLOR,
                    Color.BLACK
                ),
            "border-bottom-color: black; border-left-color: black; border-right-color: black; border-top-color: black;"
        );
    }

    @Test
    public void testTextColor() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.COLOR,
                    Color.parse("#123456")
                ),
            "color: #123456;"
        );
    }

    @Test
    public void testTextFontSize() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.FONT_SIZE,
                    FontSize.with(123)
                ),
            "font-size: 123;"
        );
    }

    @Test
    public void testTextOverflowWrap() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.OVERFLOW_WRAP,
                    OverflowWrap.BREAK_WORD
                ),
            "overflow-wrap: break-word;"
        );
    }

    @Test
    public void testText() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.TEXT,
                    "has-no-spaces"
                ),
            "text: has-no-spaces;"
        );
    }

    @Test
    public void testTextIncludesSpacesRequiresQuotes() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.TEXT,
                    "has spaces"
                ),
            "text: \"has spaces\";"
        );
    }

    @Test
    public void testTextOverflowClip() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.TEXT_OVERFLOW,
                    TextOverflow.CLIP
                ),
            "text-overflow: clip;"
        );
    }

    @Test
    public void testTextOverflowEllipsis() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.TEXT_OVERFLOW,
                    TextOverflow.ELLIPSIS
                ),
            "text-overflow: ellipsis;"
        );
    }

    @Test
    public void testTextOverflowString() {
        this.textAndCheck(
            TextStyle.EMPTY
                .set(
                    TextStylePropertyName.TEXT_OVERFLOW,
                    TextOverflow.string("Hello")
                ),
            "text-overflow: \"Hello\";"
        );
    }

    @Test
    public void testTextSeveralProperties() {
        this.textAndCheck(
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
            "border-top-color: #123456; border-top-style: dotted; border-top-width: 123px;"
        );
    }

    @Test
    public void testTextCached() {
        final TextStyle style = TextStyle.EMPTY
            .set(
                TextStylePropertyName.TEXT,
                "has-no-spaces"
            );
        assertSame(
            style.text(),
            style.text()
        );
    }

    // parseString......................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseStringEmpty() {
        this.parseStringAndCheck(
            "",
            TextStyle.EMPTY
        );
    }

    @Test
    public void testParseInvalidEntryFails() {
        this.parseStringInvalidCharacterFails(
            "color=white",
            '='
        );
    }

    @Test
    public void testParseInvalidEntryFails2() {
        this.parseStringInvalidCharacterFails(
            "color: white;text-align=left",
            '='
        );
    }

    @Test
    public void testParseColorInvalidCharacterFails() {
        this.parseStringFails(
            "color: #123XYZ",
            new IllegalArgumentException("Invalid rgb \"#123XYZ\"")
        );
    }

    @Test
    public void testParseColorHash6() {
        this.parseStringAndCheck(
            "color: #123456",
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            )
        );
    }

    @Test
    public void testParseColorRgbWithSpaces() {
        final String text = "rgb(214, 122, 127)";

        this.parseStringAndCheck(
            "color: " + text,
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse(text)
            )
        );
    }

    @Test
    public void testParseColorRgbWithSpacesSeparator() {
        final String text = "rgb(214, 122, 127)";

        this.parseStringAndCheck(
            "color: " + text + ";",
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse(text)
            )
        );
    }

    @Test
    public void testParseEnumTextAlignFail() {
        this.parseStringFails(
            "text-align: BAD",
            new IllegalArgumentException("Unknown value \"BAD\"")
        );
    }

    @Test
    public void testParseEnumTextAlign() {
        this.parseStringAndCheck(
            "text-align: LEFT",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            )
        );
    }

    @Test
    public void testParseFontFamilyQuoted() {
        this.parseStringAndCheck(
            "font-family: \"Hello 123\"",
            TextStyle.EMPTY.set(
                TextStylePropertyName.FONT_FAMILY,
                FontFamily.with("Hello 123")
            )
        );
    }

    @Test
    public void testParseFontFamilyUnquoted() {
        this.parseStringAndCheck(
            "font-family: Hello",
            TextStyle.EMPTY.set(
                TextStylePropertyName.FONT_FAMILY,
                FontFamily.with("Hello")
            )
        );
    }

    @Test
    public void testParseFontSize() {
        this.parseStringAndCheck(
            "font-size: 123",
            TextStyle.EMPTY.set(
                TextStylePropertyName.FONT_SIZE,
                FontSize.with(123)
            )
        );
    }

    @Test
    public void testParseNoneLengthPixelLengthWithNone() {
        this.parseStringAndCheck(
            "border-top-width: none",
            TextStyle.EMPTY.set(
                TextStylePropertyName.BORDER_TOP_WIDTH,
                Length.none()
            )
        );
    }

    @Test
    public void testParseNoneLengthPixelLengthWithPixels() {
        this.parseStringAndCheck(
            "border-top-width: 12px",
            TextStyle.EMPTY.set(
                TextStylePropertyName.BORDER_TOP_WIDTH,
                Length.pixel(12.0)
            )
        );
    }

    @Test
    public void testParseNoneLengthNumberLengthPixelLengthWithNone() {
        this.parseStringAndCheck(
            "tab-size: none",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TAB_SIZE,
                Length.none()
            )
        );
    }

    @Test
    public void testParseNoneLengthNumberLengthPixelLengthWithNumber() {
        this.parseStringAndCheck(
            "tab-size: 12",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TAB_SIZE,
                Length.number(12)
            )
        );
    }

    @Test
    public void testParseNoneLengthNumberLengthPixelLengthWithPixels() {
        this.parseStringAndCheck(
            "tab-size: 12px",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TAB_SIZE,
                Length.pixel(12.0)
            )
        );
    }

    @Test
    public void testParseNoneLengthNormalLengthPixelLengthWithNoneFails() {
        this.parseStringFails(
            "word-spacing: none",
            new IllegalArgumentException("Property \"word-spacing\": Expected NoneLength | PixelLength got NoneLength")
        );
    }

    @Test
    public void testParseNoneLengthNormalLengthPixelLengthWithNumberFails() {
        this.parseStringFails(
            "word-spacing: 12",
            new IllegalArgumentException("Property \"word-spacing\": Expected NoneLength | PixelLength got NumberLength")
        );
    }

    @Test
    public void testParseNoneLengthNormalLengthPixelLengthWithPixels() {
        this.parseStringAndCheck(
            "word-spacing: 12px",
            TextStyle.EMPTY.set(
                TextStylePropertyName.WORD_SPACING,
                Length.pixel(12.0)
            )
        );
    }

    @Test
    public void testParseOpacityOpaque() {
        this.parseStringAndCheck(
            "opacity: opaque",
            TextStyle.EMPTY.set(
                TextStylePropertyName.OPACITY,
                Opacity.OPAQUE
            )
        );
    }

    @Test
    public void testParseOpacityNumber() {
        this.parseStringAndCheck(
            "opacity: 0.25",
            TextStyle.EMPTY.set(
                TextStylePropertyName.OPACITY,
                Opacity.with(0.25)
            )
        );
    }

    @Test
    public void testParseString2() {
        this.parseStringAndCheck(
            "text: \"Hello 123\"",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT,
                "Hello 123"
            )
        );
    }

    @Test
    public void testParseTextOverflowClip() {
        this.parseStringAndCheck(
            "text-overflow: clip",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_OVERFLOW,
                TextOverflow.CLIP
            )
        );
    }

    @Test
    public void testParseTextOverflowString() {
        this.parseStringAndCheck(
            "text-overflow: \"Hello 123\"",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_OVERFLOW,
                TextOverflow.string("Hello 123")
            )
        );
    }

    @Test
    public void testParseSeveral() {
        this.parseStringAndCheck(
            "text: \"Hello\"; color: #123456",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT,
                "Hello"
            ).set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            )
        );
    }

    @Test
    public void testParseSeveral2() {
        this.parseStringAndCheck(
            "text: \"Hello\"; color: #123456; text-align: LEFT",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT,
                "Hello"
            ).set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            ).set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            )
        );
    }

    @Test
    public void testParseAndTextRoundtrip() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.parse("#123456")
        ).set(
            TextStylePropertyName.BACKGROUND_COLOR,
            Color.parse("#123456").toHsl()
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        ).set(
            TextStylePropertyName.FONT_FAMILY,
            FontFamily.with("Hello 123")
        ).set(
            TextStylePropertyName.FONT_SIZE,
            FontSize.with(1234)
        ).set(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.BOLD
        ).set(
            TextStylePropertyName.BORDER_TOP_WIDTH,
            Length.pixel(456.0)
        ).set(
            TextStylePropertyName.OPACITY,
            Opacity.with(0.25)
        ).set(
            TextStylePropertyName.TEXT_OVERFLOW,
            TextOverflow.CLIP
        );

        this.parseStringAndCheck(
            textStyle.text(),
            textStyle
        );
    }

    @Override
    public TextStyle parseString(final String text) {
        return TextStyle.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // hashCode/equals..................................................................................................

    @Override
    public TextStyle createObject() {
        return this.textStyle();
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
    public void testToStringWithRgbColorProperty() {
        this.toStringAndCheck(
            TextStyle.EMPTY.setValues(
                Maps.of(
                    TextStylePropertyName.COLOR,
                    Color.parse("#123456")
                )
            ),
            "{color=#123456}"
        );
    }

    // json.............................................................................................................

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
    public TextStyle unmarshall(final JsonNode from,
                                final JsonNodeUnmarshallContext context) {
        return TextStyle.unmarshall(from, context);
    }

    @Override
    public TextStyle createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    // helpers..........................................................................................................

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

    // HasTextStyle.....................................................................................................

    @Test
    public void testTextStyle() {
        final TextStyle textStyle = this.textStyle();

        assertSame(
            textStyle,
            textStyle.textStyle()
        );
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
}
