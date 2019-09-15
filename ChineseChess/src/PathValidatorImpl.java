
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.PathValidator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author harry
 */
public class PathValidatorImpl implements PathValidator<CellVertex, CCEdge>{

	@Override
	public boolean isValidPath(GraphPath<CellVertex, CCEdge> partialPath, CCEdge edge) {
		if(edge.getSrcVertx()==partialPath.getEndVertex()) // Source vertex is the first one
			return edge.getDestVertx().content == PlayerAffiliation.NONE;
		else// Destination vertex is the first one
			return edge.getSrcVertx().content == PlayerAffiliation.NONE;
	}
	
}
