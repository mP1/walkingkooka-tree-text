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
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.Text;
import walkingkooka.tree.text.TextNode;

public final class TextToTextNodeConverterTest implements ConverterTesting2<TextToTextNodeConverter<FakeConverterContext>, FakeConverterContext> {

    @Test
    public void testConvertCharSequenceToTextNode() {
        final String text = "Hello";

        this.convertAndCheck(
            new StringBuilder(text),
            TextNode.class,
            TextNode.text(text)
        );
    }

    @Test
    public void testConvertCharSequenceToText() {
        final String text = "Hello";

        this.convertAndCheck(
            new StringBuilder(text),
            Text.class,
            TextNode.text(text)
        );
    }

    @Override
    public TextToTextNodeConverter<FakeConverterContext> createConverter() {
        return TextToTextNodeConverter.instance();
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
    public Class<TextToTextNodeConverter<FakeConverterContext>> type() {
        return Cast.to(TextToTextNodeConverter.class);
    }
}
