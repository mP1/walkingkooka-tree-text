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

public final class InvalidTextStylePropertyNameExceptionTest implements ThrowableTesting2<InvalidTextStylePropertyNameException> {

    @Test
    public void testGetMessageBorder() {
        this.checkMessage(
            InvalidTextStylePropertyNameException.border(TextStylePropertyName.COLOR),
            "Border: Invalid property \"color\""
        );
    }

    @Test
    public void testGetMessageMargin() {
        this.checkMessage(
            InvalidTextStylePropertyNameException.margin(TextStylePropertyName.COLOR),
            "Margin: Invalid property \"color\""
        );
    }

    @Test
    public void testGetMessagePadding() {
        this.checkMessage(
            InvalidTextStylePropertyNameException.padding(TextStylePropertyName.COLOR),
            "Padding: Invalid property \"color\""
        );
    }

    @Test
    public void testGetMessageTextStyle() {
        this.checkMessage(
            InvalidTextStylePropertyNameException.textStyle(TextStylePropertyName.COLOR),
            "Invalid property \"color\""
        );
    }

    // class............................................................................................................

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InvalidTextStylePropertyNameException> type() {
        return InvalidTextStylePropertyNameException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
