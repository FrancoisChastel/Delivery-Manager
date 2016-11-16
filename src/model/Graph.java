package model;

import java.util.*;

public class Graph<TEdge extends Comparable<TEdge>, TVertex> {
    private Map<TVertex, CoreVertex> nodes;

    public Graph() {
        nodes = new HashMap<TVertex, CoreVertex>();
    }

    public void add(TVertex origin, TEdge vertex, TVertex destination) {
        CoreVertex TOrigin = null;
        CoreVertex TDestination = null;

        if (nodes.containsKey(origin))  TOrigin = nodes.get(origin);
        else                            TOrigin = new CoreVertex();
        if (nodes.containsKey(destination))     TDestination = nodes.get(destination);
        else                                    TDestination = new CoreVertex();

        TOrigin.add(TDestination, vertex);
    }

    private class CoreVertex {
        private Map<CoreVertex, TEdge> ingoing;
        private Map<CoreVertex, TEdge> outgoing;

        public CoreVertex() {
            this.ingoing = new HashMap<CoreVertex, TEdge>();
            this.outgoing = new HashMap<CoreVertex, TEdge>();
        }

        public void add(CoreVertex destination, TEdge vertex) {
            this.ingoing.put(destination, vertex);
            this.addInGoing(this, vertex);
        }

        private void addInGoing(CoreVertex coming, TEdge vertex) {
            this.outgoing.put(coming, vertex);
        }
    }
}

