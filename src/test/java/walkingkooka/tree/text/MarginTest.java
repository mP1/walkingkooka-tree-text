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
import walkingkooka.color.Color;

public final class MarginTest extends BorderMarginPaddingTestCase<Margin> {

    @Test
    public void testToString() {
        this.toStringAndCheck(Margin.with(BoxEdge.BOTTOM,
                TextStyle.with(Maps.of(TextStylePropertyName.MARGIN_BOTTOM, Length.pixel(12.5),
                        TextStylePropertyName.COLOR, Color.fromRgb(0x123456)))),
                "BOTTOM {color=#123456, margin-bottom=12.5px}");
    }

    // helpers..........................................................................................................

    @Override
    Margin createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.margin(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.marginPropertyName();
    }

    @Override
    public Class<Margin> type() {
        return Margin.class;
    }
}
