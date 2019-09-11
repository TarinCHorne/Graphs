public class MinimumSpanningTreeTester {
	
	public static void main(String[] args) {
		//Testing Fully Connected Graph
		System.out.println("Testing Fully Connected Graph");
		AdjacencyMatrix graph1 = new AdjacencyMatrix();
		String[] vertices1 = {"A","B","C","D","E","F","G"};
		graph1.addVertex(vertices1);
		for(int i = 0; i < vertices1.length; i++) {
			for(int j = 0; j < vertices1.length; j++) {
				Long weight = (long)((int)(Math.random()*101));
				graph1.addEdge(vertices1[i],vertices1[j],weight);
			}
		}		
		System.out.println("Graph:");
		graph1.printGraph();
		AdjacencyMatrix MST1 = graph1.createMST();
		System.out.println("MST:");
		MST1.printGraph();
		
		//Testing a 1 Vertex Graph
		System.out.println("Testing a 1 Vertex Graph");
		AdjacencyMatrix graph2 = new AdjacencyMatrix();
		String[] vertices2 = {"A"};
		graph2.addVertex(vertices2);
		System.out.println("Graph:");
		graph2.printGraph();
		AdjacencyMatrix MST2 = graph2.createMST();
		System.out.println("MST:");
		MST2.printGraph();
		
		//Testing a Graph That is a MST
		System.out.println("Testing a Graph That is a MST");
		System.out.println("Graph:");
		MST1.printGraph();
		AdjacencyMatrix MST3 = MST1.createMST();
		System.out.println("MST:");
		MST3.printGraph();
	}
}
