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

public final class TextStylePropertyValueHandlerFontFamilyTest extends TextStylePropertyValueHandlerTestCase3<TextStylePropertyValueHandlerFontFamily, FontFamily> {

    private final static String TIMES_NEW_ROMAN = "Times New Roman";

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.string(TIMES_NEW_ROMAN),
            FontFamily.with(TIMES_NEW_ROMAN)
        );
    }

    @Test
    public void testMarshall() {
        final FontFamily fontWeight = FontFamily.with(TIMES_NEW_ROMAN);

        this.marshallAndCheck(
            fontWeight,
            JsonNode.string(TIMES_NEW_ROMAN)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "FontFamily"
        );
    }

    @Override
    TextStylePropertyValueHandlerFontFamily handler() {
        return TextStylePropertyValueHandlerFontFamily.INSTANCE;
    }

    @Override
    TextStylePropertyName<FontFamily> propertyName() {
        return TextStylePropertyName.FONT_FAMILY;
    }

    @Override
    FontFamily propertyValue() {
        return FontFamily.with(TIMES_NEW_ROMAN);
    }

    @Override
    String propertyValueType() {
        return FontFamily.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return FontFamily.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerFontFamily> type() {
        return TextStylePropertyValueHandlerFontFamily.class;
    }
}
