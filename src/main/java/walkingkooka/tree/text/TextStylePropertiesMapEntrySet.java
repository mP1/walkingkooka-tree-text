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

import walkingkooka.collect.set.ImmutableSetDefaults;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * A read only {@link Set} sorted view of properties that have had their values checked.
 */
final class TextStylePropertiesMapEntrySet extends AbstractSet<Entry<TextStylePropertyName<?>, Object>> implements ImmutableSetDefaults<TextStylePropertiesMapEntrySet, Entry<TextStylePropertyName<?>, Object>> {

    /**
     * Factory that creates a {@link TextStylePropertiesMapEntrySet}.
     */
    static TextStylePropertiesMapEntrySet with(final TextStylePropertiesMap map) {
        return new TextStylePropertiesMapEntrySet(map);
    }

    private TextStylePropertiesMapEntrySet(final TextStylePropertiesMap map) {
        this.map = map;
    }

    @Override
    public Iterator<Entry<TextStylePropertyName<?>, Object>> iterator() {
        return TextStylePropertiesMapEntrySetIterator.with(this.map.values);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    private final TextStylePropertiesMap map;

    // ImmutableSetDefaults.............................................................................................

    @Override
    public void elementCheck(final Entry<TextStylePropertyName<?>, Object> entry) {
        Objects.requireNonNull(entry, "entry");
    }

    @Override
    public TextStylePropertiesMapEntrySet setElements(final Collection<Entry<TextStylePropertyName<?>, Object>> elements) {
        Objects.requireNonNull(elements, "elements");
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> toSet() {
        throw new UnsupportedOperationException();
    }

    // json.............................................................................................................

    static TextStylePropertiesMapEntrySet unmarshall(final JsonNode json,
                                                     final JsonNodeUnmarshallContext context) {
        return (TextStylePropertiesMapEntrySet)
            TextStylePropertiesMap.unmarshall(
                json,
                context
            ).entrySet();
    }

    JsonNode marshall(final JsonNodeMarshallContext context) {
        return this.map.marshall(context);
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(TextStylePropertiesMapEntrySet.class),
            TextStylePropertiesMapEntrySet::unmarshall,
            TextStylePropertiesMapEntrySet::marshall,
            TextStylePropertiesMapEntrySet.class
        );
    }
}
