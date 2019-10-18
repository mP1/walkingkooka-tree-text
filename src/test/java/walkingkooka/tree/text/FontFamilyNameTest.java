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

public final class FontFamilyNameTest implements ClassTesting2<FontFamilyName>,
        NameTesting<FontFamilyName, FontFamilyName>,
        JsonNodeMarshallingTesting<FontFamilyName> {

    private final static String TEXT = "Times New Roman";

    // HasJsonNode......................................................................................................

    @Test
    public void testJsonNodeUnmarshallStringInvalidFails() {
        this.unmarshallFails(JsonNode.string(""), IllegalArgumentException.class);
    }

    @Test
    public void testJsonNodeUnmarshallString() {
        this.unmarshallAndCheck(JsonNode.string(TEXT),
                FontFamilyName.with(TEXT));
    }

    @Test
    public void testJsonNodeMarshall() {
        this.marshallAndCheck(this.createComparable(), JsonNode.string(TEXT));
    }

    @Test
    public void testJsonNodeMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    @Override
    public FontFamilyName createName(final String name) {
        return FontFamilyName.with(name);
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
    public Class<FontFamilyName> type() {
        return FontFamilyName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public FontFamilyName createJsonNodeMappingValue() {
        return this.createObject();
    }

    @Override
    public FontFamilyName unmarshall(final JsonNode jsonNode,
                                     final JsonNodeUnmarshallContext context) {
        return FontFamilyName.unmarshall(jsonNode, context);
    }
}
