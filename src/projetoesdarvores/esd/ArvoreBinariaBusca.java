package projetoesdarvores.esd;

import aesd.ds.implementations.linear.LinkedQueue;
import aesd.ds.implementations.linear.ResizingArrayList;
import aesd.ds.interfaces.List;
import aesd.ds.interfaces.Queue;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Implementação de uma árvore binária de busca fundamental (Binary Search
 * Tree).
 * 
 * Algumas modificações de acesso foram feitas na classe, permitindo que alguns
 * detalhes internos dela sejam acessíveis externamente. Essa mudança
 * teve como objetivo permitir que os detalhes estruturais da árvore possam ser
 * usados pela classe com os algoritmos de percursos (TreeTraversals).
 * 
 * Implementação baseada na obra: SEDGEWICK, R.; WAYNE, K. Algorithms.
 * 4. ed. Boston: Pearson Education, 2011. 955 p.
 * 
 * @param <Key>   Tipo das chaves que serão armazenadas na árvore.
 * @param <Value> Tipo dos valores associados às chaves armazenadas na árvore.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class ArvoreBinariaBusca<Key extends Comparable<Key>, Value> implements Iterable<Key> {

  /*
   * Classe interna estática que define os nós da árvore binária de busca.
   */
  public static class Node<Key extends Comparable<Key>, Value> {

    public Key key;
    public Value value;
    public Node<Key, Value> left;
    public Node<Key, Value> right;
    public Node<Key,Value> previous;
    public int nivel;
    public int ranque;
    public Color cor;
    public Vector2 centro;

    @Override
    public String toString() {
      return key + " -> " + value + " nivel: " + nivel + " ranque: " + ranque;
    }

  }

  // raiz da árvore
  private Node<Key, Value> root;

  // tamanho da árvore (quantidade de pares chave/valor)
  private int size;

  /**
   * Constrói uma árvore binária de busca vazia.
   */
  public ArvoreBinariaBusca() {
    root = null; // redundante, apenas para mostrar o que acontece
  }

  public void put(Key key, Value value) throws IllegalArgumentException {

    if (key == null) {
      throw new IllegalArgumentException("first argument to put() is null");
    }

    if (value == null) {
      delete(key);
      return;
    }

    root = put(root, key, value);

  }

  /*
   * Método privado para a inserção recursiva.
   */
  private Node<Key, Value> put(Node<Key, Value> node, Key key, Value value) {

    if (node == null) {

      node = new Node<>();
      node.key = key;
      node.value = value;
      node.left = null;
      node.right = null;

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

    return node;

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

    root = delete(root, key);

  }

  /*
   * Método privado para a remoção recursiva (Hibbard Deletion).
   */
  private Node<Key, Value> delete(Node<Key, Value> node, Key key) {

    if (node != null) {

      Node<Key, Value> temp;
      int comp = key.compareTo(node.key);

      if (comp == 0) {

        size--;

        // o nó não tem filhos
        if (node.left == node.right) {

          return null;

          // o nó a ser removido não tem filho à esquerda, só à direita
          // a primeira condição garante que se os dois nós não são o mesmo,
          // um deles pode ser null.
        } else if (node.left == null) {

          temp = node.right;
          node.right = null;
          return temp;

          // o nó a ser removido não tem filho à direita, só à esquerda
          // a primeira condição garante que se os dois nós não são o mesmo,
          // um deles pode ser null.
        } else if (node.right == null) {

          temp = node.left;
          node.left = null;
          return temp;

          // o nó a ser removido tem filhos em ambos os lados
        } else {

          // busca pelo menor nó, onde a subárvore esquerda
          // será inserida
          temp = node.right;
          Node<Key, Value> min = temp;

          while (min.left != null) {
            min = min.left;
          }

          // reaponta a subárvore esquerda do nó removido
          // no menor item encontrado
          min.left = node.left;

          node.left = null;
          node.right = null;

          return temp;

        }

      } else if (comp < 0) {
        node.left = delete(node.left, key);
      } else { // comparacao > 0
        node.right = delete(node.right, key);
      }

    }

    return node;

  }

  public boolean contains(Key key) throws IllegalArgumentException {
    return get(key) != null;
  }

  public void clear() {
    root = clear(root);
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

  public List<Key> traverseInOrder() {
    List<Key> keys = new ResizingArrayList<>();
    inOrder(root, keys);
    return keys;
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
   
  private void inOrder(Node<Key, Value> node, List<Key> keys) {
    if (node != null) {
      inOrder(node.left, keys);
      keys.add(node.key);
      inOrder(node.right, keys);
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
    List<Node<Key, Value>> nos = new ResizingArrayList<>();
    emOrdemColeta(root, nos, 0);
    return nos;
  }

  private void emOrdemColeta(Node<Key, Value> node, List<Node<Key, Value>> nos, int nivel) {
    if (node != null) {
      emOrdemColeta(node.left, nos, nivel + 1);
      node.nivel = nivel;
      node.ranque = nos.getSize();
      node.cor = new Color (235,188,186);
      node.previous = null;
      nos.add(node);
      emOrdemColeta(node.right, nos, nivel + 1);
    }
  }

  
  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    if (!isEmpty()) {
      preOrderForPrint(root, "", null, sb);
    } else {
      sb.append("empty binary search tree!\n");
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

}
