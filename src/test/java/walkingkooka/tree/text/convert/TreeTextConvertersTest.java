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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.convert.ColorConverters;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterTesting;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.net.Url;
import walkingkooka.net.convert.NetConverters;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.tree.text.Hyperlink;
import walkingkooka.tree.text.Image;
import walkingkooka.tree.text.Text;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.lang.reflect.Method;

public final class TreeTextConvertersTest implements PublicStaticHelperTesting<TreeTextConverters>,
    ConverterTesting {

    @Test
    public void testConvertStringToTextStylePropertyName() {
        final TextStylePropertyName<?> propertyName = TextStylePropertyName.COLOR;

        this.convertAndCheck(
            propertyName.text(),
            TextStylePropertyName.class,
            propertyName
        );
    }

    @Test
    public void testConvertStringToTextStyle() {
        final TextStyle textStyle = TextStyle.parse("{color:red};");

        this.convertAndCheck(
            textStyle.text(),
            TextStyle.class,
            textStyle
        );
    }

    @Test
    public void testConvertTextStyleToTextStyle() {
        final TextStyle textStyle = TextStyle.parse("{color:red};");

        this.convertAndCheck(
            textStyle,
            TextStyle.class,
            textStyle
        );
    }

    @Test
    public void testConvertStringToTextNode() {
        final Text text = TextNode.text( "Hello World 123");

        this.convertAndCheck(
            text.text(),
            Text.class,
            text
        );
    }

    @Test
    public void testConvertStringToHyperlink() {
        final Url url = Url.parseAbsolute("https://www.example.com");

        this.convertAndCheck(
            url.text(),
            Hyperlink.class,
            TextNode.hyperlink(url)
        );
    }

    @Test
    public void testConvertUrlToHyperlink() {
        final Url url = Url.parseAbsolute("https://www.example.com");
        this.convertAndCheck(
            url,
            Hyperlink.class,
            TextNode.hyperlink(url)
        );
    }

    @Test
    public void testConvertStringToImage() {
        final Url url = Url.parseAbsolute("https://www.example.com");
        this.convertAndCheck(
            url,
            Image.class,
            TextNode.image(url)
        );
    }

    private <T> void convertAndCheck(final Object value,
                                     final Class<T> type,
                                     final T expected) {
        final Converter<ConverterContext> converter = Converters.collection(
            Lists.of(
                Converters.simple(),
                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                Converters.numberToNumber(),
                NetConverters.textToUrl(),
                ColorConverters.colorToColor(),
                ColorConverters.numberToColor(),
                ColorConverters.textToColor(),
                TreeTextConverters.hasTextStyleToTextStyle(),
                TreeTextConverters.hasTextStyleToTextStyle(),
                TreeTextConverters.textToTextNode(),
                TreeTextConverters.textToTextStyle(),
                TreeTextConverters.textToTextStylePropertyName(),
                TreeTextConverters.urlToHyperlink(),
                TreeTextConverters.urlToImage()
            )
        );

        this.convertAndCheck(
            converter,
            value,
            type,
            new FakeConverterContext() {
                @Override
                public boolean canConvert(final Object value,
                                          final Class<?> type) {
                    return converter.canConvert(
                        value,
                        type,
                        this
                    );
                }

                @Override
                public <T> Either<T, String> convert(final Object value,
                                                     final Class<T> type) {
                    return converter.convert(
                        value,
                        type,
                        this
                    );
                }
            },
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextConverters> type() {
        return TreeTextConverters.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
