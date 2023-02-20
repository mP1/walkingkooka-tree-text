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

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link TextStylePropertyValueHandler} for {@link Enum} parameter values.
 */
final class TextStylePropertyValueHandlerEnum<E extends Enum<E>> extends TextStylePropertyValueHandler<E> {

    /**
     * Factory that creates a new {@link TextStylePropertyValueHandlerEnum}.
     */
    static <E extends Enum<E>> TextStylePropertyValueHandlerEnum<E> with(final Function<String, E> factory,
                                                                         final Class<E> type,
                                                                         final Predicate<Object> typeChecker) {
        Objects.requireNonNull(factory, "factory");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(typeChecker, "typeChecker");

        return new TextStylePropertyValueHandlerEnum<>(factory, type, typeChecker);
    }

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerEnum(final Function<String, E> factory,
                                              final Class<E> type,
                                              final Predicate<Object> typeChecker) {
        super();
        this.factory = factory;
        this.type = type;
        this.typeChecker = typeChecker;
    }

    @Override Optional<Class<Enum<?>>> enumType() {
        return Optional.of(Cast.to(this.type));
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

    private final Class<E> type;

    @Override
    E parseValue(final String value) {
        return this.factory.apply(value);
    }

    // JsonNodeContext..................................................................................................

    @Override
    E unmarshall(final JsonNode node,
                 final TextStylePropertyName<?> name,
                 final JsonNodeUnmarshallContext context) {
        return this.factory.apply(node.stringOrFail());
    }

    private final Function<String, E> factory;

    @Override
    JsonNode marshall(final E value,
                      final JsonNodeMarshallContext context) {
        return JsonNode.string(value.name());
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }
}
