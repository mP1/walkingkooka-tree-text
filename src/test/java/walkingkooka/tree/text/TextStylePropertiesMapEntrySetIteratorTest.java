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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TextStylePropertiesMapEntrySetIteratorTest implements IteratorTesting,
    ClassTesting<TextStylePropertiesMapEntrySetIterator> {

    @Test
    public void testIterateWhenEmpty() {
        this.iterateAndCheck2(
            Maps.empty()
        );
    }

    @Test
    public void testIterateWhenFirstAndOnly() {
        this.iterateAndCheck2(
            Maps.of(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.BLACK
            )
        );
    }

    @Test
    public void testIterateWhenFirstTwo() {
        this.iterateAndCheck2(
            Maps.of(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.BLACK,
                TextStylePropertyName.BORDER_BOTTOM_COLOR,
                Color.WHITE
            )
        );
    }

    @Test
    public void testIterateWhenSecond() {
        this.iterateAndCheck2(
            Maps.of(
                TextStylePropertyName.BORDER_BOTTOM_COLOR,
                Color.WHITE
            )
        );
    }

    @Test
    public void testIterateWhenFirstAndThird() {
        this.iterateAndCheck2(
            Maps.of(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.BLACK,
                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                BorderStyle.DASHED
            )
        );
    }

    @Test
    public void testIterateWhenSeveral() {
        this.iterateAndCheck2(
            Maps.of(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.BLACK,
                TextStylePropertyName.COLOR,
                Color.WHITE
            )
        );
    }

    private void iterateAndCheck2(final Map<TextStylePropertyName<?>, Object> values) {
        final List<Entry<TextStylePropertyName<?>, Object>> entries = Lists.autoExpandArray();

        for (final Entry<TextStylePropertyName<?>, Object> entry : values.entrySet()) {
            entries.set(
                entry.getKey().index(),
                Map.entry(
                    entry.getKey(),
                    entry.getValue()
                )
            );
        }

        this.iterateAndCheck(
            this.createIterator(
                entries.stream()
                    .map(e -> null != e ? e.getValue() : null)
                    .toArray()
            ),
            entries.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
    }

    private TextStylePropertiesMapEntrySetIterator createIterator(final Object... values) {
        return TextStylePropertiesMapEntrySetIterator.with(values);
    }

    // class............................................................................................................

    @Override
    public Class<TextStylePropertiesMapEntrySetIterator> type() {
        return TextStylePropertiesMapEntrySetIterator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
