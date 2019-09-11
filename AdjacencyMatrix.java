import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/*
 * TARIN HORNE
 * 9/8/2018
 */
public class AdjacencyMatrix {
	
	private int currentSize = 0;
	private int maxSize = 10;
	private String[] labels;
	private Long[][] graph;
	private boolean isWeighted = true;
	private boolean isDirected = false;
	private int currentEdges = 0;
	
	private final double SPARSE_PERCENT = 0.3;
	private final double DENSE_PERCENT = 0.85;
	private final boolean DEBUG = false;
	private final boolean MST_DEBUG = false;
	
	//for parsing method only
	private String lastEvaluation = "";
	private boolean passed = false;
	private boolean begin = false;
	private boolean end = false;
	
	AdjacencyMatrix() {
		labels = new String[maxSize];
		graph = new Long[maxSize][maxSize];	
	}
	
	/////////////////////METHODS FOR PRIM's ALGORITHM / MINIMUM SPANNING TREE
	/////////////////////////////////////////////////////////////////////////
	
	/*
	 * 
	 */
	public Long getWeight(String vertex1, String vertex2) {
		if(!isWeighted) {return null;}
		else if(isDirected) {return null;}
		else if(!hasEdge(vertex1, vertex2)) {return null;}
		else {
			int index1 = getIndex(vertex1);
			int index2 = getIndex(vertex2);
			if(MST_DEBUG) {
				System.out.println("WEIGHT OF "+vertex1+" <--> "+vertex2+" : "+graph[index1][index2].toString()+"\n");
			}
			return graph[index1][index2];
		}
	}
	
	/*
	 * 
	 */
	public String[] getEdges(String vertex) {
		int rowIndex = getIndex(vertex);
		if(rowIndex == -1) {return null;}
		String[] edgeVerticies = new String[currentSize -1];
		int edgeIndex = 0;
		for(int i = 0; i < currentSize; i++) {
			if(graph[rowIndex][i] != null && i != rowIndex) {
				if(MST_DEBUG) {System.out.print("EDGE OF "+vertex+" : " + labels[i] + "\n");}
				edgeVerticies[edgeIndex] = labels[i];
				edgeIndex++;
			}
		}
		if(MST_DEBUG) {
			System.out.println("EDGES OF: "+vertex);
			for(String edge : edgeVerticies){
				System.out.print(edge + "\t");
			}System.out.print("\n\n");
		}
		return edgeVerticies;
	}
	
	/*
	 * 
	 */
	public AdjacencyMatrix createMST() {
		if(!isConnected()) {
			if(MST_DEBUG){
				System.out.println("IS_NOT_CONNECTED");
			}
			return null;
		}
		if(currentSize == 0) {
			if(MST_DEBUG){
				System.out.println("IS_EMPTY");
			}
			return null;
		}
		AdjacencyMatrix MST = new AdjacencyMatrix();
		MST.addVertex(this.labels[0]);
		ArrayList<String> toDo = new ArrayList<>();
		String[] edges = null;
		String leastVertex = "";
		String leastEdge = "";
		Long leastWeight = null;
		Long tempWeight = null;
		for(int i = 1; i < currentSize; i++) {
			toDo.add(this.labels[i]);
		}
		while(!toDo.isEmpty()) {
			for(int i = 0; i < MST.currentSize; i++) {
				if(MST_DEBUG) {
					System.out.println("TESTING VERTEX : " + MST.labels[i]);
				}
				edges = getEdges(MST.labels[i]);
				for(String edgeVertex : edges) {
					if(toDo.contains(edgeVertex)) {
						if(MST_DEBUG) {
							System.out.println("TESTING EDGE : " + edgeVertex + " OF VERTEX : " + MST.labels[i]);
						}
						tempWeight = getWeight(MST.labels[i], edgeVertex);
						if(leastWeight == null || tempWeight <= leastWeight) {
							leastVertex = MST.labels[i];
							leastEdge = edgeVertex;
							leastWeight = tempWeight;
							if(MST_DEBUG) {
								System.out.println("NEW_MINIMUM_EDGE_FOUND!!");
								System.out.println(leastVertex + " <--> " + edgeVertex + " @ " + leastWeight.toString() + "\n");
							}
						}
					}
				}
			}
			if(MST_DEBUG) {
				System.out.println("ADDED " + leastVertex + " --> " + leastEdge + " @ " + leastWeight.toString());
				System.out.print("TODO:\t");
				for(int i = 0; i < toDo.size(); i++){
					System.out.print(toDo.get(i) + "\t");
				}System.out.print("\n");
			}
			MST.addVertex(leastEdge);
			MST.addEdge(leastVertex, leastEdge, leastWeight);
			toDo.remove(leastEdge);
			if(MST_DEBUG) {
				MST.printGraph();
			}
			leastVertex = "";
			leastEdge = "";
			leastWeight = null;
		}
		return MST;
	}
	//////////////////////////////////////////////////////////END MST METHODS
	/////////////////////////////////////////////////////////////////////////
	
	/*
	 * method resize()
	 */
	private void resize() {
		maxSize = maxSize*2;
		String[] tempLabels = new String[maxSize];
		Long[][] tempGraph = new Long[maxSize][maxSize];
		
		for(int i = 0; i < currentSize; i++) {
			tempLabels[i] = labels[i];
		}
		
		for(int i = 0; i < currentSize; i++) {
			for(int j = 0; j < currentSize; j++) {
				tempGraph[i][j] = graph[i][j];
			}
		}
		
		labels = tempLabels;
		graph = tempGraph;
	}
	
	
	
	/*
	 * method getIndex(String vertex)
	 */
	public int getIndex(String findLabel) {
		if(DEBUG)System.out.println("GET_INDEX:"+findLabel);
		if(currentSize == 0) {
			if(DEBUG)System.out.println("EMPTY_GRAPH");
			return -1;
		}
		for(int i = 0; i < currentSize;i++) {
			if(labels[i].charAt(0)==findLabel.charAt(0)) {
				if(DEBUG)System.out.println(i+"-->"+labels[i]);
				return i;
			}
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
	public boolean hasEdge(String vertex1, String vertex2) {
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
		if(graph[index1][index2] != null) {
			if(DEBUG)System.out.println("TRUE");
			if(DEBUG)System.out.println("");
			return true;
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
		int index1 = getIndex(vertex1.trim());
		int index2 = getIndex(vertex2.trim());
		if(index1 == -1 || index2 == -1) {
			if(DEBUG)System.out.println("OUT_OF_BOUNDS");
			return false;
		}
		if(index1 == index2) {
			if(DEBUG)System.out.println("DIAGONAL");
			return false;
		}
		if(graph[index1][index2] == null) {
			currentEdges++;
		}
		graph[index1][index2] = weight;
		if(isDirected == false) {
			if(graph[index2][index1] == null) {
				currentEdges++;
			}
			graph[index2][index1] = weight;
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
	public boolean deleteEdge(String vertex1, String vertex2) {
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
		if(graph[index1][index2] != null) {
			currentEdges--;
		}
		graph[index1][index2] = null;
		if(isDirected == false) {
			if(graph[index2][index1] != null) {
				currentEdges--;
			}
			graph[index2][index1] = null;
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
	public boolean addVertex(String addLabel) {
		if(DEBUG)System.out.println("ADDING_VERTEX:"+addLabel);
		if(getIndex(addLabel.trim()) != -1) {
			if(DEBUG)System.out.println("VERTEX_ALREADY_EXISTS");
			return false;
		}
		if(currentSize == maxSize) resize();
		labels[currentSize] = addLabel.trim();
		graph[currentSize][currentSize] = 0L;
		currentSize++;
		if(DEBUG)printGraph();
		return true;
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
	public boolean deleteVertex(String removeLabel) {
		if(DEBUG)System.out.println("DELETE_VERTEX:"+removeLabel);
		int removeIndex = getIndex(removeLabel);
		if(removeIndex == -1) {
			if(DEBUG)System.out.println("OUT_OF_BOUNDS");
			return false;
		}
		//Count edge cases to remove
		for(int i = 0; i < currentSize; i++) {
			if(removeIndex == i) continue;
			if(graph[i][removeIndex] != null) currentEdges--;
			if(graph[removeIndex][i] != null) currentEdges--;
		}
		//Swap and Clear label
		if(DEBUG)System.out.println("SWAPING:"+labels[removeIndex]+" "+labels[currentSize-1]);
		labels[removeIndex] = labels[currentSize-1];
		labels[currentSize-1] = null;
				
		//Swap and Clear rows
		for(int i = 0; i < currentSize; i++) {
			graph[i][removeIndex] = graph[i][currentSize-1];
			graph[i][currentSize-1] = null;
		}
		//Swap and Clear columns
		for(int i = 0; i < currentSize; i++) {
			graph[removeIndex][i] = graph[currentSize-1][i];
			graph[currentSize-1][i] = null;
		}
		currentSize--;
		if(DEBUG)printGraph();
		return true;
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
		if(DEBUG)System.out.println("IS_SPARSE:"+(currentEdges/(currentSize*(currentSize-1))));
		if((currentEdges/(currentSize*(currentSize-1))) <= SPARSE_PERCENT) {
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
		if(DEBUG)System.out.println("IS_DENSE:"+(currentEdges/(currentSize*(currentSize-1))));
		if((currentEdges/(currentSize*(currentSize-1))) >= DENSE_PERCENT) {
			if(DEBUG)System.out.println("TRUE\n");
			return true;
		}
		if(DEBUG)System.out.println("FALSE\n");
		return false;
	}
	
	public int countVertices() {
		return currentSize;
	}
	
	public int countEdges() {
		return currentEdges;
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
		boolean[] tester = isConnected(0,new boolean[currentSize]);
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
		for(int i = 0;i<currentSize;i++) {
			if(graph[index][i] != null && index != i) {
				if(testConnections[i] == true) {
					continue;
				}else {
					testConnections[i] = true;
					testConnections = isConnected(i,testConnections);
				}
			}
		}
		return testConnections;
	}

	
	
	/*
	 * method isFullyConnected()
	 * 
	 * returns edgeCount == (vertexCount*(vertexCount-1))
	 */
	public boolean isFullyConnected() throws ArithmeticException{
		if(DEBUG)System.out.println("IS_FULLY_CONNECTED");
		if(currentEdges == (currentSize*(currentSize - 1))) {
			if(DEBUG)System.out.println("TRUE\n");
			return true;
		}
		if(currentSize == 1) {
			return true;
		}
		if(DEBUG)System.out.println("FALSE\n");
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
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		begin = false;
		end = false;
		return passed;
	}
	public boolean readGraph(String[] lines) throws Exception {
		passed = true;
		for(String line : lines) {
			parser(line);
		}
		begin = false;
		end = false;
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
				lastEvaluation = String.valueOf(currentSize);
				return;
			}
			case("countEdges"):{
				lastEvaluation = String.valueOf(currentEdges);
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
	
	public void printGraph() {
		if(DEBUG) {
			for(String vertex : labels) {
				System.out.print(vertex + "\t");
			}
			System.out.println("");
		}
		if(DEBUG)System.out.println("VERTEX_COUNT:"+currentSize+"\tEDGE_COUNT:"+currentEdges);
		for(int i = 0; i < currentSize; i++) {
			System.out.print(labels[i]);
			for(int j = 0; j < currentSize; j++) {
				if(graph[i][j] != null) {
					System.out.print("\t"+labels[j]+":"+graph[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}
	public void printGraph(String filename) throws IOException {
		FileWriter writer = new FileWriter(filename); 
		writer.write("begin\n");
		for(int i = 0; i < currentSize; i++) {
			writer.write(labels[i].charAt(0)+" ");
		}
		writer.write("\n");
		for(int i = 0; i < currentSize; i++) {
			for(int j = 0; j < currentSize; j++) {
				if(graph[i][j] != null) {
					writer.write(labels[1].charAt(0)+" "+labels[j].charAt(0));
					if(graph[i][j] != 0L)writer.write(" "+graph[i][j]);
					writer.write("\n");
				}
			}
		}
		writer.write("end");
		writer.close();
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
