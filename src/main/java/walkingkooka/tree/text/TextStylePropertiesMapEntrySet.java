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
import walkingkooka.collect.set.ImmutableSetDefaults;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * A read only {@link Set} sorted view of properties that have had their values checked.
 */
final class TextStylePropertiesMapEntrySet extends AbstractSet<Entry<TextStylePropertyName<?>, Object>> implements ImmutableSetDefaults<TextStylePropertiesMapEntrySet, Entry<TextStylePropertyName<?>, Object>> {

    /**
     * An empty {@link TextStylePropertiesMap}.
     */
    //static final TextStylePropertiesMapEntrySet EMPTY = new TextStylePropertiesMapEntrySet(Lists.empty());
    static final TextStylePropertiesMapEntrySet EMPTY = new TextStylePropertiesMapEntrySet(
        new Object[0],
        0
    );

    /**
     * Factory that creates a {@link TextStylePropertiesMapEntrySet}.
     */
    static TextStylePropertiesMapEntrySet with(final Object[] values,
                                               final int count) {
        return 0 == count ?
            EMPTY :
            new TextStylePropertiesMapEntrySet(
                values,
                count
            );
    }

    private TextStylePropertiesMapEntrySet(final Object[] values,
                                           final int count) {
        this.values = values;
        this.count = count;
    }

    @Override
    public Iterator<Entry<TextStylePropertyName<?>, Object>> iterator() {
        return TextStylePropertiesMapEntrySetIterator.with(this.values);
    }

    @Override
    public int size() {
        return this.count;
    }

    /**
     * The actual number of values in this set.
     */
    final int count;

    final Object[] values;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        int index = 0;

        for (Object value : this.values) {
            if (null != value) {
                visitor.acceptPropertyAndValue(
                    TextStylePropertyName.NAMES[index],
                    Cast.to(value)
                );
            }

            index++;
        }
    }

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

    // JsonNodeContext..................................................................................................

    /**
     * Recreates this {@link TextStylePropertiesMapEntrySet} from the json object.
     */
    static TextStylePropertiesMapEntrySet unmarshall(final JsonNode json,
                                                     final JsonNodeUnmarshallContext context) {
        final List<Object> values = Lists.autoExpandArray();
        int size = 0;

        for (final JsonNode child : json.children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshall(child);
            final int index = name.index();

            final Object value = name.handler.unmarshall(
                child,
                name,
                context
            );

            values.set(
                index,
                value
            );

            size++;
        }

        return with(
            values.toArray(),
            size
        );
    }

    /**
     * Creates a json object using the keys and values from the entries in this {@link Set}.
     */
    JsonNode toJson(final JsonNodeMarshallContext context) {
        final List<JsonNode> json = Lists.array();

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this) {
            final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.marshall(
                Cast.to(
                    propertyAndValue.getValue()
                ),
                context
            );

            json.add(value.setName(propertyName.marshallName()));
        }

        return JsonNode.object()
            .setChildren(json);
    }
}
