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

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

final class BoxEdgeParserBorder extends BoxEdgeParser<Border> {

    static BoxEdgeParserBorder with(final BoxEdge edge) {
        return new BoxEdgeParserBorder(edge);
    }

    private BoxEdgeParserBorder(final BoxEdge boxEdge) {
        super(boxEdge);
    }

    /**
     * Parses the border tokens in order COLOR, STYLE, WIDTH with each optional.
     * <pre>
     * BLACK
     * BLACK SOLID 1px
     * SOLID 1px
     * 1px
     * </pre>
     */
    Border parseBorder(final String text) {
        final TextCursor textCursor = TextCursors.charSequence(text);

        skipOptionalSpaces(textCursor);

        final BoxEdge boxEdge = this.boxEdge;

        final TextStyle textStyle = this.parseBorder0(
            textCursor,
            TextStyle.EMPTY,
            BoxEdge.ALL == boxEdge ?
                ALL_BOX_EDGES.iterator() :
                Iterators.one(boxEdge)
        );

        skipOptionalSpaces(textCursor);
        if (textCursor.isNotEmpty()) {
            throw textCursor.lineInfo()
                .invalidCharacterException()
                .get();
        }

        return this.boxEdge.border(textStyle);
    }

    private TextStyle parseBorder0(final TextCursor textCursor,
                                   final TextStyle textStyle,
                                   final Iterator<BoxEdge> nextBoxEdge) {
        final BoxEdge boxEdge = nextBoxEdge.next();

        TextStyle after = textStyle;

        Color color = null;
        BorderStyle style = null;
        Length<?> width = null;

        TextCursorSavePoint separator = null;

        while (textCursor.isNotEmpty()) {
            separator = isSeparator(textCursor);

            if (null != separator) {
                break;
            }

            // if cursor not empty expect separator after WIDTH
            if (null != width) {
                throw textCursor.lineInfo()
                    .invalidCharacterException()
                    .get();
            }

            final TextCursorSavePoint start = textCursor.save();

            TOKEN.parse(
                textCursor,
                PARSER_CONTEXT
            );

            final String token = start.textBetween()
                .toString();
            skipOptionalSpaces(textCursor);

            RuntimeException firstRuntime = null;

            if (null == color && null == width && null == style) {
                try {
                    color = Color.parse(token);
                    continue; // try next
                } catch (final RuntimeException cause) {
                    firstRuntime = cause;
                }
            }

            if (null == style && null == width) {
                try {
                    style = Arrays.stream(BorderStyle.values())
                        .filter(s -> s.name().equalsIgnoreCase(token))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown style " + CharSequences.quoteAndEscape(token)));
                    continue; // try next
                } catch (final RuntimeException cause) {
                    if (null == firstRuntime) {
                        firstRuntime = cause;
                    }
                }
            }

            if (null == width) {
                try {
                    width = Length.parse(token);
                    continue;
                } catch (final RuntimeException cause) {
                    if (null == firstRuntime) {
                        firstRuntime = cause;
                    }
                }
            }

            if (null != firstRuntime) {
                throw firstRuntime;
            }

            if (nextBoxEdge.hasNext()) {
                break;
            }

            // not color, style or width InvalidCharacterException
            throw start.lineInfo()
                .emptyTextOrInvalidCharacterExceptionOrLast("text");
        }

        if (null != color) {
            after = after.set(
                boxEdge.borderColorPropertyName(),
                color
            );
        }

        if (null != style) {
            after = after.set(
                boxEdge.borderStylePropertyName(),
                style
            );
        }

        if (null != width) {
            after = after.set(
                boxEdge.borderWidthPropertyName(),
                width
            );
        }

        if (null != separator) {
            skipOptionalSpaces(textCursor);

            if (textCursor.isEmpty() || false == nextBoxEdge.hasNext()) {
                throw separator.lineInfo()
                    .invalidCharacterException()
                    .get();
            }

            skipOptionalSpaces(textCursor);

            after = parseBorder0(
                textCursor,
                after,
                nextBoxEdge
            );
        }

        return after;
    }

    @Override
    TextStylePropertyName<Length<?>> width(final BoxEdge boxEdge) {
        throw new UnsupportedOperationException();
    }

    @Override
    Border setBoxEdgeAndTextStyle(final TextStyle textStyle) {
        return Border.with(
            this.boxEdge,
            textStyle
        );
    }

    @Override
    Border setWidth(final Optional<Length<?>> width) {
        throw new UnsupportedOperationException();
    }
}
