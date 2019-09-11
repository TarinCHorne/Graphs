import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
/*
 * TARIN HORNE
 * 9/8/2018
 */
public class AdjacencyList {
	private LinkedList<LinkedList<Node>> graph = new LinkedList<>();
	private boolean isWeighted = true;
	private boolean isDirected = false;
	private int vertexCount = 0;
	private int edgeCount = 0;
	final double SPARSE_PERCENT = 0.3;
	final double DENSE_PERCENT = 0.85;
	final boolean DEBUG = true;
	
	//for parsing method only
		private String lastEvaluation = "";
		private boolean passed = false;
		private boolean begin = false;
		private boolean end = false;
	
	
	/*
	 * method getIndex(String vertex)
	 */
	private int getIndex(String vertex) {
		if(DEBUG)System.out.println("GET_INDEX:"+vertex);
		if(vertexCount == 0) {
			if(DEBUG)System.out.println("EMPTY_GRAPH");
			return -1;
		}
		int i = 0;
		for(LinkedList<Node> currentVetrtex : graph) {
			if(currentVetrtex.get(0).name.charAt(0) == vertex.charAt(0)) {
				if(DEBUG)System.out.println(i+"-->"+vertex);
				return i;
			}
			i++;
		}
		if(DEBUG)System.out.println("NOT_A_VERTEX");
		return -1;
	}
	
	
	
	/*
	 * method hasEdge(String vertex1,String vertex2)
	 * 
	 * 		- checks if String vertex1 and 2 are in the set of vertices 
	 * 		- checks for the diagonal case
	 * 		- checks for an edge from vertex1-->vertex2
	 */
	public boolean hasEdge(String vertex1,String vertex2) {
		if(DEBUG)System.out.println("HAS_EDGE:"+vertex1+"-->"+vertex2);
		int index1 = getIndex(vertex1);
		int index2 = getIndex(vertex2);
		if(index1 == -1 || index2 == -1) {
			if(DEBUG)System.out.println("OUT_OF_BOUNDS");
			if(DEBUG)System.out.println("");
			return false;
		}
		if(index1 == index2) {
			if(DEBUG)System.out.println("DIAGONAL");
			if(DEBUG)System.out.println("");
			return true;
		}
		for(Node currentNode : graph.get(index1)) {
			if(currentNode.name == vertex2) {
				if(DEBUG)System.out.println("TRUE");
				if(DEBUG)System.out.println("");
				return true;
			}
		}
		if(DEBUG)System.out.println("FALSE");
		if(DEBUG)System.out.println("");
		return false;
	}
	
	
	
	/*
	 * method addEdge(String vertex1,String vertex2)
	 * 
	 * 	adds an edge from vertex1-->vertex2
	 * 		- checks if String vertex1 and 2 are in the set of vertices 
	 * 		- for the diagonal case
	 * 		- checks if the graph is not directed, adds the inverse
	 * 		- updates edgeCount
	 */
	public boolean addEdge(String vertex1, String vertex2, long weight) {
		if(DEBUG)System.out.println("ADDING_EDGE:"+vertex1+"-->"+vertex2);
		int index1 = getIndex(vertex1);
		int index2 = getIndex(vertex2);
		if(index1 == -1 || index2 == -1) {
			if(DEBUG)System.out.println("OUT_OF_BOUNDS");
			return false;
		}
		if(index1 == index2) {
			if(DEBUG)System.out.println("DIAGONAL");
			return false;
		}
		if(!hasEdge(vertex1,vertex2)) {
			edgeCount++;
			graph.get(index1).add(new Node(vertex2,weight));
		} else {
			for(Node currentNode : graph.get(index1)) {
				if(currentNode.name == vertex2) {
					currentNode.weight = weight;
				}
			}
		} 
		if(!isDirected) {
			if(!hasEdge(vertex2,vertex1)) {
				edgeCount++;
				graph.get(index2).add(new Node(vertex1,weight));
			} else {
				for(Node currentNode : graph.get(index2)) {
					if(currentNode.name == vertex1) {
						currentNode.weight = weight;
					}
				}
			} 
		}
		if(DEBUG)printGraph();
		return true;
	}
	public boolean addEdge(String vertex1, String vertex2) {
		return addEdge(vertex1,vertex2,0L);
	}
	
	
	
	/*
	 * method deleteEdge(String vertex1,String vertex2)
	 * 
	 * 	deletes an edge from vertex1-->vertex2
	 * 		- checks if String vertex1 and 2 are in the set of vertices 
	 * 		- for the diagonal case
	 * 		- checks if the graph is not direcred, removes the inverse
	 * 		- updates edgeCount
	 */
	public boolean deleteEdge(String vertex1,String vertex2) {
		if(DEBUG)System.out.println("DELETE_EDGE:"+vertex1+"-->"+vertex2);
		int index1 = getIndex(vertex1);
		int index2 = getIndex(vertex2);
		if(index1 == -1 || index2 == -1) {
			if(DEBUG)System.out.println("OUT_OF_BOUNDS");
			return false;
		}
		if(index1 == index2) {
			if(DEBUG)System.out.println("DIAGONAL");
			return false;
		}
		if(hasEdge(vertex1,vertex2)) {
			edgeCount--;
			int i = 0;
			for(Node currentNode : graph.get(index1)) {
				if(currentNode.name == vertex2) {
					break;
				}
				i++;
			}
			if(i != graph.get(index1).size())graph.get(index1).remove(i);
		} 
		if(!isDirected) {
			if(hasEdge(vertex2,vertex1)) {
				edgeCount--;
				int i = 0;
				for(Node currentNode : graph.get(index2)) {
					if(currentNode.name == vertex1) {
						break;
					}
					i++;
				}
				if(i != graph.get(index2).size())graph.get(index2).remove(i);
			}
		}
		if(DEBUG)printGraph();
		return true;
	}
	
	
	
	/*
	 * method addVertex(String vertex)
	 * 
	 * - checks if String vertex is in the set of vertices
	 * - adds the vertex
	 * - updates vertexCount
	 * 
	 * addVertex(String[] vertices) adds all vertices
	 */
	public boolean addVertex(String vertex) {
		if(DEBUG)System.out.println("ADDING_VERTEX:"+vertex);
		if(getIndex(vertex)==-1) {
			graph.add(new LinkedList<Node>());
			graph.getLast().add(new Node(vertex));
			vertexCount++;
			if(DEBUG)printGraph();
			return true;
		}
		if(DEBUG)System.out.println("VERTEX_ALREADY_EXISTS");
		return false;
	}
	public void addVertex(String[] vertices) {
		for(String vertex : vertices) {
			addVertex(vertex);
		}
	}
	
	
	
	/*
	 * method deleteVertex(String vertex)
	 * 
	 * - checks if String vertex is in the set of vertices
	 * - removes the vertex and any reference to said vertex
	 * - updates edgeCount and vertexCount
	 * 
	 * deleteVertex(String[] vertices) deletes all vertices
	 */
	public boolean deleteVertex(String vertex) {
		if(DEBUG)System.out.println("DELETE_VERTEX:"+vertex);
		int index = getIndex(vertex);
		if(index!=-1) {
			Node nodeToRemove = graph.get(index).get(0);
			//removes vertex
			edgeCount = edgeCount - (graph.remove(index).size() - 1);
			vertexCount--;
			//removes all edges to vertex
			for(int i = 0; i < graph.size(); i++) {
				for(int j = 0; j < graph.get(i).size(); j++) {
					if(graph.get(i).get(j).name == nodeToRemove.name) {
						graph.get(i).remove(j); j--;
						edgeCount--;
					}
				}
			}
			if(DEBUG)printGraph();
			return true;
		}
		if(DEBUG)System.out.println("OUT_OF_BOUNDS");
		return false;
	}
	public void deleteVertex(String[] vertices) {
		for(String vertex : vertices) {
			deleteVertex(vertex);
		}
	}
	
	
	
	/*
	 * method isSparse()
	 * 
	 * returns edgeCount / (vertexCount*(vertexCount-1)) <= SPARSE_PERCENT
	 */
	public boolean isSparse() throws ArithmeticException{
		if(DEBUG)System.out.println("IS_SPARSE:"+(edgeCount/(vertexCount*(vertexCount-1))));
		if((edgeCount / (vertexCount*(vertexCount-1)) <= SPARSE_PERCENT)) {
			if(DEBUG)System.out.println("TRUE");
			if(DEBUG)System.out.println();
			return true;
		}
		if(DEBUG)System.out.println("FALSE");
		if(DEBUG)System.out.println();
		return false;
	}
	
	
	
	/*
	 * method isDense()
	 * 
	 * returns edgeCount / (vertexCount*(vertexCount-1)) >= DENSE_PERCENT
	 */
	public boolean isDense() throws ArithmeticException{
		if(DEBUG)System.out.println("IS_DENSE:"+(edgeCount/(vertexCount*(vertexCount-1))));
		if((edgeCount / (vertexCount*(vertexCount-1)) >= DENSE_PERCENT)) {
			if(DEBUG)System.out.println("TRUE");
			if(DEBUG)System.out.println();
			return true;
		}
		if(DEBUG)System.out.println("FALSE");
		if(DEBUG)System.out.println();
		return false;
	}
	
	public int countVertices() {
		return vertexCount;
	}
	
	public int countEdges() {
		return edgeCount;
	}
	
	
	
	/*
	 * method isConnected()
	 * 
	 * @return boolean, returns true if a single path exists through all vertexes
	 * 		- uses a boolean[vertrexCount] to keep track of visited vertices,
	 * 		is used to avoid infinite looping, and recursion is used to check
	 * 		paths.
	 * 		- starts at the first added vertex to the graph
	 */
	public boolean isConnected() {
		if(DEBUG)System.out.println("IS_CONNECTED");
		if(isFullyConnected()) {
			if(DEBUG)System.out.println("TRUE\n");
			return true;
		}
		boolean[] tester = isConnected(0,new boolean[vertexCount]);
		for(boolean current : tester) {
			if(current == false) {
				if(DEBUG)System.out.println("FALSE\n");
				return false;
			}
		}
		if(DEBUG)System.out.println("TRUE\n");
		return true;
	}
	private boolean[] isConnected(int index, boolean[] testConnections) {
		if(DEBUG)System.out.println();
		if(DEBUG) {
			for(boolean tester : testConnections)System.out.print(tester + "\t");
			System.out.println();
		}
		int i = 0;
		for(Node listedVertex : graph.get(index)) {
			int j = getIndex(listedVertex.name);
			if(i != 0 && testConnections[j] == false) {
				testConnections[j] = true;
				testConnections = isConnected(j,testConnections);
			}
			i++;
		}
		return testConnections;
	}
	
	
	
	/*
	 * method isFullyConnected()
	 * 
	 * returns edgeCount == (vertexCount*(vertexCount-1))
	 */
	public boolean isFullyConnected() throws ArithmeticException{
		if(DEBUG)System.out.println("IS_FULLT_CONNECTED:"+(edgeCount/(vertexCount*(vertexCount-1))));
		if(edgeCount == (vertexCount*(vertexCount-1))) {
			if(DEBUG)System.out.println("TRUE");
			if(DEBUG)System.out.println();
			return true;
		}
		if(vertexCount == 1) {
			if(DEBUG)System.out.println("TRUE");
			if(DEBUG)System.out.println();
			return true;
		}
		if(DEBUG)System.out.println("FALSE");
		if(DEBUG)System.out.println();
		return false;
	}
	
	
	
	/*
	 * method	readGraph(String fileName)
	 * 			readGraph(File lines)
	 * 			readGraph(String[] lines)
	 * 
	 * 	All readGraph methods will feed file lined into the parser
	 * 		
	 * 		@return boolean, sets a boolean passed flag that can be toggled to
	 * 			false by parser method, returns passed.
	 */
	public boolean readGraph(String fileName) {
		File parseFile = new File(fileName);
		return readGraph(parseFile);
	}
	public boolean readGraph(File lines) {
		passed = true;
		try {
			Scanner reader = new Scanner(lines);
			while(reader.hasNextLine()) {
				parser(reader.nextLine());
			}
			reader.close();
		} catch (IOException e) {
			
		} catch (Exception e) {
			
		}
		return passed;
	}
	public boolean readGraph(String[] lines) throws Exception {
		passed = true;
		for(String line : lines) {
			parser(line);
		}
		return passed;
	}
	
	
	
	/*
	 * method parser(String line)
	 * 
	 * 	@Param String line, this parameter is the current line fed in from
	 * 		the @redGraph method
	 * 
	 * 	This method assumes no errors (as in will just throw all errors) and
	 * 		parses based on <Command><Space><List of parameters separated by space>
	 * 		checks <Command> via switch statement
	 * 		if no command given, will check the input against the last evaluation.
	 * 		-	tests via @testEvaluation method
	 * 		-	if one error is detected, passed is set to false
	 * 		ignores commented lines
	 */
	public void parser(String line) throws Exception{
		line = this.trim(line);
		if(line == "") return;
		if(line.charAt(0)=='*') return;
		String[] parsedLine = this.split(line, ' ');
		//Check for keywords
		if(begin == true || end == true) {
			if(begin) {
				begin = false;
				addVertex(parsedLine);
				return;
			}else{
				if(parsedLine[0].equalsIgnoreCase("end")) {
					end = false;
					return;
				}
				if(isWeighted) {
					addEdge(parsedLine[0],parsedLine[1],Long.getLong(parsedLine[2]));
				} else {
					addEdge(parsedLine[0],parsedLine[1]);
				}
				return;
			}
		}
		else {
		switch(parsedLine[0]) {
			case("begin"):{
				begin = true;
				end = true;
				return;
			}
			case("weighted"):{
				isWeighted = true;
				return;
			}
			case("unweighted"):{
				isWeighted = false;
				return;
			}
			case("directed"):{
				isDirected = true;
				return;
			}
			case("undirected"):{
				isDirected = false;
				return;
			}
			case("isWeighted"):{
				isWeighted = Boolean.valueOf(parsedLine[1]);
				return;
			}
			case("isDirected"):{
				isDirected = Boolean.valueOf(parsedLine[1]);
				return;
			}
			case("getIndex"):{
				lastEvaluation = String.valueOf(getIndex(parsedLine[1]));
				return;
			}
			case("hasEdge"):{
				if(DEBUG)System.out.println("hasEdge"+" "+parsedLine[1]+" "+parsedLine[2]);
				lastEvaluation = String.valueOf(hasEdge(parsedLine[1],parsedLine[2]));
				return;
			}
			case("addEdge"):{
				if(DEBUG)System.out.println("addEdge"+" "+parsedLine[1]+" "+parsedLine[2]);
				if(isWeighted) {
					addEdge(parsedLine[1],parsedLine[2],Long.getLong(parsedLine[3]));
				} else {
					addEdge(parsedLine[1],parsedLine[2]);
				}
				return;
			}
			case("deleteEdge"):{
				if(DEBUG)System.out.println("deleteEdge"+" "+parsedLine[1]+" "+parsedLine[2]);
				lastEvaluation = String.valueOf(deleteEdge(parsedLine[1],parsedLine[2]));
				return;
			}
			case("addVertex"):{
				String[] params = new String[parsedLine.length-1];
				for(int i = 0; i < parsedLine.length-1;i++)params[i]=parsedLine[i+1];
				addVertex(params);
				return;
			}
			case("deleteVertex"):{
				String[] params = new String[parsedLine.length-1];
				for(int i = 0; i < parsedLine.length-1;i++)params[i]=parsedLine[i+1];
				deleteVertex(params);
				return;
			}
			case("isSparse"):{
				lastEvaluation = String.valueOf(isSparse());
				return;
			}
			case("isDense"):{
				lastEvaluation = String.valueOf(isDense());
				return;
			}
			case("countVertices"):{
				lastEvaluation = String.valueOf(vertexCount);
				return;
			}
			case("countEdges"):{
				lastEvaluation = String.valueOf(edgeCount);
				return;
			}
			case("isConnected"):{
				lastEvaluation = String.valueOf(isConnected());
				return;
			}
			case("isFullyConnected"):{
				lastEvaluation = String.valueOf(isFullyConnected());
				return;
			}
			default:{
				testEvaluation(parsedLine[0]);
			}
		}
		}
	}
	private void testEvaluation(String evalArgument) {
		passed = lastEvaluation.equalsIgnoreCase(evalArgument);
	}
	
	
	
	private void printGraph() {
		if(DEBUG)System.out.println("VERTEX_COUNT:"+vertexCount+"\tEDGE_COUNT:"+edgeCount);
		for(LinkedList<Node> currentVertex : graph) {
			for(Node currentEdge : currentVertex) {
				System.out.print(currentEdge.name);
				if(currentEdge.weight != 0L) {
					System.out.print(":"+currentEdge.weight);
				}
				System.out.print("\t");
			}
			System.out.println("");
		}
		System.out.println("\n\n");
	}
	public void printGraph(String filename) throws IOException {
		FileWriter writer = new FileWriter(filename); 
		writer.write("begin\n");
		for(LinkedList<Node> currentVertex : graph) {
			writer.write(currentVertex.get(0).name.charAt(0)+" ");
		}
		writer.write("\n");
		for(LinkedList<Node> currentVertex : graph) {
			for(Node currentEdge : currentVertex) {
				writer.write(currentVertex.get(0).name.charAt(0)+" "+currentEdge.name.charAt(0));
				if(currentEdge.weight != 0L) {
					writer.write(" "+currentEdge.weight);
				}
				writer.write("\n");
			}

		}
		writer.write("end");
		writer.close();
	}
	
	private class Node implements Comparable<Object>{
		protected String name;
		protected Long weight;
		
		Node(String name){
			this.name = name;
			weight = 0L;
		}
		Node(String name, long weight){
			this.name = name;
			this.weight = weight;
		}
		
		@Override
		public int compareTo(Object node) {
			if(node instanceof Node) {
				Node testNode = (Node) node;
				return name.compareTo(testNode.name);
			}
			if(node instanceof String) {
				String testString = (String) node;
				return name.compareTo(testString);
			}
			return 0;
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	//PARSING METHODS
	private String[] split(String lineSplit, char split){
		String line = trim(lineSplit, split);
		int scannedSplit = 0;
		for(int i = 0; i < line.length()-1;i++){
			//Testing case where split char is found
			//Testing cases for chunk of split chars are present
			//		only 1 char from a chunk should be counted 
			if(line.charAt(i)==split && line.charAt(i+1)!=split) scannedSplit++;
		}
		
		//Concatenate each portion of the string, 
		String[] returnStringArray = new String[1+scannedSplit];
		
		//Removing NULL strings
		for(int i = 0;i<returnStringArray.length;i++) {
			returnStringArray[i] = "";
		}
		if(line == "") return returnStringArray;
		
		int j = 0;
		for(int i = 0; i < line.length()-1 && j <= scannedSplit;i++){
			if(line.charAt(i)!=split)returnStringArray[j]+=line.charAt(i);
			else if(line.charAt(i)==split && line.charAt(i+1)!=split)j++;	
		}
		//testing the last char of line
		if (line.charAt(line.length()-1) != split) returnStringArray[j]+=line.charAt(line.length()-1);
		return returnStringArray;
	}

	private String trim(String line){
		//Testing trivial case
		if(line == "") return "";
		
		int i;
		int j;
		
		//Finding the lower index
		for(i = 0;i <= line.length() -1;i++){
			if(line.charAt(i) != ' ')break;
			//Testing all spaces case
			if(line.charAt(i)==' '&&i == line.length() -1)return "";
		}
		//Finding the upper index
		for(j = line.length() -1;j > i;j--){
			if(line.charAt(j) != ' ')break;
		}
		
		//Concatenating range of non space chars
		String returnString = "";
		while(i <= j){
			returnString += line.charAt(i);
			i++;
		}
		return returnString;
	}
	
	private String trim(String line, char trim){
		//Testing trivial case
		if(line == "") return "";
		
		int i;
		int j;
		
		//Finding the lower index
		for(i = 0;i <= line.length() -1;i++){
			if(line.charAt(i) != trim)break;
			//Testing all spaces case
			if(line.charAt(i)==trim&&i == line.length() -1)return "";
		}
		//Finding the upper index
		for(j = line.length() -1;j > i;j--){
			if(line.charAt(j) != trim)break;
		}
		
		//Concatenating range of non space chars
		String returnString = "";
		while(i <= j){
			returnString += line.charAt(i);
			i++;
		}
		return returnString;
	}
}
