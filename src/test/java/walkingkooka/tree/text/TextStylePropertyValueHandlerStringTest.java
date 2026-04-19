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

public final class TextStylePropertyValueHandlerStringTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerString, String> {

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Invalid \"text\" expected non empty String but got " + this.getClass().getSimpleName()
        );
    }

    @Test
    public void testCheckValueWithEmptyStringFails() {
        this.checkValueFails(
            "",
            "Invalid \"text\" expected non empty String"
        );
    }

    @Test
    public void testCheckValueWithWhitespace() {
        this.checkValue(" \t");
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseValueTextWithEmptyString() {
        final String text = "";

        this.parseStringAndCheck(
            text,
            text
        );
    }

    @Test
    public void testParseValueTextWithNonEmptyString() {
        final String text = "Hello 123";

        this.parseStringAndCheck(
            text,
            text
        );
    }

    @Test
    public void testUnmarshall() {
        final String value = "abc123";
        this.unmarshallAndCheck(JsonNode.string(value), value);
    }

    @Test
    public void testMarshall() {
        final String value = "abc123";
        this.marshallAndCheck(value, JsonNode.string(value));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "String");
    }

    @Override
    TextStylePropertyValueHandlerString handler() {
        return TextStylePropertyValueHandlerString.INSTANCE;
    }

    @Override
    TextStylePropertyName<String> propertyName() {
        return TextStylePropertyName.TEXT;
    }

    @Override
    String propertyValue() {
        return "abc123";
    }

    @Override
    public String typeNameSuffix() {
        return String.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerString> type() {
        return TextStylePropertyValueHandlerString.class;
    }
}
