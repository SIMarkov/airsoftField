package pu.project.app;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pu.project.app.map.MapModel;
import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.stereotype.Component;
import jade.core.Runtime;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import java.io.File;
import java.util.concurrent.CountDownLatch;

@Component
public class JadeAgentStarter {
    private static MapModel sharedMapModel;
    private ContainerController container;
    private OWLOntology ontologyInstance;
    private CountDownLatch agentDoneLatch;

public synchronized void startAgents(int scenario) throws Exception {
    if (container == null || sharedMapModel == null) {
        initializeContainer(); // Ensure model exists
    }

    int agentCount = 4; 
    if (scenario == 1) agentCount++;
    if (scenario == 2) agentCount++;
    if (scenario == 3) agentCount++;

    
    agentDoneLatch = new CountDownLatch(agentCount);
    sharedMapModel.resetGrid();

    startBaseRespawnAgent();
    startMilitaryVehicleAgent(scenario);
    
    // Run scenario-specific agents BEFORE MissionAgent
    switch (scenario) {
        case 1: startCommCenterAgent(); break;
        case 2: startHelicopterAgent(); break;
        case 3: startFlagAgent(); break;
    }
    
    startMissionAgent(scenario); // Now runs LAST
}
    

private void initializeContainer() throws Exception {
    Runtime runtime = Runtime.instance();
    Profile profile = new ProfileImpl(null, 1099, null);
    container = runtime.createMainContainer(profile);

    String ontologyFilePath = "ontology/airsoftField.owx";
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    File ontologyFile = new File(ontologyFilePath);
    ontologyInstance = manager.loadOntologyFromOntologyDocument(ontologyFile);

    sharedMapModel = new MapModel(ontologyFilePath, ontologyInstance);
}

    private void startBaseRespawnAgent() throws StaleProxyException {
        Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch};
        AgentController agent = container.createNewAgent(
            "BaseRespawnAgent_" + System.currentTimeMillis(),
            "pu.project.app.agents.BaseRespawnAgent",
            agentArgs
        );
        agent.start();
    }

/*public void waitForAgentCompletion() throws InterruptedException {
    if (agentDoneLatch != null) {
        agentDoneLatch.await(10, TimeUnit.SECONDS); // Increased timeout
    }
}*/

public static MapModel getSharedMapModel() {
    return sharedMapModel;
}
private void startMilitaryVehicleAgent(int scenario) throws StaleProxyException {
    Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch, scenario};
    
    AgentController vehicleAgent = container.createNewAgent(
        "MilitaryVehicleAgent_" + System.currentTimeMillis(),
        "pu.project.app.agents.MilitaryVehicleAgent",
        agentArgs
    );
    vehicleAgent.start();
}

private void startCommCenterAgent() throws StaleProxyException {
    Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch};
    
    AgentController commCenterAgent = container.createNewAgent(
        "CommCenterAgent_" + System.currentTimeMillis(),
        "pu.project.app.agents.CommCenterAgent",
        agentArgs
    );
    commCenterAgent.start();
}

private void startFlagAgent() throws StaleProxyException {
    Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch};
    
    AgentController flagAgent = container.createNewAgent(
        "FlagAgent_" + System.currentTimeMillis(),
        "pu.project.app.agents.FlagAgent",
        agentArgs
    );
    flagAgent.start();
}

private void startHelicopterAgent() throws StaleProxyException {
    Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch};
    
    AgentController helicopterAgent = container.createNewAgent(
        "HelicopterAgent_" + System.currentTimeMillis(),
        "pu.project.app.agents.HelicopterAgent",
        agentArgs
    );
    helicopterAgent.start();
}
    private void startMissionAgent(int scenario) throws StaleProxyException {
        Object[] agentArgs = new Object[]{ontologyInstance, sharedMapModel, agentDoneLatch, scenario};
        AgentController missionAgent = container.createNewAgent(
            "MissionAgent_" + System.currentTimeMillis(),
            "pu.project.app.agents.MissionAgent",
            agentArgs
        );
        missionAgent.start();
    }
}
