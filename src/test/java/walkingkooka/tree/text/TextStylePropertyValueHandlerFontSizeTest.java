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

public final class TextStylePropertyValueHandlerFontSizeTest extends TextStylePropertyValueHandlerTestCase3<TextStylePropertyValueHandlerFontSize, FontSize> {

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.number(123),
            FontSize.with(123)
        );
    }

    @Test
    public void testMarshall() {
        final FontSize fontSize = FontSize.with(123);

        this.marshallAndCheck(
            fontSize,
            JsonNode.number(fontSize.value().doubleValue())
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "FontSize"
        );
    }

    @Override
    TextStylePropertyValueHandlerFontSize handler() {
        return TextStylePropertyValueHandlerFontSize.INSTANCE;
    }

    @Override
    TextStylePropertyName<FontSize> propertyName() {
        return TextStylePropertyName.FONT_SIZE;
    }

    @Override
    FontSize propertyValue() {
        return FontSize.with(123);
    }

    @Override
    String propertyValueType() {
        return FontSize.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return FontSize.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerFontSize> type() {
        return TextStylePropertyValueHandlerFontSize.class;
    }
}
