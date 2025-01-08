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

import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A {@link TextStylePropertyValueHandler} that acts as  bridge to a type that can be made into a {@link JsonNode}.
 */
final class TextStylePropertyValueHandlerJsonNode<T> extends TextStylePropertyValueHandler<T> {

    /**
     * Factory
     */
    static <T> TextStylePropertyValueHandlerJsonNode<T> with(final Class<T> type,
                                                             final Predicate<Object> typeChecker) {
        return new TextStylePropertyValueHandlerJsonNode<>(type, typeChecker);
    }

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerJsonNode(final Class<T> type,
                                                  final Predicate<Object> typeChecker) {
        super();
        this.type = type;
        this.typeChecker = typeChecker;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, this.typeChecker, name);
    }

    private final Predicate<Object> typeChecker;

    @Override
    String expectedTypeName(final Class<?> type) {
        return this.type.getSimpleName();
    }

    @Override
    T parseValue(final String value) {
        return this.unmarshall(
            JsonNode.string(value),
            null,
            CONTEXT
        );
    }

    private final static JsonNodeUnmarshallContext CONTEXT = JsonNodeUnmarshallContexts.basic(
        ExpressionNumberKind.DOUBLE,
        MathContext.DECIMAL32
    );

    // JsonNodeContext..................................................................................................

    @Override
    T unmarshall(final JsonNode node,
                 final TextStylePropertyName<?> name,
                 final JsonNodeUnmarshallContext context) {
        return context.unmarshall(node, this.type);
    }

    @Override
    JsonNode marshall(final T value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }

    private final Class<T> type;
}
