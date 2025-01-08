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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class LengthUnitTest implements ClassTesting2<LengthUnit<?, ?>>,
    ConstantsTesting<LengthUnit<?, ?>>,
    ToStringTesting<LengthUnit<?, ?>>,
    TypeNameTesting<LengthUnit<?, ?>> {

    @Test
    public void testUnits() {
        assertArrayEquals(new Object[]{LengthUnit.PIXEL}, LengthUnit.units());
    }

    @Test
    public void testSuffix() {
        this.checkEquals("px", LengthUnit.PIXEL.suffix(), "suffix");
    }

    @Test
    public void testUnitPresentPresent() {
        this.unitPresentAndCheck("12px", LengthUnit.PIXEL, true);
    }

    @Test
    public void testUnitPresentAbsent() {
        this.unitPresentAndCheck("12", LengthUnit.PIXEL, false);
    }

    @Test
    public void testUnitPresentAbsent2() {
        this.unitPresentAndCheck("12?", LengthUnit.PIXEL, false);
    }

    private void unitPresentAndCheck(final String text, final LengthUnit<?, ?> unit, final boolean expected) {
        this.checkEquals(expected, unit.unitPresent(text), () -> "unitPresent " + CharSequences.quoteAndEscape(text));
    }

    @Test
    public void testCreate() {
        this.checkEquals(Length.pixel(99.0), LengthUnit.PIXEL.create(99.0));
    }

    @Test
    public void testParse() {
        this.checkEquals(Length.pixel(99.0), LengthUnit.PIXEL.parse("99.0px"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(LengthUnit.PIXEL, "px");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<LengthUnit<?, ?>> type() {
        return Cast.to(LengthUnit.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Set<LengthUnit<?, ?>> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return Length.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
