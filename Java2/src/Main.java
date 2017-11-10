
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

public class Main {

    private static final Map<Character, IntBinaryOperator> binaryOperators = new HashMap<>();
    static {
        binaryOperators.put('+', (a, b) -> a + b);
        binaryOperators.put('-', (a, b) -> a - b);
        binaryOperators.put('*', (a, b) -> a * b);
    }

    public static void main(String[] args) {

        //(15 + 3) - (12 * 8) - 11
        Node<Integer, Character> node1 = new BiNode<>('+',
                new Leaf<>(15),
                new Leaf<>(3));
        Node<Integer, Character> node2 = new BiNode<>('*',
                new Leaf<>(12),
                new Leaf<>(8));
        Node<Integer, Character> node3 = new BiNode<>('-',
                node1,
                node2);
        Node<Integer, Character> node4 = new BiNode<>('-',
                node3,
                new Leaf<>(11));

        System.out.println(treeToString(node4) + " = " + calculateTree(node4));
        System.out.println("Inverted leaves");
        System.out.println(treeToString(invertTree(node4)) + " = " + calculateTree(invertTree(node4)));
    }

    private static Integer calculateTree(Node<Integer, Character> tree){
        return tree.process(Function.identity(), (c, a, b) -> binaryOperators.get(c).applyAsInt(a, b));
    }

    private static String treeToString(Node<Integer, Character> tree){
        return tree.process(Object::toString, (c, a, b) -> "(" + a + " " + c + " " + b + ")");
    }

    private static Node<Integer, Character> invertTree(Node<Integer, Character> tree){
        return tree.process(t -> new Leaf<>(-t), BiNode<Integer, Character>::new);
    }

}
