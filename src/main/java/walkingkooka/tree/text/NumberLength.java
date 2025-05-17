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

import walkingkooka.Value;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A positive whole number
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
public final class NumberLength extends Length<Long> implements Value<Long> {

    static NumberLength parseNumber0(final String text) {
        try {
            return with(Long.parseLong(text));
        } catch (final NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid text " + CharSequences.quoteAndEscape(text), cause);
        }
    }

    static NumberLength with(final Long value) {
        Objects.requireNonNull(value, "value");

        if (value < 0) {
            throw new IllegalArgumentException("Invalid length " + value + " < 0");
        }

        return new NumberLength(value);
    }

    private NumberLength(final Long value) {
        super();
        this.value = value;
    }

    @Override
    public Long value() {
        return this.value;
    }

    private final Long value;

    @Override
    double doubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    long longValue() {
        return this.value;
    }

    @Override
    public double pixelValue() {
        throw new UnsupportedOperationException();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Long, Length<Long>>> unit() {
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
        return Long.hashCode(this.value);
    }

    @Override
    boolean equals0(final Length<?> other) {
        return this.longValue() == other.longValue();
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
