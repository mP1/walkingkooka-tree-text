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
import walkingkooka.ToStringTesting;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.HasTextStyle;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class HasTextStyleToTextStyleConverterTest implements ConverterTesting2<HasTextStyleToTextStyleConverter<FakeConverterContext>, FakeConverterContext>,
    ToStringTesting<HasTextStyleToTextStyleConverter<FakeConverterContext>> {

    @Test
    public void testConvertStringToString() {
        this.convertFails(
            "hello",
            String.class
        );
    }

    @Test
    public void testConvertTextStyleToTextStyle() {
        this.convertAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
            ),
            HasTextStyle.class
        );
    }

    @Test
    public void testConvertTextWithTextStyleToTextStyle() {
        final TextStyle textStyle = TextStyle.parse("text-align: left");
        final TextNode textNode = TextNode.text("Hello")
            .setTextStyle(textStyle);

        this.convertAndCheck(
            textNode,
            HasTextStyle.class,
            textStyle
        );
    }

    @Override
    public HasTextStyleToTextStyleConverter<FakeConverterContext> createConverter() {
        return HasTextStyleToTextStyleConverter.instance();
    }

    @Override
    public FakeConverterContext createContext() {
        return (FakeConverterContext) ConverterContexts.fake();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverter(),
            "HasTextStyle to TextStyle"
        );
    }

    // class............................................................................................................

    @Override
    public Class<HasTextStyleToTextStyleConverter<FakeConverterContext>> type() {
        return Cast.to(HasTextStyleToTextStyleConverter.class);
    }
}
