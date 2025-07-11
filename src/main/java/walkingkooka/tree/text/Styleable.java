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

/**
 * Adds merging to {@link HasTextStyle}.
 */
public interface Styleable extends HasTextStyle {

    /**
     * Merges this with the given {@link TextStyle}.
     */
    Styleable merge(final TextStyle textStyle);

    /**
     * Sets or replaces a single {@link TextStylePropertyName}.
     * Note a null propertyValue will throw a {@link IllegalArgumentException}.
     */
    <T> Styleable set(final TextStylePropertyName<T> propertyName,
                      final T propertyValue);

    /**
     * Sets or replaces or remove a single {@link TextStylePropertyName}.
     * When the value is null that property will be removed if it exists.
     */
    <T> Styleable setOrRemove(final TextStylePropertyName<T> propertyName,
                              final T propertyValue);

    /**
     * Removes the existing {@link TextStylePropertyName}.
     */
    Styleable remove(final TextStylePropertyName<?> propertyName);
}
