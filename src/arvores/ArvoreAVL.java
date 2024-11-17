package arvores;

import aesd.ds.implementations.linear.LinkedQueue;
import aesd.ds.implementations.linear.ResizingArrayList;
import aesd.ds.interfaces.List;
import aesd.ds.interfaces.Queue;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import java.util.Iterator;

/**
 * Implementação de uma árvore AVL (Adelson-Velsky e Landis).
 *
 * Implementação baseada na obra: WEISS, M. A. Data Structures and Algorithm
 * Analysis in Java. 3. ed. Pearson Education: New Jersey, 2012. 614 p.
 *
 * @param <Key> Tipo das chaves que serão armazenadas na árvore.
 * @param <Value> Tipo dos valores associados às chaves armazenadas na árvore.
 *
 * @author Prof. Dr. David Buzatto
 */
public class ArvoreAVL<Key extends Comparable<Key>, Value> implements Iterable<Key> {

    /*
     * Classe interna estática que define os nós da árvore AVL.
     */
    public static class Node<Key extends Comparable<Key>, Value> {

        public Key key;
        public Value value;
        public Node<Key, Value> left;
        public Node<Key, Value> right;
        public Node<Key, Value> previous;
        public int nivel;
        public int ranque;
        public Color cor;
        public Vector2 centro;

        public int height;
        public String AVLIndicator;
      

        @Override
        public String toString() {
            return key + " -> " + value + " (" + height + ")";
        }

    }

    // raiz da árvore
    private Node<Key, Value> root;

    // tamanho da árvore (quantidade de pares chave/valor)
    private int size;

    // fator de balanceamento
    // valor máximo na diferença de alturas de duas subárvores
    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * Constrói uma Árvore AVL vazia.
     */
    public ArvoreAVL() {
        root = null;
    }

    public void put(Key key, Value value) throws IllegalArgumentException {

        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }

        if (value == null) {
            delete(key);
            return;
        }

        root = (Node<Key, Value>) put(root, key, value);

    }

    private Node<Key, Value> put(Node<Key, Value> node, Key key, Value value) {

        if (node == null) {

            Node<Key, Value> avlNode = new Node<>();
            avlNode.key = key;
            avlNode.value = value;
            avlNode.left = null;
            avlNode.right = null;
            avlNode.height = 1;

            node = avlNode;

            size++;

        } else {

            int comp = key.compareTo(node.key);

            if (comp < 0) {
                node.left = put(node.left, key, value);
            } else if (comp > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }

        }

        // balanceia a árvore
        return balance(node);

    }

    public Value get(Key key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        return get(root, key);
    }

    private Value get(Node<Key, Value> node, Key key) {

        while (node != null) {

            int comp = key.compareTo(node.key);

            if (comp < 0) {
                node = node.left;
            } else if (comp > 0) {
                node = node.right;
            } else {
                return node.value;
            }

        }

        return null;

    }

    public void delete(Key key) throws IllegalArgumentException {

        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }

        if (!contains(key)) {
            return;
        }

        root = (Node<Key, Value>) delete(root, key);
        size--;

    }

    private Node<Key, Value> delete(Node<Key, Value> node, Key key) {

        if (node == null) {
            return node;
        }

        int comp = key.compareTo(node.key);

        if (comp < 0) {
            node.left = delete(node.left, key);
        } else if (comp > 0) {
            node.right = delete(node.right, key);
        } else {

            // dois filhos
            if (node.left != null && node.right != null) {

                Node<Key, Value> min = min(node.right);
                node.key = min.key;
                node.value = min.value;

                node.right = delete(node.right, node.key);

                // um ou nenhum filho
            } else {
                node = (node.left != null) ? node.left : node.right;
            }

        }

        return balance(node);

    }

    public boolean contains(Key key) throws IllegalArgumentException {
        return get(key) != null;
    }

    public Node<Key, Value> min() {

        if (isEmpty()) {
            return null;
        }

        return min(root);

    }

    private Node<Key, Value> min(Node<Key, Value> node) {

        if (node == null) {
            return node;
        }

        while (node.left != null) {
            node = node.left;
        }

        return node;

    }

    public Node<Key, Value> max() {

        if (isEmpty()) {
            return null;
        }

        return max(root);

    }

    private Node<Key, Value> max(Node<Key, Value> node) {

        if (node == null) {
            return node;
        }

        while (node.right != null) {
            node = node.right;
        }

        return node;

    }

    /**
     * Esvazia a árvore.
     */
    public void clear() {
        root = (Node<Key, Value>) clear(root);
        size = 0;
    }

    /*
     * Método privado para remoção de todos os itens de forma recursiva.
     */
    private Node<Key, Value> clear(Node<Key, Value> node) {

        if (node != null) {
            node.left = clear(node.left);
            node.right = clear(node.right);
        }

        return null;

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    private Node<Key, Value> balance(Node<Key, Value> node) {

        if (node == null) {
            return node;
        }

        if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE) {
            if (height(node.left.left) >= height(node.left.right)) {
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE) {
            if (height(node.right.right) >= height(node.right.left)) {
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        ((Node<Key, Value>) node).height = Math.max(height(node.left), height(node.right)) + 1;

        return node;

    }

    /**
     * Retorna a altura de um nó ou 0 caso o nó seja nulo.
     */
    private int height(Node<Key, Value> node) {
        return node == null ? 0 : ((Node<Key, Value>) node).height;
    }

    /**
     * Rotação EE/LL - simétrica à DD/RR
     *
     * Para a inserção, N foi inserido na subárvore esquerda da subárvore
     * esquerda de A, ou seja, rotacionada um nó com filho à esquerda.
     *
     * Para as árvores AVL, essa é uma rotação simples apresentada no slide 11.
     *
     * Diferença entre alturas: A = +2; B = +1;
     *
     * Ao final, atualiza as alturas e retorna a nova raiz.
     */
    private Node<Key, Value> rotateWithLeftChild(Node<Key, Value> a) {

        Node<Key, Value> b = a.left;
        a.left = b.right;
        b.right = a;

        Node<Key, Value> aAvl = (Node<Key, Value>) a;
        Node<Key, Value> bAvl = (Node<Key, Value>) b;

        aAvl.height = Math.max(height(a.left), height(a.right)) + 1;
        bAvl.height = Math.max(height(b.left), height(b.right)) + 1;

        return b;

    }

    /**
     * Rotação DD/RR - simétrica à EE/LL
     *
     * Para a inserção, N foi inserido na subárvore direita da subárvore direita
     * de A, ou seja, rotacionada um nó com filho à direita.
     *
     * Para as árvores AVL, essa é uma rotação simples apresentada no slide 17.
     *
     * Diferença entre alturas: A = -2; B = -1;
     *
     * Ao final, atualiza as alturas e retorna a nova raiz.
     */
    private Node<Key, Value> rotateWithRightChild(Node<Key, Value> a) {

        Node<Key, Value> b = a.right;
        a.right = b.left;
        b.left = a;

        Node<Key, Value> aAvl = (Node<Key, Value>) a;
        Node<Key, Value> bAvl = (Node<Key, Value>) b;

        aAvl.height = Math.max(height(a.left), height(a.right)) + 1;
        bAvl.height = Math.max(height(b.left), height(b.right)) + 1;

        return b;

    }

    /**
     * Rotação ED/LR - simétrica à DE/RL
     *
     * Para a inserção, N foi inserido na subárvore direita da subárvore
     * esquerda de A, ou seja, realiza uma rotação dupla: 1 - filho da esquerda
     * com seu filho da direita; 2 - nó A com seu novo filho à esquerda.
     *
     * Para as árvores AVL, essa é uma rotação dupla apresentada nos slides 23,
     * 24 e 25.
     *
     * Diferença entre alturas: A = +2; B = -1;
     *
     * Ao final, atualiza as alturas e retorna a nova raiz.
     */
    private Node<Key, Value> doubleWithLeftChild(Node<Key, Value> a) {
        a.left = rotateWithRightChild(a.left);
        return rotateWithLeftChild(a);
    }

    /**
     * Rotação DE/RL - simétrica à ED/LR
     *
     * Para a inserção, N foi inserido na subárvore esquerda da subárvore
     * direita de A, ou seja, realiza uma rotação dupla: 1 - filho da direita
     * com seu filho da esquerda; 2 - nó A com seu novo filho à direita.
     *
     * Para as árvores AVL, essa é uma rotação dupla apresentada nos slides 37,
     * 38 e 39.
     *
     * Diferença entre alturas: A = -2; B = +1;
     *
     * Ao final, atualiza as alturas e retorna a nova raiz.
     */
    private Node<Key, Value> doubleWithRightChild(Node<Key, Value> a) {
        a.right = rotateWithLeftChild(a.right);
        return rotateWithRightChild(a);
    }

    @Override
    public Iterator<Key> iterator() {
        return traverseInOrder().iterator();
    }

    public Iterable<Key> getKeys() {
        Queue<Key> keys = new LinkedQueue<>();
        for (Key k : traverseInOrder()) {
            keys.enqueue(k);
        }
        return keys;
    }

    private void inOrder(Node<Key, Value> node, List<Key> keys) {
        if (node != null) {
            inOrder(node.left, keys);
            keys.add(node.key);
            inOrder(node.right, keys);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (!isEmpty()) {
            preOrderForPrint(root, "", null, sb);
        } else {
            sb.append("empty AVL tree!\n");
        }

        return sb.toString();

    }

    private void preOrderForPrint(Node<Key, Value> node, String ident, String leftRight, StringBuilder sb) {

        if (node != null) {

            String rootIdent = "";
            String leafIdent = "";

            if (node != root) {
                rootIdent = ident + "|--";
                leafIdent = ident + "|  ";
            }

            sb.append(rootIdent);
            if (leftRight != null) {
                sb.append("(").append(leftRight).append(") ");
            }
            sb.append(node);
            if (node == root) {
                sb.append(" <- root");
            }
            sb.append("\n");

            preOrderForPrint(node.left, leafIdent, "L", sb);
            preOrderForPrint(node.right, leafIdent, "R", sb);

        }

    }
    
    private List<Key> traverseInOrder() {
        List<Key> keys = new ResizingArrayList<>();
        inOrder(root, keys);
        return keys;
    }
 
  public aesd.ds.interfaces.List<Node<Key, Value>> traverseInOrderDesenho() {
    aesd.ds.interfaces.List<Node<Key, Value>> nodes = new ResizingArrayList<>();
    inOrderDesenho(root, nodes);
    return nodes;
  }

  public List<Key> traversePreOrder() {
    List<Key> keys = new ResizingArrayList<>();
    preOrder(root, keys);
    return keys;
  }

  public aesd.ds.interfaces.List<Node<Key, Value>> traversePreOrderDesenho() {
    aesd.ds.interfaces.List<Node<Key, Value>> nodes = new ResizingArrayList<>();
    preOrderDesenho(root, nodes);
    return nodes;
  }

  public List<Key> traversePostOrder() {
    List<Key> keys = new ResizingArrayList<>();
    postOrder(root, keys);
    return keys;
  }

  public aesd.ds.interfaces.List<Node<Key, Value>> traversePostOrderDesenho() {
    aesd.ds.interfaces.List<Node<Key, Value>> nodes = new ResizingArrayList<>();
    postOrderDesenho(root, nodes);
    return nodes;
  }

  public List<Key> traverseInLevel() {
    List<Key> keys = new ResizingArrayList<>();
    inLevel(root, keys);
    return keys;
  }

  public aesd.ds.interfaces.List<Node<Key, Value>> traverseInLevelDesenho() {
    aesd.ds.interfaces.List<Node<Key, Value>> nodes = new ResizingArrayList<>();
    inLevelDesenho(root, nodes);
    return nodes;
  }
  
  private void inOrderDesenho(Node<Key, Value> node, aesd.ds.interfaces.List<Node<Key, Value>> nodes) {
    if (node != null) {
      inOrderDesenho(node.left, nodes);
      nodes.add(node);
      inOrderDesenho(node.right, nodes);
    }
  }

  private void preOrder(Node<Key, Value> node, List<Key> keys) {
    if (node != null) {
      keys.add(node.key);
      preOrder(node.left, keys);
      preOrder(node.right, keys);
    }
  }

  private void preOrderDesenho(Node<Key, Value> node, aesd.ds.interfaces.List<Node<Key, Value>> nodes) {
    if (node != null) {
      nodes.add(node);
      preOrderDesenho(node.left, nodes);
      preOrderDesenho(node.right, nodes);
    }
  }

  private void postOrder(Node<Key, Value> node, List<Key> keys) {
    if (node != null) {
      postOrder(node.left, keys);
      postOrder(node.right, keys);
      keys.add(node.key);
    }
  }

  private void postOrderDesenho(Node<Key, Value> node, aesd.ds.interfaces.List<Node<Key, Value>> nodes) {
    if (node != null) {
      postOrderDesenho(node.left, nodes);
      postOrderDesenho(node.right, nodes);
      nodes.add(node);
    }
  }

  private void inLevel(Node<Key, Value> node, List<Key> keys) {
    if (node == null) {
      return;
    }

    Queue<Node<Key, Value>> queue = new LinkedQueue<>();
    queue.enqueue(node);

    while (!queue.isEmpty()) {
      Node<Key, Value> current = queue.dequeue();
      keys.add(current.key);

      if (current.left != null) {
        queue.enqueue(current.left);
      }

      if (current.right != null) {
        queue.enqueue(current.right);
      }
    }
  }

  private void inLevelDesenho(Node<Key, Value> node, aesd.ds.interfaces.List<Node<Key, Value>> nodes) {
    if (node == null) {
      return;
    }

    Queue<Node<Key, Value>> queue = new LinkedQueue<>();
    queue.enqueue(node);

    while (!queue.isEmpty()) {
      Node<Key, Value> current = queue.dequeue();
      nodes.add(current);

      if (current.left != null) {
        queue.enqueue(current.left);
      }

      if (current.right != null) {
        queue.enqueue(current.right);
      }
    }
  }

    public List<Node<Key, Value>> coletarParaDesenho() {
        List<ArvoreAVL.Node<Key, Value>> nos = new ResizingArrayList<>();
        emOrdemColeta(root, nos, 0);
        return nos;
    }

    private void emOrdemColeta(ArvoreAVL.Node<Key, Value> node, List<ArvoreAVL.Node<Key, Value>> nos, int nivel) {
        if (node != null) {
            emOrdemColeta(node.left, nos, nivel + 1);
            node.nivel = nivel;
            node.ranque = nos.getSize();
            node.cor = new Color(235, 188, 186);
            node.previous = null;
            setAVLIndicator(node);
            nos.add(node);
            emOrdemColeta(node.right, nos, nivel + 1);
        }
    }
    
    private void setAVLIndicator(ArvoreAVL.Node<Key, Value> node) {
        int a;
        int b;
        if (node.left == null) {
            a = 0;
        } else {
            a = node.left.height;
        }
        if (node.right == null) {
            b = 0;
        } else {
            b = node.right.height;
        }
        
        node.AVLIndicator = String.valueOf(a - b);
    }
}
