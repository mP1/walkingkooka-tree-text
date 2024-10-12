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

import walkingkooka.text.CaseKind;

import java.util.Objects;

/**
 * Implemented by values that have a CSS representation.
 */
public interface HasCssText {

    /**
     * Converts the name of the enum into a cssText compatible.
     */
    static String cssFromEnumName(final Enum<?> value) {
        Objects.requireNonNull(value, "value");

        return value instanceof HasCssText ?
                ((HasCssText) value).cssText() :
                CaseKind.SNAKE.change(
                        value.name().toLowerCase(),
                        CaseKind.KEBAB
                );
    }

    String cssText();
}
