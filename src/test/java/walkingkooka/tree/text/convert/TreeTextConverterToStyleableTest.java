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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextConverterToStyleableTest extends TreeTextConverterTestCase<TreeTextConverterToStyleable<FakeConverterContext>, FakeConverterContext>
    implements ToStringTesting<TreeTextConverterToStyleable<FakeConverterContext>> {

    @Test
    public void testStringToStringFails() {
        this.convertFails(
            "color: #111;",
            String.class
        );
    }

    @Test
    public void testStringToStyleable() {
        final String text = "color: #111";

        this.convertAndCheck(
            text,
            Styleable.class,
            TextStyle.parse(text)
        );
    }

    @Test
    public void testTextNodeToStyleable() {
        this.convertAndCheck(
            TextNode.EMPTY_TEXT.setTextStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.CENTER
                )
            ),
            Styleable.class
        );
    }

    @Test
    public void testTextStyleToStyleable() {
        this.convertAndCheck(
            TextStyle.EMPTY,
            Styleable.class
        );
    }

    @Override
    public TreeTextConverterToStyleable<FakeConverterContext> createConverter() {
        return TreeTextConverterToStyleable.instance();
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
                return this.converter.convert(
                    value,
                    target,
                    this
                );
            }

            private final Converter<FakeConverterContext> converter = Converters.collection(
                Lists.of(
                    Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                    TreeTextConverters.textToTextStyle()
                )
            );
        };
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverter(),
            "to Styleable"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextConverterToStyleable<FakeConverterContext>> type() {
        return Cast.to(TreeTextConverterToStyleable.class);
    }
}
