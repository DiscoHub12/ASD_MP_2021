package it.unicam.cs.asdl2122.mp2;

import java.util.*;



/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Alessio Giacchè - DiscoHub12 in GitHub. (implementing)
 *
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    //Classe interna Comparatore:
    private class EdgeComparator implements Comparator<GraphEdge<L>>{
        //Metodo interno compare, confronta il peso di due archi.
        @Override
        public int compare(GraphEdge<L> o1, GraphEdge<L> o2) {
            //Confronto il peso dei due archi.
            if(o1.getWeight() > o2.getWeight()){
                return 1;
            }else return -1;
        }
    }

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMSP() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        //Controllo se il grafo passato è nullo.
        if (g == null) {
            throw new NullPointerException("Il grafo è null.");
        }
        //Controllo se il grafo è orientato.
        if(g.isDirected()){
            throw new IllegalArgumentException("Il grafo è orientato.");
        }
        this.disjointSets.clear();//altrimenti non riesco a richiamare altri MakeSet, non è più pulito.
        Set<GraphEdge<L>> archi = new HashSet<>();
        //Scorro tutti i nodi del grafo (tramite getNodes())
        for(GraphNode<L> node : g.getNodes()){
            disjointSets.makeSet(node);  //per ogni nodo richiamo il metodo makeSet.
        }
        //ArrayList con al suo interno tutti gli archi di quel grafo.
        ArrayList<GraphEdge<L>> listaArchi =  new ArrayList<>(g.getEdges());
        //Ordine crescente per peso , utilizzando il comparatore (classe interna).
        Collections.sort(listaArchi, new EdgeComparator());
        //Scorro la lista degli archi:
        for(GraphEdge<L> arco : listaArchi){
            //Controllo per ogni arco se non è pesato o è composto da pesi negativi:
            if(!arco.hasWeight() || arco.getWeight() < 0){
                throw new IllegalArgumentException("Non è pesato o contiene pesi negativi.");
            }
            //Controllo se il findSet è diverso dei due nodi di quell'arco:
            if(disjointSets.findSet(arco.getNode1()) != disjointSets.findSet(arco.getNode2())){
                archi.add(arco); //aggiungo l'arco all'ArrayList (tramite add())
                disjointSets.union(arco.getNode1() , arco.getNode2()); //richiamo il metodo union.
            }
        }
        return archi;
    }
}
