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
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.convert.TextToTryingShortCircuitingConverter;
import walkingkooka.net.Url;
import walkingkooka.tree.text.Image;
import walkingkooka.tree.text.TextNode;

public final class UrlToImageConverterTest implements ConverterTesting2<UrlToImageConverter<FakeConverterContext>, FakeConverterContext> {

    @Test
    public void testConvertCharSequenceToImage() {
        final String url = "http://www.example.com/image.jpg";

        this.convertAndCheck(
            url,
            Image.class,
            TextNode.image(
                Url.parseAbsolute(url)
            )
        );
    }

    @Test
    public void testConvertUrlToImage() {
        final Url url = Url.parse("http://www.example.com/image.jpg");

        this.convertAndCheck(
            url,
            Image.class,
            TextNode.image(url)
        );
    }

    @Override
    public UrlToImageConverter<FakeConverterContext> createConverter() {
        return UrlToImageConverter.instance();
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

            private final Converter<FakeConverterContext> converter = Converters.collection(
                Lists.of(
                    Converters.simple(),
                    new TextToTryingShortCircuitingConverter<>() {

                        @Override
                        public boolean isTargetType(final Object value,
                                                    final Class<?> type,
                                                    final ConverterContext context) {
                            return Url.isClass(type);
                        }

                        @Override
                        public Object parseText(final String text,
                                                final Class<?> type,
                                                final ConverterContext context) {
                            return Url.parseAsUrl(
                                text,
                                Cast.to(type)
                            );
                        }
                    },
                    Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString()
                )
            ).cast(FakeConverterContext.class);
        };
    }

    @Override
    public Class<UrlToImageConverter<FakeConverterContext>> type() {
        return Cast.to(UrlToImageConverter.class);
    }
}
