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

import static org.junit.jupiter.api.Assertions.assertSame;
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

    // setOrRemove......................................................................................................

    @Test
    default void testSetOrRemoveWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .setOrRemove(
                    null,
                    Color.BLACK
                )
        );
    }

    default <S extends Styleable, PV> S setOrRemoveAndCheck(final S styleable,
                                                            final TextStylePropertyName<PV> propertyName,
                                                            final PV propertyValue,
                                                            final S expected) {
        final Styleable after = styleable.setOrRemove(
            propertyName,
            propertyValue
        );

        this.checkEquals(
            expected,
            after,
            styleable + " setOrRemove " + propertyName + " " + propertyValue
        );

        return (S) after;
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

    // removeIf.........................................................................................................

    @Test
    default void removeIfWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .removeIf(
                    null,
                    1
                )
        );
    }

    @Test
    default void removeIfWithNullPropertyValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .removeIf(
                    TextStylePropertyName.COLOR,
                    null
                )
        );
    }

    default <S extends Styleable, V> S removeIfAndCheck(final S styleable,
                                                        final TextStylePropertyName<V> propertyName,
                                                        final V propertyValue) {
        return this.removeIfAndCheck(
            styleable,
            propertyName,
            propertyValue,
            styleable
        );
    }

    default <S extends Styleable, V> S removeIfAndCheck(final S styleable,
                                                        final TextStylePropertyName<V> propertyName,
                                                        final V propertyValue,
                                                        final S expected) {
        final Styleable removed = styleable.removeIf(
            propertyName,
            propertyValue
        );

        if (styleable.equals(expected)) {
            assertSame(
                expected,
                removed,
                () -> styleable + " removeIf " + propertyName
            );
        } else {
            this.checkEquals(
                expected,
                removed,
                () -> styleable + " removeIf " + propertyName
            );
        }

        return (S) removed;
    }

    T createStyleable();
}
