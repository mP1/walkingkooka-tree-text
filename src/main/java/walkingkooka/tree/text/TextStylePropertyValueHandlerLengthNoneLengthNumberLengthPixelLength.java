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

package walkingkooka.tree.text;

/**
 * A {@link TextStylePropertyValueHandler} that only allows {@link NormalLength} and {@link PixelLength} values.
 */
final class TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength extends TextStylePropertyValueHandlerLength {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength INSTANCE = new TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength() {
        super();
    }

    @Override
    boolean lengthCheck(final Length<?> length) {
        return length.isNone() ||
            length.isNumber() ||
            length.isPixel();
    }

    @Override
    String invalidValueMessage(final Object value) {
        return "Expected NoneLength | NumberLength | PixelLength got " + value.getClass().getSimpleName();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return NoneLength.class.getSimpleName() + "|" + NumberLength.class.getSimpleName() + "|" + PixelLength.class.getSimpleName();
    }
}
