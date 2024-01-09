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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;

public final class PaddingTest extends BorderMarginPaddingTestCase<Padding> {

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                Padding.with(BoxEdge.BOTTOM,
                        TextStyle.EMPTY.setValues(
                                Maps.of(
                                        TextStylePropertyName.PADDING_BOTTOM, Length.pixel(12.5),
                                        TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED)
                        )
                ),
                "BOTTOM {border-right-style=DOTTED, padding-bottom=12.5px}"
        );
    }

    // helpers..........................................................................................................

    @Override
    Padding createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.padding(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.paddingPropertyName();
    }

    @Override
    public Class<Padding> type() {
        return Padding.class;
    }
}
