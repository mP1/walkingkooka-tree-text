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

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasCssTestingTest implements HasCssTesting {

    @Test
    public void testHasCssAndCheck() {
        final String css = "{ property: value}";
        this.hasCssAndCheck(
                new HasCss() {
                    @Override
                    public String css() {
                        return css;
                    }
                },
                css
        );
    }

    // cssFromEnumName..................................................................................................

    @Test
    public void testCssFromEnumNameNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> HasCss.cssFromEnumName(null)
        );
    }

    @Test
    public void testCssFromEnumNameWithHasEnum() {
        this.checkEquals(
                TestHasCssEnum.FIRST.css(),
                HasCss.cssFromEnumName(TestHasCssEnum.FIRST)
        );
    }

    enum TestHasCssEnum implements HasCss {
        FIRST;

        @Override
        public String css() {
            return "ABC123";
        }

    }

    @Test
    public void testCssFromEnum() {
        this.checkEquals(
                "half-down",
                HasCss.cssFromEnumName(RoundingMode.HALF_DOWN)
        );
    }
}
