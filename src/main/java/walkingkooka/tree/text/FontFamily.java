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
import walkingkooka.InvalidTextLengthException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

/**
 * A font family name, which are also case insensitive when compared.
 */
public final class FontFamily implements Name,
    Comparable<FontFamily> {

    /**
     * A {@link FontFamily} may include any printable ascii characters and must be between {@link #MIN_LENGTH} and
     * {@link #MAX_LENGTH}.
     */
    public static FontFamily with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
            name,
            "name",
            INITIAL,
            PART
        );

        InvalidTextLengthException.throwIfFail(
            "name",
            name,
            MIN_LENGTH,
            MAX_LENGTH
        );

        return new FontFamily(name);
    }

    public final static CharPredicate INITIAL = CharPredicates.letter()
        .and(
            CharPredicates.ascii()
        );

    public final static CharPredicate PART = CharPredicates.asciiPrintable();

    public final static int MIN_LENGTH = 1;

    public final static int MAX_LENGTH = 255;

    private FontFamily(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link FontFamily} from a {@link JsonNode}.
     */
    static FontFamily unmarshall(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        return with(node.stringOrFail());
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.name);
    }

    static {
        JsonNodeContext.register("font-family",
            FontFamily::unmarshall,
            FontFamily::marshall,
            FontFamily.class);
    }

    // Object..................................................................................................

    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof FontFamily &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final FontFamily other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FontFamily other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;
}
