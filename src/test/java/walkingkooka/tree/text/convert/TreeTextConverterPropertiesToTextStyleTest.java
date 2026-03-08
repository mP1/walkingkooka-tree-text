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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

public final class TreeTextConverterPropertiesToTextStyleTest extends TreeTextConverterTestCase<TreeTextConverterPropertiesToTextStyle<FakeConverterContext>, FakeConverterContext>  {

    @Test
    public void testConvertPropertiesToTextStyle() {
        final TextStyle textStyle = TextStyle.parse("color:red; text-align: left");

        this.convertAndCheck(
            textStyle.properties(),
            textStyle
        );
    }

    @Test
    public void testConvertStringToTextStyle() {
        final TextStyle textStyle = TextStyle.parse("color:red; text-align: left");

        this.convertAndCheck(
            textStyle.properties()
                .toString(),
            textStyle
        );
    }

    @Override
    public TreeTextConverterPropertiesToTextStyle<FakeConverterContext> createConverter() {
        return TreeTextConverterPropertiesToTextStyle.instance();
    }

    @Override
    public FakeConverterContext createContext() {
        return new FakeConverterContext() {

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return CONVERTER.canConvert(
                    value,
                    type,
                    this
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type) {
                return CONVERTER.convert(
                    value,
                    type,
                    this
                );
            }

            private final Converter<ConverterContext> CONVERTER = Converters.collection(
                Lists.of(
                    Converters.simple(),
                    Converters.textToProperties()
                )
            );

            @Override
            public Optional<Currency> currencyForCurrencyCode(final String currencyCode) {
                return Optional.of(
                    Currency.getInstance(currencyCode)
                );
            }

            @Override
            public Optional<Locale> localeForLanguageTag(final String languageTag) {
                return Optional.of(
                    Locale.forLanguageTag(languageTag)
                );
            }
        };
    }

    @Override
    public Class<TreeTextConverterPropertiesToTextStyle<FakeConverterContext>> type() {
        return Cast.to(TreeTextConverterPropertiesToTextStyle.class);
    }
}
