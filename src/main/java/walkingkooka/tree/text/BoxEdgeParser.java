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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class BoxEdgeParser<T extends BorderMarginPadding> {

    BoxEdgeParser(final BoxEdge boxEdge) {
        super();

        this.boxEdge = boxEdge;
    }

    final T parseMarginOrPadding(final String text) {
        return this.boxEdge == BoxEdge.ALL ?
            this.parseMarginOrPaddingAll(text) :
            this.parseMarginOrPaddingNotAll(text);
    }

    private T parseMarginOrPaddingAll(final String text) {
        final TextCursor textCursor = TextCursors.charSequence(text);

        final List<Length<?>> widths = Lists.array();

        while (textCursor.isNotEmpty()) {
            if (widths.isEmpty()) {
                skipOptionalSpaces(textCursor);
            } else {
                skipRequiredSpaces(textCursor);
            }

            if (textCursor.isEmpty()) {
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

            try {
                widths.add(
                    Length.parse(token)
                );
                continue;
            } catch (final RuntimeException cause) {
                if (null == firstRuntime) {
                    firstRuntime = cause;
                }
            }

            if (null != firstRuntime) {
                throw firstRuntime;
            }
        }

        final T marginOrPadding;

        final int tokenCount = widths.size();

        switch (tokenCount) {
            case 0:
                marginOrPadding = this.setWidth(
                    Optional.empty()
                );
                break;
            case 1:
                marginOrPadding = this.setWidth(
                    Optional.of(
                        widths.get(0)
                    )
                );
                break;

            case 4:
                // top right bottom left
                final Map<TextStylePropertyName<?>, Object> nameToValues = Maps.ordered();

                int i = 0;
                for (final BoxEdge boxEdge : ALL_BOX_EDGES) {
                    nameToValues.put(
                        this.width(boxEdge),
                        widths.get(i)
                    );
                    i++;
                }

                marginOrPadding = this.setBoxEdgeAndTextStyle(
                    TextStyle.EMPTY.setValues(nameToValues)
                );
                break;
            default:
                throw new IllegalArgumentException("Expected 4 tokens got " + tokenCount);
        }

        return marginOrPadding;
    }

    final static List<BoxEdge> ALL_BOX_EDGES = Lists.of(
        BoxEdge.TOP,
        BoxEdge.RIGHT,
        BoxEdge.BOTTOM,
        BoxEdge.LEFT
    );

    abstract TextStylePropertyName<Length<?>> width(final BoxEdge boxEdge);

    abstract T setBoxEdgeAndTextStyle(final TextStyle textStyle);

    private T parseMarginOrPaddingNotAll(final String text) {
        final TextCursor textCursor = TextCursors.charSequence(text);

        Length<?> width = null;

        boolean optionalSpaces = true;

        while (textCursor.isNotEmpty()) {
            if (optionalSpaces) {
                skipOptionalSpaces(textCursor);
                optionalSpaces = false; // required
            } else {
                skipRequiredSpaces(textCursor);
            }

            if (textCursor.isEmpty()) {
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

            try {
                width = Length.parse(token);
                continue;
            } catch (final RuntimeException cause) {
                if (null == firstRuntime) {
                    firstRuntime = cause;
                }
            }

            if (null != firstRuntime) {
                throw firstRuntime;
            }

            if (textCursor.isEmpty()) {
                break;
            }
        }

        return this.setWidth(
            Optional.ofNullable(width)
        );
    }

    abstract T setWidth(final Optional<Length<?>> width);

    final BoxEdge boxEdge;

    // parser helpers...................................................................................................

    static void skipRequiredSpaces(final TextCursor textCursor) {
        REQUIRED_SPACE.parse(textCursor, PARSER_CONTEXT);
    }

    private final static Parser<ParserContext> REQUIRED_SPACE = Parsers.charPredicateString(
        CharPredicates.whitespace(),
        1,
        65536
    );

    static void skipOptionalSpaces(final TextCursor textCursor) {
        OPTIONAL_SPACE.parse(textCursor, PARSER_CONTEXT);
    }

    private final static Parser<ParserContext> OPTIONAL_SPACE = REQUIRED_SPACE.optional();

    final static Parser<ParserContext> NOT_SPACE = Parsers.charPredicateString(
        CharPredicates.whitespace()
            .negate(),
        1,
        255
    );

    final static ParserContext PARSER_CONTEXT = ParserContexts.fake();
}
