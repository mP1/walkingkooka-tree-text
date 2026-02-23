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

package walkingkooka.tree.text.convert;

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public abstract class TreeTextConverterTextToTestCase<C extends TreeTextConverterTextTo<CC>, CC extends ConverterContext> implements ConverterTesting2<C, CC>,
    ClassTesting2<C> {

    TreeTextConverterTextToTestCase() {
        super();
    }

    // class............................................................................................................


    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return TreeTextConverter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
