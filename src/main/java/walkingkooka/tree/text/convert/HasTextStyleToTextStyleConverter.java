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

import walkingkooka.Cast;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.tree.text.HasTextStyle;
import walkingkooka.tree.text.TextStyle;

/**
 * A {@link Converter} that accepts converts a {@link walkingkooka.tree.text.HasTextStyle} into a {@link TextStyle}.
 */
final class HasTextStyleToTextStyleConverter<C extends ConverterContext> implements TryingShortCircuitingConverter<C> {

    static <C extends ConverterContext> HasTextStyleToTextStyleConverter<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static HasTextStyleToTextStyleConverter<?> INSTANCE = new HasTextStyleToTextStyleConverter<>();

    private HasTextStyleToTextStyleConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return value instanceof HasTextStyle && HasTextStyle.class == type;
    }


    @Override
    public TextStyle tryConvertOrFail(final Object value,
                                      final Class<?> type,
                                      final C context) {
        return ((HasTextStyle) value).textStyle();
    }

    @Override
    public String toString() {
        return HasTextStyle.class.getSimpleName() + " to " + TextStyle.class.getSimpleName();
    }
}
