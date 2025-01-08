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
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

public final class TextOverflowNonStringTest extends TextOverflowTestCase<TextOverflowNonString> {

    private final static String TEXT = "abc123";

    @Test
    public void testWith() {
        final TextOverflowString textOverflow = TextOverflowString.with(TEXT);
        this.checkEquals(Optional.of(TEXT), textOverflow.value());
    }

    @Test
    public void testIsClipClip() {
        this.checkEquals(true, TextOverflow.CLIP.isClip());
    }

    @Test
    public void testIsClipEllipse() {
        this.checkEquals(false, TextOverflow.ELLIPSIS.isClip());
    }

    @Test
    public void testIsEllipseClip() {
        this.checkEquals(false, TextOverflow.CLIP.isEllipse());
    }

    @Test
    public void testIsEllipseEllipse() {
        this.checkEquals(true, TextOverflow.ELLIPSIS.isEllipse());
    }

    @Test
    public void testIsString() {
        this.checkEquals(false, TextOverflow.ELLIPSIS.isString());
    }

    @Test
    public void testParseValueClip() {
        this.checkEquals(
            TextOverflow.CLIP,
            this.textStylePropertyName()
                .parseValue(TextOverflow.CLIP_TEXT)
        );
    }

    @Test
    public void testParseValueEllipsis() {
        this.checkEquals(
            TextOverflow.ELLIPSIS,
            this.textStylePropertyName()
                .parseValue(TextOverflow.ELLIPSIS_TEXT)
        );
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(TextOverflow.CLIP, TextOverflow.ELLIPSIS);
    }

    @Test
    public void testToStringClip() {
        this.toStringAndCheck(TextOverflow.CLIP, "clip");
    }

    @Test
    public void testToStringEllipsis() {
        this.toStringAndCheck(TextOverflow.ELLIPSIS, "ellipsis");
    }

    @Test
    public void testUnmarshallClip() {
        this.unmarshallAndCheck(JsonNode.string("clip"), TextOverflow.CLIP);
    }

    @Test
    public void testUnmarshallEllispsis() {
        this.unmarshallAndCheck(JsonNode.string("ellipsis"), TextOverflow.ELLIPSIS);
    }

    @Test
    public void testMarshallClip() {
        this.marshallAndCheck(TextOverflow.CLIP, JsonNode.string("clip"));
    }

    @Test
    public void testMarshallEllipsis() {
        this.marshallAndCheck(TextOverflow.ELLIPSIS, JsonNode.string("ellipsis"));
    }

    @Override
    TextOverflow createTextStylePropertyValue() {
        return TextOverflow.CLIP;
    }

    @Override
    Class<TextOverflowNonString> textOverflowType() {
        return TextOverflowNonString.class;
    }
}
