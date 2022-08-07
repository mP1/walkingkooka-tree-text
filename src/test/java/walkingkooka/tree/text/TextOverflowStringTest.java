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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextOverflowStringTest extends TextOverflowTestCase<TextOverflowString> {

    private final static String TEXT = "abc123";

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> TextOverflowString.with(null));
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> TextOverflowString.with(""));
    }

    @Test
    public void testWith() {
        final TextOverflowString textOverflow = TextOverflowString.with(TEXT);
        this.checkEquals(Optional.of(TEXT), textOverflow.value());
    }

    @Test
    public void testIsClip() {
        this.checkEquals(false, TextOverflowString.with(TEXT).isClip());
    }

    @Test
    public void testIsEllipse() {
        this.checkEquals(false, TextOverflowString.with(TEXT).isEllipse());
    }

    @Test
    public void testIsString() {
        this.checkEquals(true, TextOverflowString.with(TEXT).isString());
    }

    @Test
    public void testDifferentEllipsis() {
        this.checkNotEquals(TextOverflowString.with("abc123"), TextOverflow.ELLIPSIS);
    }

    @Test
    public void testDifferentString() {
        this.checkNotEquals(TextOverflowString.with("abc123"), TextOverflowString.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createTextStylePropertyValue(), CharSequences.quoteAndEscape(TEXT).toString());
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(JsonNode.string("string-abc123"), this.createTextStylePropertyValue());
    }

    @Test
    public void testJsonNodeMarshall() {
        this.marshallAndCheck(this.createTextStylePropertyValue(), JsonNode.string("string-abc123"));
    }

    @Override
    TextOverflow createTextStylePropertyValue() {
        return TextOverflowString.with(TEXT);
    }

    @Override
    Class<TextOverflowString> textOverflowType() {
        return TextOverflowString.class;
    }
}
