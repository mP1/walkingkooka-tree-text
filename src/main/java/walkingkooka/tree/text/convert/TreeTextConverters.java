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

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.reflect.PublicStaticHelper;

public final class TreeTextConverters implements PublicStaticHelper{

    /**
     * {@see HasTextNodeToTextNodeConverter}
     */
    public static <C extends ConverterContext> Converter<C> hasTextNodeToTextNode() {
        return HasTextNodeToTextNodeConverter.instance();
    }

    /**
     * {@see TextToTextNodeConverter}
     */
    public static <C extends ConverterContext> Converter<C> textToTextNode() {
        return TextToTextNodeConverter.instance();
    }

    /**
     * {@see TextToTextStyleConverter}
     */
    public static <C extends ConverterContext> Converter<C> textToTextStyle() {
        return TextToTextStyleConverter.instance();
    }

    /**
     * {@see TextToTextStylePropertyNameConverter}
     */
    public static <C extends ConverterContext> Converter<C> textToTextStylePropertyName() {
        return TextToTextStylePropertyNameConverter.instance();
    }

    /**
     * {@see UrlToHyperlinkConverter}
     */
    public static <C extends ConverterContext> Converter<C> urlToHyperlink() {
        return UrlToHyperlinkConverter.instance();
    }

    /**
     * {@see UrlToImageConverter}
     */
    public static <C extends ConverterContext> Converter<C> urlToImage() {
        return UrlToImageConverter.instance();
    }

    /**
     * Stop creation
     */
    private TreeTextConverters() {
        throw new UnsupportedOperationException();
    }
}
