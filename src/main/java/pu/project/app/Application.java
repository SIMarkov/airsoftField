package pu.project.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

import pu.project.app.map.MapEnvironment;

@SpringBootApplication
@Theme(value = "airsoft-field")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        MapEnvironment.loadResources();
        SpringApplication.run(Application.class, args);
    }
}