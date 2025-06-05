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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLengthTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength, Length<?>> {

    @Test
    public void testCheckValueNone() {
        this.checkValue(
            Length.none()
        );
    }

    @Test
    public void testCheckValueWithNormalFails() {
        this.checkValueFails(
            Length.normal(),
            "Property \"tab-size\": Expected NoneLength | NumberLength | PixelLength got NormalLength"
        );
    }

    @Test
    public void testCheckValueNumber() {
        this.checkValue(
            Length.number(1L)
        );
    }

    @Test
    public void testCheckValuePixel() {
        this.checkValue(Length.pixel(1.5));
    }

    @Test
    public void testCheckWrongValueTypeFails() {
        this.checkValueFails(
            this,
            "Property \"tab-size\": Expected NoneLength | NumberLength | PixelLength got TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLengthTest"
        );
    }

    @Test
    public void testUnmarshallNormalFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .unmarshall(
                    this.marshall(Length.normal()),
                    this.propertyName(),
                    this.unmarshallContext()
                )
        );
    }

    @Test
    public void testUnmarshallNone() {
        final NoneLength none = Length.none();
        this.unmarshallAndCheck(
            this.marshall(none),
            none
        );
    }

    @Test
    public void testUnmarshallNumber() {
        final NumberLength number = Length.number(123);
        this.unmarshallAndCheck(
            this.marshall(number),
            number
        );
    }

    @Test
    public void testUnmarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.unmarshallAndCheck(
            this.marshall(pixel),
            pixel
        );
    }

    @Test
    public void testMarshallNone() {
        final NoneLength none = Length.none();
        this.marshallAndCheck(
            none,
            this.marshall(none)
        );
    }

    @Test
    public void testMarshallNumber() {
        final NumberLength number = Length.number(12);
        this.marshallAndCheck(
            number,
            this.marshall(number)
        );
    }

    @Test
    public void testMarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.marshallAndCheck(
            pixel,
            this.marshall(pixel)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "NoneLength|NumberLength|PixelLength"
        );
    }

    @Override
    TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength handler() {
        return TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength.INSTANCE;
    }

    @Override
    TextStylePropertyName<Length<?>> propertyName() {
        return TextStylePropertyName.TAB_SIZE;
    }

    @Override
    Length<?> propertyValue() {
        return Length.pixel(1.0);
    }

    @Override
    public String typeNameSuffix() {
        return NoneLength.class.getSimpleName() + NumberLength.class.getSimpleName() + PixelLength.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength> type() {
        return TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength.class;
    }
}
