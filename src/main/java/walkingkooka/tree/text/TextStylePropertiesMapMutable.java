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

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A mutable {@link Map} that is backed by an array and is used by {@link TextStyle} when processing a few set/remove operations.
 */
final class TextStylePropertiesMapMutable extends AbstractMap<TextStylePropertyName<?>, Object> implements CanBeEmpty {

    static TextStylePropertiesMapMutable empty() {
        return new TextStylePropertiesMapMutable();
    }

    private TextStylePropertiesMapMutable() {
        super();
        this.values = new Object[TextStylePropertyName.NAMES.length];
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    int size;

    @Override
    public Object get(final Object name) {
        return name instanceof TextStylePropertyName ?
            this.getTextStylePropertyName(
                (TextStylePropertyName) name) :
            null;
    }

    private Object getTextStylePropertyName(final TextStylePropertyName<?> name) {
        return this.values[name.index];
    }

    @Override
    public Object put(final TextStylePropertyName<?> name, final Object value) {
        Objects.requireNonNull(name, "name");

        final int index = name.index;

        Object old = this.values[index];
        name.checkValue(value);
        this.values[index] = value;

        if (null == old) {
            this.size++;
        }

        return old;
    }

    final Object[] values;

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        return TextStylePropertiesMapEntrySet.with(
            this.values,
            this.size
        );
    }
}
