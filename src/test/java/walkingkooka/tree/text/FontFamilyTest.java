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
import walkingkooka.naming.NameTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class FontFamilyTest implements ClassTesting2<FontFamily>,
    NameTesting<FontFamily, FontFamily>,
    JsonNodeMarshallingTesting<FontFamily> {

    private final static String TEXT = "Times New Roman";

    // TypeNaming.......................................................................................................

    @Override
    public void testTypeNaming() {
    }

    // HasJsonNode......................................................................................................

    @Test
    public void testUnmarshallStringInvalidFails() {
        this.unmarshallFails(JsonNode.string(""));
    }

    @Test
    public void testUnmarshallString() {
        this.unmarshallAndCheck(JsonNode.string(TEXT),
            FontFamily.with(TEXT));
    }

    @Test
    public void testMarshall() {
        this.marshallAndCheck(this.createComparable(), JsonNode.string(TEXT));
    }

    @Test
    public void testMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    @Override
    public FontFamily createName(final String name) {
        return FontFamily.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return TEXT;
    }

    @Override
    public String differentNameText() {
        return "Different";
    }

    @Override
    public String nameTextLess() {
        return "Antiqua";
    }

    @Override
    public Class<FontFamily> type() {
        return FontFamily.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public FontFamily createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    @Override
    public FontFamily unmarshall(final JsonNode jsonNode,
                                 final JsonNodeUnmarshallContext context) {
        return FontFamily.unmarshall(jsonNode, context);
    }
}
