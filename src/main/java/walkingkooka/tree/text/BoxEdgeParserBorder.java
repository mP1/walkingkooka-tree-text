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

import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.util.Arrays;
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

        Color color = null;
        BorderStyle style = null;
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

            // not color, style or width InvalidCharacterException
            throw start.lineInfo()
                .emptyTextOrInvalidCharacterExceptionOrLast("text");
        }

        return this.boxEdge.setBorder(
            Optional.ofNullable(color),
            Optional.ofNullable(style),
            Optional.ofNullable(width)
        );
    }

    @Override
    TextStylePropertyName<Length<?>> width(final BoxEdge boxEdge) {
        throw new UnsupportedOperationException();
    }

    @Override
    Border setBoxEdgeAndTextStyle(final TextStyle textStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    Border setWidth(final Optional<Length<?>> width) {
        throw new UnsupportedOperationException();
    }
}
