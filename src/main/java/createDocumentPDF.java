import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class createDocumentPDF {

    public static String documentReceiver;
    public static String mainReference;
    public static String textOpening;
    public static String mainText1;
    public static String mainText2;
    public static String mainText3;
    public static String mainText4;
    public static String externalReference;
    public static String textClosing;
    public static String emailWriter;

    public static void generate(DocumentationGenerate.PDFData data) {
        PDPage pdPage;

        documentReceiver = "**" + data.receiver + " ** ";
        mainReference = "Dotyczy: ** Uwagi po montażu instalacji odwodnienia na obiektach " + data.location + " w ramach kontraktu: „" + data.contract + "” **.";
        textOpening = "Szanowni Państwo,";
        mainText1 = "W nawiązaniu do zakończonych prac montażowych instalacji odwodnienia na obiektach " + data.location + ", w ramach wyżej wymienionego kontraktu, przedstawiamy niniejsze pismo informacyjne mające na celu wskazanie uwag oraz niezbędnych działań naprawczych nie będących w zakresie naszej firmy.";
        mainText2 = "Podczas prowadzenia robót stwierdzono, iż na wskazanych obiektach wymagane jest przeprowadzenia prac poprawkowych w zakresie przedstawionym w dokumentacji stanowiącej załącznik do niniejszego pisma. Wskazane w nim nieprawidłowości powodują, że obecny stan instalacji odwodnienia nie odpowiada parametrom projektowym, co w przyszłości może skutkować nieprawidłowym jej funkcjonowaniem";
        mainText3 = "Podjęcie działań korygujących jest niezbędne, aby zapewnić zgodność wykonanych robót z dokumentacją projektową oraz wymaganiami kontraktowymi. Pragniemy jednocześnie podkreślić, iż bez realizacji wskazanych poprawek firma ** DWD System ** nie będzie w stanie udzielić gwarancji na prawidłowe funkcjonowanie instalacji odwodnienia w okresie gwarancyjnym. W takim przypadku odpowiedzialność za ewentualne usterki lub awarie nie będzie spoczywała po stronie naszej firmy.";
        mainText4 = "Prosimy o niezwłoczne podjęcie stosownych działań naprawczych.";
        externalReference = "Załącznik: Dokumentacja obiektów " + data.location + " z uwagami i wykazem niezbędnych prac poprawkowych";
        textClosing = "Z poważaniem,";
        emailWriter = data.sender;

        float imageMargin = 40f;
        float pageHeight = PDRectangle.A4.getHeight();
        float pageWidth = PDRectangle.A4.getWidth();

        try {
            PDDocument document = new PDDocument();
            pdPage = new PDPage(PDRectangle.A4);
            document.addPage(pdPage);

            PDImageXObject logoImg   = loadImageFromResources(document, "/dwd-logo.png");
            PDImageXObject podpisDWD = loadImageFromResources(document, "/podpisDWD.jpg");
            PDImageXObject sideImg   = loadImageFromResources(document, "/sideDecoration.png");
            PDImageXObject footerImg = loadImageFromResources(document, "/dwdFooter.png");

            PDType0Font boldFont = PDType0Font.load(document, new File("C:/Windows/Fonts/arialbd.ttf"));
            PDType0Font font = PDType0Font.load(document, new File("C:/Windows/Fonts/arial.ttf"));
            int fontSize = 10;

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            // Header page
            PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
            contentStream.drawImage(logoImg, 410, 700, 160, 160);

            contentStream.drawImage(sideImg, 3, 245, 80, 600);

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(445, 715);
            contentStream.showText("Poznań, dn. " + df.format(date) + " r.");
            contentStream.endText();

            drawWrappedText(contentStream, font, boldFont, documentReceiver, fontSize, 100, 730, 450, 15, false);
            drawWrappedText(contentStream, font, boldFont, mainReference, fontSize, 100, 645, 450, 15, false);

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(100, 600);
            contentStream.showText(textOpening);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(20, 20);
            contentStream.showText(emailWriter);
            contentStream.endText();

            float y = 570;
            y = drawWrappedText(contentStream, font, boldFont, mainText1, fontSize, 100, y, 450, 15, true);
            y = drawWrappedText(contentStream, font, boldFont, mainText2, fontSize, 100, y - 30, 450, 15, true);
            y = drawWrappedText(contentStream, font, boldFont, mainText3, fontSize, 100, y - 30, 450, 15, true);
            y = drawWrappedText(contentStream, font, boldFont, mainText4, fontSize, 100, y - 30, 450, 15, true);
            y = drawWrappedText(contentStream, font, boldFont, externalReference, fontSize, 100, y - 30, 450, 15, false);
            y = drawWrappedText(contentStream, font, boldFont, textClosing, fontSize, 400, y - 40, 450, 15, false);
            contentStream.drawImage(podpisDWD, 250, 130, 280, 90);
            contentStream.close();

            // draw footer on first page
            PDPageContentStream firstCs = new PDPageContentStream(document, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            firstCs.drawImage(footerImg, 105, 30, 400, 70);
            firstCs.close();

            // === Images + Descriptions ===
            float topMargin = 20f;
            float footerMargin = 20f;
            float gapImageDescription = 20f;
            float gapAfterDescription = 25f;
            float descriptionReserve = 0f;

            PDPageContentStream imgCs = null;
            float yPosition = 0;

            for (Map.Entry<File, String> entry : data.imagesWithDescriptions.entrySet()) {
                File file = entry.getKey();
                String description = entry.getValue();

                PDImageXObject image = PDImageXObject.createFromFile(file.getAbsolutePath(), document);

                float maxWidth = pageWidth - 2 * imageMargin;
                float maxHeight = (pageHeight - topMargin - descriptionReserve - gapImageDescription - gapAfterDescription)*0.45f - footerMargin ;
                float scale = Math.min(maxWidth / image.getWidth(), maxHeight / image.getHeight());
                float scaledWidth = image.getWidth() * scale;
                float scaledHeight = image.getHeight() * scale;

                float requiredSpace = scaledHeight + gapImageDescription + descriptionReserve + gapAfterDescription;

                if (yPosition == 0 || yPosition - requiredSpace < footerMargin) {
                    if (imgCs != null) imgCs.close();

                    pdPage = new PDPage(PDRectangle.A4);
                    document.addPage(pdPage);
                    imgCs = new PDPageContentStream(document, pdPage);

                    // draw footer
                    imgCs.drawImage(footerImg, 130, 15, 300, 55);

                    yPosition = pageHeight - topMargin;
                }

                // center image horizontally
                float xImage = (pageWidth - scaledWidth) / 2f;

                // draw image
                imgCs.drawImage(image, xImage, yPosition - scaledHeight, scaledWidth, scaledHeight);

                // draw description aligned with image left edge
                yPosition -= (scaledHeight + gapImageDescription);
                yPosition = drawWrappedText(imgCs, font, boldFont, description,
                        fontSize, xImage, yPosition,
                        scaledWidth, 15, false);

                yPosition -= gapAfterDescription;
            }

            if (imgCs != null) imgCs.close();

            // === Page Numbers ===
            int totalPages = document.getNumberOfPages();
            for (int i = 0; i < totalPages; i++) {
                PDPage page = document.getPage(i);
                contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                contentStream.beginText();
                contentStream.setFont(font, 10);
                contentStream.newLineAtOffset(pageWidth - 60, 20);
                contentStream.showText((i + 1) + " z " + totalPages);
                contentStream.endText();
                contentStream.close();
            }

            // === Save with File Chooser ===
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Zapisz PDF jako...");
            fileChooser.setSelectedFile(new File(data.location + ".pdf")); // default filename

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                File chosenFile = fileChooser.getSelectedFile();

                // Ensure file ends with .pdf
                if (!chosenFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
                    chosenFile = new File(chosenFile.getAbsolutePath() + ".pdf");
                }

                document.save(chosenFile);
                document.close();

                System.out.println("Dokument zapisany: " + chosenFile.getAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(chosenFile);
                }
            } else {
                document.close();
                System.out.println("Zapis anulowany przez użytkownika.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float drawWrappedText(PDPageContentStream cs,
                                        PDType0Font font, PDType0Font boldFont,
                                        String text, float fontSize,
                                        float startX, float startY,
                                        float maxWidth, float leading,
                                        boolean withTab) throws IOException {

        float indent = withTab ? 20f : 0f;
        float lineStartX = startX + indent;
        float cursorX = lineStartX;
        float y = startY;
        boolean boldOn = false;

        String[] tokens = text.split("((?<=\\*\\*)|(?=\\*\\*)| )");

        cs.beginText();
        cs.newLineAtOffset(cursorX, y);

        for (String token : tokens) {
            if (token == null || token.isEmpty()) continue;
            if (token.equals("**")) {
                boldOn = !boldOn;
                continue;
            }
            if (token.equals("--")) {
                y -= leading;
                cs.endText();
                cursorX = lineStartX;
                cs.beginText();
                cs.newLineAtOffset(cursorX, y);
                continue;
            }
            PDType0Font useFont = boldOn ? boldFont : font;
            String chunk = token + " ";
            float chunkWidth = useFont.getStringWidth(chunk) / 1000f * fontSize;

            if ((cursorX - lineStartX) + chunkWidth > maxWidth) {
                y -= leading;
                cs.endText();
                cursorX = lineStartX;
                cs.beginText();
                cs.newLineAtOffset(cursorX, y);
            }
            cs.setFont(useFont, fontSize);
            cs.showText(chunk);
            cursorX += chunkWidth;
        }

        cs.endText();
        return y;
    }
    private static PDImageXObject loadImageFromResources(PDDocument doc, String resourcePath) throws IOException {
        try (var is = createDocumentPDF.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            byte[] bytes = is.readAllBytes();
            return PDImageXObject.createFromByteArray(doc, bytes, resourcePath);
        }
    }
}