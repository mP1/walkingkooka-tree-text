package test;

import com.google.gwt.junit.client.GWTTestCase;

import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.text.FakeTextNodeVisitor;
import walkingkooka.tree.text.HyperLink;
import walkingkooka.tree.text.Text;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyleName;
import walkingkooka.tree.text.TextStyleNameNode;
import walkingkooka.tree.text.TextStyleNode;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.visit.Visiting;
@LocaleAware
public class TestGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "test.Test";
    }

    public void testAssertEquals() {
        assertEquals(
                1,
                1
        );
    }

    public void testBuildAndPrintHtml() {
        final TextNode node = TextNode.styleName(TextStyleName.with("HTML"))
                .appendChild(
                        TextNode.styleName(
                                TextStyleName.with("head")
                        ).appendChild(
                                TextNode.styleName(
                                        TextStyleName.with("TITLE")
                                ).appendChild(
                                        TextNode.text("title123")
                                )
                        )
                ).appendChild(TextNode.styleName(TextStyleName.with("BODY"))
                        .appendChild(
                                TextNode.hyperLink(
                                        Url.parseAbsolute("https://example.com/hello")
                                ).appendChild(TextNode.text("hyper link text 123"))
                        ).appendChild(TextNode.text("before"))
                        .appendChild(TextNode.text("something gray")
                                .setAttributes(
                                        Maps.of(
                                                TextStylePropertyName.COLOR,
                                                Color.parse("#678")
                                        )
                                )
                        ).appendChild(TextNode.text("after"))
                );

        final StringBuilder html = new StringBuilder();
        final LineEnding eol = LineEnding.SYSTEM;
        final IndentingPrinter printer = Printers.stringBuilder(html, eol)
                .indenting(Indentation.SPACES2);

        // very simple html printer
        new FakeTextNodeVisitor() {
            @Override
            protected Visiting startVisit(final TextNode node) {
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextNode node) {
            }

            @Override
            protected Visiting startVisit(final TextStyleNode node) {
                printer.lineStart();
                printer.print("<SPAN style=\"");

                node.attributes()
                        .forEach((p, v) -> {
                            printer.print(p + ": " + v + ";");
                        });

                printer.print("\">" + eol);
                printer.indent();
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyleNode node) {
                printer.outdent();
                printer.lineStart();
                printer.print("</SPAN>" + eol);
            }

            @Override
            protected Visiting startVisit(final HyperLink node) {
                this.beginElement("A href=\"" + node.url() + "\"");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HyperLink node) {
                this.endElement("A");
            }

            @Override
            protected Visiting startVisit(final TextStyleNameNode node) {
                this.beginElement(node.styleName().value());
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyleNameNode node) {
                this.endElement(node.styleName().value());
            }

            @Override
            protected void visit(final Text node) {
                printer.print(node.value());
            }

            private void beginElement(final String element) {
                printer.lineStart();
                printer.print("<" + element + ">" + eol);
                printer.indent();
            }

            private void endElement(final String element) {
                printer.outdent();
                printer.lineStart();
                printer.print("</" + element + ">" + eol);
            }
        }.accept(node);

        assertEquals(
                "<HTML>\n" +
                        "  <head>\n" +
                        "    <TITLE>\n" +
                        "      title123\n" +
                        "    </TITLE>\n" +
                        "  </head>\n" +
                        "  <BODY>\n" +
                        "    <A href=\"https://example.com/hello\">\n" +
                        "      hyper link text 123\n" +
                        "    </A>\n" +
                        "    before\n" +
                        "    <SPAN style=\"color: #667788;\">\n" +
                        "      something gray\n" +
                        "    </SPAN>\n" +
                        "    after\n" +
                        "  </BODY>\n" +
                        "</HTML>",
                html.toString()
        );
    }
}
