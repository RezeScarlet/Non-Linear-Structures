
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import arvores.ArvoreBinariaBusca.Node;
import arvores.ArvoreBinariaBusca;

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
   private int margemDireita;
  private int raio;
  private Vector2 espacamento;
  private Color texto = new Color (57,53,82);
  private Color noCor =  new Color (235,188,186);
  private Color noOrdem = new Color (235,111,146);
  private Color noContorno = new Color (215,130,126);

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
    arvore.put(9, "nove");
    arvore.put(22, "22");
    arvore.put(50, "50");
    arvore.put(66, "66");
    arvore.put(13, "13");
    arvore.put(14, "14");
    arvore.put(49, "49");
    arvore.put(21, "21");
    
    espacamento = new Vector2(100, 50);
    nos = arvore.coletarParaDesenho();

    margemCima = 100;
    margemEsquerda= 50;
    margemDireita = getScreenWidth();
    raio = 20;
    
    
   
    atualizarCentro(nos);
    
  }

  @Override
  public void update() {
      
    atualizarCentro(nos);
    Vector2 mousePos = getMousePositionPoint();

    if (isMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
        atualizarCentro(nos);


      for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {

        if (CollisionUtils.checkCollisionPointCircle(mousePos, no.centro, raio)) {          
              arvore.delete(no.key);
              nos = arvore.coletarParaDesenho();
            }
        }

      }

    }

  @Override
  public void draw() {
    clearBackground(new Color (31,29,46));
    for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
      desenharSetas(no);    
      
    }
    
    for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
        desenharNo(no);
        int tamanhoTexto = measureText(no.key.toString(), 20);
        drawText(no.key.toString(), no.centro.x - tamanhoTexto / 2, no.centro.y - 5, 20, texto);
    }
  }

  private void desenharNo(ArvoreBinariaBusca.Node<Integer, String> no) {
    fillCircle(no.centro.x, no.centro.y, raio, noCor);
    drawCircle(no.centro.x, no.centro.y, raio, noContorno);
  }

  private void desenharSetas(ArvoreBinariaBusca.Node<Integer, String> no) {
    if (no.previous == null) {
        drawLine(no.centro.x, no.centro.y - 20, no.centro.x, no.centro.y - 40, noContorno);
        drawText("raiz", no.centro.x - measureText("raiz", 20) / 2, no.centro.y-60, 20, new Color (250,244,237));
    }
    if (no.left != null) {
        drawLine(no.centro.x, no.centro.y, no.left.centro.x, no.left.centro.y, noContorno);
    }

    if (no.right != null) {

        drawLine(no.centro.x, no.centro.y, no.right.centro.x, no.right.centro.y, noContorno);
    }
  }

  private void atualizarCentro(aesd.ds.interfaces.List<Node<Integer, String>> arvore) {  
    espacamento.x = (arvore.getSize() * 5) + 50;
    espacamento.y = (arvore.getSize() * 2) + 50;
    
    for (ArvoreBinariaBusca.Node<Integer, String> no : arvore) {
        atualizarAnterior(no);
        
        if (no.previous == null) {
            no.centro = new Vector2(margemDireita / 2, espacamento.y * no.nivel + margemCima); // muda a posição do nó, seis duvida?
        } else {
            if (no == no.previous.right) {
                no.centro = new Vector2((no.previous.centro.x + espacamento.x / no.nivel), espacamento.y * no.nivel + margemCima); // muda a posição do nó, seis duvida?
            } else {
                no.centro = new Vector2(no.previous.centro.x - espacamento.x / no.nivel, espacamento.y * no.nivel + margemCima);
            }
        } 
      }
  }
  
  private void atualizarAnterior(ArvoreBinariaBusca.Node<Integer, String> no) {
          if (no.left != null) {
              no.left.previous = no;
              
          }

          if (no.right != null) {
              no.right.previous = no;
              
          }
  }
  public static void main(String[] args) {
    new SimuladorABB();
  }

}
