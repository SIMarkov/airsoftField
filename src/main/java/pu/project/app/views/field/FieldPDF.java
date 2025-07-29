package pu.project.app.views.field;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.*;
import org.springframework.stereotype.Component;

@Component
public class FieldPDF {

    // Method to generate PDF content from an image and scenario text
    public byte[] generatePDFContent(BufferedImage image, String scenarioText) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                addImageToPDF(contentStream, image, document);
                addLegendImage(contentStream, document);
                addHeader(contentStream);
                addScenario(contentStream, scenarioText);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to add image to PDF
    private void addImageToPDF(PDPageContentStream contentStream, BufferedImage image, PDDocument document)
            throws IOException {
        try {
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
            float scale = 0.65f;
            contentStream.drawImage(pdImage, 51, 155, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to add legend image to PDF
    private void addLegendImage(PDPageContentStream contentStream, PDDocument document) throws IOException {
        try {
            BufferedImage legendImage = ImageIO.read(new File("icons/legend.png"));
            PDImageXObject pdLegendImage = LosslessFactory.createFromImage(document, legendImage);
            float scale = 0.3f;
            contentStream.drawImage(pdLegendImage, 51, 30, pdLegendImage.getWidth() * scale,
                    pdLegendImage.getHeight() * scale);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to add header to PDF
    private void addHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(250, 725);
        contentStream.showText("Airsoft field");
        contentStream.endText();
    }

    // Method to add scenario text to PDF
    private void addScenario(PDPageContentStream contentStream, String scenarioText) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(51, 660);
        contentStream.showText(scenarioText);
        contentStream.endText();
    }
}
