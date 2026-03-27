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

final class BoxEdgeParserPadding extends BoxEdgeParser<Padding> {

    static BoxEdgeParserPadding with(final BoxEdge edge) {
        return new BoxEdgeParserPadding(edge);
    }

    private BoxEdgeParserPadding(final BoxEdge boxEdge) {
        super(boxEdge);
    }

    @Override
    TextStylePropertyName<Length<?>> width(final BoxEdge boxEdge) {
        return boxEdge.paddingPropertyName();
    }

    @Override
    Padding setBoxEdgeAndTextStyle(final TextStyle textStyle) {
        return Padding.with(
            this.boxEdge,
            textStyle
        );
    }

    @Override
    Padding setWidth(final Optional<Length<?>> width) {
        return this.boxEdge.setPadding(width);
    }
}
