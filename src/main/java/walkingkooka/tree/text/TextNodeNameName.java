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
import walkingkooka.InvalidTextLengthException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

/**
 * Base class of all {@link Name names} in this package.
 */
abstract class TextNodeNameName<N extends TextNodeNameName<N> & Comparable<N>> implements Name,
    Comparable<N> {

    /**
     * Verifies that the given name has valid characters and a valid length.
     */
    static String checkName(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
            name,
            "name",
            INITIAL,
            PART
        );

        final int dashDash = name.indexOf("--");
        if (dashDash != -1) {
            throw new InvalidCharacterException(
                name,
                dashDash + 1
            );
        }

        return InvalidTextLengthException.throwIfFail(
            "name",
            name,
            MIN_LENGTH,
            MAX_LENGTH
        );
    }

    public final static CharPredicate INITIAL = CharPredicates.letter();

    public final static CharPredicate PART = CharPredicates.letterOrDigit().or(CharPredicates.any("-"));

    public final static int MIN_LENGTH = 1;

    public final static int MAX_LENGTH = 255;

    TextNodeNameName(final String name) {
        super();
        this.name = name;
    }

    @Override
    public final String value() {
        return this.name;
    }

    final String name;

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return this.caseSensitivity().hash(this.name);
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            this.canBeEqual(other) &&
                this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final N other) {
        return caseSensitivity().equals(this.name, other.name);
    }

    @Override
    public final String toString() {
        return this.name;
    }

    // Comparable ......................................................................................................

    @Override
    public final int compareTo(final N other) {
        return this.caseSensitivity().comparator().compare(this.name, other.name);
    }

    // JsonNodeContext..................................................................................................

    final JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.name);
    }
}
