import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class createCustomPDF {

    public static void generate(PdfPrintProgramme.PDFData data) {
        try {
            PDDocument document = new PDDocument();
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDImageXObject pdImage = PDImageXObject.createFromFile("dwd-logo.png", document);
            document.addPage(pdPage);

            PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            contentStream.drawImage(pdImage, 10, 720, 130, 130);

            contentStream.setLineWidth(1.5f);
            contentStream.setStrokingColor(0, 0, 0);
            contentStream.moveTo(70, 740);
            contentStream.lineTo(530, 740);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.newLineAtOffset(170,785);
            contentStream.showText("DWD System Sp z.o.o - Systemy odwodnien mostowych");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),6);
            contentStream.beginText();
            contentStream.newLineAtOffset(500,820);
            contentStream.showText("data wydruku " + df.format(date));
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Nr zamowienia");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 660);
            contentStream.showText("Klient");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 580);
            contentStream.showText("Obiekt");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),24);

            contentStream.beginText();
            contentStream.newLineAtOffset(250, 700);
            contentStream.showText(data.smon);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(250, 660);
            contentStream.showText(data.client);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(250, 580);
            contentStream.showText(data.location);
            contentStream.endText();

            contentStream.close();
            document.save( data.smon + ".pdf");
            document.close();

            System.out.println("Dokument utworzony");

            String filePath = data.smon + ".pdf";
            File file = new File(filePath);

            if (Desktop.isDesktopSupported()) {

                    Desktop.getDesktop().open(file);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}