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
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextStyle;

/**
 * A {@link Converter} that accepts converts an object if it implements {@link Styleable}.
 */
final class ToStyleableConverter<C extends ConverterContext> implements TryingShortCircuitingConverter<C> {

    /**
     * Type-safe getter.
     */
    static <C extends ConverterContext> ToStyleableConverter<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Private singleton
     */
    private final static ToStyleableConverter<?> INSTANCE = new ToStyleableConverter<>();

    private ToStyleableConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return Styleable.class == type &&
            (
                value instanceof Styleable ||
                    value instanceof CharSequence && context.canConvert(
                        value,
                        TextStyle.class
                    )
            );
    }

    @Override
    public Styleable tryConvertOrFail(final Object value,
                                      final Class<?> type,
                                      final C context) {
        return value instanceof CharSequence ?
            context.convertOrFail(
                value,
                TextStyle.class
            ) :
            (Styleable) value;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "to " + Styleable.class.getSimpleName();
    }
}
