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

import walkingkooka.naming.HasName;

import java.util.Objects;

/**
 * Reports an missing {@link TextStylePropertyName}.
 */
public final class MissingTextStylePropertyNameException extends IllegalArgumentException
    implements HasName<TextStylePropertyName<?>> {

    private static final long serialVersionUID = 1L;

    public MissingTextStylePropertyNameException(final TextStylePropertyName<?> name) {
        super();
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String getMessage() {
        return "TextStyle: Missing property " + this.name.inQuotes();
    }

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<?> name() {
        return this.name;
    }

    private final TextStylePropertyName<?> name;
}
