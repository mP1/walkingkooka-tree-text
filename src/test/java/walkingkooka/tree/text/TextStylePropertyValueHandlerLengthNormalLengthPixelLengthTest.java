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

public final class TextStylePropertyValueHandlerLengthNormalLengthPixelLengthTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerLengthNormalLengthPixelLength, Length<?>> {

    @Test
    public void testCheckValueWithNormal() {
        this.checkValue(Length.normal());
    }

    @Test
    public void testCheckValueNumberFails() {
        this.checkValueFails(
            Length.number(1L),
            "Property \"line-height\": Expected NoneLength | PixelLength got NumberLength"
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
            "Property \"line-height\": Expected NoneLength | PixelLength got TextStylePropertyValueHandlerLengthNormalLengthPixelLengthTest"
        );
    }

    @Test
    public void testUnmarshallNoneFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .unmarshall(
                    this.marshall(Length.none()),
                    this.propertyName(),
                    this.unmarshallContext()
                )
        );
    }

    @Test
    public void testUnmarshallNormal() {
        final NormalLength normal = Length.normal();
        this.unmarshallAndCheck(this.marshall(normal), normal);
    }

    @Test
    public void testUnmarshallNumberFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .unmarshall(
                    this.marshall(Length.number(1)),
                    this.propertyName(),
                    this.unmarshallContext()
                )
        );
    }

    @Test
    public void testUnmarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.unmarshallAndCheck(this.marshall(pixel), pixel);
    }

    @Test
    public void testMarshallNormal() {
        final NormalLength normal = Length.normal();
        this.marshallAndCheck(normal, this.marshall(normal));
    }

    @Test
    public void testMarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.marshallAndCheck(pixel, this.marshall(pixel));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "NormalLength|PixelLength");
    }

    @Override
    TextStylePropertyValueHandlerLengthNormalLengthPixelLength handler() {
        return TextStylePropertyValueHandlerLengthNormalLengthPixelLength.INSTANCE;
    }

    @Override
    TextStylePropertyName<Length<?>> propertyName() {
        return TextStylePropertyName.LINE_HEIGHT;
    }

    @Override
    Length<?> propertyValue() {
        return Length.pixel(1.0);
    }

    @Override
    public String typeNameSuffix() {
        return NormalLength.class.getSimpleName() + PixelLength.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerLengthNormalLengthPixelLength> type() {
        return TextStylePropertyValueHandlerLengthNormalLengthPixelLength.class;
    }
}
