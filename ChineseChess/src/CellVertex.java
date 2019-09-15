
import java.awt.Point;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author harry
 */
public class CellVertex {

	public int content;
	private Point l;

	public CellVertex(Point l) {
		this(l, 0);
	}

	public CellVertex(Point l, int content) {
		this.l = l;
		this.content = content;
	}

	public Point getLocation() {
		return this.l;
	}

	public int getContent() {
		return this.content;
	}

	@Override
	public boolean equals(Object v) {
		return ((CellVertex) v).hashCode() == this.hashCode();
	}

	@Override
	public int hashCode() {
		return (this.l.y*GraphFacilities.W)+this.l.x;
	}
}
