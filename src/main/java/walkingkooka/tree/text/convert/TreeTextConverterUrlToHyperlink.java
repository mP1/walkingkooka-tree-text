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
import walkingkooka.net.Url;
import walkingkooka.tree.text.Hyperlink;
import walkingkooka.tree.text.TextNode;

/**
 * Converts {@link Url}, creating a {@link Hyperlink} with the given {@link Url}.
 */
final class TreeTextConverterUrlToHyperlink<C extends ConverterContext> extends TreeTextConverterUrlTo<Hyperlink, C> {

    static <C extends ConverterContext> TreeTextConverterUrlToHyperlink<C> instance() {
        return INSTANCE;
    }

    private final static TreeTextConverterUrlToHyperlink INSTANCE = new TreeTextConverterUrlToHyperlink();

    private TreeTextConverterUrlToHyperlink() {
        super();
    }

    @Override
    Class<Hyperlink> type() {
        return Hyperlink.class;
    }

    @Override
    Hyperlink textNode(final Url url) {
        return TextNode.hyperlink(url);
    }
}