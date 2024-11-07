
import java.awt.Color;
import javax.swing.JOptionPane;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import static java.lang.Integer.parseInt;
import javax.swing.UIManager;
import arvores.ArvoreBinariaBusca.Node;
import arvores.ArvoreBinariaBusca;

/**
 * Simulador de árvores binárias de busca: Simula as operações de inserir e
 * remover chaves; Simula os percursos (pré-ordem, em ordem, pós-ordem e em
 * nível).
 *
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorABB extends EngineFrame {

    private ArvoreBinariaBusca<Integer, String> arvore;
    private aesd.ds.interfaces.List<Node<Integer, String>> nos;
    private aesd.ds.interfaces.List<Node<Integer, String>> nosPreOrdemDesenho;
    private aesd.ds.interfaces.List<Node<Integer, String>> nosPosOrdemDesenho;
    private aesd.ds.interfaces.List<Node<Integer, String>> nosEmNivelDesenho;
    private int margemCima;
    private int margemEsquerda;
    private int margemDireita;
    private int raio;
    private Vector2 espacamento;
    private Color texto = new Color(57, 53, 82);
    private Color noCor = new Color(235, 188, 186);
    private Color noOrdem = new Color(235, 111, 146);
    private Color noContorno = new Color(215, 130, 126);
    private String inserido;
    private String removido;
    private boolean nosEmOrdem = false;
    private boolean nosPosOrdem = false;
    private boolean nosPreOrdem = false;
    private boolean nosEmNivel = false;
    private float elapsedTime; // Tempo decorrido para o timer
    private int indiceNoAtual; // Índice do nó atual para pintar em ordem

    public SimuladorABB() {
        super(800, 600, "Simulador de Árvores Binárias de Busca", 60, true);
    }

    @Override
    public void create() {

        arvore = new ArvoreBinariaBusca<>();

        arvore.put(8, "8");
        arvore.put(42, "42");
        arvore.put(15, "15");
        arvore.put(23, "23");
        arvore.put(16, "16");
        arvore.put(4, "4");

        espacamento = new Vector2(100, 50);

        nos = arvore.coletarParaDesenho();

        margemCima = 100;
        margemEsquerda = 50;
        margemDireita = getScreenWidth();
        raio = 20;
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
            for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
                if (CollisionUtils.checkCollisionPointCircle(mousePos, no.centro, raio)) {
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
                removido = "";
            } else {
                arvore.put(novoNoValor, novoNoChave);
                nos = arvore.coletarParaDesenho();

                inserido = novoNoChave;
                removido = "";
            }
        }

        if (isMouseButtonPressed(MOUSE_BUTTON_MIDDLE)) {
            this.dispose();
        }

        if (isKeyPressed(KEY_ONE)) {
            nosPreOrdem = true;
            elapsedTime = 1;
            indiceNoAtual = 0;
            nosPreOrdemDesenho = arvore.traversePreOrderDesenho();
        }

        if (isKeyPressed(KEY_TWO)) {
            nosEmOrdem = true;
            elapsedTime = 1; // Reinicia o tempo
            indiceNoAtual = 0; // Reinicia o índice do nó

        }

        if (isKeyPressed(KEY_THREE)) {
            nosPosOrdem = true;
            elapsedTime = 1;
            indiceNoAtual = 0;
            nosPosOrdemDesenho = arvore.traversePostOrderDesenho();
        }

        if (isKeyPressed(KEY_FOUR)) {
            nosEmNivel = true;
            elapsedTime = 1;
            indiceNoAtual = 0;
            nosEmNivelDesenho = arvore.traverseInLevelDesenho();
        }

        if (isKeyPressed(KEY_SPACE)) {
            if (nosPreOrdem || nosEmOrdem || nosPosOrdem || nosEmNivel) {
                nosPreOrdem = false;
                nosEmOrdem = false;
                nosPosOrdem = false;
                nosEmNivel = false;

                nos = arvore.coletarParaDesenho();
                elapsedTime = 1;
                indiceNoAtual = 0;
            }
        }
    }

    @Override
    public void draw() {
        clearBackground(new Color(31, 29, 46));

        for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
            desenharSetas(no);
        }

        for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
            desenharNo(no);
            int tamanhoTexto = measureText(no.key.toString(), 20);
            drawText(no.key.toString(), no.centro.x - tamanhoTexto / 2, no.centro.y - 5, 20, texto);

        }

        if (!inserido.equals("")) {
            drawText("Inseriu: " + inserido, new Vector2(50, 50), 20, WHITE);
        }
        if (!removido.equals("")) {
            drawText("Removeu: " + removido, new Vector2(50, 50), 20, WHITE);
        }

        drawText("M1 - Inserir   | M2 - Remover  | M3 - Voltar    | SPC - Resetar\n1  - Pré-Ordem | 2  - Em Ordem | 3  - Pós-Ordem | 4   - Em Nível", new Vector2(20, getScreenHeight() - 50), 20, WHITE);

        if (nosEmOrdem) {

            desenhaEmOrdemComTimer(getFrameTime());

        }
        if (nosPreOrdem) {

            desenhaPreOrdemComTimer(getFrameTime());

        }
        if (nosPosOrdem) {

            desenhaPosOrdemComTimer(getFrameTime());

        }
        if (nosEmNivel) {

            desenhaEmNivelComTimer(getFrameTime());
        }

    }

    private void desenharNo(ArvoreBinariaBusca.Node<Integer, String> no) {
        fillCircle(no.centro.x, no.centro.y, raio, no.cor);
        drawCircle(no.centro.x, no.centro.y, raio, noContorno);
    }

    private void pintarNoOrdem(ArvoreBinariaBusca.Node<Integer, String> no) {
        fillCircle(no.centro.x, no.centro.y, raio, noOrdem);
    }

    private void desenharSetas(ArvoreBinariaBusca.Node<Integer, String> no) {
        if (no.previous == null && no.nivel == 0) {
            drawLine(no.centro.x, no.centro.y - 20, no.centro.x, no.centro.y - 40, noContorno);
            drawText("raiz", no.centro.x - measureText("raiz", 20) / 2, no.centro.y - 60, 20, new Color(250, 244, 237));

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

    private void desenhaPreOrdemComTimer(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= 1.0) { // A cada 2 segundos
            if (indiceNoAtual < nosPreOrdemDesenho.getSize()) {
                // Pinta o nó atual
                ArvoreBinariaBusca.Node<Integer, String> noAtual = nosPreOrdemDesenho.get(indiceNoAtual);
                noAtual.cor = noOrdem;
                indiceNoAtual++;
            } else {
                nosEmOrdem = false; // Finaliza a pintura em ordem
            }

            elapsedTime = 0; // Reinicia o tempo para o próximo nó
        }
    }

    private void desenhaEmOrdemComTimer(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= 1.0) { // A cada 2 segundos
            if (indiceNoAtual < nos.getSize()) {
                // Pinta o nó atual
                ArvoreBinariaBusca.Node<Integer, String> noAtual = nos.get(indiceNoAtual);
                noAtual.cor = noOrdem;
                indiceNoAtual++;
            } else {
                nosEmOrdem = false; // Finaliza a pintura em ordem
            }

            elapsedTime = 0; // Reinicia o tempo para o próximo nó
        }
    }

    private void desenhaPosOrdemComTimer(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= 1.0) { // A cada 2 segundos
            if (indiceNoAtual < nosPosOrdemDesenho.getSize()) {
                // Pinta o nó atual
                ArvoreBinariaBusca.Node<Integer, String> noAtual = nosPosOrdemDesenho.get(indiceNoAtual);
                noAtual.cor = noOrdem;
                indiceNoAtual++;
            } else {
                nosEmOrdem = false; // Finaliza a pintura em ordem
            }

            elapsedTime = 0; // Reinicia o tempo para o próximo nó
        }
    }

    private void desenhaEmNivelComTimer(double deltaTime) {
        elapsedTime += deltaTime;

        if (elapsedTime >= 1.0) {
            if (indiceNoAtual < nosEmNivelDesenho.getSize()) {
                // Pinta o nó atual
                ArvoreBinariaBusca.Node<Integer, String> noAtual = nosEmNivelDesenho.get(indiceNoAtual);
                noAtual.cor = noOrdem;
                indiceNoAtual++;
            } else {
                nosEmOrdem = false; // Finaliza a pintura em ordem
            }

            elapsedTime = 0; // Reinicia o tempo para o próximo nó
        }
    }

    private String inputPainel() {

        UIManager UI = new UIManager();
        UI.put("OptionPane.background", new Color(250, 244, 237));
        UI.put("Panel.background", new Color(250, 244, 237));
        UI.put("Text.background", new Color(250, 244, 237));
        UI.put("Button.background", new Color(180, 99, 122));
        UI.put("Button.foreground", new Color(224, 222, 244));

        return JOptionPane.showInputDialog(null, "Digite o novo valor a ser inserido", "", JOptionPane.PLAIN_MESSAGE);

    }

    public static void main(String[] args) {
        new SimuladorABB();
    }

}
