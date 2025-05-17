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

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A measurement in pixels.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
public final class PixelLength extends Length<Double> implements Value<Double> {

    private final static LengthUnit<Double, PixelLength> UNIT = LengthUnit.PIXEL;

    static PixelLength parsePixels0(final String text) {
        UNIT.parseUnitTextCheck(text);

        try {
            return with(Double.parseDouble(text.substring(0, text.length() - 2)));
        } catch (final NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid pixel count " + CharSequences.quoteAndEscape(text), cause);
        }
    }

    static PixelLength with(final double value) {
        return new PixelLength(value);
    }

    private PixelLength(final double value) {
        super();
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    private final double value;

    @Override
    double doubleValue() {
        return this.value;
    }

    @Override
    long longValue() {
        throw new UnsupportedOperationException();
    }


    @Override
    public double pixelValue() {
        return this.doubleValue();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Double, Length<Double>>> unit() {
        return UNIT_OPTIONAL;
    }

    private final static Optional<LengthUnit<Double, Length<Double>>> UNIT_OPTIONAL = Cast.to(Optional.of(UNIT));

    // LengthVisitor....................................................................................................

    @Override
    void accept(final LengthVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    boolean equals0(final Length<?> other) {
        return 0 == Double.compare(this.value, other.doubleValue());
    }

    @Override
    public String toString() {
        return UNIT.toString(this.value);
    }
}
