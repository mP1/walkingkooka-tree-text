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
import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
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
        CharSequences.failIfNullOrEmpty(text, "text");

        final int MODE_WHOLE = 0;
        final int MODE_DECIMAL = 1;
        final int MODE_UNIT = 2;

        int mode = MODE_WHOLE;

        double value = 0;
        double multiplier = 0.1;

        int unitStart = -1;

        int i = 0;
        for(final char c : text.toCharArray()) {
            switch(mode) {
                case MODE_WHOLE:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            final int digit = Character.digit(c, 10);
                            value = value * 10 + digit;
                            break;
                        case '.':
                            mode = MODE_DECIMAL;
                            break;
                        default:
                            unitStart = i;
                            mode = MODE_UNIT;
                            break;
                    }
                    break;
                case MODE_DECIMAL:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = value + Character.digit(c, 10) * multiplier;
                            multiplier = multiplier * 0.1;
                            break;
                        default:
                            unitStart = i;
                            mode = MODE_UNIT;
                            break;
                    }
                    break;
                case MODE_UNIT:
                    break;
                default:
                    NeverError.unhandledCase(
                        mode,
                        MODE_WHOLE,
                        MODE_DECIMAL,
                        MODE_UNIT
                    );
            }

            if(MODE_UNIT == mode) {
                if(UNIT.text().charAt(i - unitStart) != c) {
                    throw new InvalidCharacterException(
                        text,
                        i
                    );
                }
            }

            i++;
        }

        if(-1 == unitStart) {
            throw new IllegalArgumentException("Missing unit");
        }

        return with(value);
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
