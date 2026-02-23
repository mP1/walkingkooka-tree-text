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

import walkingkooka.convert.ConverterContext;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * Useful to convert string parameters to a function that requires a {@link TextStylePropertyName}.
 */
final class TreeTextConverterTextToTextStylePropertyName<C extends ConverterContext> extends TreeTextConverterTextTo<C> {

    static <C extends ConverterContext> TreeTextConverterTextToTextStylePropertyName<C> instance() {
        return INSTANCE;
    }

    private final static TreeTextConverterTextToTextStylePropertyName INSTANCE = new TreeTextConverterTextToTextStylePropertyName();

    private TreeTextConverterTextToTextStylePropertyName() {
        super();
    }

    @Override
    public boolean isTargetType(final Object value,
                                final Class<?> type,
                                final C context) {
        return TextStylePropertyName.class == type;
    }

    @Override
    public Object parseText(final String text,
                            final Class<?> type,
                            final C context) {
        return TextStylePropertyName.with(text);
    }

    @Override
    public String toString() {
        return "text to TextStylePropertyName";
    }
}
