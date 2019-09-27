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
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

/**
 * A {@link TextStylePropertyValueHandler} that acts as  bridge to a type that can be made into a {@link JsonNode}.
 */
final class TextStylePropertyValueHandlerJsonNode<H> extends TextStylePropertyValueHandler<H> {

    /**
     * Factory
     */
    static <T> TextStylePropertyValueHandlerJsonNode<T> with(final Class<T> type) {
        return new TextStylePropertyValueHandlerJsonNode<>(type);
    }

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerJsonNode(final Class<H> type) {
        super();
        this.type = type;
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, this.type, name);
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return this.type.getSimpleName();
    }

    // JsonNodeContext..................................................................................................

    @Override
    H unmarshall(final JsonNode node,
                 final TextStylePropertyName<?> name,
                 final JsonNodeUnmarshallContext context) {
        return context.unmarshall(node, this.type);
    }

    @Override
    JsonNode marshall(final H value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }

    private final Class<H> type;
}
