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
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.Flag;
import walkingkooka.tree.text.TextNode;

public final class TreeTextConverterTextToFlagTest extends TreeTextConverterTestCase<TreeTextConverterTextToFlag<FakeConverterContext>, FakeConverterContext> {

    @Test
    public void testConvertTextNodeToTextNodeFails() {
        this.convertFails(
            TextNode.text("Hello"),
            TextNode.class
        );
    }

    @Test
    public void testConvertNullToFlagFails() {
        this.convertFails(
            null,
            Flag.class
        );
    }

    @Test
    public void testConvertTextNodeToFlag() {
        this.convertAndCheck(
            TextNode.flag("AU"),
            Flag.class,
            TextNode.flag("AU")
        );
    }

    @Test
    public void testConvertCharSequenceToFlag() {
        final String text = "AU";

        this.convertAndCheck(
            new StringBuilder(text),
            Flag.class,
            TextNode.flag(text)
        );
    }

    @Test
    public void testConvertStringToFlag() {
        final String text = "AU";

        this.convertAndCheck(
            text,
            Flag.class,
            TextNode.flag(text)
        );
    }

    @Override
    public TreeTextConverterTextToFlag<FakeConverterContext> createConverter() {
        return TreeTextConverterTextToFlag.instance();
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
    public Class<TreeTextConverterTextToFlag<FakeConverterContext>> type() {
        return Cast.to(TreeTextConverterTextToFlag.class);
    }
}
