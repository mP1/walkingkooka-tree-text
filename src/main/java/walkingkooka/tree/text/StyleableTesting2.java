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

public interface StyleableTesting2<T extends Styleable> extends StyleableTesting {

    @Test
    default void testMergeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .merge(null)
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

    // replaceIf.........................................................................................................

    @Test
    default void testReplaceIfWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .replaceIf(
                    null,
                    Color.BLACK,
                    Color.WHITE
                )
        );
    }

    @Test
    default void testReplaceIfWithNullOldPropertyValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .replaceIf(
                    TextStylePropertyName.COLOR,
                    null,
                    Color.WHITE
                )
        );
    }

    @Test
    default void testReplaceIfWithNullNewPropertyValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStyleable()
                .replaceIf(
                    TextStylePropertyName.COLOR,
                    Color.BLACK,
                    null
                )
        );
    }

    T createStyleable();
}
