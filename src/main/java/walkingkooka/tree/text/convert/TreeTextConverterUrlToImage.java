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
import walkingkooka.tree.text.Image;
import walkingkooka.tree.text.TextNode;

final class TreeTextConverterUrlToImage<C extends ConverterContext> extends TreeTextConverterUrlTo<Image, C> {

    static <C extends ConverterContext> TreeTextConverterUrlToImage<C> instance() {
        return INSTANCE;
    }

    private final static TreeTextConverterUrlToImage INSTANCE = new TreeTextConverterUrlToImage();

    private TreeTextConverterUrlToImage() {
        super();
    }

    @Override
    Class<Image> type() {
        return Image.class;
    }

    @Override
    Image textNode(final Url url) {
        return TextNode.image(url);
    }
}