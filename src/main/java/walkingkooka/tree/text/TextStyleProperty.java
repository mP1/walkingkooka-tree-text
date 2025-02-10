/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.compare.CompareResult;
import walkingkooka.naming.HasName;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Optional;

/**
 * A pair that captures a {@link TextStylePropertyName} and an optional value for that property.
 */
public final class TextStyleProperty<T> implements HasName<TextStylePropertyName<T>>,
    Value<Optional<T>>,
    TreePrintable,
    Comparable<TextStyleProperty<T>> {

    public static <T> TextStyleProperty<T> with(final TextStylePropertyName<T> name,
                                                final Optional<T> value) {
        return new TextStyleProperty<T>(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(value, "value")
        );
    }

    private TextStyleProperty(final TextStylePropertyName<T> name,
                              final Optional<T> value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public TextStylePropertyName<T> name() {
        return this.name;
    }

    private final TextStylePropertyName<T> name;

    @Override
    public Optional<T> value() {
        return this.value;
    }

    private final Optional<T> value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.name,
            this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TextStyleProperty &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStyleProperty<?> other) {
        return this.name.equals(other.name) &&
            this.value.equals(other.value);
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append(this.name);

        final Object value = this.value.orElse(null);
        if (null != value) {
            b.append(": ");
            b.append(value);
        }

        return b.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.name.value());

        {
            final Optional<T> value = this.value;
            if (value.isPresent()) {
                printer.indent();
                {
                    TreePrintable.printTreeOrToString(
                        value.get(),
                        printer
                    );

                    printer.lineStart();
                }
                printer.outdent();
            }
        }
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final TextStyleProperty<T> other) {
        int compare = this.name.compareTo(other.name);

        if (CompareResult.EQ.test(compare)) {
            // safest to compare values as Strings...
            final String leftValue = this.value.map(Object::toString)
                .orElse("");
            final String rightValue = other.value.map(Object::toString)
                .orElse("");

            compare = leftValue.compareTo(rightValue);
        }

        return compare;
    }
}
