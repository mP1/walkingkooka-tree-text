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

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.TextToTryingShortCircuitingConverter;
import walkingkooka.tree.text.Text;
import walkingkooka.tree.text.TextNode;

/**
 * Converts a String to a {@link Text}
 */
final class TextToTextNodeConverter<C extends ConverterContext> implements TextToTryingShortCircuitingConverter<C> {

    static <C extends ConverterContext> TextToTextNodeConverter<C> instance() {
        return INSTANCE;
    }

    private final static TextToTextNodeConverter INSTANCE = new TextToTextNodeConverter();

    private TextToTextNodeConverter() {
        super();
    }

    @Override
    public boolean isTargetType(final Object value,
                                final Class<?> type,
                                final C context) {
        return false == value instanceof TextNode &&
            (Text.class == type || TextNode.class == type);
    }

    @Override
    public Object parseText(final String text,
                            final Class<?> type,
                            final C context) {
        return TextNode.text(text);
    }

    @Override
    public String toString() {
        return "Text to TextNode";
    }
}