import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SortConversationsTest {

    private SortConversations sc;
    @BeforeEach
    public void setUp(){
        sc = new SortConversations();
    }
    @Test
    public void sortTestPass(){
        List<int[]> conditions = new ArrayList<>();
        conditions.add(new int[]{1, 5});
        conditions.add(new int[]{2, 3});
        conditions.add(new int[]{4, 1});
        conditions.add(new int[]{4, 3});
        conditions.add(new int[]{5, 2});

        List<Integer> result = sc.sortTopologisch(5, conditions);
        List<Integer> actual_list = new ArrayList<>(Arrays.asList(4,1,5,2,3));
        Assertions.assertEquals(actual_list, result);
    }

    @Test
    public void sortTestZyklus(){
        List<int[]> conditions = new ArrayList<>();
        conditions.add(new int[]{1, 2});
        conditions.add(new int[]{2, 3});
        conditions.add(new int[]{3, 4});
        conditions.add(new int[]{4, 5});
        conditions.add(new int[]{5, 2});

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sc.sortTopologisch(5, conditions);
        });

        String expectedMessage = "Es gibt einen Zyklus in den Ordnungsbedingungen. Keine Reihenfolge möglich.";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortTestLeereBedingungen() {
        List<int[]> conditions = new ArrayList<>();
        List<Integer> result = sc.sortTopologisch(3, conditions);
        List<Integer> expected = List.of(1,2,3);
        Assertions.assertEquals(expected, result);
    }
}
