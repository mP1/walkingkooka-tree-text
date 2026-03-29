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
import walkingkooka.naming.HasName;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Reports an invalid value for a {@link TextStylePropertyName}.
 */
public final class InvalidTextStylePropertyValueException extends IllegalArgumentException
    implements HasName<TextStylePropertyName<?>>,
    Value<Object> {

    private static final long serialVersionUID = 1L;

    public InvalidTextStylePropertyValueException(final TextStylePropertyName<?> name,
                                                  final Object value) {
        super();
        this.name = Objects.requireNonNull(name, "name");
        this.value = value;
    }

    @Override
    public String getMessage() {
        final String expected = this.expected;

        // Invalid "color" value "InvalidValue123"
        // Invalid "color" expected $expected got "InvalidValue123"

        final CharSequence quotedValue = CharSequences.quoteIfChars(this.value);

        return "Invalid " + this.name.inQuotes() +
            (
                null != expected ?
                    " expected " + expected + " got " + quotedValue :
                    " value " + quotedValue
            );
    }

    /**
     * Adds an expected part of the message, note it is not necessary to start the message with expected.
     */
    public InvalidTextStylePropertyValueException setExpected(final String expected) {
        this.expected = CharSequences.failIfNullOrEmpty(expected, "expected");

        return this;
    }

    private String expected;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<?> name() {
        return this.name;
    }

    private final TextStylePropertyName<?> name;

    // Value............................................................................................................

    @Override
    public Object value() {
        return this.value;
    }

    private final Object value;
}
