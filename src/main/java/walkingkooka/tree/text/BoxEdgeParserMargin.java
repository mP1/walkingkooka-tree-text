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

package walkingkooka.tree.text;

import java.util.Optional;

final class BoxEdgeParserMargin extends BoxEdgeParser<Margin> {

    static BoxEdgeParserMargin with(final BoxEdge edge) {
        return new BoxEdgeParserMargin(edge);
    }

    private BoxEdgeParserMargin(final BoxEdge boxEdge) {
        super(boxEdge);
    }

    @Override
    TextStylePropertyName<Length<?>> width(final BoxEdge boxEdge) {
        return boxEdge.marginPropertyName();
    }

    @Override
    Margin setBoxEdgeAndTextStyle(final TextStyle textStyle) {
        return Margin.with(
            this.boxEdge,
            textStyle
        );
    }

    @Override
    Margin setWidth(final Optional<Length<?>> width) {
        return this.boxEdge.setMargin(width);
    }
}
