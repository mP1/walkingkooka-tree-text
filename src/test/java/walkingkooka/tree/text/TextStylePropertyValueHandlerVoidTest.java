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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyValueHandlerVoidTest extends TextStylePropertyValueHandlerTestCase<TextStylePropertyValueHandlerVoid, Void> {

    @Test
    public void testUnmarshallFails() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> this.handler()
                        .unmarshall(
                                JsonNode.nullNode(),
                                this.propertyName(),
                                this.unmarshallContext()
                        )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), Void.class.getSimpleName());
    }

    @Override
    TextStylePropertyValueHandlerVoid handler() {
        return TextStylePropertyValueHandlerVoid.INSTANCE;
    }

    @Override
    TextStylePropertyName<Void> propertyName() {
        return TextStylePropertyName.ALL;
    }

    @Override
    String propertyValueType() {
        return Void.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Void.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerVoid> type() {
        return TextStylePropertyValueHandlerVoid.class;
    }
}
