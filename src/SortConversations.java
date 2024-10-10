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
                    numPredecessor[neighbor]--;

                    // Wenn der Nachbar keinen Vorgänger hat, füge ihn zur Queue hinzu
                    if (numPredecessor[neighbor] == 0) {
                        zeroPredecessor.offer(neighbor);
                    }
                }
            }
        }

        // Zyklusprüfung (Bei einem Zyklus kann keine Reihenfolge bestimmt werden)
        if (order.size() != numConversations) {
            throw new IllegalArgumentException("Es gibt einen Zyklus in den Ordnungsbedingungen, eine Reihenfolge ist nicht möglich.");
        }

        return order;
    }
}
