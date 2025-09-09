/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.CanBeEmpty;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.InvalidCharacterExceptionFactory;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;

import java.math.MathContext;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A parser that may be used to parse individual tokens within a plugin expression such as a selector.
 */
final class TextStyleParser implements CanBeEmpty {

    static <N extends Name & Comparable<N>> TextStyleParser with(final String text) {
        return new TextStyleParser(
            Objects.requireNonNull(text, "text")
        );
    }

    private TextStyleParser(final String text) {
        this.cursor = TextCursors.charSequence(text);
    }

    /**
     * Reads a {@link TextStylePropertyName}, failing if it is absent.
     */
    Optional<TextStylePropertyName<?>> name() {
        return NAME_PARSER.parse(
            this.cursor,
            PARSER_CONTEXT
        ).map(t -> TextStylePropertyName.with(t.text()));
    }

    private final Parser<ParserContext> NAME_PARSER = Parsers.initialAndPartCharPredicateString(
        TextStylePropertyName.INITIAL,
        TextStylePropertyName.PART,
        TextStylePropertyName.MIN_LENGTH,
        TextStylePropertyName.MAX_LENGTH
    );

    //private final BiFunction<TextCursor, ParserContext, Optional<Name>> nameParser;

    /**
     * Consumes any whitespace, don't really care how many or if any were skipped.
     */
    void skipSpaces() {
        text(SPACE);
    }

    private final static Parser<ParserContext> SPACE = Parsers.character(CharPredicates.whitespace())
        .repeating();

    /**
     * Expects to read the assignment character.
     */
    void assignment() {
        final TextCursor cursor = this.cursor;
        if(cursor.isEmpty()) {
            throw new IllegalArgumentException("Missing " + CharSequences.quoteIfChars(TextStyle.ASSIGNMENT));
        }
        if(cursor.at() != TextStyle.ASSIGNMENT.character()) {
            throw cursor.lineInfo()
                .invalidCharacterException()
                .get();
        }

        cursor.next();
    }

    /**
     * Matches the separator character
     */
    boolean separator() {
        return this.text(
            Parsers.string(TextStyle.SEPARATOR.string(), CaseSensitivity.SENSITIVE)
            ).isPresent();
    }

    String colorToken() {
        return this.text(COLOR_TOKEN)
            .orElse("");
    }

    private final static Parser<ParserContext> COLOR_TOKEN = Parsers.charPredicateString(
        CharPredicates.is(TextStyle.SEPARATOR.character())
            .negate(),
        1,
        256
    );

    /**
     * Consumes a token returning an empty string and is terminated by whitespace or separator.
     */
    String token() {
        return this.text(TOKEN)
            .orElse("");
    }

    private final static Parser<ParserContext> TOKEN = Parsers.charPredicateString(
        CharPredicates.whitespace()
            .negate()
            .andNot(CharPredicates.is(TextStyle.SEPARATOR.character())),
        1,
        256
    );

    Optional<String> quotedText() {
        return this.text(QUOTED_TEXT);
    }

    private final static Parser<ParserContext> QUOTED_TEXT = Parsers.doubleQuoted();

    private Optional<String> text(final Parser<ParserContext> parser) {
        return this.parse(
            parser,
            ParserToken::text
        );
    }

    private <T> Optional<T> parse(final Parser<ParserContext> parser,
                                  final Function<ParserToken, T> mapper) {
        return parser.parse(
            this.cursor,
            PARSER_CONTEXT
        ).map(mapper);
    }

    /**
     * Singleton which can be reused.
     */
    private final static ParserContext PARSER_CONTEXT = ParserContexts.basic(
        false, // canNumbersHaveGroupSeparator
        InvalidCharacterExceptionFactory.POSITION,
        ',', // valueSeparator
        DateTimeContexts.fake(), // dates are not supported
        DecimalNumberContexts.american(MathContext.UNLIMITED) // only the decimal char is actually required.
    );

//    /**
//     * Helper that reports an invalid character.
//     */
//    IllegalArgumentException invalidCharacter() {
//        return this.cursor.lineInfo()
//            .emptyTextOrInvalidCharacterExceptionOrLast("text");
//    }

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.cursor.isEmpty();
    }

    final TextCursor cursor;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.cursor.toString();
    }
}
