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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.color.Color;
import walkingkooka.naming.Name;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * The {@link Name} of an {@link TextStyle} property.
 */
public final class TextStylePropertyName<T> extends TextNodeNameName<TextStylePropertyName<?>> implements HasUrlFragment {

    /**
     * First because required by {@link #CONSTANTS} init.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // constants

    /**
     * A read only cache of already styles.
     */
    private final static Map<String, TextStylePropertyName<?>> CONSTANTS = Maps.sorted(TextStylePropertyName.CASE_SENSITIVITY.comparator());

    /**
     * Returns all {@link TextStylePropertyName}.
     */
    public static Set<TextStylePropertyName<?>> values() {
        final Set<TextStylePropertyName<?>> values = SortedSets.tree();
        values.addAll(CONSTANTS.values());
        return Sets.readOnly(values);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link BorderStyle} values.
     */
    private static TextStylePropertyName<BorderStyle> registerBorderStyle(final String property,
                                                                          final BiConsumer<BorderStyle, TextStyleVisitor> visitor) {
        return registerEnumConstant(
            property,
            BorderStyle.values(),
            BorderStyle.class,
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Color} values.
     */
    private static TextStylePropertyName<Color> registerColor(final String property,
                                                              final BiConsumer<Color, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.color(),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Enum} values.
     */
    private static <E extends Enum<E>> TextStylePropertyName<E> registerEnumConstant(final String property,
                                                                                     final E[] values,
                                                                                     final Class<E> type,
                                                                                     final BiConsumer<E, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.enumTextPropertyValueHandler(
                values,
                type
            ),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NoneLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNoneLengthPixelLengthConstant(final String property,
                                                                                          final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.noneLengthPixelLength(),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NormalLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNormalLengthPixelLengthConstant(final String property,
                                                                                            final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.normalLengthPixelLength(),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NoneLength}, {@link NumberLength} and {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNoneLengthNumberLengthPixelLengthConstant(final String property,
                                                                                                      final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.noneLengthNumberLengthPixelLength(),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link String} values.
     */
    private static TextStylePropertyName<String> registerStringConstant(final String property,
                                                                        final BiConsumer<String, TextStyleVisitor> visitor) {
        return registerConstant(
            property,
            TextStylePropertyValueHandler.string(),
            visitor
        );
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} to the cache being built.
     */
    private static <T> TextStylePropertyName<T> registerConstant(final String property,
                                                                 final TextStylePropertyValueHandler<T> handler,
                                                                 final BiConsumer<T, TextStyleVisitor> visitor) {
        final TextStylePropertyName<T> textStylePropertyName = new TextStylePropertyName<>(
            property,
            handler,
            visitor
        );
        TextStylePropertyName.CONSTANTS.put(
            property,
            textStylePropertyName
        );
        return textStylePropertyName;
    }

    /**
     * A {@link TextStylePropertyName} that matches all properties.
     * Its only practical use is to clear or remove all style properties from a {@link TextStyle}, being the implementation of a clear style from a cell.
     */
    public final static TextStylePropertyName<Void> WILDCARD = registerConstant(
        "*",
        TextStylePropertyValueHandlerVoid.INSTANCE,
        null
    );

    /**
     * background-color
     */
    public final static TextStylePropertyName<Color> BACKGROUND_COLOR = registerColor(
        "background-color",
        (c, v) -> v.visitBackgroundColor(c)
    );

    /**
     * border-bottom-color
     */
    public final static TextStylePropertyName<Color> BORDER_BOTTOM_COLOR = registerColor(
        "border-bottom-color",
        (c, v) -> v.visitBorderBottomColor(c)
    );

    /**
     * border-bottom-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_BOTTOM_STYLE = registerBorderStyle(
        "border-bottom-style",
        (s, v) -> v.visitBorderBottomStyle(s)
    );

    /**
     * border-bottom-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_BOTTOM_WIDTH = registerNoneLengthPixelLengthConstant(
        "border-bottom-width",
        (l, v) -> v.visitBorderBottomWidth(l)
    );

    /**
     * border-collapse
     */
    public final static TextStylePropertyName<BorderCollapse> BORDER_COLLAPSE = registerEnumConstant(
        "border-collapse",
        BorderCollapse.values(),
        BorderCollapse.class,
        (b, v) -> v.visitBorderCollapse(b)
    );

    /**
     * border-color
     */
    public final static TextStylePropertyName<Color> BORDER_COLOR = registerColor(
        "border-color", // property name
        null // visitor should NEVER happen
    );

    /**
     * border-left-color
     */
    public final static TextStylePropertyName<Color> BORDER_LEFT_COLOR = registerColor(
        "border-left-color",
        (c, v) -> v.visitBorderLeftColor(c)
    );

    /**
     * border-left-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_LEFT_STYLE = registerBorderStyle(
        "border-left-style",
        (s, v) -> v.visitBorderLeftStyle(s)
    );

    /**
     * border-left-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_LEFT_WIDTH = registerNoneLengthPixelLengthConstant(
        "border-left-width",
        (l, v) -> v.visitBorderLeftWidth(l)
    );

    /**
     * border-space
     */
    public final static TextStylePropertyName<Length<?>> BORDER_SPACING = registerNormalLengthPixelLengthConstant(
        "border-spacing",
        (l, v) -> v.visitBorderSpacing(l)
    );

    /**
     * border-right-color
     */
    public final static TextStylePropertyName<Color> BORDER_RIGHT_COLOR = registerColor(
        "border-right-color",
        (c, v) -> v.visitBorderRightColor(c)
    );

    /**
     * border-right-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_RIGHT_STYLE = registerBorderStyle(
        "border-right-style",
        (s, v) -> v.visitBorderRightStyle(s)
    );

    /**
     * border-right-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_RIGHT_WIDTH = registerNoneLengthPixelLengthConstant(
        "border-right-width",
        (l, v) -> v.visitBorderRightWidth(l)
    );

    /**
     * border-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_STYLE = registerConstant(
        "border-style", // property name
        TextStylePropertyValueHandler.enumTextPropertyValueHandler(
            BorderStyle.values(),
            BorderStyle.class
        ), // handler
        null // visitor should NEVER happen
    );

    /**
     * border-top-color
     */
    public final static TextStylePropertyName<Color> BORDER_TOP_COLOR = registerColor(
        "border-top-color",
        (c, v) -> v.visitBorderTopColor(c)
    );

    /**
     * border-top-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_TOP_STYLE = registerBorderStyle(
        "border-top-style",
        (s, v) -> v.visitBorderTopStyle(s)
    );

    /**
     * border-top-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_TOP_WIDTH = registerNoneLengthPixelLengthConstant(
        "border-top-width",
        (l, v) -> v.visitBorderTopWidth(l)
    );

    /**
     * border-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_WIDTH = registerConstant(
        "border-width", // property name
        TextStylePropertyValueHandler.noneLengthPixelLength(), // handler
        null // visitor should NEVER happen
    );

    /**
     * color
     */
    public final static TextStylePropertyName<Color> COLOR = registerColor(
        "color",
        (c, v) -> v.visitColor(c)
    );

    /**
     * direction
     */
    public final static TextStylePropertyName<Direction> DIRECTION = registerEnumConstant(
        "direction",
        Direction.values(),
        Direction.class,
        (d, v) -> v.visitDirection(d)
    );

    /**
     * font-family
     */
    public final static TextStylePropertyName<FontFamily> FONT_FAMILY = registerConstant(
        "font-family",
        TextStylePropertyValueHandler.fontFamily(),
        (f, v) -> v.visitFontFamily(f)
    );

    /**
     * font-kerning
     */
    public final static TextStylePropertyName<FontKerning> FONT_KERNING = registerEnumConstant(
        "font-kerning",
        FontKerning.values(),
        FontKerning.class,
        (f, v) -> v.visitFontKerning(f)
    );

    /**
     * font-size
     */
    public final static TextStylePropertyName<FontSize> FONT_SIZE = registerConstant(
        "font-size",
        TextStylePropertyValueHandler.fontSize(),
        (f, v) -> v.visitFontSize(f)
    );

    /**
     * font-stretch
     */
    public final static TextStylePropertyName<FontStretch> FONT_STRETCH = registerEnumConstant(
        "font-stretch",
        FontStretch.values(),
        FontStretch.class,
        (f, v) -> v.visitFontStretch(f)
    );

    /**
     * font-style
     */
    public final static TextStylePropertyName<FontStyle> FONT_STYLE = registerEnumConstant(
        "font-style",
        FontStyle.values(),
        FontStyle.class,
        (f, v) -> v.visitFontStyle(f)
    );

    /**
     * font-variant
     */
    public final static TextStylePropertyName<FontVariant> FONT_VARIANT = registerEnumConstant(
        "font-variant",
        FontVariant.values(),
        FontVariant.class,
        (f, v) -> v.visitFontVariant(f)
    );

    /**
     * font-weight
     */
    public final static TextStylePropertyName<FontWeight> FONT_WEIGHT = registerConstant(
        "font-weight",
        TextStylePropertyValueHandler.fontWeight(),
        (f, v) -> v.visitFontWeight(f)
    );

    /**
     * hanging-punctuation
     */
    public final static TextStylePropertyName<HangingPunctuation> HANGING_PUNCTUATION = registerEnumConstant(
        "hanging-punctuation",
        HangingPunctuation.values(),
        HangingPunctuation.class,
        (h, v) -> v.visitHangingPunctuation(h)
    );

    /**
     * height
     */
    public final static TextStylePropertyName<Length<?>> HEIGHT = registerNoneLengthPixelLengthConstant(
        "height",
        (l, v) -> v.visitHeight(l));

    /**
     * hyphens
     */
    public final static TextStylePropertyName<Hyphens> HYPHENS = registerEnumConstant(
        "hyphens",
        Hyphens.values(),
        Hyphens.class,
        (h, v) -> v.visitHyphens(h)
    );

    /**
     * letter-space
     */
    public final static TextStylePropertyName<Length<?>> LETTER_SPACING = registerNormalLengthPixelLengthConstant(
        "letter-spacing",
        (l, v) -> v.visitLetterSpacing(l)
    );

    /**
     * line-height
     */
    public final static TextStylePropertyName<Length<?>> LINE_HEIGHT = registerNormalLengthPixelLengthConstant(
        "line-height",
        (l, v) -> v.visitLineHeight(l)
    );

    /**
     * list-style-position
     */
    public final static TextStylePropertyName<ListStylePosition> LIST_STYLE_POSITION = registerEnumConstant(
        "list-style-position",
        ListStylePosition.values(),
        ListStylePosition.class,
        (p, v) -> v.visitListStylePosition(p)
    );

    /**
     * list-style-type
     */
    public final static TextStylePropertyName<ListStyleType> LIST_STYLE_TYPE = registerEnumConstant(
        "list-style-type",
        ListStyleType.values(),
        ListStyleType.class,
        (t, v) -> v.visitListStyleType(t)
    );

    /**
     * margin
     */
    public final static TextStylePropertyName<Length<?>> MARGIN = registerConstant(
        "margin", // property name
        TextStylePropertyValueHandler.noneLengthPixelLength(), // handler
        null // visitor should NEVER happen
    );

    /**
     * margin-bottom
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_BOTTOM = registerNoneLengthPixelLengthConstant(
        "margin-bottom",
        (l, v) -> v.visitMarginBottom(l)
    );

    /**
     * margin-left
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_LEFT = registerNoneLengthPixelLengthConstant(
        "margin-left",
        (l, v) -> v.visitMarginLeft(l)
    );

    /**
     * margin-right
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_RIGHT = registerNoneLengthPixelLengthConstant(
        "margin-right",
        (l, v) -> v.visitMarginRight(l)
    );

    /**
     * margin-top
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_TOP = registerNoneLengthPixelLengthConstant(
        "margin-top",
        (l, v) -> v.visitMarginTop(l)
    );

    /**
     * max-height
     */
    public final static TextStylePropertyName<Length<?>> MAX_HEIGHT = registerNoneLengthPixelLengthConstant(
        "max-height",
        (m, v) -> v.visitMaxHeight(m)
    );

    /**
     * max-width
     */
    public final static TextStylePropertyName<Length<?>> MAX_WIDTH = registerNoneLengthPixelLengthConstant(
        "max-width",
        (m, v) -> v.visitMaxWidth(m)
    );

    /**
     * min-height
     */
    public final static TextStylePropertyName<Length<?>> MIN_HEIGHT = registerNoneLengthPixelLengthConstant(
        "min-height",
        (m, v) -> v.visitMinHeight(m)
    );

    /**
     * min-width
     */
    public final static TextStylePropertyName<Length<?>> MIN_WIDTH = registerNoneLengthPixelLengthConstant(
        "min-width",
        (m, v) -> v.visitMinWidth(m)
    );

    /**
     * opacity
     */
    public final static TextStylePropertyName<Opacity> OPACITY = registerConstant(
        "opacity",
        TextStylePropertyValueHandler.opacity(),
        (o, v) -> v.visitOpacity(o)
    );

    /**
     * outline-color
     */
    public final static TextStylePropertyName<Color> OUTLINE_COLOR = registerColor(
        "outline-color",
        (t, v) -> v.visitOutlineColor(t)
    );

    /**
     * outline-offset
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_OFFSET = registerNoneLengthPixelLengthConstant(
        "outline-offset",
        (l, v) -> v.visitOutlineOffset(l)
    );

    /**
     * outline-style
     */
    public final static TextStylePropertyName<OutlineStyle> OUTLINE_STYLE = registerEnumConstant(
        "outline-style",
        OutlineStyle.values(),
        OutlineStyle.class,
        (s, v) -> v.visitOutlineStyle(s)
    );

    /**
     * outline-width
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_WIDTH = registerNoneLengthPixelLengthConstant(
        "outline-width",
        (l, v) -> v.visitOutlineWidth(l)
    );


    /**
     * overflow-wrap
     */
    public final static TextStylePropertyName<OverflowWrap> OVERFLOW_WRAP = registerEnumConstant(
        "overflow-wrap",
        OverflowWrap.values(),
        OverflowWrap.class,
        (o, v) -> v.visitOverflowWrap(o)
    );

    /**
     * overflow-x
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_X = registerEnumConstant(
        "overflow-x",
        Overflow.values(),
        Overflow.class,
        (o, v) -> v.visitOverflowX(o)
    );

    /**
     * overflow-y
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_Y = registerEnumConstant(
        "overflow-y",
        Overflow.values(),
        Overflow.class,
        (o, v) -> v.visitOverflowY(o)
    );

    /**
     * padding
     */
    public final static TextStylePropertyName<Length<?>> PADDING = registerConstant(
        "padding", // property name
        TextStylePropertyValueHandler.noneLengthPixelLength(), // handler
        null // visitor should NEVER happen
    );

    /**
     * padding-bottom
     */
    public final static TextStylePropertyName<Length<?>> PADDING_BOTTOM = registerNoneLengthPixelLengthConstant(
        "padding-bottom",
        (l, v) -> v.visitPaddingBottom(l)
    );

    /**
     * padding-left
     */
    public final static TextStylePropertyName<Length<?>> PADDING_LEFT = registerNoneLengthPixelLengthConstant(
        "padding-left",
        (l, v) -> v.visitPaddingLeft(l)
    );

    /**
     * padding-right
     */
    public final static TextStylePropertyName<Length<?>> PADDING_RIGHT = registerNoneLengthPixelLengthConstant(
        "padding-right",
        (l, v) -> v.visitPaddingRight(l)
    );

    /**
     * padding-top
     */
    public final static TextStylePropertyName<Length<?>> PADDING_TOP = registerNoneLengthPixelLengthConstant(
        "padding-top",
        (l, v) -> v.visitPaddingTop(l)
    );

    /**
     * tab-size
     */
    public final static TextStylePropertyName<Length<?>> TAB_SIZE = registerNoneLengthNumberLengthPixelLengthConstant(
        "tab-size",
        (l, v) -> v.visitTabSize(l)
    );

    /**
     * text
     */
    public final static TextStylePropertyName<String> TEXT = registerStringConstant(
        "text",
        (t, v) -> v.visitText(t)
    );

    /**
     * text-align
     */
    public final static TextStylePropertyName<TextAlign> TEXT_ALIGN = registerEnumConstant(
        "text-align",
        TextAlign.values(),
        TextAlign.class,
        (t, v) -> v.visitTextAlign(t)
    );

    /**
     * text-decoration-color
     */
    public final static TextStylePropertyName<Color> TEXT_DECORATION_COLOR = registerColor(
        "text-decoration-color",
        (t, v) -> v.visitTextDecorationColor(t)
    );

    /**
     * text-decoration-line: UNDERLINE, OVERLINE, LINE_THROUGH
     */
    public final static TextStylePropertyName<TextDecorationLine> TEXT_DECORATION_LINE = registerEnumConstant(
        "text-decoration-line",
        TextDecorationLine.values(),
        TextDecorationLine.class,
        (t, v) -> v.visitTextDecorationLine(t)
    );

    /**
     * text-decoration-style
     */
    public final static TextStylePropertyName<TextDecorationStyle> TEXT_DECORATION_STYLE = registerEnumConstant(
        "text-decoration-style",
        TextDecorationStyle.values(),
        TextDecorationStyle.class,
        (t, v) -> v.visitTextDecorationStyle(t)
    );

    /**
     * text-decoration-thickness
     */
    public final static TextStylePropertyName<Length<?>> TEXT_DECORATION_THICKNESS = registerNoneLengthPixelLengthConstant(
        "text-decoration-thickness",
        (l, v) -> v.visitTextDecorationThickness(l)
    );

    /**
     * text-indent
     */
    public final static TextStylePropertyName<Length<?>> TEXT_INDENT = registerNoneLengthPixelLengthConstant(
        "text-indent",
        (l, v) -> v.visitTextIndent(l)
    );

    /**
     * text-justify
     */
    public final static TextStylePropertyName<TextJustify> TEXT_JUSTIFY = registerEnumConstant(
        "text-justify",
        TextJustify.values(),
        TextJustify.class,
        (t, v) -> v.visitTextJustify(t)
    );

    /**
     * text-overflow
     */
    public final static TextStylePropertyName<TextOverflow> TEXT_OVERFLOW = registerConstant(
        "text-overflow",
        TextStylePropertyValueHandler.textOverflow(),
        (t, v) -> v.visitTextOverflow(t)
    );

    /**
     * text-transform
     */
    public final static TextStylePropertyName<TextTransform> TEXT_TRANSFORM = registerEnumConstant(
        "text-transform",
        TextTransform.values(),
        TextTransform.class,
        (t, v) -> v.visitTextTransform(t)
    );

    /**
     * text-wrapping
     */
    public final static TextStylePropertyName<TextWrapping> TEXT_WRAPPING = registerEnumConstant(
        "text-wrapping",
        TextWrapping.values(),
        TextWrapping.class,
        (t, v) -> v.visitTextWrapping(t)
    );

    /**
     * vertical-align
     */
    public final static TextStylePropertyName<VerticalAlign> VERTICAL_ALIGN = registerEnumConstant(
        "vertical-align",
        VerticalAlign.values(),
        VerticalAlign.class,
        (va, v) -> v.visitVerticalAlign(va)
    );

    /**
     * visibility
     */
    public final static TextStylePropertyName<Visibility> VISIBILITY = registerEnumConstant(
        "visibility",
        Visibility.values(),
        Visibility.class,
        (o, v) -> v.visitVisibility(o)
    );

    /**
     * white-space
     */
    public final static TextStylePropertyName<TextWhitespace> WHITE_SPACE = registerEnumConstant(
        "white-space",
        TextWhitespace.values(),
        TextWhitespace.class,
        (w, v) -> v.visitWhitespace(w)
    );

    /**
     * width
     */
    public final static TextStylePropertyName<Length<?>> WIDTH = registerNoneLengthPixelLengthConstant(
        "width",
        (w, v) -> v.visitWidth(w)
    );

    /**
     * word-break
     */
    public final static TextStylePropertyName<WordBreak> WORD_BREAK = registerEnumConstant(
        "word-break",
        WordBreak.values(),
        WordBreak.class,
        (w, v) -> v.visitWordBreak(w)
    );

    /**
     * word-spacing
     */
    public final static TextStylePropertyName<Length<?>> WORD_SPACING = registerNormalLengthPixelLengthConstant(
        "word-spacing",
        (w, v) -> v.visitWordSpacing(w)
    );

    /**
     * word-wrap
     */
    public final static TextStylePropertyName<WordWrap> WORD_WRAP = registerEnumConstant(
        "word-wrap",
        WordWrap.values(),
        WordWrap.class,
        (w, v) -> v.visitWordWrap(w)
    );

    /**
     * writing-mode
     */
    public final static TextStylePropertyName<WritingMode> WRITING_MODE = registerEnumConstant(
        "writing-mode",
        WritingMode.values(),
        WritingMode.class,
        (w, v) -> v.visitWritingMode(w)
    );

    /**
     * Factory that retrieves an existing property or if unknown a property that assumes non empty string value.
     */
    public static TextStylePropertyName<?> with(final String name) {
        Objects.requireNonNull(name, "name");

        final TextStylePropertyName<?> textStylePropertyName = CONSTANTS.get(name);
        return null != textStylePropertyName ?
            textStylePropertyName :
            new TextStylePropertyName<>(checkName(name),
                TextStylePropertyValueHandler.jsonNodeWithType(),
                TextStylePropertyName::acceptUnknown);
    }

    private static void acceptUnknown(final Object value,
                                      final TextStyleVisitor visitor) {
        visitor.visitUnknown(value);
    }

    private TextStylePropertyName(final String name,
                                  final TextStylePropertyValueHandler<T> handler,
                                  final BiConsumer<T, TextStyleVisitor> visitor) {
        super(name);
        this.handler = handler;
        this.visitor = visitor;

        this.urlFragment = UrlFragment.with(name);
        this.jsonPropertyName = JsonPropertyName.with(
            CaseKind.KEBAB.change(
                name,
                CaseKind.CAMEL
            )
        );
        this.patchRemove = JsonNode.object()
            .set(
                this.jsonPropertyName,
                JsonNode.nullNode()
            );
    }

    /**
     * If the type parameter is for an {@link Enum} return the {@link Class enum class} or {@link Optional#empty()}.
     */
    public Optional<Class<Enum<?>>> enumType() {
        return this.handler.enumType();
    }

    /**
     * Returns this property name as a constant, that is the name is transformed into UPPERCASED form with underscores.
     */
    public String constantName() {
        return this.value()
            .toUpperCase()
            .replace('-', '_');
    }

    /**
     * Parses a {@link String} into the type value of this {@link TextStylePropertyName}.
     */
    public T parseValue(final String value) {
        Objects.requireNonNull(value, "value");

        return this.handler.parseValueText(value);
    }

    /**
     * Verifies that a value is legal for this {@link TextStylePropertyName}
     */
    public void checkValue(final Object value) {
        this.handler.checkValue(value, this);
    }

    /**
     * Used to handle/validate property values.
     */
    final TextStylePropertyValueHandler<T> handler;

    /**
     * Returns the name in quotes, useful for error messages.
     */
    CharSequence inQuotes() {
        return CharSequences.quoteAndEscape(this.name);
    }

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final UrlFragment urlFragment;

    // JsonNodeContext..................................................................................................

    static TextStylePropertyName<?> unmarshall(final JsonNode node) {
        return unmarshall(
            node.name()
                .value()
        );
    }

    static TextStylePropertyName<?> unmarshall(final JsonNode node,
                                               final JsonNodeUnmarshallContext context) {
        return unmarshall(
            node.stringOrFail()
        );
    }

    private static TextStylePropertyName<?> unmarshall(final String value) {
        return with(
            CaseKind.CAMEL.change(
                value,
                CaseKind.KEBAB
            )
        );
    }

    JsonPropertyName marshallName() {
        return this.jsonPropertyName;
    }

    private final JsonPropertyName jsonPropertyName;

    static {
        // force JsonNodeContext.register for collaborating types.
        Color.fromRgb(0);
        FontFamily.with("Times New Roman");
        FontSize.with(1);
        FontWeight.NORMAL.value();
        Length.none();
        Opacity.OPAQUE.value();
        TextOverflow.CLIP.value();

        JsonNodeContext.register("text-style-property-name",
            TextStylePropertyName::unmarshall,
            TextStylePropertyName::marshall,
            TextStylePropertyName.class);
    }

    // TextStyleVisitor.................................................................................................

    /**
     * Dispatches to the appropriate {@link TextStyleVisitor} visit method.
     */
    void accept(final Object value, final TextStyleVisitor visitor) {
        this.visitor.accept(Cast.to(value), visitor);
    }

    /**
     * Calls the appropriate {@link TextStyleVisitor} visit method
     */
    private final BiConsumer<T, TextStyleVisitor> visitor;

    // patch............................................................................................................

    /**
     * Creates a {@link JsonNode} which may be used to patch a {@link TextStyle}.
     */
    public JsonNode stylePatch(final T value) {
        return null == value ?
            this.patchRemove :
            JsonNode.object()
                .set(
                    this.jsonPropertyName,
                    this.handler.marshall(
                        value,
                        MARSHALL_CONTEXT
                    )
                );
    }

    /**
     * Cached {@link JsonNode} for patches with a null value.
     */
    private final JsonNode patchRemove;

    /**
     * Used to marshall a value when creating a patch {@link JsonNode}.
     */
    private final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextStylePropertyName;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
