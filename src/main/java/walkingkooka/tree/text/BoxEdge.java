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

import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * One of the four edge around a box with methods to retrieve {@link Border}, {@link Margin} and {@link Padding}
 * style view.
 */
public enum BoxEdge {
    BOTTOM {
        @Override
        public BoxEdge flip() {
            return TOP;
        }

        @Override
        public TextStylePropertyName<Color> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_COLOR;
        }

        @Override
        public TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_STYLE;
        }

        @Override
        public TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_WIDTH;
        }

        @Override
        public TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_BOTTOM;
        }

        @Override
        public TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_BOTTOM;
        }

        @Override
        boolean isTextStyleProperty(final String propertyName) {
            return propertyName.startsWith("border-bottom") ||
                propertyName.startsWith("margin-bottom") ||
                propertyName.startsWith("padding-bottom");
        }
    },
    LEFT {
        @Override
        public BoxEdge flip() {
            return RIGHT;
        }

        @Override
        public TextStylePropertyName<Color> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_LEFT_COLOR;
        }

        @Override
        public TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_LEFT_STYLE;
        }

        @Override
        public TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_LEFT_WIDTH;
        }

        @Override
        public TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_LEFT;
        }

        @Override
        public TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_LEFT;
        }

        @Override
        boolean isTextStyleProperty(final String propertyName) {
            return propertyName.startsWith("border-left") ||
                propertyName.startsWith("margin-left") ||
                propertyName.startsWith("padding-left");
        }
    },
    RIGHT {
        @Override
        public BoxEdge flip() {
            return LEFT;
        }

        @Override
        public TextStylePropertyName<Color> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_COLOR;
        }

        @Override
        public TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_STYLE;
        }

        @Override
        public TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_WIDTH;
        }

        @Override
        public TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_RIGHT;
        }

        @Override
        public TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_RIGHT;
        }

        @Override
        boolean isTextStyleProperty(final String propertyName) {
            return propertyName.startsWith("border-right") ||
                propertyName.startsWith("margin-right") ||
                propertyName.startsWith("padding-right");
        }
    },
    TOP {
        @Override
        public BoxEdge flip() {
            return BOTTOM;
        }

        @Override
        public TextStylePropertyName<Color> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_TOP_COLOR;
        }

        @Override
        public TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_TOP_STYLE;
        }

        @Override
        public TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_TOP_WIDTH;
        }

        @Override
        public TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_TOP;
        }

        @Override
        public TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_TOP;
        }

        @Override
        boolean isTextStyleProperty(final String propertyName) {
            return propertyName.startsWith("border-top") ||
                propertyName.startsWith("margin-top") ||
                propertyName.startsWith("padding-top");
        }
    },

    ALL {
        @Override
        public BoxEdge flip() {
            return ALL;
        }

        @Override
        public TextStylePropertyName<Color> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_COLOR;
        }

        @Override
        public TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_STYLE;
        }

        @Override
        public TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_WIDTH;
        }

        @Override
        public TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN;
        }

        @Override
        public TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING;
        }

        @Override
        boolean isTextStyleProperty(final String propertyName) {
            return propertyName.startsWith("border") ||
                propertyName.startsWith("margin") ||
                propertyName.startsWith("padding");
        }
    };

    public abstract BoxEdge flip();

    // border...........................................................................................................

    /**
     * Creates a {@link Border} pre-populated using the {@link TextStyle}.
     */
    public final Border border(final TextStyle style) {
        return style.border(this);
    }

    /**
     * Singleton for each edge.
     */
    final Border emptyBorder = Border.with(this, TextStyle.EMPTY);

    /**
     * Creates a {@link Border} with this {@link BoxEdge} and the given {@link Color}, {@link BorderStyle}, {@link Length}.
     */
    public final Border setBorder(final Optional<Color> color,
                                  final Optional<BorderStyle> style,
                                  final Optional<Length<?>> width) {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(width, "width");

        final Map<TextStylePropertyName<?>, Object> nameToValues = Maps.sorted();

        for (final BoxEdge boxEdge : BoxEdge.values()) {
            if (this == boxEdge || this == ALL) {
                {
                    final Color colorOrNull = color.orElse(null);
                    if (null != colorOrNull) {
                        nameToValues.put(
                            boxEdge.borderColorPropertyName(),
                            colorOrNull
                        );
                    }
                }
                {
                    final BorderStyle styleOrNull = style.orElse(null);
                    if (null != styleOrNull) {
                        nameToValues.put(
                            boxEdge.borderStylePropertyName(),
                            styleOrNull
                        );
                    }
                }
                {
                    final Length<?> widthOrNull = width.orElse(null);
                    if (null != widthOrNull) {
                        nameToValues.put(
                            boxEdge.borderWidthPropertyName(),
                            widthOrNull
                        );
                    }
                }
            }
        }

        return Border.with(
            this,
            TextStyle.EMPTY.setValues(nameToValues)
        );
    }

    /**
     * Creates a {@link Margin} with this {@link BoxEdge} and the given {@link Length}.
     */
    public final Margin setMargin(final Optional<Length<?>> width) {
        Objects.requireNonNull(width, "width");

        return TextStyle.EMPTY.setMargin(width)
            .margin(this);
    }

    /**
     * Creates a {@link Padding} with this {@link BoxEdge} and the given {@link Length}.
     */
    public final Padding setPadding(final Optional<Length<?>> width) {
        Objects.requireNonNull(width, "width");

        return TextStyle.EMPTY.setPadding(width)
            .padding(this);
    }

    // parseBorder......................................................................................................

    /**
     * Parses a border. Spaces before and after are optional, but at least 1 space is required between tokens. Note
     * tokens are all optional.
     * <pre>
     * black SOLID 1px
     * #123 solid 2px
     * </pre>
     */
    public final Border parseBorder(final String text) {
        final TextCursor textCursor = TextCursors.charSequence(text);

        Color color = null;
        BorderStyle style = null;
        Length<?> width = null;

        boolean optionalSpaces = true;

        while(textCursor.isNotEmpty()) {
            if(optionalSpaces) {
                skipOptionalSpaces(textCursor);
                optionalSpaces = false; // required
            } else {
                skipRequiredSpaces(textCursor);
            }

            if(textCursor.isEmpty()) {
                break;
            }

            final TextCursorSavePoint start = textCursor.save();

            NOT_SPACE.parse(
                textCursor,
                PARSER_CONTEXT
            );

            final String token = start.textBetween()
                .toString();
            RuntimeException firstRuntime = null;

            if(null == color) {
                try {
                    color = Color.parse(token);
                    continue; // try next
                } catch (final RuntimeException cause) {
                    firstRuntime = cause;
                }
            }

            if(null == style) {
                try {
                    style = Arrays.stream(BorderStyle.values())
                        .filter(s -> s.name().equalsIgnoreCase(token))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown style " + CharSequences.quoteAndEscape(token)));
                    continue; // try next
                } catch (final RuntimeException cause) {
                    if(null == firstRuntime) {
                        firstRuntime = cause;
                    }
                }
            }

            if(null == width) {
                try {
                    width = Length.parse(token);
                    continue;
                } catch (final RuntimeException cause) {
                    if(null == firstRuntime) {
                        firstRuntime = cause;
                    }
                }
            }

            if(null != firstRuntime) {
                throw firstRuntime;
            }

            // not color, style or width InvalidCharacterException
            throw start.lineInfo()
                .emptyTextOrInvalidCharacterExceptionOrLast("text");
        }

        return this.setBorder(
            Optional.ofNullable(color),
            Optional.ofNullable(style),
            Optional.ofNullable(width)
        );
    }

    private void skipRequiredSpaces(final TextCursor textCursor) {
        REQUIRED_SPACE.parse(textCursor, PARSER_CONTEXT);
    }

    private final static Parser<ParserContext> REQUIRED_SPACE = Parsers.charPredicateString(
        CharPredicates.whitespace(),
        1,
        65536
    );

    private void skipOptionalSpaces(final TextCursor textCursor) {
        OPTIONAL_SPACE.parse(textCursor, PARSER_CONTEXT);
    }

    private final static Parser<ParserContext> OPTIONAL_SPACE = REQUIRED_SPACE.optional();

    private final static Parser<ParserContext> NOT_SPACE = Parsers.charPredicateString(
        CharPredicates.whitespace()
            .negate(),
        1,
        255
    );

    private final static ParserContext PARSER_CONTEXT = ParserContexts.fake();

    // parseMargin......................................................................................................

    public final Margin parseMargin(final String width) {
        return this.setMargin(
            Optional.of(
                Length.parse(width)
            )
        );
    }
    
    public final Padding parsePadding(final String width) {
        return this.setPadding(
            Optional.of(
                Length.parse(width)
            )
        );
    }

    // margin...........................................................................................................

    /**
     * Creates a {@link Margin} pre-populated using the {@link TextStyle}.
     */
    public final Margin margin(final TextStyle style) {
        return style.margin(this);
    }

    /**
     * Singleton for each edge.
     */
    final Margin emptyMargin = Margin.with(this, TextStyle.EMPTY);

    // padding..........................................................................................................

    /**
     * Creates a {@link Padding} pre-populated using the {@link TextStyle}.
     */
    public final Padding padding(final TextStyle style) {
        return style.padding(this);
    }

    /**
     * Singleton for each edge.
     */
    final Padding emptyPadding = Padding.with(this, TextStyle.EMPTY);

    // property names...................................................................................................

    public abstract TextStylePropertyName<Color> borderColorPropertyName();

    public abstract TextStylePropertyName<BorderStyle> borderStylePropertyName();

    public abstract TextStylePropertyName<Length<?>> borderWidthPropertyName();

    public abstract TextStylePropertyName<Length<?>> marginPropertyName();

    public abstract TextStylePropertyName<Length<?>> paddingPropertyName();

    /**
     * Tests if the given {@link TextStylePropertyName} matches this {@link BoxEdge}.
     * {@link #ALL} will match all properties, {@link #TOP} will only match properties like {@link TextStylePropertyName#BORDER_TOP_WIDTH} etc.
     */
    final boolean isTextStyleProperty(final TextStylePropertyName<?> propertyName) {
        return this.isTextStyleProperty(
            propertyName.value()
        );
    }

    abstract boolean isTextStyleProperty(final String propertyName);
}
