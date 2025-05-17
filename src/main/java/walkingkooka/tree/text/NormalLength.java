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

import java.util.Optional;

/**
 * A normal measurement.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
public final class NormalLength extends Length<Void> implements Value<Void> {

    final static String TEXT = "normal";

    static NormalLength parseNormal0(final String text) {
        if (!TEXT.equals(text)) {
            throw new IllegalArgumentException("Invalid normal text " + CharSequences.quoteAndEscape(text));
        }
        return INSTANCE;
    }

    final static NormalLength INSTANCE = new NormalLength();

    private NormalLength() {
        super();
    }

    @Override
    public Void value() {
        throw new UnsupportedOperationException();
    }

    @Override
    double doubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    long longValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double pixelValue() {
        throw new UnsupportedOperationException();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Void, Length<Void>>> unit() {
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
        return System.identityHashCode(this);
    }

    @Override
    boolean equals0(final Length<?> other) {
        return true;
    }

    @Override
    public String toString() {
        return TEXT;
    }
}
