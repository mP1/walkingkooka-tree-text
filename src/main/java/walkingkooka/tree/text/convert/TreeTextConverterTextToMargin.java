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

package walkingkooka.tree.text.convert;

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.text.Margin;

/**
 * A {@link Converter} that creates a {@link Margin}
 */
final class TreeTextConverterTextToMargin<C extends ConverterContext> extends TreeTextConverterTextTo<C> {

    static <C extends ConverterContext> TreeTextConverterTextToMargin<C> instance() {
        return INSTANCE;
    }

    private final static TreeTextConverterTextToMargin INSTANCE = new TreeTextConverterTextToMargin<>();

    private TreeTextConverterTextToMargin() {
        super();
    }

    @Override
    public boolean isTargetType(final Object value,
                                final Class<?> type,
                                final C context) {
        return Margin.class == type;
    }

    @Override
    public Object parseText(final String text,
                            final Class<?> type,
                            final C context) {
        return Margin.parse(text);
    }

    @Override
    public String toString() {
        return "text to " + Margin.class.getSimpleName();
    }
}
