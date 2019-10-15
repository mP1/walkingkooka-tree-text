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
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A read only {@link Set} sorted view of textStyle that have had their values checked.
 */
final class TextNodeMapEntrySet extends AbstractSet<Entry<TextStylePropertyName<?>, Object>> {

    static {
        Sets.registerImmutableType(TextNodeMapEntrySet.class);
    }

    /**
     * An empty {@link TextNodeMap}.
     */
    static final TextNodeMapEntrySet EMPTY = new TextNodeMapEntrySet(Lists.empty());

    /**
     * Factory that creates a {@link TextNodeMapEntrySet}.
     */
    static TextNodeMapEntrySet with(final Map<TextStylePropertyName<?>, Object> entries) {
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : entries.entrySet()) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();
            final Object value = propertyAndValue.getValue();
            property.check(value);

            list.add(Maps.entry(property, value));
        }

        sort(list);
        return list.isEmpty() ?
                EMPTY :
                withList(list);
    }

    /**
     * Sorts the {@link List} so all textStyle using the {@link TextStylePropertyName} {@link Comparator}.
     */
    static void sort(final List<Entry<TextStylePropertyName<?>, Object>> list) {
        list.sort(TextNodeMapEntrySet::comparator);
    }

    /**
     * A {@link Comparator} that maybe used to sort all entries so they appear in alphabetical order.
     */
    private static int comparator(final Entry<TextStylePropertyName<?>, Object> first,
                                  final Entry<TextStylePropertyName<?>, Object> second) {
        return first.getKey().compareTo(second.getKey());
    }

    static TextNodeMapEntrySet withList(final List<Entry<TextStylePropertyName<?>, Object>> entries) {
        return new TextNodeMapEntrySet(entries);
    }

    private TextNodeMapEntrySet(final List<Entry<TextStylePropertyName<?>, Object>> entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Iterator<Entry<TextStylePropertyName<?>, Object>> iterator() {
        return Iterators.readOnly(this.entries.iterator());
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    private final List<Entry<TextStylePropertyName<?>, Object>> entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        this.entries
                .forEach(visitor::acceptPropertyAndValue);
    }

    // JsonNodeContext..................................................................................................

    /**
     * Recreates this {@link TextNodeMapEntrySet} from the json object.
     */
    static TextNodeMapEntrySet fromJson(final JsonNode json,
                                        final JsonNodeUnmarshallContext context) {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();

        for (JsonNode child : json.children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshallEntryKey(child);
            properties.put(name,
                    name.handler.unmarshall(child, name, context));
        }

        return with(properties);
    }

    /**
     * Creates a json object using the keys and values from the entries in this {@link Set}.
     */
    JsonNode toJson(final JsonNodeMarshallContext context) {
        final List<JsonNode> json = Lists.array();

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.entries) {
            final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.marshall(Cast.to(propertyAndValue.getValue()), context);

            json.add(value.setName(propertyName.marshallName()));
        }

        return JsonNode.object()
                .setChildren(json);
    }
}
