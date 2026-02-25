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

public final class TextStylePropertyValueHandlerTextOverflowTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerTextOverflow, TextOverflow> {

    private final static String STRING = "Hello";

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Property \"text-overflow\": Expected TextOverflow got TextStylePropertyValueHandlerTextOverflowTest"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.string("\"" + STRING + "\""),
            TextOverflow.string(STRING)
        );
    }

    @Test
    public void testMarshall() {
        final TextOverflow textOverflow = TextOverflow.string(STRING);

        this.marshallAndCheck(
            textOverflow,
            JsonNode.string("\"" + STRING + "\"")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "TextOverflow"
        );
    }

    @Override
    TextStylePropertyValueHandlerTextOverflow handler() {
        return TextStylePropertyValueHandlerTextOverflow.INSTANCE;
    }

    @Override
    TextStylePropertyName<TextOverflow> propertyName() {
        return TextStylePropertyName.TEXT_OVERFLOW;
    }

    @Override
    TextOverflow propertyValue() {
        return TextOverflow.string(STRING);
    }

    @Override
    public String typeNameSuffix() {
        return TextOverflow.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerTextOverflow> type() {
        return TextStylePropertyValueHandlerTextOverflow.class;
    }
}
