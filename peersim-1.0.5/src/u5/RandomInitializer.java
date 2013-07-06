package u5;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;
import u5.LeaderElectionMsg.TYPE;

public class RandomInitializer implements Control {
	
    private static final String PAR_PROT = "protocol";
    private final int protocolID;
    
    public RandomInitializer(String prefix) {
        protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
    	// select a random node
    	Node root = Network.get( CommonState.r.nextInt(Network.size()) );
		Transport transport = ((Transport) root.getProtocol(FastConfig.getTransport(protocolID)));
		
		// and send the init message to it
		transport.send(
				root, 
				root,
				new LeaderElectionMsg(TYPE.INIT, null, null),
				protocolID);
   
        return false;
    }
    
}
