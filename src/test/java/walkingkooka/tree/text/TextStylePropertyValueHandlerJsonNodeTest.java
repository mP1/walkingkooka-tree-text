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
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.tree.json.JsonNode;

public final class TextStylePropertyValueHandlerJsonNodeTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerJsonNode<Color>, Color> {

    @Test
    public void testJsonNodeUnmarshall() {
        final Color color = Color.fromRgb(0x123456);
        this.unmarshallAndCheck(this.marshall(color), color);
    }

    @Test
    public void testJsonNodeUnmarshallRgba() {
        final Color color = Color.fromArgb(0x12345678);
        this.unmarshallAndCheck(this.marshall(color), color);
    }

    @Test
    public void testJsonNodeMarshall() {
        final Color color = Color.fromRgb(0x123456);
        this.marshallAndCheck(color, this.marshall(color));
    }

    @Test
    public void testJsonNodeMarshallRgba() {
        final Color color = Color.fromArgb(0x12345678);
        this.marshallAndCheck(color, this.marshall(color));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), Color.class.getSimpleName());
    }

    @Override
    TextStylePropertyValueHandlerJsonNode<Color> handler() {
        return TextStylePropertyValueHandlerJsonNode.with(Color.class);
    }

    @Override
    TextStylePropertyName<Color> propertyName() {
        return TextStylePropertyName.BACKGROUND_COLOR;
    }

    @Override
    Color propertyValue() {
        return Color.BLACK;
    }

    @Override
    String propertyValueType() {
        return Color.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return JsonNode.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerJsonNode<Color>> type() {
        return Cast.to(TextStylePropertyValueHandlerJsonNode.class);
    }
}
