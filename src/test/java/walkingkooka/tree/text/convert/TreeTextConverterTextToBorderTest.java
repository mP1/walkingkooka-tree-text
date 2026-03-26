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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;

public final class TreeTextConverterTextToBorderTest extends TreeTextConverterTestCase<TreeTextConverterTextToBorder<FakeConverterContext>, FakeConverterContext> {

    @Test
    public void testConvertNullToBorderFails() {
        this.convertFails(
            null,
            Border.class
        );
    }

    @Test
    public void testConvertStringToBorder() {
        final Border border = TextStyle.EMPTY.setBorder(
            Color.BLACK,
            BorderStyle.DASHED,
            Length.pixel(1.0)
        ).border(BoxEdge.ALL);

        this.convertAndCheck(
            border.text(),
            Border.class,
            border
        );
    }

    @Override
    public TreeTextConverterTextToBorder<FakeConverterContext> createConverter() {
        return TreeTextConverterTextToBorder.instance();
    }

    @Override
    public FakeConverterContext createContext() {
        return new FakeConverterContext() {

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return this.converter.canConvert(
                    value,
                    type,
                    this
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return converter.convert(
                    value,
                    target,
                    this
                );
            }

            private final Converter<FakeConverterContext> converter = Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString();
        };
    }

    @Override
    public Class<TreeTextConverterTextToBorder<FakeConverterContext>> type() {
        return Cast.to(TreeTextConverterTextToBorder.class);
    }
}
