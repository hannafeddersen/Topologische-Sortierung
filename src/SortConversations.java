import java.util.*;

/**
 * Die Klasse SortConversations ermöglicht es, eine topologische Sortierung
 * für eine gegebene Menge von Gesprächen und deren Bedingungen durchzuführen.
 * Sie überprüft dabei, ob ein Zyklus vorliegt, der eine Sortierung unmöglich macht.
 */
public class SortConversations {

    /**
     * Führt eine topologische Sortierung auf einer gegebenen Anzahl von Gesprächen durch.
     *
     * @param numConversations Die Anzahl der Gespräche.
     * @param conditions Eine Liste von Bedingungen, wobei jede Bedingung ein Array der Form [x, y] ist.
     *                   Dies bedeutet, dass Gespräch x vor Gespräch y stattfinden muss.
     * @return Eine Liste von Integers, die die Reihenfolge der topologisch sortierten Gespräche repräsentiert.
     * @throws IllegalArgumentException Wenn es einen Zyklus in den Ordnungsbedingungen gibt und daher
     *                                  keine gültige Reihenfolge bestimmt werden kann.
     */
    List<Integer> sortTopologisch(int numConversations, List<int[]> conditions) {
        List<Integer> order = new ArrayList<>();

        // Adjazenzliste als Repräsentation eines Digraph
        Map<Integer, LinkedList<Integer>> adjacentList = new HashMap<>();
        int[] numPredecessor = new int[numConversations + 1];

        // Aufbau der Adjazenzliste und Berechnung der Anzahl der Vorgänger
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
            throw new IllegalArgumentException("Es gibt einen Zyklus in den Ordnungsbedingungen. Keine Reihenfolge möglich.");
        }

        return order;
    }

    /**
     * Überprüft, ob es eine Kante (i, s) in der Adjazenzliste gibt, die nicht Teil des Suchbaums ist.
     * Dies weist auf eine Rückkante hin, die auf einen Zyklus im Graphen hindeutet.
     *
     * @param G Die Adjazenzliste des ursprünglichen Graphen, die die Kanten zwischen den Gesprächen repräsentiert.
     * @param T Die Adjazenzliste des Suchbaums, die die besuchten Kanten während der topologischen Sortierung repräsentiert.
     * @return {@code true}, wenn eine Kante (i, s) existiert, die in G aber nicht in T enthalten ist (d.h., eine Rückkante), andernfalls {@code false}.
     */
    public boolean hasEdgeNotInTree(Map<Integer, LinkedList<Integer>> G, Map<Integer, List<Integer>> T) {
        for (int i : G.keySet()) {
            List<Integer> neighbors = G.get(i);
            for (int neighbor : neighbors) {
                // Prüfe, ob die Kante (i, neighbor) in G existiert, aber nicht in T
                if (!T.containsKey(i) || !T.get(i).contains(neighbor)) {
                    return true; // Rückkante gefunden -> Zyklus
                }
            }
        }
        return false; // Keine Rückkanten gefunden -> kein Zyklus
    }
}
