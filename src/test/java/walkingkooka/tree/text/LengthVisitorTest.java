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
import walkingkooka.type.JavaVisibility;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class LengthVisitorTest implements LengthVisitorTesting<LengthVisitor> {

    @Override
    public void testCheckToStringOverridden() {
    }

    @Test
    public void testAcceptSkipRemaining() {
        final StringBuilder b = new StringBuilder();

        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> length) {
                b.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final Length<?> length) {
                b.append("2");
            }

        }.accept(Length.pixel(10.0));

        assertEquals("12", b.toString());
    }

    @Test
    public void testNoneLength() {
        final StringBuilder b = new StringBuilder();
        final Length length = Length.none();

        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> length) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> length) {
                b.append("2");
            }

            @Override
            protected void visit(final NoneLength l) {
                assertSame(length, l);
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    @Test
    public void testNoneLength2() {
        this.createVisitor().accept(Length.none());
    }

    @Test
    public void testNormalLength() {
        final StringBuilder b = new StringBuilder();
        final Length length = Length.normal();

        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> length) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> length) {
                b.append("2");
            }

            @Override
            protected void visit(final NormalLength l) {
                assertSame(length, l);
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    @Test
    public void testNormalLength2() {
        this.createVisitor().accept(Length.normal());
    }

    @Test
    public void testNumberLength() {
        final StringBuilder b = new StringBuilder();
        final Length length = Length.number(1);

        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> length) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> length) {
                b.append("2");
            }

            @Override
            protected void visit(final NumberLength l) {
                assertSame(length, l);
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    @Test
    public void testNumberLength2() {
        this.createVisitor().accept(Length.number(1));
    }

    @Test
    public void testPixelLength() {
        final StringBuilder b = new StringBuilder();
        final Length length = Length.pixel(12.3);

        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> length) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> length) {
                b.append("2");
            }

            @Override
            protected void visit(final PixelLength l) {
                assertSame(length, l);
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    @Test
    public void testPixelLength2() {
        this.createVisitor().accept(Length.pixel(12.3));
    }

    // LengthVisitorTesting.............................................................................................

    @Override
    public LengthVisitor createVisitor() {
        return new LengthVisitor() {
        };
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<LengthVisitor> type() {
        return LengthVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
