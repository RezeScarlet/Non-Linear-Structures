
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import projetoesdarvores.esd.ArvoreBinariaBusca.Node;
import projetoesdarvores.esd.ArvoreBinariaBusca;

/**
 * Simulador de árvores binárias de busca:
 * Simula as operações de inserir e remover chaves;
 * Simula os percursos (pré-ordem, em ordem, pós-ordem e em nível).
 * 
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorABB extends EngineFrame {

  private ArvoreBinariaBusca<Integer, String> arvore;
  private aesd.ds.interfaces.List<Node<Integer, String>> nos;
  private int margemCima;
  private int margemEsquerda;
  private int raio;
  private int espacamento;

  public SimuladorABB() {
    super(800, 600, "Simulador de Árvores Binárias de Busca", 60, true);
  }

  @Override
  public void create() {
    arvore = new ArvoreBinariaBusca<>();
    arvore.put(5, "cinco");
    arvore.put(2, "dois");
    arvore.put(10, "dez");
    arvore.put(15, "quinze");
    arvore.put(12, "doze");
    arvore.put(1, "um");
    arvore.put(3, "três");
    nos = arvore.coletarParaDesenho();

    margemCima = 100;
    margemEsquerda = 50;
    raio = 20;
    espacamento = 50;
//    for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
//      no.centro = new Vector2(espacamento * no.ranque + margemEsquerda, espacamento * no.nivel + margemCima);
//    }
atualizarCentro(nos, espacamento);
  }

  @Override
  public void update() {
//    for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
//      no.centro = new Vector2(espacamento * no.ranque + margemEsquerda, espacamento * no.nivel + margemCima);
//    }
    atualizarCentro(nos, espacamento);
    Vector2 mousePos = getMousePositionPoint();

    if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
//      for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
//        no.centro = new Vector2(espacamento * no.ranque + margemEsquerda, espacamento * no.nivel + margemCima);
//      }
atualizarCentro(nos, espacamento);

      for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {

        if (CollisionUtils.checkCollisionPointCircle(mousePos, no.centro, raio)) {
          SwingUtilities.invokeLater(() -> {
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "Remover o nó " + no.key + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
              arvore.delete(no.key);
              nos = arvore.coletarParaDesenho();
            }
          });

        }

      }

    }
  }

  @Override
  public void draw() {
    for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
      desenharNo(no);
      desenharSetas(no);
    }
  }

  private void desenharNo(ArvoreBinariaBusca.Node<Integer, String> no) {
    fillCircle(no.centro.x, no.centro.y, raio, no.cor);
    drawCircle(no.centro.x, no.centro.y, raio, BLACK);
  }

  private void desenharSetas(ArvoreBinariaBusca.Node<Integer, String> no) {
    if (no.left != null) {
      drawLine(new Vector2(no.centro.x - raio, no.centro.y), new Vector2(no.left.centro.x, no.left.centro.y - raio),
          Color.BLACK);
    }

    if (no.right != null) {
      drawLine(new Vector2(no.centro.x + raio, no.centro.y), new Vector2(no.right.centro.x, no.right.centro.y - raio),
          Color.BLACK);
    }
  }

  private void atualizarCentro(aesd.ds.interfaces.List<Node<Integer, String>> a, int espacamento) {
    for (ArvoreBinariaBusca.Node<Integer, String> no : a) {
        no.centro = new Vector2(espacamento * no.ranque + margemEsquerda, espacamento * no.nivel + margemCima); // muda a posição do nó, seis duvida?
      }
  }

  public static void main(String[] args) {
    new SimuladorABB();
  }

}
