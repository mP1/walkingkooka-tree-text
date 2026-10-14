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
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.tree.text.Margin;

public final class TreeTextConverterTextToMarginTest extends TreeTextConverterTestCase<TreeTextConverterTextToMargin<FakeTreeTextConverterContext>, FakeTreeTextConverterContext> {

    @Test
    public void testConvertNullToMarginFails() {
        this.convertFails(
            null,
            Margin.class
        );
    }

    @Test
    public void testConvertStringToMargin() {
        final Margin margin = Margin.parse("1.0px");

        this.convertAndCheck(
            margin.text(),
            Margin.class,
            margin
        );
    }

    @Override
    public TreeTextConverterTextToMargin<FakeTreeTextConverterContext> createConverter() {
        return TreeTextConverterTextToMargin.instance();
    }

    @Override
    public FakeTreeTextConverterContext createContext() {
        return new FakeTreeTextConverterContext() {

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

            private final Converter<FakeTreeTextConverterContext> converter = Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString();
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            TreeTextConverterTextToMargin.instance(),
            "TEXT to Margin"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextConverterTextToMargin<FakeTreeTextConverterContext>> type() {
        return Cast.to(TreeTextConverterTextToMargin.class);
    }
}
