package pu.project.app.views.field;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import pu.project.app.JadeAgentStarter;
import pu.project.app.map.MapEnvironment;
import pu.project.app.map.MapModel;
import pu.project.app.services.FactionService;
import pu.project.app.views.MainLayout;
import pu.project.app.views.factions.FactionView;

@PageTitle("Field")
@PreserveOnRefresh
@Route(value = "field", layout = MainLayout.class)
@AnonymousAllowed
public class FieldView extends VerticalLayout {
    private static FieldView instance;
 private final JadeAgentStarter jadeAgentStarter;
    private final FactionView factionView;
    private final FieldPDF fieldPDF;
    private final FactionService factionService;
    private BufferedImage generatedImage;
    private Image generatedImageView = new Image();
    private Span scenarioSpan = new Span();
    private Button downloadFieldButton = new Button(new Icon(VaadinIcon.BOOK));
    private Button generateFieldButton = new Button(new Icon(VaadinIcon.MAP_MARKER));

    public FieldView(FactionView factionView, FieldPDF fieldPDF, FactionService factionService, JadeAgentStarter jadeAgentStarter) {
        this.factionView = factionView;
        this.fieldPDF = fieldPDF;
        this.factionService = factionService;
        instance = this;
        this.jadeAgentStarter = jadeAgentStarter;
        generateFieldButton.addClickListener(event -> generateField());
        initComponent();
        downloadFieldButton.setEnabled(false);
        updateGenerateFieldButtonState();
        subscribeToFactionViewEvents();
    }
private void generateField() {
    if (factionView.hasDataInGrids()) {
        try {
            int scenario = getScenario();
            jadeAgentStarter.startAgents(scenario);
            //jadeAgentStarter.waitForAgentCompletion(); // Wait for agents to finish
            
            MapModel model = JadeAgentStarter.getSharedMapModel();
            BufferedImage generatedMap = MapEnvironment.generateMap(model);
            
            generatedImageView.setSrc(new StreamResource("generated_map.png", 
                () -> new ByteArrayInputStream(imageToBase64(generatedMap))));
            downloadFieldButton.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
    public static FieldView getInstance() {
        return instance;
    }

    private void initComponent() {
        generateFieldButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateFieldButton.setTooltipText("Generate field");
        generateFieldButton.addClickListener(event -> {
            if (factionView.hasDataInGrids()) {
                              MapModel model = JadeAgentStarter.getSharedMapModel();
                BufferedImage generatedMap = MapEnvironment.generateMap(model);
                if (generatedMap != null) {
                    this.generatedImage = generatedMap;
                    generatedImageView.setSrc(new StreamResource("generated_map.png",
                            () -> new ByteArrayInputStream(imageToBase64(generatedMap))));
                    downloadFieldButton.setEnabled(true);
                    saveImageToSession(Base64.getEncoder().encodeToString(imageToBase64(generatedMap)));
                }
            }
        });

        downloadFieldButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        downloadFieldButton.setTooltipText("Download PDF");
        downloadFieldButton.addClickListener(event -> {
            try {
                byte[] pdfContent = fieldPDF.generatePDFContent(generatedImage, scenarioSpan.getText());
                StreamResource resource = new StreamResource("field.pdf", () -> new ByteArrayInputStream(pdfContent));
                resource.setContentType("application/pdf");
                resource.setCacheTime(0);
                Anchor anchor = new Anchor(resource, "");
                anchor.getElement().setAttribute("download", true);
                anchor.getElement().setAttribute("target", "_blank");
                anchor.getElement().getStyle().set("display", "none");
                add(anchor);
                anchor.getElement().executeJs("this.click();");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        updateScenarioSpan();
        HorizontalLayout buttonLayout = new HorizontalLayout(generateFieldButton, downloadFieldButton);
        buttonLayout.setSizeFull();

        VerticalLayout fieldLayout = new VerticalLayout(buttonLayout, scenarioSpan, generatedImageView);
        fieldLayout.setSizeFull();
        fieldLayout.setAlignItems(Alignment.START);
        add(fieldLayout);
    }

    private void updateGenerateFieldButtonState() {
        boolean hasDataInGrids = factionView.hasDataInGrids();
        generateFieldButton.setEnabled(hasDataInGrids);
    }

    private void subscribeToFactionViewEvents() {
        factionView.addGridDataChangeListener(() -> updateGenerateFieldButtonState());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        updateGenerateFieldButtonState();
        String imageBase64 = loadImageFromSession();
        if (imageBase64 != null) {
            generatedImageView.setSrc(new StreamResource("generated_map.png",
                    () -> new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64))));
            downloadFieldButton.setEnabled(true);
        }
    }

    private byte[] imageToBase64(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private void saveImageToSession(String imageBase64) {
        VaadinSession.getCurrent().setAttribute("generatedImage", imageBase64);
    }

    private String loadImageFromSession() {
        return (String) VaadinSession.getCurrent().getAttribute("generatedImage");
    }

    private void updateScenarioSpan() {
        int scenario = factionService.getScenario();

        String scenarioText;
        switch (scenario) {
            case 1:
                scenarioText = "Scenario 1: Capture the flag";
                break;
            case 2:
                scenarioText = "Scenario 2: King of the hill";
                break;
            case 3:
                scenarioText = "Scenario 3: Search and destroy";
                break;
            default:
                scenarioText = "Scenario: To be determined";
        }

        scenarioSpan.setText(scenarioText);
    }

    public int getScenario() {
        int scenario = factionService.getScenario();
        return scenario;
    }
}
