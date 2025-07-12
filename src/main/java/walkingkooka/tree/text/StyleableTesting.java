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

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface StyleableTesting<T extends Styleable> extends HasTextStyleTesting {

    @Test
    default void testMergeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .merge(null)
        );
    }

    default void mergeAndCheck(final T styleable,
                               final TextStyle textStyle,
                               final Styleable expected) {
        this.checkEquals(
            expected,
            styleable.merge(textStyle),
            styleable::toString
        );
    }

    // set..............................................................................................................

    @Test
    default void testSetWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .set(
                    null,
                    Color.BLACK
                )
        );
    }

    @Test
    default void testSetWithNullPropertyValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .set(
                    TextStylePropertyName.COLOR,
                    null
                )
        );
    }

    default <S extends Styleable, PV> S setAndCheck(final S styleable,
                                                    final TextStylePropertyName<PV> propertyName,
                                                    final PV propertyValue,
                                                    final S expected) {
        final Styleable set = styleable.set(
            propertyName,
            propertyValue
        );

        this.checkEquals(
            expected,
            set,
            styleable + " set " + propertyName + " " + propertyValue
        );

        return (S) set;
    }

    // remove...........................................................................................................

    @Test
    default void testRemoveWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .remove(
                    null
                )
        );
    }

    default <S extends Styleable> S removeAndCheck(final S styleable,
                                                   final TextStylePropertyName<?> propertyName,
                                                   final S expected) {
        final Styleable set = styleable.remove(
            propertyName
        );

        this.checkEquals(
            expected,
            set,
            styleable + " remove " + propertyName
        );

        return (S) set;
    }

    T createStyleable();
}
