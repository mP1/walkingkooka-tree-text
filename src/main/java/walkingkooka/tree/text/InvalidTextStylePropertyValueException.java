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

        this.appendValue = true;
    }

    @Override
    public String getMessage() {
        // Invalid "color" value "InvalidValue123"
        // Invalid "color" expected $expected
        // Invalid "color" expected $expected got "InvalidValue123"

        final StringBuilder b = new StringBuilder()
            .append("Invalid ")
            .append(this.name.inQuotes());

        final String expected = this.expected;
        if (null != expected) {
            b.append(" expected ")
                .append(expected);
        }

        final Object value = this.value;
        final boolean appendValue = this.appendValue;
        final boolean appendValueType = this.appendValueType;

        if (appendValue || (appendValueType && null == value)) {
            b.append(
                null == expected ?
                    " value " :
                    " but got "
            );

            b.append(
                CharSequences.quoteIfChars(value)
            );
        } else {
            if (appendValueType) {
                b.append(" but got ")
                    .append(value.getClass().getSimpleName());
            }
        }

        return b.toString();
    }

    /**
     * Adds an expected part of the message, note it is not necessary to start the message with expected. Note {@link #appendValue}
     * must be invoked to append the value.
     */
    public InvalidTextStylePropertyValueException setExpected(final String expected) {
        this.expected = CharSequences.failIfNullOrEmpty(expected, "expected");
        this.appendValue = false;
        this.appendValueType = false;
        return this;
    }

    private String expected;

    public InvalidTextStylePropertyValueException appendValue() {
        this.appendValue = true;
        this.appendValueType = false;
        return this;
    }

    private boolean appendValue;

    public InvalidTextStylePropertyValueException appendValueType() {
        this.appendValueType = true;
        this.appendValue = false;
        return this;
    }

    private boolean appendValueType;

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
