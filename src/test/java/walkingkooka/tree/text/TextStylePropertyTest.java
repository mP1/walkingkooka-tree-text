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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.naming.HasNameTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyTest implements ClassTesting<TextStyleProperty<TextAlign>>,
        HashCodeEqualsDefinedTesting2<TextStyleProperty<TextAlign>>,
        HasNameTesting<TextStylePropertyName<TextAlign>>,
        ToStringTesting<TextStyleProperty<TextAlign>>,
        TreePrintableTesting {
    // with.............................................................................................................

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> TextStyleProperty.with(
                        null,
                        Optional.of(
                                TextAlign.CENTER
                        )
                )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> TextStyleProperty.with(
                        TextStylePropertyName.TEXT_ALIGN,
                        null
                )
        );
    }

    @Test
    public void testWith() {
        final TextStylePropertyName<TextAlign> name = TextStylePropertyName.TEXT_ALIGN;
        final Optional<TextAlign> value = Optional.of(
                TextAlign.CENTER
        );

        final TextStyleProperty<TextAlign> property = TextStyleProperty.with(
                name,
                value
        );

        this.nameAndCheck(
                property,
                name
        );

        this.checkEquals(
                value,
                property.value(),
                "value"
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
                TextStyleProperty.with(
                        TextStylePropertyName.COLOR,
                        Optional.of(
                                Color.BLACK
                        )
                ),
                TextStyleProperty.with(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        Optional.of(
                                Color.BLACK
                        )
                )
        );
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
                TextStyleProperty.with(
                        TextStylePropertyName.COLOR,
                        Optional.of(
                                Color.BLACK
                        )
                ),
                TextStyleProperty.with(
                        TextStylePropertyName.COLOR,
                        Optional.of(
                                Color.WHITE
                        )
                )
        );
    }

    @Override
    public TextStyleProperty<TextAlign> createObject() {
        return TextStyleProperty.with(
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(
                        TextAlign.CENTER
                )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                TextStyleProperty.with(
                        TextStylePropertyName.TEXT_ALIGN,
                        Optional.of(
                                TextAlign.CENTER
                        )
                ),
                "text-align: CENTER"
        );
    }

    @Test
    public void testToStringEmptyValue() {
        this.toStringAndCheck(
                TextStyleProperty.with(
                        TextStylePropertyName.TEXT_ALIGN,
                        Optional.empty()
                ),
                "text-align"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                TextStyleProperty.with(
                        TextStylePropertyName.TEXT_ALIGN,
                        Optional.of(
                                TextAlign.CENTER
                        )
                ),
                "text-align\n" +
                        "  CENTER\n"
        );
    }

    @Test
    public void testTreePrintEmptyValue() {
        this.treePrintAndCheck(
                TextStyleProperty.with(
                        TextStylePropertyName.TEXT_ALIGN,
                        Optional.empty()
                ),
                "text-align\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleProperty<TextAlign>> type() {
        return Cast.to(TextStyleProperty.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
