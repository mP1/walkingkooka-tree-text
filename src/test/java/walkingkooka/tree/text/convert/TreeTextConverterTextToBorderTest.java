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
import walkingkooka.tree.text.Border;

public final class TreeTextConverterTextToBorderTest extends TreeTextConverterTestCase<TreeTextConverterTextToBorder<FakeTreeTextConverterContext>, FakeTreeTextConverterContext> {

    @Test
    public void testConvertNullToBorderFails() {
        this.convertFails(
            null,
            Border.class
        );
    }

    @Test
    public void testConvertStringToBorder() {
        final Border border = Border.parse("BLACK DASHED 1.0px");

        this.convertAndCheck(
            border.text(),
            Border.class,
            border
        );
    }

    @Override
    public TreeTextConverterTextToBorder<FakeTreeTextConverterContext> createConverter() {
        return TreeTextConverterTextToBorder.instance();
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
            TreeTextConverterTextToBorder.instance(),
            "TEXT to Border"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextConverterTextToBorder<FakeTreeTextConverterContext>> type() {
        return Cast.to(TreeTextConverterTextToBorder.class);
    }
}
