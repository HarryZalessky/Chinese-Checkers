/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Point;
import java.util.Iterator;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author harry
 */
public class GraphFacilities {

	public static final int W = 25, H = 17;
	public static CellVertex[][] vertexMat = new CellVertex[H][W];
	public static Graph<CellVertex, CCEdge> g = new SimpleGraph<CellVertex, CCEdge>(CCEdge.class);

	public static void CreateGraph(Point[][] edgeArr) {
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				if (vertexMat[i][j] != null) {
					g.addVertex(vertexMat[i][j]);
				}
			}
		}
		for (int i = 0; i < edgeArr.length; i++) {
			if (edgeArr[i].length == 2) {
				addEdge(edgeArr[i][0].y, edgeArr[i][0].x, edgeArr[i][1].y, edgeArr[i][1].x);
			} else {
				if (vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent() == PlayerAffiliation.CPU_PLAYER
						|| vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent() == PlayerAffiliation.HUMAN_PLAYER) {
					addEdge(edgeArr[i][0].y, edgeArr[i][0].x, edgeArr[i][2].y, edgeArr[i][2].x);
				}
			}
		}
	}

	public static void updateGraph(Point[][] edgeArr) {
		for (int i = 0; i < edgeArr.length; i++) {
			if (edgeArr[i].length == 2) {
				addEdge(edgeArr[i][0].y, edgeArr[i][0].x, edgeArr[i][1].y, edgeArr[i][1].x);
			} else {
				if (vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent()
						== PlayerAffiliation.CPU_PLAYER
						|| vertexMat[edgeArr[i][1].y][edgeArr[i][1].x].getContent()
						== PlayerAffiliation.HUMAN_PLAYER) {
					addEdge(edgeArr[i][0].y, edgeArr[i][0].x,
							edgeArr[i][2].y, edgeArr[i][2].x);
				} else {
					g.removeEdge(vertexMat[edgeArr[i][0].y][edgeArr[i][0].x],
							vertexMat[edgeArr[i][2].y][edgeArr[i][2].x]);
				}
			}
		}
	}

	public static GraphPath findShortestPathLength(CellVertex ver, CellVertex dest) //calculating dijkstra
	{
		GraphPath min;
		min = new KShortestSimplePaths<>(g, new PathValidatorImpl()).getPaths(ver, dest, 1).get(0);
		for (Iterator<CellVertex> it = min.getVertexList().listIterator(); it.hasNext();) {
			CellVertex next = it.next();
			System.out.println("(" + next.getLocation().x + ", " + next.getLocation().y + ")");
		}
		return min;

	}

	public static void addEdge(int SrcRow, int SrcCol, int DestRow, int DestCol) {
		CellVertex[][] mat = GraphFacilities.vertexMat;
		GraphFacilities.g.addEdge(mat[SrcRow][SrcCol], mat[DestRow][DestCol], new CCEdge(mat[SrcRow][SrcCol], mat[DestRow][DestCol]));
	}
}
