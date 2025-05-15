package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheHHAdj implements VarGraph {
	private final Map<String, List<Arc<String>>> adjacencyList = new HashMap<>();

	@Override
	public List<Arc<String>> getSucc(String s) {
		return adjacencyList.getOrDefault(s, List.of());
	}

	@Override
	public void ajouterSommet(String noeud) {
		adjacencyList.putIfAbsent(noeud, new ArrayList<>());
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		// Ajouter les sommets s'ils n'existent pas
		ajouterSommet(source);
		ajouterSommet(destination);

		// Vérifier si l'arc existe déjà
		List<Arc<String>> arcs = adjacencyList.get(source);
		for (Arc<String> arc : arcs) {
			if (arc.dst().equals(destination)) {
				throw new IllegalArgumentException("Arc déjà existant");
			}
		}

		// Ajouter le nouvel arc
		arcs.add(new Arc<>(valeur, destination));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean firstEntry = true;

		for (Map.Entry<String, List<Arc<String>>> entry : adjacencyList.entrySet()) {
			String source = entry.getKey();
			List<Arc<String>> arcs = entry.getValue();

			if (arcs.isEmpty()) {
				if (!firstEntry) {
					sb.append(", ");
				}
				sb.append(source).append(":");
				firstEntry = false;
				continue;
			}

			for (Arc<String> arc : arcs) {
				if (!firstEntry) {
					sb.append(", ");
				}
				sb.append(source)
						.append("-")
						.append(arc.dst())
						.append("(")
						.append(arc.val())
						.append(")");
				firstEntry = false;
			}
		}

		return sb.toString();
	}
}