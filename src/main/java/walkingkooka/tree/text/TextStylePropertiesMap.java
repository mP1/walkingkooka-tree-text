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
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractMap;
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
     * Factory that takes a copy if the given {@link Map} is not a {@link TextStylePropertiesMap}.
     */
    static TextStylePropertiesMap with(final Map<TextStylePropertyName<?>, Object> map) {
        Objects.requireNonNull(map, "map");

        TextStylePropertiesMap textStylePropertiesMap;

        if (map instanceof TextStylePropertiesMap) {
            textStylePropertiesMap = (TextStylePropertiesMap) map;
        } else {
            textStylePropertiesMap = withTextStyleMapEntrySet(
                TextStylePropertiesMapEntrySet.with(map)
            );
        }

        return textStylePropertiesMap;
    }

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
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        return this.entries;
    }

    final TextStylePropertiesMapEntrySet entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        this.entries.accept(visitor);
    }

    // JsonNodeContext..................................................................................................

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
