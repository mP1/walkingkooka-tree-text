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

public final class TextStylePropertyValueHandlerNormalLengthPixelLengthTest extends TextStylePropertyValueHandlerTestCase<TextStylePropertyValueHandlerNormalLengthPixelLength, Length<?>> {

    @Test
    public void testCheckNormal() {
        this.check(Length.normal());
    }

    @Test
    public void testCheckNumberFails() {
        this.checkFails(Length.number(1L), "Property \"line-height\" value 1(NumberLength) is not a NormalLength|PixelLength");
    }

    @Test
    public void testCheckPixel() {
        this.check(Length.pixel(1.5));
    }

    @Test
    public void testCheckWrongValueTypeFails() {
        this.checkFails(this, "Property " + this.propertyName().inQuotes() + " value " + this + "(" + this.getClass().getSimpleName() + ") is not a " + this.propertyValueType());
    }

    @Test
    public void testCheckWrongValueTypeFails2() {
        final FakeNode<?, ?, ?, ?> fakeNode = new FakeNode<>();
        this.checkFails(
                fakeNode,
                "Property " + this.propertyName().inQuotes() + " value " + fakeNode + "(" + FakeNode.class.getName() + ") is not a " + this.propertyValueType()
        );
    }

    @Test
    public void testJsonNodeUnmarshallNoneFails() {
        assertThrows(TextStylePropertyValueException.class, () -> this.handler().unmarshall(this.marshall(Length.none()),
                this.propertyName(),
                this.unmarshallContext()));
    }

    @Test
    public void testJsonNodeUnmarshallNormal() {
        final NormalLength normal = Length.normal();
        this.unmarshallAndCheck(this.marshall(normal), normal);
    }

    @Test
    public void testJsonNodeUnmarshallNumberFails() {
        assertThrows(TextStylePropertyValueException.class, () -> this.handler().unmarshall(this.marshall(Length.number(1)),
                this.propertyName(),
                this.unmarshallContext()));
    }

    @Test
    public void testJsonNodeUnmarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.unmarshallAndCheck(this.marshall(pixel), pixel);
    }

    @Test
    public void testJsonNodeMarshallNormal() {
        final NormalLength normal = Length.normal();
        this.marshallAndCheck(normal, this.marshall(normal));
    }

    @Test
    public void testJsonNodeMarshallPixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.marshallAndCheck(pixel, this.marshall(pixel));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "NormalLength|PixelLength");
    }

    @Override
    TextStylePropertyValueHandlerNormalLengthPixelLength handler() {
        return TextStylePropertyValueHandlerNormalLengthPixelLength.INSTANCE;
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
    String propertyValueType() {
        return "NormalLength|PixelLength";
    }

    @Override
    public String typeNameSuffix() {
        return NormalLength.class.getSimpleName() + PixelLength.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerNormalLengthPixelLength> type() {
        return TextStylePropertyValueHandlerNormalLengthPixelLength.class;
    }
}
