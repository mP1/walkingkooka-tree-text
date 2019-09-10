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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;
import walkingkooka.tree.json.marshall.ToJsonNodeContext;

/**
 * A {@link TextStylePropertyValueHandler} that acts as  bridge to a type a type that marshals into a {@link JsonNode}
 * with the type recorded.
 */
final class TextStylePropertyValueHandlerJsonNodeWithType extends TextStylePropertyValueHandler<Object> {

    /**
     * Singleton
     */
    static TextStylePropertyValueHandlerJsonNodeWithType INSTANCE = new TextStylePropertyValueHandlerJsonNodeWithType();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerJsonNodeWithType() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return "supported type";
    }

    // JsonNodeContext..................................................................................................

    @Override
    Object fromJsonNode(final JsonNode node,
                        final TextStylePropertyName<?> name,
                        final FromJsonNodeContext context) {
        return context.fromJsonNodeWithType(node);
    }

    @Override
    JsonNode toJsonNode(final Object value,
                        final ToJsonNodeContext context) {
        return context.toJsonNodeWithType(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return "HasJsonNodeWithType";
    }
}
