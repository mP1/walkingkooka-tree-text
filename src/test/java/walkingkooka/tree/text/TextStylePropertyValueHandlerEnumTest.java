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
import walkingkooka.tree.json.JsonNode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyValueHandlerEnumTest extends TextStylePropertyValueHandlerTestCase3<TextStylePropertyValueHandlerEnum<TextWrapping>, TextWrapping> {

    @Test
    public void testParseValueTextWithUnknownFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .parseValueText("BAD")
        );

        this.checkEquals(
            "Unknown value \"BAD\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseValueTextDifferentCase() {
        this.checkEquals(
            TextAlign.LEFT,
            TextStylePropertyValueHandlerEnum.with(
                    TextAlign.values(),
                    TextAlign.class
                ).parseValueText("leFT")
        );
    }

    @Test
    public void testUnmarshall() {
        final TextWrapping textWrapping = TextWrapping.CLIP;
        this.unmarshallAndCheck(JsonNode.string(textWrapping.name()), textWrapping);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), TextWrapping.class.getSimpleName());
    }

    @Override
    TextStylePropertyValueHandlerEnum<TextWrapping> handler() {
        return TextStylePropertyValueHandlerEnum.with(
            TextWrapping.values(),
            TextWrapping.class
        );
    }

    @Override
    TextStylePropertyName<TextWrapping> propertyName() {
        return TextStylePropertyName.TEXT_WRAPPING;
    }

    @Override
    TextWrapping propertyValue() {
        return TextWrapping.CLIP;
    }

    @Override
    String propertyValueType() {
        return TextWrapping.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Enum.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerEnum<TextWrapping>> type() {
        return Cast.to(TextStylePropertyValueHandlerEnum.class);
    }
}
