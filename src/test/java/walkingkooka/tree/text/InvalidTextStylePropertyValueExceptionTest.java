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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class InvalidTextStylePropertyValueExceptionTest implements ThrowableTesting2<InvalidTextStylePropertyValueException> {

    @Test
    public void testNewWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> new InvalidTextStylePropertyValueException(
                null,
                "Hello"
            )
        );
    }

    @Test
    public void testGetMessageNullValue() {
        this.checkMessage(
            new InvalidTextStylePropertyValueException(
                TextStylePropertyName.COLOR,
                null
            ),
            "Invalid \"color\" value null"
        );
    }

    @Test
    public void testGetMessageColor() {
        this.checkMessage(
            new InvalidTextStylePropertyValueException(
                TextStylePropertyName.COLOR,
                "Invalid123"
            ),
            "Invalid \"color\" value \"Invalid123\""
        );
    }

    @Test
    public void testGetMessageMargin() {
        this.checkMessage(
            new InvalidTextStylePropertyValueException(
                TextStylePropertyName.MARGIN,
                "Invalid123"
            ),
            "Invalid \"margin\" value \"Invalid123\""
        );
    }

    @Test
    public void testGetMessageWithExpected() {
        this.checkMessage(
            new InvalidTextStylePropertyValueException(
                TextStylePropertyName.MARGIN,
                "Invalid123"
            ).setExpected("top or left"),
            "Invalid \"margin\" expected top or left but got \"Invalid123\""
        );
    }

    // class............................................................................................................

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InvalidTextStylePropertyValueException> type() {
        return InvalidTextStylePropertyValueException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
