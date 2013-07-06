package u5;

import java.util.LinkedList;
import java.util.Queue;

import peersim.Simulator;
import peersim.cdsim.CDProtocol;


import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
import peersim.vector.SingleValueHolder;
import u5.LeaderElectionMsg.TYPE;

public class LeaderElectionED extends SingleValueHolder implements EDProtocol  {

	private Node parent = null;
	private int neighbors;
	private int responses = 0;
	
	private Max max;

	
	public LeaderElectionED(String prefix) {
		super(prefix);
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		LeaderElectionMsg msg = (LeaderElectionMsg) event;
		Transport transport = ((Transport)node.getProtocol(FastConfig.getTransport(pid)));
		Linkable linkable = (Linkable) node.getProtocol( FastConfig.getLinkable(pid) ); 
				
		switch (msg.type) {
		case INIT:
			System.out.println("Init on " + node.getID());
			// we are root and thus our own parent
			parent = node;
			
			// assign the current value as max -> TODO move to init control or something?
			max = new Max(node, value);
			
			// contact every neighbor 
			neighbors = linkable.degree();
			for (int i = 0; i < neighbors; i++) {
				transport.send(
						node, 
						linkable.getNeighbor(i), 
						new LeaderElectionMsg(TYPE.BUILD, node, null),
						pid);
			}
			break;
		
		case BUILD:
			if (parent == null) {
				System.out.println("Node " + node.getID() + " accepts " + msg.sender.getID() + " as parent");
				// set the sender as the parent
				parent = msg.sender;
				
				// assign the current value as max -> TODO move to init control or something?
				max = new Max(node, value);
				
				// contact every neighbor
				neighbors = linkable.degree();
				
				for (int i = 0; i < neighbors; i++) {
					transport.send(
							node, 
							linkable.getNeighbor(i), 
							new LeaderElectionMsg(TYPE.BUILD, node, null),
							pid);
				}
				
			} else {
				System.out.println("Node " + node.getID() + " rejects build from " + msg.sender.getID());
				// respond with rejection, since we already got a parent
				transport.send(
						node, 
						msg.sender, 
						new LeaderElectionMsg(TYPE.REJECT, node, null),
						pid);
			}
			break;
			
			
		case REJECT:
			System.out.println("Node " + node.getID() + " rejected " + msg.sender.getID() + " as parent");
			++responses;
			break;
			
		case ELECT:
			System.out.println("Node " + msg.max.node.getID() + " sent max " + msg.max.capacity + " to " + node.getID());
			++responses;
			
			// TODO: use the own value instead of max?
			if (msg.max.capacity > max.capacity) {
				max = msg.max;
				
				System.out.println("new max:" + max.capacity);
			}
		}
		

		
		// either every node rejected and we are a leaf
		// or we got responses from every child and can pass on the max
		if (responses == neighbors) {
			// when we reached the root
			if (parent == node) {
				// stop the simulation
				System.out.println("max capacity = " + msg.max.capacity + " found on node " + msg.max.node.getID());		
				return;
			} else {
				System.out.println("we are a leaf " + node.getID());
				// send the maximum up to the parent
				transport.send(
						node, 
						parent, 
						new LeaderElectionMsg(TYPE.ELECT, node, max),
						pid);
			}
		}
		
	}

}

class Max {
	final Node node;
	final Double capacity;
	
	public Max(Node node, Double capacity) {
		this.node = node;
		this.capacity = capacity;
	}
}

class LeaderElectionMsg {
	enum TYPE {
		INIT,
		BUILD,
		REJECT, 
		ELECT
	}

	final TYPE type;
	final Max max;
	final Node sender;
			
	public LeaderElectionMsg(TYPE type, Node sender, Max max) {
		this.max = max;
		this.type = type;
		this.sender = sender;
	}
}
