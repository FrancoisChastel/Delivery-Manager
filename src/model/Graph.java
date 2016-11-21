package model;

import java.util.*;

public class Graph<TVertex,TEdge extends Comparable<TEdge>> {
    private Map<TVertex, CoreVertex> nodes;

    public Graph() {
        nodes = new HashMap<TVertex, CoreVertex>();
    }

    public void add(TVertex origin) {
        
    	CoreVertex TOrigin = null;
        TOrigin = new CoreVertex();
        nodes.put(origin, TOrigin);
    }
    public void addDestination(TVertex origin,TEdge edge,TVertex destination)
    {
    	CoreVertex TOrigin = null;
    	if (nodes.containsKey(origin))
    	{
    		TOrigin = nodes.get(origin);
    		TOrigin.add(destination, edge);
    	}
        else
        {
        	System.out.println("Origin Undefined");;
        }
    }
    
    public HashMap<TVertex, TEdge> getDestinations(TVertex origin)
    {
    	CoreVertex TOrigin = null;
    	if(nodes.containsKey(origin))
    	{
    		TOrigin = nodes.get(origin);
    		return TOrigin.getOutgoing();
    	}
    	return null;
    }

    private class CoreVertex {
        private HashMap<TVertex, TEdge> outgoing;

        public CoreVertex() {
            this.outgoing = new HashMap<TVertex, TEdge>();
        }

        public void add(TVertex destination, TEdge edge) {
            this.outgoing.put(destination, edge);
        }

		public HashMap<TVertex, TEdge> getOutgoing() {
			return outgoing;
		}

		public void setOutgoing(HashMap<TVertex, TEdge> outgoing) {
			this.outgoing = outgoing;
		}
        
    }
}

