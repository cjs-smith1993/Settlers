package shared.locations;

import java.util.*;

public class Geometer {

	public static EdgeLocation newEdge(int x, int y, EdgeDirection dir) {
		return new EdgeLocation(new HexLocation(x, y), dir).getNormalizedLocation();
	}

	public static VertexLocation newVertex(int x, int y, VertexDirection dir) {
		return new VertexLocation(new HexLocation(x, y), dir).getNormalizedLocation();
	}

	public static Collection<EdgeLocation> getAdjacentEdges(HexLocation hex) {
		Collection<EdgeLocation> adjacentEdges = new ArrayList<EdgeLocation>();

		int x = hex.getX();
		int y = hex.getY();

		adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthWest));
		adjacentEdges.add(newEdge(x, y, EdgeDirection.North));
		adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthEast));
		adjacentEdges.add(newEdge(x, y, EdgeDirection.SouthEast));
		adjacentEdges.add(newEdge(x, y, EdgeDirection.South));
		adjacentEdges.add(newEdge(x, y, EdgeDirection.SouthWest));

		return adjacentEdges;
	}

	@SuppressWarnings("incomplete-switch")
	public static Collection<EdgeLocation> getAdjacentEdges(EdgeLocation edge) {
		Collection<EdgeLocation> adjacentEdges = new ArrayList<EdgeLocation>();
		HexLocation hex = edge.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (edge.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentEdges.add(newEdge(x - 1, y + 1, EdgeDirection.NorthEast));
			adjacentEdges.add(newEdge(x - 1, y + 1, EdgeDirection.North));
			adjacentEdges.add(newEdge(x - 1, y, EdgeDirection.NorthEast));
			adjacentEdges.add(newEdge(x, y, EdgeDirection.North));
			break;
		case North:
			adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthWest));
			adjacentEdges.add(newEdge(x - 1, y, EdgeDirection.NorthEast));
			adjacentEdges.add(newEdge(x + 1, y - 1, EdgeDirection.NorthWest));
			adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthEast));
			break;
		case NorthEast:
			adjacentEdges.add(newEdge(x, y, EdgeDirection.North));
			adjacentEdges.add(newEdge(x + 1, y - 1, EdgeDirection.NorthWest));
			adjacentEdges.add(newEdge(x + 1, y, EdgeDirection.North));
			adjacentEdges.add(newEdge(x + 1, y, EdgeDirection.NorthWest));
			break;
		}

		return adjacentEdges;
	}

	@SuppressWarnings("incomplete-switch")
	public static Collection<EdgeLocation> getAdjacentEdges(VertexLocation vertex) {
		Collection<EdgeLocation> adjacentEdges = new ArrayList<EdgeLocation>();
		HexLocation hex = vertex.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (vertex.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthWest));
			adjacentEdges.add(newEdge(x - 1, y, EdgeDirection.NorthEast));
			adjacentEdges.add(newEdge(x, y, EdgeDirection.North));
			break;
		case NorthEast:
			adjacentEdges.add(newEdge(x, y, EdgeDirection.North));
			adjacentEdges.add(newEdge(x + 1, y - 1, EdgeDirection.NorthWest));
			adjacentEdges.add(newEdge(x, y, EdgeDirection.NorthEast));
			break;
		}

		return adjacentEdges;
	}

	public static Collection<VertexLocation> getAdjacentVertices(HexLocation hex) {
		Collection<VertexLocation> adjacentVertices = new ArrayList<VertexLocation>();

		int x = hex.getX();
		int y = hex.getY();

		adjacentVertices.add(newVertex(x, y, VertexDirection.NorthWest));
		adjacentVertices.add(newVertex(x, y, VertexDirection.NorthEast));
		adjacentVertices.add(newVertex(x, y, VertexDirection.East));
		adjacentVertices.add(newVertex(x, y, VertexDirection.SouthEast));
		adjacentVertices.add(newVertex(x, y, VertexDirection.SouthWest));
		adjacentVertices.add(newVertex(x, y, VertexDirection.West));

		return adjacentVertices;
	}

	@SuppressWarnings("incomplete-switch")
	public static Collection<VertexLocation> getAdjacentVertices(EdgeLocation edge) {
		Collection<VertexLocation> adjacentVertices = new ArrayList<VertexLocation>();
		HexLocation hex = edge.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (edge.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentVertices.add(newVertex(x - 1, y + 1, VertexDirection.NorthEast));
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthWest));
			break;
		case North:
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthWest));
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthEast));
			break;
		case NorthEast:
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthEast));
			adjacentVertices.add(newVertex(x + 1, y, VertexDirection.NorthWest));
			break;
		}

		return adjacentVertices;
	}

	@SuppressWarnings("incomplete-switch")
	public static Collection<VertexLocation> getAdjacentVertices(VertexLocation vertex) {
		Collection<VertexLocation> adjacentVertices = new ArrayList<VertexLocation>();
		HexLocation hex = vertex.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (vertex.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentVertices.add(newVertex(x - 1, y + 1, VertexDirection.NorthEast));
			adjacentVertices.add(newVertex(x - 1, y, VertexDirection.NorthEast));
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthEast));
			break;
		case NorthEast:
			adjacentVertices.add(newVertex(x, y, VertexDirection.NorthWest));
			adjacentVertices.add(newVertex(x + 1, y - 1, VertexDirection.NorthWest));
			adjacentVertices.add(newVertex(x + 1, y, VertexDirection.NorthWest));
			break;
		}

		return adjacentVertices;
	}

	public static VertexLocation getSharedVertex(EdgeLocation edge1, EdgeLocation edge2) {
		Collection<VertexLocation> adjacentVertices1 = Geometer.getAdjacentVertices(edge1);
		Collection<VertexLocation> adjacentVertices2 = Geometer.getAdjacentVertices(edge2);
		Set<VertexLocation> vertices1 = new HashSet<VertexLocation>(adjacentVertices1);
		Set<VertexLocation> vertices2 = new HashSet<VertexLocation>(adjacentVertices2);
		vertices1.retainAll(vertices2);

		if (vertices1.isEmpty()) {
			return null;
		}
		else {
			return vertices1.iterator().next();
		}
	}

	public static EdgeLocation getSharedEdge(VertexLocation vertex1, VertexLocation vertex2) {
		Collection<EdgeLocation> adjacentEdges1 = Geometer.getAdjacentEdges(vertex1);
		Collection<EdgeLocation> adjacentEdges2 = Geometer.getAdjacentEdges(vertex2);
		Set<EdgeLocation> edges1 = new HashSet<EdgeLocation>(adjacentEdges1);
		Set<EdgeLocation> edges2 = new HashSet<EdgeLocation>(adjacentEdges2);
		edges1.retainAll(edges2);

		if (edges1.isEmpty()) {
			return null;
		}
		else {
			return edges1.iterator().next();
		}
	}
	
	public static EdgeDirection getEquivalentDirection(VertexLocation location1, VertexLocation location2) {
		if ((location1.getDir() == VertexDirection.NorthWest
				&& location2.getDir() == VertexDirection.NorthEast)
				|| (location2.getDir() == VertexDirection.NorthWest
						&& location1.getDir() == VertexDirection.NorthEast)) {
			return EdgeDirection.North;
		}
		else if ((location1.getDir() == VertexDirection.NorthEast
				&& location2.getDir() == VertexDirection.East)
				|| (location2.getDir() == VertexDirection.NorthEast
						&& location1.getDir() == VertexDirection.East)) {
			return EdgeDirection.NorthEast;
		}
		else if ((location1.getDir() == VertexDirection.East
				&& location2.getDir() == VertexDirection.SouthEast)
				|| (location2.getDir() == VertexDirection.East
						&& location1.getDir() == VertexDirection.SouthEast)) {
			return EdgeDirection.SouthEast;
		}
		else if ((location1.getDir() == VertexDirection.SouthEast
				&& location2.getDir() == VertexDirection.SouthWest)
				||(location2.getDir() == VertexDirection.SouthEast
						&& location1.getDir() == VertexDirection.SouthWest)) {
			return EdgeDirection.South;
		}
		else if ((location1.getDir() == VertexDirection.SouthWest
				&& location2.getDir() == VertexDirection.West)
				|| (location2.getDir() == VertexDirection.SouthWest
						&& location1.getDir() == VertexDirection.West)) {
			return EdgeDirection.SouthWest;
		}
		else if ((location1.getDir() == VertexDirection.West
				&& location2.getDir() == VertexDirection.NorthWest)
				|| (location2.getDir() == VertexDirection.West
						&& location1.getDir() == VertexDirection.NorthWest)) {
			return EdgeDirection.NorthWest;
		}
		
		return null;
	}
}
