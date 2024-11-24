public class DesiredDistancePathFinder extends AbstractRoutingAlgorithm {

    private final IntObjectHashMap<IsoLabel> fromMap;
    private final PriorityQueue<IsoLabel> queue;
    private int visitedNodes;
    private double targetDistance;
    private final boolean reverseFlow;
    private final double DISTANCE_TOLERANCE = 200; // 200m tolerance

    public static class IsoLabel {
        public boolean deleted = false;
        public int node;
        public int edge;
        public double distance;
        public IsoLabel parent;
        public ArrayList<Integer> path;

        IsoLabel(int node, int edge, double distance, IsoLabel parent, ArrayList<Integer> path) {
            this.node = node;
            this.edge = edge;
            this.distance = distance;
            this.parent = parent;
            this.path = path;
        }
    }

    public DesiredDistancePathFinder(Graph g, Weighting weighting, boolean reverseFlow, TraversalMode traversalMode) {
        super(g, weighting, traversalMode);
        queue = new PriorityQueue<>(1000, comparingDouble(l -> Math.abs(l.distance - targetDistance)));
        fromMap = new GHIntObjectHashMap<>(1000);
        this.reverseFlow = reverseFlow;
    }

    public void setTargetDistance(double distance) {
        this.targetDistance = distance;
    }

    @Override
    public Path calcPath(int from, int to) {
        throw new IllegalStateException("call findPath instead");
    }

    public ArrayList<Integer> findPath(int from, int to) {
        checkAlreadyRun();
        ArrayList<Integer> initialPath = new ArrayList<>();
        initialPath.add(from);
        IsoLabel currentLabel = new IsoLabel(from, -1, 0, null, initialPath);
        queue.add(currentLabel);
        fromMap.put(from, currentLabel);
        Set<Integer> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            currentLabel = queue.poll();
            if (currentLabel.deleted) continue;

            if (Math.abs(currentLabel.distance - targetDistance) <= DISTANCE_TOLERANCE) {
                return currentLabel.path;
            }

            if (visited.contains(currentLabel.node)) continue;
            visited.add(currentLabel.node);

            visitedNodes++;

            EdgeIterator iter = edgeExplorer.setBaseNode(currentLabel.node);
            while (iter.next()) {
                if (!accept(iter, currentLabel.edge)) continue;

                double nextDistance = iter.getDistance() + currentLabel.distance;
                int adjNode = iter.getAdjNode();

                if (visited.contains(adjNode)) continue;

                ArrayList<Integer> newPath = new ArrayList<>(currentLabel.path);
                newPath.add(adjNode);

                IsoLabel nextLabel = new IsoLabel(adjNode, iter.getEdge(), nextDistance, currentLabel, newPath);
                queue.add(nextLabel);
                fromMap.put(adjNode, nextLabel);
            }
        }

        return null; // No path found within the desired distance
    }

    @Override
    public String getName() {
        return "desiredDistancePath";
    }

    @Override
    public int getVisitedNodes() {
        return visitedNodes;
    }
}
