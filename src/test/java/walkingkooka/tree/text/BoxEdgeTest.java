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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class BoxEdgeTest implements ClassTesting2<BoxEdge> {

    @Test
    public void testFlipBottom() {
        this.flipAndCheck(BoxEdge.BOTTOM, BoxEdge.TOP);
    }

    @Test
    public void testFlipLeft() {
        this.flipAndCheck(BoxEdge.LEFT, BoxEdge.RIGHT);
    }

    @Test
    public void testFlipRight() {
        this.flipAndCheck(BoxEdge.RIGHT, BoxEdge.LEFT);
    }

    @Test
    public void testFlipTop() {
        this.flipAndCheck(BoxEdge.TOP, BoxEdge.BOTTOM);
    }

    private void flipAndCheck(final BoxEdge edge, final BoxEdge expected) {
        assertSame(expected, edge.flip(), () -> edge + " flip");
    }

    @Override
    public Class<BoxEdge> type() {
        return BoxEdge.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
