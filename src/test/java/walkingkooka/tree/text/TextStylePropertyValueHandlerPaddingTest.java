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

public final class TextStylePropertyValueHandlerPaddingTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerPadding, Padding> {

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Invalid \"padding\" expected Padding but got " + this.getClass().getSimpleName()
        );
    }

    @Test
    public void testUnmarshall() {
        final Padding padding = BoxEdge.BOTTOM.setPadding(
            Optional.of(
                Length.pixel(1.0)
            )
        );

        this.unmarshallAndCheck(
            JsonNode.string(
                BoxEdge.BOTTOM + " " + padding.text()
            ),
            padding
        );
    }

    @Test
    public void testMarshall() {
        final Padding padding = BoxEdge.BOTTOM.setPadding(
            Optional.of(
                Length.pixel(1.0)
            )
        );

        this.marshallAndCheck(
            padding,
            JsonNode.string(
                BoxEdge.BOTTOM + " " + padding.text()
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "Padding"
        );
    }

    @Override
    TextStylePropertyValueHandlerPadding handler() {
        return TextStylePropertyValueHandlerPadding.INSTANCE;
    }

    @Override
    TextStylePropertyName<Padding> propertyName() {
        return TextStylePropertyName.PADDING;
    }

    @Override
    Padding propertyValue() {
        return Padding.parse("1px 2px 3px 4px");
    }

    @Override
    public String typeNameSuffix() {
        return Padding.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerPadding> type() {
        return TextStylePropertyValueHandlerPadding.class;
    }
}
