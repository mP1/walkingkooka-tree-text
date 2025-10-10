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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Iterator that returns all non-null values in the given array.
 */
final class TextStylePropertiesMapEntrySetIterator implements Iterator<Entry<TextStylePropertyName<?>, Object>> {

    static TextStylePropertiesMapEntrySetIterator with(final Object[] values) {
        return new TextStylePropertiesMapEntrySetIterator(values);
    }

    private TextStylePropertiesMapEntrySetIterator(final Object[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        int nextIndex = this.nextIndex;
        final Object[] values = this.values;
        final int length = values.length;

        while (nextIndex < length) {
            if (values[nextIndex] != null) {
                break;
            }

            nextIndex++;
        }

        this.nextIndex = nextIndex;
        return nextIndex < length;
    }

    @Override
    public Entry<TextStylePropertyName<?>, Object> next() {
        if (false == this.hasNext()) {
            throw new NoSuchElementException();
        }

        final int nextIndex = this.nextIndex;

        final Entry<TextStylePropertyName<?>, Object> next = Map.entry(
            TextStylePropertyName.NAMES[nextIndex],
            this.values[nextIndex]
        );

        this.nextIndex = nextIndex + 1;
        return next;
    }

    private final Object[] values;

    /**
     * The index to the next non-null value. If this value is greater than the length of {@link #values} the iterator is exhausted / empty.
     */
    int nextIndex;
}
