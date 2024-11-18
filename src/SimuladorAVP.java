
import static java.lang.Integer.parseInt;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import arvores.ArvoreVermelhoPreto;
import arvores.ArvoreVermelhoPreto.Node;
import arvores.ArvoreVermelhoPreto.NodeColor;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;

/**
 * Simulador de árvores vermelho e preto:
 * Simula as operações de inserir e remover chaves;
 * Simula os percursos (pré-ordem, em ordem, pós-ordem e em nível).
 * 
 * @author Prof. Dr. David Buzatto
 */
enum traverse {
  None,
  PreOrder,
  InOrder,
  PostOrder,
  InLevel
}

public class SimuladorAVP extends EngineFrame {
  private ArvoreVermelhoPreto<Integer, String> arvore;
  private aesd.ds.interfaces.List<Node<Integer, String>> nos;

  private aesd.ds.interfaces.List<Node<Integer, String>> traverseNodes;
  private int margemCima;
  private int margemDireita;
  private int radius;
  private Vector2 espacamento;
  private Color texto = new Color(57, 53, 82);
  private Color noOrdem = new Color(235, 111, 146);
  private Color lineColor = new Color(215, 130, 126);
  private String inserido;
  private String removido;

  traverse traverseState = traverse.None;
  private float elapsedTime; // Tempo decorrido para o timer
  private int indiceNoAtual; // Índice do nó atual para pintar em ordem

  public SimuladorAVP() {
    super(800, 600, "Simulador de Árvores Vermelho e Preto", 60, true);
  }

  @Override
  public void create() {
    arvore = new ArvoreVermelhoPreto<>();

    arvore.put(8, "8");
    arvore.put(42, "42");
    arvore.put(15, "15");
    arvore.put(23, "23");
    arvore.put(16, "16");
    arvore.put(4, "4");

    espacamento = new Vector2(100, 50);

    nos = arvore.coletarParaDesenho();

    margemCima = 100;
    // NOTE: Not used
    // margemEsquerda = 50;
    margemDireita = getScreenWidth();
    radius = 20;
    inserido = "";
    removido = "";

    atualizarCentro(nos);

  }

  @Override
  public void update() {
    atualizarCentro(nos);
    Vector2 mousePos = getMousePositionPoint();

    if (isMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
      atualizarCentro(nos);
      for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
        if (CollisionUtils.checkCollisionPointCircle(mousePos, no.centro, radius)) {
          removido = no.value;
          inserido = "";
          arvore.delete(no.key);
          nos = arvore.coletarParaDesenho();
        }
      }
    }

    if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {

      int novoNoValor;
      String novoNoChave = inputPainel();
      novoNoValor = parseInt(novoNoChave);

      if (arvore.contains(Integer.valueOf(novoNoChave))) {
        inserido = "Valor já existe";
      } else {
        arvore.put(novoNoValor, novoNoChave);
        nos = arvore.coletarParaDesenho();

        inserido = novoNoChave;
      }
      removido = "";
    }

    if (isKeyPressed(KEY_Q)) {
      this.dispose();
    }
    if (isKeyPressed(KEY_ONE) || isKeyPressed(KEY_KP_1)) {
      traverseState = traverse.PreOrder;
      elapsedTime = 1;
      indiceNoAtual = 0;
      traverseNodes = arvore.traversePreOrderDesenho();
    }

    if (isKeyPressed(KEY_TWO)|| isKeyPressed(KEY_KP_2)) {
      traverseState = traverse.InOrder;
      elapsedTime = 1;
      indiceNoAtual = 0;
      traverseNodes = arvore.traverseInOrderDesenho();
    }

    if (isKeyPressed(KEY_THREE) || isKeyPressed(KEY_KP_3)) {
      traverseState = traverse.PostOrder;
      elapsedTime = 1;
      indiceNoAtual = 0;
      traverseNodes = arvore.traversePostOrderDesenho();
    }

    if (isKeyPressed(KEY_FOUR) || isKeyPressed(KEY_KP_4)) {
      traverseState = traverse.InLevel;
      elapsedTime = 1;
      indiceNoAtual = 0;
      traverseNodes = arvore.traverseInLevelDesenho();
    }

    if (isKeyPressed(KEY_SPACE)) {
      traverseState = traverse.None;
      nos = arvore.coletarParaDesenho();
      elapsedTime = 1;
      indiceNoAtual = 0;
    }
  }

  @Override
  public void draw() {
    clearBackground(new Color(31, 29, 46));

    for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
      desenharSetas(no, lineColor);
    }

    for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
      desenharNo(no, radius, lineColor);
      int tamanhoTexto = measureText(no.key.toString(), 20);
      drawText(no.key.toString(), no.centro.x - tamanhoTexto / 2, no.centro.y - 5, 20, texto);

    }
    // Desenha caso inserido
    if (!inserido.equals("")) {
      drawText("Inseriu: " + inserido, new Vector2(50, 50), 20, WHITE);
    }
    // Desenha caso removido
    if (!removido.equals("")) {
      drawText("Removeu: " + removido, new Vector2(50, 50), 20, WHITE);
    }

    drawText(
        "M1 - Inserir   | M2 - Remover  | q - Voltar    | SPC - Resetar\n1  - Pré-Ordem | 2  - Em Ordem | 3 - Pós-Ordem | 4   - Em Nível",
        new Vector2(20, getScreenHeight() - 50), 20, WHITE);

    if (traverseState != traverse.None) {
      traverseAnimation(getFrameTime());
    }

  }

  private void desenharNo(ArvoreVermelhoPreto.Node<Integer, String> node, int radius, Color lineColor) {
    fillCircle(node.centro.x, node.centro.y, radius, node.cor);
    drawCircle(node.centro.x, node.centro.y, radius, lineColor);
  }

  // NOTE: Not used
  // private void pintarNoOrdem(ArvoreVermelhoPreto.Node<Integer, String> no) {
  // fillCircle(no.centro.x, no.centro.y, radius, noOrdem);
  // }
  private void desenharSetas(ArvoreVermelhoPreto.Node<Integer, String> no, Color lineColor) {
    if (no.previous == null && no.nivel == 0) {
      drawLine(no.centro.x, no.centro.y - 20, no.centro.x, no.centro.y - 40, lineColor);
      drawText("raiz", no.centro.x - measureText("raiz", 20) / 2, no.centro.y - 60, 20, new Color(250, 244, 237));

    }
    if (no.left != null) {
        if (no.left.color == NodeColor.RED) {
            drawLine(no.centro.x, no.centro.y, no.left.centro.x, no.left.centro.y, RED);
        } else {
            drawLine(no.centro.x, no.centro.y, no.left.centro.x, no.left.centro.y, lineColor);
        }
    }
    if (no.right != null) {
      drawLine(no.centro.x, no.centro.y, no.right.centro.x, no.right.centro.y, lineColor);
    }
    
  }

  private void atualizarCentro(aesd.ds.interfaces.List<Node<Integer, String>> arvore) {
    espacamento.x = (arvore.getSize() * 5) + 50;
    espacamento.y = (arvore.getSize() * 2) + 50;

    for (ArvoreVermelhoPreto.Node<Integer, String> no : arvore) {
      atualizarAnterior(no);

      if (no.previous == null) {
        no.centro = new Vector2(margemDireita / 2, espacamento.y * no.nivel + margemCima); // Nó raiz Bp
                                                                                           // duvida?
      } else {
          
        if (no == no.previous.right) {
          no.centro = new Vector2((no.previous.centro.x + espacamento.x / no.nivel),
              espacamento.y * no.nivel + margemCima); // muda a posição do nó, seis duvida?
        } else {
            if (no.color == NodeColor.RED) {
                no.centro = new Vector2(no.previous.centro.x - (espacamento.x * 2.5) / no.nivel,
              espacamento.y * no.previous.nivel + margemCima);
            }
            else {
                no.centro = new Vector2(no.previous.centro.x - espacamento.x / no.nivel,
                espacamento.y * no.nivel + margemCima); // muda a posição do nó, seis duvida?
        }
      }
    }
  }
  }

  private void atualizarAnterior(ArvoreVermelhoPreto.Node<Integer, String> no) {
    if (no.left != null) {
      no.left.previous = no;

    }

    if (no.right != null) {
      no.right.previous = no;

    }
  }

  private void traverseAnimation(double deltaTime) {
    elapsedTime += deltaTime;

    if (elapsedTime >= 1.0) { // A cada 2 segundos
      if (indiceNoAtual < traverseNodes.getSize()) {
        // Pinta o nó atual
        ArvoreVermelhoPreto.Node<Integer, String> noAtual = traverseNodes.get(indiceNoAtual);
        noAtual.cor = noOrdem;
        indiceNoAtual++;
      } else {
        traverseState = traverse.None; // Finaliza a pintura em ordem
      }

      elapsedTime = 0; // Reinicia o tempo para o próximo nó
    }
  }

  private String inputPainel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel label = new JLabel("Digite o número a ser inserido");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(label, BorderLayout.NORTH);

    // UIManager UI = new UIManager();
    UIManager.put("OptionPane.background", new Color(250, 244, 237));
    UIManager.put("Panel.background", new Color(250, 244, 237));
    UIManager.put("Text.background", new Color(250, 244, 237));
    UIManager.put("Button.background", new Color(180, 99, 122));
    UIManager.put("Button.foreground", new Color(224, 222, 244));

    return JOptionPane.showInputDialog(null, panel, "", JOptionPane.PLAIN_MESSAGE);

  }

  public static void main(String[] args) {
    new SimuladorAVP();
  }

}
