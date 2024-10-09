import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;

public class SortConversations {
    public static void main(String[] args){
        List<int[]> conditions = new ArrayList<>();
        conditions.add(new int[]{1, 5});
        conditions.add(new int[]{2, 3});
        conditions.add(new int[]{4, 1});
        conditions.add(new int[]{4, 3});
        conditions.add(new int[]{5, 2});

        List<Integer> result = sortTopologisch(5, conditions);
        System.out.println("Erhaltene Reihenfolge: " + result);

        conditions.add(new int[]{1, 2});
        conditions.add(new int[]{2, 3});
        conditions.add(new int[]{3, 4});
        conditions.add(new int[]{4, 5});
        conditions.add(new int[]{5, 2});

        List<Integer> result_zyklus = sortTopologisch(5, conditions);
        System.out.println("Erhaltene Reihenfolge: " + result_zyklus);


    }

    public static List<Integer> sortTopologisch(int numConversations, List<int[]> conditions) {
        List<Integer> order = new ArrayList<>(); // Liste für die sortierte Reihenfolge der Gespräche

        // Adjazenzliste und In-Degree-Array initialisieren
        Map<Integer, LinkedList<Integer>> adjacentList = new HashMap<>();
        int[] inDegree = new int[numConversations + 1]; // Array zur Speicherung der Anzahl der eingehenden Kanten

        // Aufbau der Adjazenzliste und Berechnung der In-Degree-Werte
        for (int[] condition : conditions) {
            int x = condition[0];
            int y = condition[1];

            adjacentList.putIfAbsent(x, new LinkedList<>());
            adjacentList.get(x).add(y);

            // Erhöhe den In-Degree von y, da eine eingehende Kante von x zu y existiert
            inDegree[y]++;
        }

        // Stack zur Verwaltung der Knoten mit einem In-Degree von 0 (ohne Vorgänger)
        Stack<Integer> zeroSuccessor = new Stack<>();
        for (int i = 1; i <= numConversations; i++) {
            if (inDegree[i] == 0) {
                zeroSuccessor.push(i);
            }
        }

        // Topologische Sortierung durch Verarbeitung der Knoten mit In-Degree 0
        while (!zeroSuccessor.isEmpty()) {
            int node = zeroSuccessor.pop();
            order.add(node); // Füge den Knoten zur Reihenfolge hinzu

            if (adjacentList.containsKey(node)) {
                for (int neighbor : adjacentList.get(node)) {
                    inDegree[neighbor]--; // Reduziere den In-Degree des Nachbarn

                    // Wenn der In-Degree eines Nachbarn 0 erreicht, füge ihn zum Stack hinzu
                    if (inDegree[neighbor] == 0) {
                        zeroSuccessor.push(neighbor);
                    }
                }
            }
        }

        //Zyklusprüfung
        if (order.size() != numConversations) {
            throw new IllegalArgumentException("Es gibt einen Zyklus in den Ordnungsbedingungen, eine Reihenfolge ist nicht möglich.");
        }

        System.out.println("Mögliche Reihenfolge: " + order);
        return order;
    }
}
