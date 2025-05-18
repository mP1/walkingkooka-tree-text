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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TextStylePropertyValueHandler} for {@link Enum} parameter values.
 */
final class TextStylePropertyValueHandlerEnum<E extends Enum<E>> extends TextStylePropertyValueHandler<E> {

    /**
     * Factory that creates a new {@link TextStylePropertyValueHandlerEnum}.
     */
    static <E extends Enum<E>> TextStylePropertyValueHandlerEnum<E> with(final E[] values,
                                                                         final Class<E> type) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(type, "type");

        return new TextStylePropertyValueHandlerEnum<>(
            values,
            type
        );
    }

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerEnum(final E[] values,
                                              final Class<E> type) {
        super();
        this.values = Lists.of(values);
        this.type = type;
    }

    @Override Optional<Class<Enum<?>>> enumType() {
        return Optional.of(Cast.to(this.type));
    }

    @Override
    void checkNonNullValue(final Object value,
                           final TextStylePropertyName<?> name) {
        this.checkType(
            value,
            this.values::contains,
            name
        );
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return this.type.getSimpleName();
    }

    private final Class<E> type;

    @Override
    E parseValueText(final String text) {
        return this.values.stream()
            .filter(v -> v.name().equalsIgnoreCase(text))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown value " + CharSequences.quoteAndEscape(text)));
    }

    private final List<E> values;

    // JsonNodeContext..................................................................................................

    @Override
    E unmarshall(final JsonNode node,
                 final TextStylePropertyName<?> name,
                 final JsonNodeUnmarshallContext context) {
        return this.parseValueText(node.stringOrFail());
    }

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
