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

public final class TextStylePropertyValueHandlerFontWeightTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerFontWeight, FontWeight> {

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
                JsonNode.number(123),
                FontWeight.with(123)
        );
    }

    @Test
    public void testMarshall() {
        final FontWeight fontWeight = FontWeight.with(123);

        this.marshallAndCheck(
                fontWeight,
                JsonNode.number(123)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.handler(),
                "FontWeight"
        );
    }

    @Override
    TextStylePropertyValueHandlerFontWeight handler() {
        return TextStylePropertyValueHandlerFontWeight.INSTANCE;
    }

    @Override
    TextStylePropertyName<FontWeight> propertyName() {
        return TextStylePropertyName.FONT_WEIGHT;
    }

    @Override
    FontWeight propertyValue() {
        return FontWeight.with(123);
    }

    @Override
    String propertyValueType() {
        return FontWeight.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return FontWeight.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerFontWeight> type() {
        return TextStylePropertyValueHandlerFontWeight.class;
    }
}
