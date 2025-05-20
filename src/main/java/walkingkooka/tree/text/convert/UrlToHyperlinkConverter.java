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
import walkingkooka.tree.text.Hyperlink;
import walkingkooka.tree.text.TextNode;

final class UrlToHyperlinkConverter <C extends ConverterContext> implements TryingShortCircuitingConverter<C> {

    static <C extends ConverterContext> UrlToHyperlinkConverter<C> instance() {
        return INSTANCE;
    }

    private final static UrlToHyperlinkConverter INSTANCE = new UrlToHyperlinkConverter();

    private UrlToHyperlinkConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return Hyperlink.class == type &&
            context.canConvert(
                value,
                Url.class
            );
    }

    @Override
    public Object tryConvertOrFail(final Object value,
                                   final Class<?> type,
                                   final C context) {
        return TextNode.hyperlink(
            context.convertOrFail(
                value,
                Url.class
            )
        );
    }

    @Override
    public String toString() {
        return "url to Hyperlink";
    }
}