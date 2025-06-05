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

public final class TextStylePropertyValueHandlerOpacityTest extends TextStylePropertyValueHandlerTestCase3<TextStylePropertyValueHandlerOpacity, Opacity> {

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Property \"opacity\": Expected Opacity got TextStylePropertyValueHandlerOpacityTest"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.string(Opacity.OPAQUE_TEXT),
            Opacity.OPAQUE
        );
    }

    @Test
    public void testMarshall() {
        final Opacity opacity = Opacity.OPAQUE;

        this.marshallAndCheck(
            opacity,
            JsonNode.string(Opacity.OPAQUE_TEXT)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "Opacity"
        );
    }

    @Override
    TextStylePropertyValueHandlerOpacity handler() {
        return TextStylePropertyValueHandlerOpacity.INSTANCE;
    }

    @Override
    TextStylePropertyName<Opacity> propertyName() {
        return TextStylePropertyName.OPACITY;
    }

    @Override
    Opacity propertyValue() {
        return Opacity.with(0.5);
    }

    @Override
    public String typeNameSuffix() {
        return Opacity.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerOpacity> type() {
        return TextStylePropertyValueHandlerOpacity.class;
    }
}
