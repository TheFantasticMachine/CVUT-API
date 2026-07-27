package com.testgen.restapi.core.managers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;

public class PdfManager {
    private static final String PDF_FILEPATH = "/output/pdf/hello.pdf";

    public void test() throws IOException {
        File htmlFile = new File("/templates/index.html");
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        try (OutputStream os = new FilterOutputStream(PDF_FILEPATH))  {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext context = renderer.getSharedContext();
            context.setPrint(true);
            context.setInteractive(false);
            String baseURL = FileSystems.getDefault().getPath("/output/pdf/").toUri().toURL().toString();
            renderer.setDocumentFromString(doc.html(), baseURL);
            renderer.layout();
            renderer.createPDF(os);
            System.out.println("done");
        }
    }
}
