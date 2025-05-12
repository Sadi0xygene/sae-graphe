package dijkstra;

import graph.Graph;
import graph.ShortestPath;
import java.util.*;

public class Dijkstra<T> implements ShortestPath<T> {
	private static final class Node<T> implements Comparable<Node<T>> {
		final T vertex;
		double distance;

		Node(T vertex, double distance) {
			this.vertex = vertex;
			this.distance = distance;
		}

		@Override
		public int compareTo(Node<T> other) {
			return Double.compare(this.distance, other.distance);
		}
	}

	@Override
	public Distances<T> compute(Graph<T> g, T src, Animator<T> animator) throws IllegalArgumentException {
		Map<T, Double> distances = new HashMap<>();
		Map<T, T> predecessors = new HashMap<>();
		PriorityQueue<Node<T>> queue = new PriorityQueue<>();

		// Initialisation
		distances.put(src, 0.0);
		queue.add(new Node<>(src, 0.0));

		while (!queue.isEmpty()) {
			Node<T> current = queue.poll();
			animator.accept(current.vertex, (int) current.distance); // Correction : cast en int

			for (Graph.Arc<T> arc : g.getSucc(current.vertex)) {
				int valuation = arc.val(); // Accès via val() ✅
				T neighbor = arc.dst();     // Accès via dst() ✅

				// Vérification des valuations négatives
				if (valuation < 0) {
					throw new IllegalArgumentException("Arc à valuation négative détecté");
				}

				double newDist = current.distance + valuation;

				if (newDist < distances.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
					distances.put(neighbor, newDist);
					predecessors.put(neighbor, current.vertex);
					queue.add(new Node<>(neighbor, newDist));
				}
			}
		}

		// Conversion des distances en Integer
		Map<T, Integer> intDistances = new HashMap<>();
		distances.forEach((k, v) -> intDistances.put(k, v.intValue()));

		return new Distances<>(intDistances, predecessors);
	}
}