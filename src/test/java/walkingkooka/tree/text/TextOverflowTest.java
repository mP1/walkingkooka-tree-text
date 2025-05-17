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
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

public final class TextOverflowTest implements ClassTesting2<TextOverflow>,
    ConstantsTesting<TextOverflow>,
    JsonNodeMarshallingTesting<TextOverflow>,
    ParseStringTesting<TextOverflow> {

    // parse............................................................................................................

    @Test
    public void testParseUnknownConstant() {
        this.parseStringFails(
            "abc",
            new IllegalArgumentException("Invalid text")
        );
    }

    @Test
    public void testParseMissingClosingQuote() {
        this.parseStringFails(
            "\"abc",
            new IllegalArgumentException("Missing closing quote")
        );
    }

    @Test
    public void testParseEarlyClosingQuote() {
        this.parseStringInvalidCharacterFails(
            "\"abc\" ",
            1 + 3
        );
    }

    @Test
    public void testParseClipWrongCaseFails() {
        this.parseStringFails(
            "CLIP",
            new IllegalArgumentException("Invalid text")
        );
    }

    @Test
    public void testParseEllipseWrongCaseFails() {
        this.parseStringFails(
            "ELLIPSE",
            new IllegalArgumentException("Invalid text")
        );
    }

    @Test
    public void testParseClip() {
        this.parseStringAndCheck(
            "clip",
            TextOverflow.CLIP
        );
    }

    @Test
    public void testParseEllipsis() {
        this.parseStringAndCheck(
            "ellipsis",
            TextOverflow.ELLIPSIS
        );
    }

    @Test
    public void testParseQuotedText() {
        this.parseStringAndCheck(
            "\"Hello\"",
            TextOverflow.string("Hello")
        );
    }

    @Test
    public void testParseQuotedTextIncludesEscaping() {
        this.parseStringAndCheck(
            "\"Hello\t\"Good\'bye\"\"",
            TextOverflow.string("Hello\t\"Good'bye\"")
        );
    }

    @Override
    public TextOverflow parseString(final String text) {
        return TextOverflow.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // json.............................................................................................................

    @Override
    public TextOverflow unmarshall(final JsonNode from,
                                   final JsonNodeUnmarshallContext context) {
        return TextOverflow.unmarshall(from, context);
    }

    @Override
    public TextOverflow createJsonNodeMarshallingValue() {
        return TextOverflow.string("hello-123");
    }

    @Test
    public void testJsonClipRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.CLIP);
    }

    @Test
    public void testJsonEllipsisRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.ELLIPSIS);
    }

    @Test
    public void testJsonStringRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("abc123"));
    }

    @Test
    public void testJsonStringRoundtrip2() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("clip"));
    }

    @Test
    public void testJsonStringRoundtrip3() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("ellipsis"));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextOverflow> type() {
        return TextOverflow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantTesting..................................................................................................

    @Override
    public Set<TextOverflow> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
