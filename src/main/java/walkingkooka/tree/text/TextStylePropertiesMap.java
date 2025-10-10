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

import walkingkooka.CanBeEmpty;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A read only sorted view of attributes or text style to values that appear within a {@link TextStyleNode}.
 */
final class TextStylePropertiesMap extends AbstractMap<TextStylePropertyName<?>, Object> implements CanBeEmpty {

    static {
        Maps.registerImmutableType(TextStylePropertiesMap.class);
    }

    /**
     * An empty {@link TextStylePropertiesMap}.
     */
    static final TextStylePropertiesMap EMPTY = new TextStylePropertiesMap(TextStylePropertiesMapEntrySet.EMPTY);

    /**
     * Factory that returns a {@link TextStylePropertiesMap} taking a copy of the values inside the {@link Map}.
     */
    static TextStylePropertiesMap with(final Map<TextStylePropertyName<?>, Object> map) {
        Objects.requireNonNull(map, "map");

        final TextStylePropertiesMap textStylePropertiesMap;

        if (map instanceof TextStylePropertiesMap) {
            textStylePropertiesMap = (TextStylePropertiesMap) map;
        } else {
            int count = 0;
            final List<Object> values = Lists.autoExpandArray();

            for (final Entry<TextStylePropertyName<?>, Object> entry : map.entrySet()) {
                final TextStylePropertyName<?> propertyName = entry.getKey();
                final int index = propertyName.index();

                final Object value = entry.getValue();
                propertyName.checkValue(value);

                values.set(
                    index,
                    entry.getValue()
                );

                count++;
            }

            textStylePropertiesMap = withTextStyleMapEntrySet(
                TextStylePropertiesMapEntrySet.with(
                    values.toArray(),
                    count
                )
            );
        }

        return textStylePropertiesMap;
    }

    /**
     * Factory that returns a {@link TextStylePropertiesMap} with the given entries.
     */
    static TextStylePropertiesMap withTextStyleMapEntrySet(final TextStylePropertiesMapEntrySet entrySet) {
        return entrySet.isEmpty() ?
            EMPTY :
            new TextStylePropertiesMap(entrySet);
    }

    private TextStylePropertiesMap(final TextStylePropertiesMapEntrySet entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Object get(final Object key) {
        return key instanceof TextStylePropertyName ?
            this.getValue(
                (TextStylePropertyName<?>) key
            ) :
            null;
    }

    Object getValue(final TextStylePropertyName<?> name) {
        Objects.requireNonNull(name, "name");

        final Object[] values = this.entries.values;
        final int index = name.index();

        return index >= values.length ?
            null :
            values[index];
    }

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        return this.entries;
    }

    final TextStylePropertiesMapEntrySet entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        this.entries.accept(visitor);
    }

    // json.............................................................................................................

    static TextStylePropertiesMap unmarshall(final JsonNode json,
                                             final JsonNodeUnmarshallContext context) {
        return withTextStyleMapEntrySet(
            TextStylePropertiesMapEntrySet.unmarshall(
                json,
                context
            )
        );
    }

    JsonNode toJson(final JsonNodeMarshallContext context) {
        return this.entries.toJson(context);
    }
}
