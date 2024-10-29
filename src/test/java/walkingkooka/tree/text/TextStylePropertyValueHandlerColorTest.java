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
import walkingkooka.color.Color;
import walkingkooka.color.WebColorName;
import walkingkooka.tree.json.JsonNode;

public final class TextStylePropertyValueHandlerColorTest extends TextStylePropertyValueHandlerTestCase3<TextStylePropertyValueHandlerColor, Color> {

    @Test
    public void testUnmarshallHashRgb() {
        this.unmarshallAndCheck(
                JsonNode.string("#123"),
                Color.parse("#123")
        );
    }

    @Test
    public void testUnmarshallWebColorName() {
        this.unmarshallAndCheck(
                JsonNode.string("red"),
                WebColorName.RED.color()
        );
    }

    @Test
    public void testMarshallRed() {
        this.marshallAndCheck(
                WebColorName.RED.color(),
                JsonNode.string("red")
        );
    }

    @Test
    public void testMarshallNonWebColorNameColor() {
        this.marshallAndCheck(
                Color.parse("#123456"),
                JsonNode.string("#123456")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.handler(),
                "Color"
        );
    }

    @Override
    TextStylePropertyValueHandlerColor handler() {
        return TextStylePropertyValueHandlerColor.INSTANCE;
    }

    @Override
    TextStylePropertyName<Color> propertyName() {
        return TextStylePropertyName.COLOR;
    }

    @Override
    Color propertyValue() {
        return Color.parse("#123");
    }

    @Override
    String propertyValueType() {
        return Color.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Color.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerColor> type() {
        return TextStylePropertyValueHandlerColor.class;
    }
}
