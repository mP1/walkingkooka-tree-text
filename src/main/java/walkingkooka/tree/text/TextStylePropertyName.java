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
import walkingkooka.color.Color;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The {@link Name} of an {@link TextStyle} property.
 */
public final class TextStylePropertyName<T> extends TextNodeNameName<TextStylePropertyName<?>> {

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
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link BorderStyle} values.
     */
    private static TextStylePropertyName<BorderStyle> registerBorderStyle(final String property,
                                                                          final BiConsumer<BorderStyle, TextStyleVisitor> visitor) {
        return registerEnumConstant(property,
                BorderStyle::valueOf,
                BorderStyle.class,
                v -> v instanceof BorderStyle,
                visitor);
    }
    
    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Color} values.
     */
    private static TextStylePropertyName<Color> registerColor(final String property,
                                                              final BiConsumer<Color, TextStyleVisitor> visitor) {
        return registerJsonNodeConstant(property,
                Color.class,
                v -> v instanceof Color,
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Enum} values.
     */
    private static <E extends Enum<E>> TextStylePropertyName<E> registerEnumConstant(final String property,
                                                                                     final Function<String, E> factory,
                                                                                     final Class<E> type,
                                                                                     final Predicate<Object> typeChecker,
                                                                                     final BiConsumer<E, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.enumTextPropertyValueHandler(factory, type, typeChecker),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Enum} values.
     */
    private static <V> TextStylePropertyName<V> registerJsonNodeConstant(final String property,
                                                                         final Class<V> type,
                                                                         final Predicate<Object> typeTester,
                                                                         final BiConsumer<V, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.jsonNode(type, typeTester),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link Length} values.
     */
    private static TextStylePropertyName<Length<?>> registerLength(final String property,
                                                                   final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        final Class<Length<?>> length = Cast.to(Length.class);
        return registerJsonNodeConstant(property,
                length,
                v -> v instanceof Length,
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NoneLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNoneLengthPixelLengthConstant(final String property,
                                                                                          final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.noneLengthPixelLength(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NormalLength} or {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNormalLengthPixelLengthConstant(final String property,
                                                                                            final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.normalLengthPixelLength(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link PixelLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerPixelLengthConstant(final String property,
                                                                                final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerLength(property, visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link NumberLength} values.
     */
    private static TextStylePropertyName<Length<?>> registerNumberLengthConstant(final String property,
                                                                                 final BiConsumer<Length<?>, TextStyleVisitor> visitor) {
        return registerLength(property, visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} that handles {@link String} values.
     */
    private static TextStylePropertyName<String> registerStringConstant(final String property,
                                                                        final BiConsumer<String, TextStyleVisitor> visitor) {
        return registerConstant(property,
                TextStylePropertyValueHandler.string(),
                visitor);
    }

    /**
     * Creates and adds a new {@link TextStylePropertyName} to the cache being built.
     */
    private static <T> TextStylePropertyName<T> registerConstant(final String property,
                                                                 final TextStylePropertyValueHandler<T> handler,
                                                                 final BiConsumer<T, TextStyleVisitor> visitor) {
        final TextStylePropertyName<T> textStylePropertyName = new TextStylePropertyName<>(property, handler, visitor);
        TextStylePropertyName.CONSTANTS.put(property, textStylePropertyName);
        return textStylePropertyName;
    }

    /**
     * Background rgb
     */
    public final static TextStylePropertyName<Color> BACKGROUND_COLOR = registerColor("background-color",
            (c, v) -> v.visitBackgroundColor(c));

    /**
     * border-bottom-color
     */
    public final static TextStylePropertyName<Color> BORDER_BOTTOM_COLOR = registerColor("border-bottom-color",
            (c, v) -> v.visitBorderBottomColor(c));

    /**
     * border-bottom-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_BOTTOM_STYLE = registerBorderStyle("border-bottom-style",
            (s, v) -> v.visitBorderBottomStyle(s));

    /**
     * border-bottom-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_BOTTOM_WIDTH = registerPixelLengthConstant("border-bottom-width",
            (l, v) -> v.visitBorderBottomWidth(l));

    /**
     * border-collapse
     */
    public final static TextStylePropertyName<BorderCollapse> BORDER_COLLAPSE = registerEnumConstant("border-collapse",
            BorderCollapse::valueOf,
            BorderCollapse.class,
            v -> v instanceof BorderCollapse,
            (b, v) -> v.visitBorderCollapse(b));

    /**
     * border-left-color
     */
    public final static TextStylePropertyName<Color> BORDER_LEFT_COLOR = registerColor("border-left-color",
            (c, v) -> v.visitBorderLeftColor(c));

    /**
     * border-left-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_LEFT_STYLE = registerBorderStyle("border-left-style",
            (s, v) -> v.visitBorderLeftStyle(s));

    /**
     * border-left-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_LEFT_WIDTH = registerPixelLengthConstant("border-left-width",
            (l, v) -> v.visitBorderLeftWidth(l));


    /**
     * border-space
     */
    public final static TextStylePropertyName<BorderSpacing> BORDER_SPACING = registerJsonNodeConstant("border-spacing",
            BorderSpacing.class,
            v -> v instanceof BorderSpacing,
            (l, v) -> v.visitBorderSpacing(l));

    /**
     * border-right-color
     */
    public final static TextStylePropertyName<Color> BORDER_RIGHT_COLOR = registerColor("border-right-color",
            (c, v) -> v.visitBorderRightColor(c));

    /**
     * border-right-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_RIGHT_STYLE = registerBorderStyle("border-right-style",
            (s, v) -> v.visitBorderRightStyle(s));

    /**
     * border-right-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_RIGHT_WIDTH = registerPixelLengthConstant("border-right-width",
            (l, v) -> v.visitBorderRightWidth(l));

    /**
     * border-top-color
     */
    public final static TextStylePropertyName<Color> BORDER_TOP_COLOR = registerColor("border-top-color",
            (c, v) -> v.visitBorderTopColor(c));

    /**
     * border-top-style
     */
    public final static TextStylePropertyName<BorderStyle> BORDER_TOP_STYLE = registerBorderStyle("border-top-style",
            (s, v) -> v.visitBorderTopStyle(s));

    /**
     * border-top-width
     */
    public final static TextStylePropertyName<Length<?>> BORDER_TOP_WIDTH = registerPixelLengthConstant("border-top-width",
            (l, v) -> v.visitBorderTopWidth(l));

    /**
     * font-family-name
     */
    public final static TextStylePropertyName<FontFamilyName> FONT_FAMILY_NAME = registerJsonNodeConstant("font-family-name",
            FontFamilyName.class,
            v -> v instanceof FontFamilyName,
            (f, v) -> v.visitFontFamilyName(f));

    /**
     * font-kerning
     */
    public final static TextStylePropertyName<FontKerning> FONT_KERNING = registerEnumConstant("font-kerning",
            FontKerning::valueOf,
            FontKerning.class,
            v -> v instanceof FontKerning,
            (f, v) -> v.visitFontKerning(f));

    /**
     * font-size
     */
    public final static TextStylePropertyName<FontSize> FONT_SIZE = registerJsonNodeConstant("font-size",
            FontSize.class,
            v -> v instanceof FontSize,
            (f, v) -> v.visitFontSize(f));

    /**
     * font-stretch
     */
    public final static TextStylePropertyName<FontStretch> FONT_STRETCH = registerEnumConstant("font-stretch",
            FontStretch::valueOf,
            FontStretch.class,
            v -> v instanceof FontStretch,
            (f, v) -> v.visitFontStretch(f));

    /**
     * font-style
     */
    public final static TextStylePropertyName<FontStyle> FONT_STYLE = registerEnumConstant("font-style",
            FontStyle::valueOf,
            FontStyle.class,
            v -> v instanceof FontStyle,
            (f, v) -> v.visitFontStyle(f));

    /**
     * font-variant
     */
    public final static TextStylePropertyName<FontVariant> FONT_VARIANT = registerEnumConstant("font-variant",
            FontVariant::valueOf,
            FontVariant.class,
            v -> v instanceof FontVariant,
            (f, v) -> v.visitFontVariant(f));

    /**
     * font-weight
     */
    public final static TextStylePropertyName<FontWeight> FONT_WEIGHT = registerJsonNodeConstant("font-weight",
            FontWeight.class,
            v -> v instanceof FontWeight,
            (f, v) -> v.visitFontWeight(f));

    /**
     * hanging-punctuation
     */
    public final static TextStylePropertyName<HangingPunctuation> HANGING_PUNCTUATION = registerEnumConstant("hanging-punctuation",
            HangingPunctuation::valueOf,
            HangingPunctuation.class,
            v -> v instanceof HangingPunctuation,
            (h, v) -> v.visitHangingPunctuation(h));

    /**
     * horizontal-alignment
     */
    public final static TextStylePropertyName<HorizontalAlignment> HORIZONTAL_ALIGNMENT = registerEnumConstant("horizontal-alignment",
            HorizontalAlignment::valueOf,
            HorizontalAlignment.class,
            v -> v instanceof HorizontalAlignment,
            (h, v) -> v.visitHorizontalAlignment(h));

    /**
     * height
     */
    public final static TextStylePropertyName<Length<?>> HEIGHT = registerPixelLengthConstant("height",
            (l, v) -> v.visitHeight(l));

    /**
     * hyphens
     */
    public final static TextStylePropertyName<Hyphens> HYPHENS = registerEnumConstant("hyphens",
            Hyphens::valueOf,
            Hyphens.class,
            v -> v instanceof Hyphens,
            (h, v) -> v.visitHyphens(h));

    /**
     * letter-space
     */
    public final static TextStylePropertyName<LetterSpacing> LETTER_SPACING = registerJsonNodeConstant("letter-spacing",
            LetterSpacing.class,
            v -> v instanceof LetterSpacing,
            (l, v) -> v.visitLetterSpacing(l));

    /**
     * line-height
     */
    public final static TextStylePropertyName<Length<?>> LINE_HEIGHT = registerNormalLengthPixelLengthConstant("line-height",
            (l, v) -> v.visitLineHeight(l));

    /**
     * list-style-position
     */
    public final static TextStylePropertyName<ListStylePosition> LIST_STYLE_POSITION = registerEnumConstant("list-style-position",
            ListStylePosition::valueOf,
            ListStylePosition.class,
            v -> v instanceof ListStylePosition,
            (p, v) -> v.visitListStylePosition(p));

    /**
     * list-style-type
     */
    public final static TextStylePropertyName<ListStyleType> LIST_STYLE_TYPE = registerEnumConstant("list-style-type",
            ListStyleType::valueOf,
            ListStyleType.class,
            v -> v instanceof ListStyleType,
            (t, v) -> v.visitListStyleType(t));

    /**
     * margin-bottom
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_BOTTOM = registerPixelLengthConstant("margin-bottom",
            (l, v) -> v.visitMarginBottom(l));

    /**
     * margin-left
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_LEFT = registerPixelLengthConstant("margin-left",
            (l, v) -> v.visitMarginLeft(l));

    /**
     * margin-right
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_RIGHT = registerPixelLengthConstant("margin-right",
            (l, v) -> v.visitMarginRight(l));

    /**
     * margin-top
     */
    public final static TextStylePropertyName<Length<?>> MARGIN_TOP = registerPixelLengthConstant("margin-top",
            (l, v) -> v.visitMarginTop(l));

    /**
     * max-height
     */
    public final static TextStylePropertyName<Length<?>> MAX_HEIGHT = registerNoneLengthPixelLengthConstant("max-height",
            (m, v) -> v.visitMaxHeight(m));

    /**
     * max-width
     */
    public final static TextStylePropertyName<Length<?>> MAX_WIDTH = registerNoneLengthPixelLengthConstant("max-width",
            (m, v) -> v.visitMaxWidth(m));

    /**
     * min-height
     */
    public final static TextStylePropertyName<Length<?>> MIN_HEIGHT = registerPixelLengthConstant("min-height",
            (m, v) -> v.visitMinHeight(m));

    /**
     * min-width
     */
    public final static TextStylePropertyName<Length<?>> MIN_WIDTH = registerPixelLengthConstant("min-width",
            (m, v) -> v.visitMinWidth(m));

    /**
     * opacity
     */
    public final static TextStylePropertyName<Opacity> OPACITY = registerJsonNodeConstant("opacity",
            Opacity.class,
            v -> v instanceof Opacity,
            (o, v) -> v.visitOpacity(o));

    /**
     * outline-color
     */
    public final static TextStylePropertyName<Color> OUTLINE_COLOR = registerColor("outline-color",
            (t, v) -> v.visitOutlineColor(t));

    /**
     * outline-offset
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_OFFSET = registerPixelLengthConstant("outline-offset",
            (l, v) -> v.visitOutlineOffset(l));

    /**
     * outline-style
     */
    public final static TextStylePropertyName<OutlineStyle> OUTLINE_STYLE = registerEnumConstant("outline-style",
            OutlineStyle::valueOf,
            OutlineStyle.class,
            v -> v instanceof OutlineStyle,
            (s, v) -> v.visitOutlineStyle(s));

    /**
     * outline-width
     */
    public final static TextStylePropertyName<Length<?>> OUTLINE_WIDTH = registerPixelLengthConstant("outline-width",
            (l, v) -> v.visitOutlineWidth(l));

    /**
     * overflow-x
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_X = registerEnumConstant("overflow-x",
            Overflow::valueOf,
            Overflow.class,
            v -> v instanceof Overflow,
            (o, v) -> v.visitOverflowX(o));

    /**
     * overflow-y
     */
    public final static TextStylePropertyName<Overflow> OVERFLOW_Y = registerEnumConstant("overflow-y",
            Overflow::valueOf,
            Overflow.class,
            v -> v instanceof Overflow,
            (o, v) -> v.visitOverflowY(o));

    /**
     * padding-bottom
     */
    public final static TextStylePropertyName<Length<?>> PADDING_BOTTOM = registerPixelLengthConstant("padding-bottom",
            (l, v) -> v.visitPaddingBottom(l));

    /**
     * padding-left
     */
    public final static TextStylePropertyName<Length<?>> PADDING_LEFT = registerPixelLengthConstant("padding-left",
            (l, v) -> v.visitPaddingLeft(l));

    /**
     * padding-right
     */
    public final static TextStylePropertyName<Length<?>> PADDING_RIGHT = registerPixelLengthConstant("padding-right",
            (l, v) -> v.visitPaddingRight(l));

    /**
     * padding-top
     */
    public final static TextStylePropertyName<Length<?>> PADDING_TOP = registerPixelLengthConstant("padding-top",
            (l, v) -> v.visitPaddingTop(l));

    /**
     * tab-size
     */
    public final static TextStylePropertyName<Length<?>> TAB_SIZE = registerNumberLengthConstant("tab-size",
            (l, v) -> v.visitTabSize(l));

    /**
     * text
     */
    public final static TextStylePropertyName<String> TEXT = registerStringConstant("text",
            (t, v) -> v.visitText(t));

    /**
     * text-alignment
     */
    public final static TextStylePropertyName<TextAlignment> TEXT_ALIGNMENT = registerEnumConstant("text-align",
            TextAlignment::valueOf,
            TextAlignment.class,
            v -> v instanceof TextAlignment,
            (t, v) -> v.visitTextAlignment(t));

    /**
     * Text rgb
     */
    public final static TextStylePropertyName<Color> TEXT_COLOR = registerColor("text-color",
            (t, v) -> v.visitTextColor(t));

    /**
     * text-decoration: UNDERLINE, OVERLINE, LINE_THROUGH
     */
    public final static TextStylePropertyName<TextDecoration> TEXT_DECORATION = registerEnumConstant("text-decoration",
            TextDecoration::valueOf,
            TextDecoration.class,
            v -> v instanceof TextDecoration,
            (t, v) -> v.visitTextDecoration(t));

    /**
     * text-decoration-color
     */
    public final static TextStylePropertyName<Color> TEXT_DECORATION_COLOR = registerColor("text-decoration-color",
            (t, v) -> v.visitTextDecorationColor(t));

    /**
     * text-decoration-style
     */
    public final static TextStylePropertyName<TextDecorationStyle> TEXT_DECORATION_STYLE = registerEnumConstant("text-decoration-style",
            TextDecorationStyle::valueOf,
            TextDecorationStyle.class,
            v -> v instanceof TextDecorationStyle,
            (t, v) -> v.visitTextDecorationStyle(t));

    /**
     * text-direction
     */
    public final static TextStylePropertyName<TextDirection> TEXT_DIRECTION = registerEnumConstant("text-direction",
            TextDirection::valueOf,
            TextDirection.class,
            v -> v instanceof TextDirection,
            (d, v) -> v.visitTextDirection(d));

    /**
     * text-indent
     */
    public final static TextStylePropertyName<Length<?>> TEXT_INDENT = registerPixelLengthConstant("text-indent",
            (l, v) -> v.visitTextIndent(l));

    /**
     * text-justify
     */
    public final static TextStylePropertyName<TextJustify> TEXT_JUSTIFY = registerEnumConstant("text-justify",
            TextJustify::valueOf,
            TextJustify.class,
            v -> v instanceof TextJustify,
            (t, v) -> v.visitTextJustify(t));

    /**
     * text-overflow
     */
    public final static TextStylePropertyName<TextOverflow> TEXT_OVERFLOW = registerJsonNodeConstant("text-overflow",
            TextOverflow.class,
            v -> v instanceof TextOverflow,
            (t, v) -> v.visitTextOverflow(t));

    /**
     * text-transform
     */
    public final static TextStylePropertyName<TextTransform> TEXT_TRANSFORM = registerEnumConstant("text-transform",
            TextTransform::valueOf,
            TextTransform.class,
            v -> v instanceof TextTransform,
            (t, v) -> v.visitTextTransform(t));

    /**
     * text-wrapping
     */
    public final static TextStylePropertyName<TextWrapping> TEXT_WRAPPING = registerEnumConstant("text-wrapping",
            TextWrapping::valueOf,
            TextWrapping.class,
            v -> v instanceof TextWrapping,
            (t, v) -> v.visitTextWrapping(t));

    /**
     * vertical-alignment
     */
    public final static TextStylePropertyName<VerticalAlignment> VERTICAL_ALIGNMENT = registerEnumConstant("vertical-alignment",
            VerticalAlignment::valueOf,
            VerticalAlignment.class,
            v -> v instanceof VerticalAlignment,
            (va, v) -> v.visitVerticalAlignment(va));

    /**
     * visibility
     */
    public final static TextStylePropertyName<Visibility> VISIBILITY = registerEnumConstant("visibility",
            Visibility::valueOf,
            Visibility.class,
            v -> v instanceof Visibility,
            (o, v) -> v.visitVisibility(o));

    /**
     * white-space
     */
    public final static TextStylePropertyName<TextWhitespace> WHITE_SPACE = registerEnumConstant("white-space",
            TextWhitespace::valueOf,
            TextWhitespace.class,
            v -> v instanceof TextWhitespace,
            (w, v) -> v.visitWhitespace(w));

    /**
     * width
     */
    public final static TextStylePropertyName<Length<?>> WIDTH = registerPixelLengthConstant("width",
            (w, v) -> v.visitWidth(w));

    /**
     * word-break
     */
    public final static TextStylePropertyName<WordBreak> WORD_BREAK = registerEnumConstant("word-break",
            WordBreak::valueOf,
            WordBreak.class,
            v -> v instanceof WordBreak,
            (w, v) -> v.visitWordBreak(w));

    /**
     * word-spacing
     */
    public final static TextStylePropertyName<WordSpacing> WORD_SPACING = registerJsonNodeConstant("word-spacing",
            WordSpacing.class,
            v -> v instanceof WordSpacing,
            (w, v) -> v.visitWordSpacing(w));

    /**
     * word-wrap
     */
    public final static TextStylePropertyName<WordWrap> WORD_WRAP = registerEnumConstant("word-wrap",
            WordWrap::valueOf,
            WordWrap.class,
            v -> v instanceof WordWrap,
            (w, v) -> v.visitWordWrap(w));

    /**
     * writing-mode
     */
    public final static TextStylePropertyName<WritingMode> WRITING_MODE = registerEnumConstant("writing-mode",
            WritingMode::valueOf,
            WritingMode.class,
            v -> v instanceof WritingMode,
            (w, v) -> v.visitWritingMode(w));

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

    private static void acceptUnknown(final Object value, final TextStyleVisitor visitor) {
        visitor.visitUnknown(value);
    }

    private TextStylePropertyName(final String name,
                                  final TextStylePropertyValueHandler<T> handler,
                                  final BiConsumer<T, TextStyleVisitor> visitor) {
        super(name);
        this.handler = handler;
        this.visitor = visitor;

        this.jsonPropertyName = JsonPropertyName.with(this.name);
    }

    /**
     * Verifies that a value is legal for this {@link TextStylePropertyName}
     */
    public void check(final Object value) {
        this.handler.check(value, this);
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

    // JsonNodeContext..................................................................................................

    static TextStylePropertyName<?> unmarshallEntryKey(final JsonNode node) {
        return with(node.name().value());
    }

    static TextStylePropertyName<?> unmarshall(final JsonNode node,
                                               final JsonNodeUnmarshallContext context) {
        return with(node.stringValueOrFail());
    }

    JsonPropertyName marshallName() {
        return this.jsonPropertyName;
    }

    private final JsonPropertyName jsonPropertyName;

    static {
        // force JsonNodeContext.register for collaborating types.
        BorderSpacing.with(Length.pixel(1.0));
        FontFamilyName.with("Times New Roman");
        FontSize.with(1);
        FontWeight.NORMAL.value();
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
