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
import walkingkooka.tree.FakeNode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyValueHandlerLengthNoneLengthPixelLengthTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerLengthNoneLengthPixelLength, Length<?>> {

    @Test
    public void testCheckValueWithNone() {
        this.checkValue(Length.none());
    }

    @Test
    public void testCheckValueWithNormalFails() {
        this.checkValueFails(
            Length.normal(),
            "Property \"max-height\": Expected NoneLength | PixelLength got NormalLength"
        );
    }

    @Test
    public void testCheckValueWithNumberFails() {
        this.checkValueFails(
            Length.number(1L),
            "Property \"max-height\": Expected NoneLength | PixelLength got NumberLength");
    }

    @Test
    public void testCheckValueWithPixel() {
        this.checkValue(Length.pixel(1.5));
    }

    @Test
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Property \"max-height\": Expected NoneLength | PixelLength got TextStylePropertyValueHandlerLengthNoneLengthPixelLengthTest"
        );
    }

    @Test
    public void testCheckValueWithIncompatibleTypeFails2() {
        final FakeNode<?, ?, ?, ?> fakeNode = new FakeNode<>();
        this.checkValueFails(
            fakeNode,
            "Property \"max-height\": Expected NoneLength | PixelLength got FakeNode"
        );
    }

    @Test
    public void testUnmarshallNone() {
        final NoneLength none = Length.none();
        this.unmarshallAndCheck(this.marshall(none), none);
    }

    @Test
    public void testUnmarshallNormalFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .unmarshall(
                    this.marshall(
                        Length.normal()
                    ),
                    this.propertyName(),
                    this.unmarshallContext()
                )
        );
    }

    @Test
    public void testUnmarshallNumberFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.handler()
                .unmarshall(
                    this.marshall(
                        Length.number(1)
                    ),
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
        this.toStringAndCheck(this.handler(), "NoneLength|PixelLength");
    }

    @Override
    TextStylePropertyValueHandlerLengthNoneLengthPixelLength handler() {
        return TextStylePropertyValueHandlerLengthNoneLengthPixelLength.INSTANCE;
    }

    @Override
    TextStylePropertyName<Length<?>> propertyName() {
        return TextStylePropertyName.MAX_HEIGHT;
    }

    @Override
    Length<?> propertyValue() {
        return Length.pixel(1.0);
    }

    @Override
    public String typeNameSuffix() {
        return NoneLength.class.getSimpleName() + PixelLength.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerLengthNoneLengthPixelLength> type() {
        return TextStylePropertyValueHandlerLengthNoneLengthPixelLength.class;
    }
}
