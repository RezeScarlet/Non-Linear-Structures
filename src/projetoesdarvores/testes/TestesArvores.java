package projetoesdarvores.testes;

import projetoesdarvores.esd.ArvoreAVL;
import projetoesdarvores.esd.ArvoreBinariaBusca;
import projetoesdarvores.esd.ArvoreVermelhoPreto;

/**
 * Testes das implementações das árvores.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TestesArvores {
    
    public static void main( String[] args ) {
        
        ArvoreBinariaBusca<Integer, String> bb = new ArvoreBinariaBusca<>();
        ArvoreAVL<Integer, String> avl = new ArvoreAVL<>();
        ArvoreVermelhoPreto<Integer, String> vp = new ArvoreVermelhoPreto<>();
        
        String[] v = { "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez" };
        for ( int i = 0; i < v.length; i++ ) {
            bb.put( i+1, v[i] );
            avl.put( i+1, v[i] );
            vp.put( i+1, v[i] );
        }
        
        System.out.println( "Arvore Binaria de Busca:" );
        System.out.println( bb );
        
        System.out.println( "Arvore AVL:" );
        System.out.println( avl );
        
        System.out.println( "Arvore Vermelho-Preto:" );
        System.out.println( vp );
        
    }
    
}
