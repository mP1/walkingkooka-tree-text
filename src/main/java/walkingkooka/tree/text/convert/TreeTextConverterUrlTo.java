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
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.net.Url;
import walkingkooka.tree.text.TextNode;

abstract class TreeTextConverterUrlTo<T extends TextNode, C extends ConverterContext> extends TreeTextConverter<C> implements TryingShortCircuitingConverter<C> {

    TreeTextConverterUrlTo() {
        super();
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final C context) {
        return this.type() == type &&
            context.canConvert(
                value,
                Url.class
            );
    }

    abstract Class<T> type();

    @Override
    public final Object tryConvertOrFail(final Object value,
                                   final Class<?> type,
                                   final C context) {
        return this.textNode(
            context.convertOrFail(
                value,
                Url.class
            )
        );
    }

    abstract T textNode(final Url url);

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return "url to " + this.type();
    }
}
