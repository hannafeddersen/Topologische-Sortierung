import java.util.*;

public class SortConversations {

    List<Integer> sortTopologisch(int numConversations, List<int[]> conditions) {
        List<Integer> order = new ArrayList<>();

        // Adjazenzliste als Repräsentation eines gerichteten Graphen
        Map<Integer, LinkedList<Integer>> adjacentList = new HashMap<>();
        int[] numPredecessor = new int[numConversations + 1];

        // Aufbau der Adjazenzliste und Berechnung der In-Degree-Werte
        for (int[] condition : conditions) {
            int x = condition[0];
            int y = condition[1];

            adjacentList.putIfAbsent(x, new LinkedList<>());
            adjacentList.get(x).add(y);

            numPredecessor[y]++;
        }

        Map<Integer, List<Integer>> searchTree = new HashMap<>();

        // Queue zur Verwaltung der Knoten ohne Vorgänger
        Queue<Integer> zeroPredecessor = new LinkedList<>();
        for (int i = 1; i <= numConversations; i++) {
            if (numPredecessor[i] == 0) {
                zeroPredecessor.offer(i);
            }
        }

        // Topologische Sortierung durch Verarbeitung der Knoten ohne Vorgänger
        while (!zeroPredecessor.isEmpty()) {
            int node = zeroPredecessor.poll();
            order.add(node);

            if (adjacentList.containsKey(node)) {
                for (int neighbor : adjacentList.get(node)) {
                    searchTree.putIfAbsent(node, new ArrayList<>());
                    searchTree.get(node).add(neighbor);

                    numPredecessor[neighbor]--;

                    // Wenn der Nachbar keinen Vorgänger hat, füge ihn zur Queue hinzu
                    if (numPredecessor[neighbor] == 0) {
                        zeroPredecessor.offer(neighbor);
                    }
                }
            }
        }

        // Zyklusprüfung (Bei einem Zyklus kann keine Reihenfolge bestimmt werden)
        if (hasEdgeNotInTree(adjacentList, searchTree)) {
            throw new IllegalArgumentException("Es gibt einen Zyklus in den Ordnungsbedingungen, eine Reihenfolge ist nicht möglich.");
        }

        return order;
    }

    public boolean hasEdgeNotInTree(Map<Integer, LinkedList<Integer>> G, Map<Integer, List<Integer>> T) {
        for (int i : G.keySet()) {
            List<Integer> neighbors = G.get(i);
            for (int neighbor : neighbors) {
                // Prüfe, ob die Kante (i, neighbor) in G existiert, aber nicht in T
                if (!T.containsKey(i) || !T.get(i).contains(neighbor)) {
                    return true; // Rückkante gefunden, also gibt es einen Zyklus
                }
            }
        }
        return false; // Keine Rückkanten gefunden, also kein Zyklus
    }
}
