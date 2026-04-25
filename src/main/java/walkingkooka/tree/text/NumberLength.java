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

import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
import walkingkooka.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * A length with only a positive number and no unit.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
public final class NumberLength extends Length<Double> implements Value<Double> {

    static NumberLength parseNumber0(final String text) {
        final int MODE_WHOLE = 0;
        final int MODE_DECIMAL = 1;

        int mode = MODE_WHOLE;

        double value = 0;
        double multiplier = 0.1;

        int i = 0;
        for (final char c : text.toCharArray()) {
            switch (mode) {
                case MODE_WHOLE:
                    switch (c) {
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
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                    }
                    break;
                case MODE_DECIMAL:
                    switch (c) {
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
                            throw new InvalidCharacterException(
                                text,
                                i
                            );
                    }
                    break;
                default:
                    NeverError.unhandledCase(
                        mode,
                        MODE_WHOLE,
                        MODE_DECIMAL
                    );
            }

            i++;
        }

        return with(value);
    }

    static NumberLength with(final Double value) {
        return new NumberLength(
            Objects.requireNonNull(value, "value")
        );
    }

    private NumberLength(final Double value) {
        super();
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    private final Double value;

    @Override
    double doubleValue() {
        return this.value;
    }

    @Override
    public double pixelValue() {
        throw new UnsupportedOperationException();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Double, Length<Double>>> unit() {
        return Optional.empty();
    }

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
        return this.doubleValue() == other.doubleValue();
    }

    @Override
    public String toString() {
        String toString = String.valueOf(this.value);
        if(toString.endsWith(".0")) {
            toString = toString.substring(
                0,
                toString.length() - 2
            );
        }
        return toString;
    }
}
