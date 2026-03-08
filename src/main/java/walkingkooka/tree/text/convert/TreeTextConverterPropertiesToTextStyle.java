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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.props.Properties;
import walkingkooka.tree.text.TextStyle;

/**
 * Converts a {@link Properties} into a {@link TextStyle}. Note the source value will be converted to a {@link Properties}.
 */
final class TreeTextConverterPropertiesToTextStyle<C extends ConverterContext> extends TreeTextConverter<C>
    implements TryingShortCircuitingConverter<C> {

    /**
     * Type safe singleton getter
     */
    static <C extends ConverterContext> TreeTextConverterPropertiesToTextStyle<C> instance() {
        return Cast.to(INSTANCE);
    }

    private final static TreeTextConverterPropertiesToTextStyle<?> INSTANCE = new TreeTextConverterPropertiesToTextStyle<>();

    private TreeTextConverterPropertiesToTextStyle() {
        super();
    }


    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return TextStyle.isStyleClass(type) &&
            context.canConvert(
            value,
            Properties.class
        );
    }

    @Override
    public Object tryConvertOrFail(final Object value,
                                   final Class<?> type,
                                   final C context) {
        return TextStyle.fromProperties(
            context.convertOrFail(
                value,
                Properties.class
            )
        );
    }

    @Override
    public String toString() {
        return Properties.class.getSimpleName() + " to " + TextStyle.class.getSimpleName();
    }
}
