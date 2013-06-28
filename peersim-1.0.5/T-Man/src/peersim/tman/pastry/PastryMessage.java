package peersim.tman.pastry;

import java.util.*;

import peersim.tman.*;



/**
 * 
 */
public class PastryMessage extends ViewMessage
{

BitSet bitset;

public PastryMessage(int msgsize, int nfingers)
{
	super(msgsize);
	bitset = new BitSet(nfingers);
}

}