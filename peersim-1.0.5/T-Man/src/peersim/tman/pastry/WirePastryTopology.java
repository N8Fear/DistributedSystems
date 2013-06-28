/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package peersim.tman.pastry;

import peersim.config.*;
import peersim.core.*;

/**
 * Extract a Pastry structure from a ring.
 * 
 * @author Alberto Montresor
 * @version $Revision: 1.2 $
 */
public class WirePastryTopology implements Control
{

/**
 * The identifier of the protocol where the Pastry topology should be stored.
 */
private static final String PAR_PROT = "protocol";

/** Identifier of the protocol to be wired */
private final int pid;

/**
 * 
 */
public WirePastryTopology(String prefix)
{
	pid = Configuration.getPid(prefix + "." + PAR_PROT);
}

public boolean execute()
{
	int size = Network.size();
	for (int i = 0; i < size; i++) {
		Node node = Network.get(i);
		Pastry pastry = (Pastry) node.getProtocol(pid);
		for (int j = 0; j < size; j++) {
			pastry.addFinger(Network.get(j));
		}
	}
	return false;
}

}
