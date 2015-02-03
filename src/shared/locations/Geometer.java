package shared.locations;

import java.util.*;

public class Geometer {

	public static Collection<EdgeLocation> getAdjacentEdges(EdgeLocation edge) {
		Collection<EdgeLocation> adjacentEdges = new ArrayList<EdgeLocation>();
		HexLocation hex = edge.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (edge.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentEdges.add(new EdgeLocation(new HexLocation(x-1, y+1), EdgeDirection.NorthEast));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x-1, y+1), EdgeDirection.North));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x-1, y), EdgeDirection.NorthEast));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.North));
			break;
		case North:
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.NorthWest));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x-1, y), EdgeDirection.NorthEast));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x+1, y-1), EdgeDirection.NorthWest));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.NorthEast));
			break;
		case NorthEast:
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.North));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x+1, y-1), EdgeDirection.NorthWest));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x+1, y), EdgeDirection.North));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x+1, y), EdgeDirection.NorthWest));
			break;
		}

		return adjacentEdges;
	}

	public static Collection<EdgeLocation> getAdjacentEdges(VertexLocation vertex) {
		Collection<EdgeLocation> adjacentEdges = new ArrayList<EdgeLocation>();
		HexLocation hex = vertex.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (vertex.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.NorthWest));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x-1, y), EdgeDirection.NorthEast));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.North));
			break;
		case NorthEast:
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.North));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x+1, y-1), EdgeDirection.NorthWest));
			adjacentEdges.add(new EdgeLocation(new HexLocation(x, y), EdgeDirection.NorthEast));
			break;
		}

		return adjacentEdges;
	}

	public static Collection<VertexLocation> getAdjacentVertices(EdgeLocation edge) {
		Collection<VertexLocation> adjacentVertices = new ArrayList<VertexLocation>();
		HexLocation hex = edge.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (edge.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentVertices.add(new VertexLocation(new HexLocation(x-1, y+1), VertexDirection.NorthEast));
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthWest));
			break;
		case North:
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthWest));
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthEast));
			break;
		case NorthEast:
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthEast));
			adjacentVertices.add(new VertexLocation(new HexLocation(x+1, y), VertexDirection.NorthWest));
			break;
		}

		return adjacentVertices;
	}

	public static Collection<VertexLocation> getAdjacentVertices(VertexLocation vertex) {
		Collection<VertexLocation> adjacentVertices = new ArrayList<VertexLocation>();
		HexLocation hex = vertex.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();

		switch (vertex.getNormalizedLocation().getDir()) {
		case NorthWest:
			adjacentVertices.add(new VertexLocation(new HexLocation(x-1, y+1), VertexDirection.NorthEast));
			adjacentVertices.add(new VertexLocation(new HexLocation(x-1, y), VertexDirection.NorthEast));
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthEast));
			break;
		case NorthEast:
			adjacentVertices.add(new VertexLocation(new HexLocation(x, y), VertexDirection.NorthWest));
			adjacentVertices.add(new VertexLocation(new HexLocation(x+1, y-1), VertexDirection.NorthWest));
			adjacentVertices.add(new VertexLocation(new HexLocation(x+1, y), VertexDirection.NorthWest));
			break;
		}

		return adjacentVertices;
	}
}
