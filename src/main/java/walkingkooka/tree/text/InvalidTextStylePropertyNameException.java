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

import java.util.Objects;

/**
 * Reports an invalid {@link TextStylePropertyName} such as passing a padding to a {@link Border}.
 */
public final class InvalidTextStylePropertyNameException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static InvalidTextStylePropertyNameException border(final TextStylePropertyName<?> property) {
        return new InvalidTextStylePropertyNameException(
            "Border",
            property
        );
    }

    public static InvalidTextStylePropertyNameException margin(final TextStylePropertyName<?> property) {
        return new InvalidTextStylePropertyNameException(
            Margin.class.getSimpleName(),
            property
        );
    }

    public static InvalidTextStylePropertyNameException padding(final TextStylePropertyName<?> property) {
        return new InvalidTextStylePropertyNameException(
            Padding.class.getSimpleName(),
            property
        );
    }

    public InvalidTextStylePropertyNameException(final String filter,
                                                 final TextStylePropertyName<?> property) {
        super();
        this.filter = Objects.requireNonNull(filter, "filter");
        this.propertyName = Objects.requireNonNull(property, "propertyName");
    }

    @Override
    public String getMessage() {
        return this.filter + ": Invalid property " + propertyName.inQuotes();
    }

    public String filter() {
        return this.filter;
    }

    private final String filter;

    public TextStylePropertyName<?> propertyName() {
        return this.propertyName;
    }

    private final TextStylePropertyName<?> propertyName;
}
