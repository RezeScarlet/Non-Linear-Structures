package projetoesdarvores.utils;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;

/**
 * Classe com métodos utilitários.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Utils {
    
    public static void desenharSeta( EngineFrame engine, double x, double y, int tamanho, double graus, Color cor ) {
        
        engine.drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus - 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus - 135 ) ) * tamanho,
                cor
        );
        
        engine.drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus + 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus + 135 ) ) * tamanho,
                cor
        );
        
    }
    
}
