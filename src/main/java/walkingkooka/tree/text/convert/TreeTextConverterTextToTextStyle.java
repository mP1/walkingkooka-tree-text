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
import walkingkooka.tree.text.HasTextStyle;
import walkingkooka.tree.text.TextStyle;

/**
 * A {@link Converter} that accepts text and parses it into a {@link TextStyle}. Note a type of {@link HasTextStyle}
 * is also accepted and will return a {@link TextStyle}.
 */
final class TreeTextConverterTextToTextStyle<C extends ConverterContext> extends TreeTextConverterTextTo<C> {

    static <C extends ConverterContext> TreeTextConverterTextToTextStyle<C> instance() {
        return INSTANCE;
    }

    private final static TreeTextConverterTextToTextStyle INSTANCE = new TreeTextConverterTextToTextStyle();

    private TreeTextConverterTextToTextStyle() {
        super();
    }

    @Override
    public boolean isTargetType(final Object value,
                                final Class<?> type,
                                final C context) {
        return HasTextStyle.class == type || TextStyle.isStyleClass(type);
    }

    @Override
    public Object parseText(final String text,
                            final Class<?> type,
                            final C context) {
        return TextStyle.parse(text);
    }

    @Override
    public String toString() {
        return "text to TextStyle";
    }
}
