
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
  }

  @Override
  public void update() {

    Vector2 mousePos = getMousePositionPoint();

    if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {

      for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {

        Vector2 centro = new Vector2(
            espacamento * no.ranque + margemEsquerda,
            espacamento * no.nivel + margemCima);

        if (CollisionUtils.checkCollisionPointCircle(mousePos, centro, raio)) {
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
      desenharNo(no, espacamento, espacamento);
    }
  }

  private void desenharNo(ArvoreBinariaBusca.Node<Integer, String> no, int espHorizontal, int espVertical) {
    fillCircle(espHorizontal * no.ranque + margemEsquerda, espVertical * no.nivel + margemCima, raio, no.cor);
    drawCircle(espHorizontal * no.ranque + margemEsquerda, espVertical * no.nivel + margemCima, raio, BLACK);
  }

  public static void main(String[] args) {
    new SimuladorABB();
  }

}
