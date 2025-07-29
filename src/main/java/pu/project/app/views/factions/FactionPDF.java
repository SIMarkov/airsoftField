package pu.project.app.views.factions;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;
import pu.project.app.models.Player;

@Component
public class FactionPDF {

    private static final int PLAYERS_PER_PAGE_FIRST = 29;
    private static final int PLAYERS_PER_PAGE_OTHER = 34;
    private static final float Y_OFFSET_ALPHA_FIRST_PAGE = 650;
    private static final float Y_OFFSET_BRAVO_FIRST_PAGE = 650;
    private static final float Y_OFFSET_ALPHA_OTHER_PAGES = 750;
    private static final float Y_OFFSET_BRAVO_OTHER_PAGES = 750;

    // Method to generate PDF content based on player lists for Alpha and Bravo
    // teams
    public byte[] generatePDFContent(List<Player> alphaPlayers, List<Player> bravoPlayers) throws IOException {
        try (PDDocument document = new PDDocument()) {
            int alphaPlayerCount = alphaPlayers.size();
            int bravoPlayerCount = bravoPlayers.size();
            int totalPagesAlpha = (int) Math.ceil((double) alphaPlayerCount / PLAYERS_PER_PAGE_OTHER);
            int totalPagesBravo = (int) Math.ceil((double) bravoPlayerCount / PLAYERS_PER_PAGE_OTHER);
            int totalPages = Math.max(totalPagesAlpha, totalPagesBravo);

            for (int i = 0; i < totalPages; i++) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    if (i == 0) {
                        addHeader(contentStream);
                    }

                    int playersPerPage = (i == 0) ? PLAYERS_PER_PAGE_FIRST : PLAYERS_PER_PAGE_OTHER;
                    float yOffsetAlpha = (i == 0) ? Y_OFFSET_ALPHA_FIRST_PAGE : Y_OFFSET_ALPHA_OTHER_PAGES;
                    float yOffsetBravo = (i == 0) ? Y_OFFSET_BRAVO_FIRST_PAGE : Y_OFFSET_BRAVO_OTHER_PAGES;

                    int startIndexAlpha = i * playersPerPage;
                    int endIndexAlpha = Math.min(startIndexAlpha + playersPerPage, alphaPlayerCount);
                    int startIndexBravo = i * playersPerPage;
                    int endIndexBravo = Math.min(startIndexBravo + playersPerPage, bravoPlayerCount);

                    generateTable(contentStream, alphaPlayers.subList(startIndexAlpha, endIndexAlpha), "Alpha", 40,
                            yOffsetAlpha, Color.WHITE, new Color(158, 154, 117));
                    generateTable(contentStream, bravoPlayers.subList(startIndexBravo, endIndexBravo), "Bravo", 335,
                            yOffsetBravo, Color.WHITE, new Color(65, 83, 59));
                }
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    // Method to add header to the PDF
    private void addHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(240, 725);
        contentStream.showText("Airsoft factions");
        contentStream.endText();
    }

    // Method to generate table with player information
    private float generateTable(PDPageContentStream contentStream, List<Player> players, String title, float x, float y,
            Color titleColor, Color backgroundColor) throws IOException {
        float nameColumnWidth = 105;
        float roleColumnWidth = 85;
        float levelColumnWidth = 75;

        contentStream.setNonStrokingColor(backgroundColor);
        contentStream.addRect(x, y - 5, 235, 20);
        contentStream.fill();

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        float titleX = x + (200) / 2;
        contentStream.beginText();
        contentStream.setNonStrokingColor(titleColor);
        contentStream.newLineAtOffset(titleX, y);
        contentStream.showText(title);
        contentStream.endText();

        y -= 30;

        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.newLineAtOffset(x, y + 5);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.showText("Name");
        contentStream.newLineAtOffset(105, 0);
        contentStream.showText("Role");
        contentStream.newLineAtOffset(85, 0);
        contentStream.showText("Level");
        contentStream.newLineAtOffset(75, 0);
        contentStream.endText();

        float rowHeight = 20;
        float tableWidth = 235;
        float tableHeight = 20 + rowHeight * players.size();
        float startX = x - 10;
        float endX = x + 10 + tableWidth;
        float currentY = y - 2;
        for (int i = 0; i <= players.size(); i++) {
            contentStream.moveTo(startX, currentY);
            contentStream.lineTo(endX, currentY);
            contentStream.stroke();
            currentY -= rowHeight;
        }

        contentStream.moveTo(startX, y + 18);
        contentStream.lineTo(startX, y + 18 - tableHeight);
        contentStream.stroke();
        contentStream.moveTo(endX, y + 19);
        contentStream.lineTo(endX, y + 18 - tableHeight);
        contentStream.stroke();

        contentStream.moveTo(startX, y + 18);
        contentStream.lineTo(endX, y + 18);
        contentStream.stroke();

        currentY = y - 15;
        for (Player player : players) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(x, currentY);
            contentStream.showText(player.getName());
            contentStream.newLineAtOffset(nameColumnWidth, 0);
            contentStream.showText(player.getRole());
            contentStream.newLineAtOffset(roleColumnWidth, 0);
            contentStream.showText(player.getLevel());
            contentStream.newLineAtOffset(levelColumnWidth, 0);
            contentStream.endText();
            currentY -= rowHeight;
        }
        return currentY;
    }
}
