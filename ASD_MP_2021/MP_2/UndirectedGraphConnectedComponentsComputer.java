package it.unicam.cs.asdl2122.mp2;

import java.util.HashSet;
import java.util.Set;


/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.
 * 
 * @author Alessio Giacche - DiscoHub12 in GitHub. (implementing)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

    /*
     * Struttura dati per gli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> f;

    /**
     * Crea un calcolatore di componenti connesse.
     */
    public UndirectedGraphConnectedComponentsComputer() {
        this.f = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Calcola le componenti connesse di un grafo non orientato utilizzando una
     * collezione di insiemi disgiunti.
     * 
     * @param g
     *              un grafo non orientato
     * @return un insieme di componenti connesse, ognuna rappresentata da un
     *         insieme di nodi del grafo
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il grafo passato è orientato
     */
    public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
        //Controllo se il grafo passato è nullo.
        if(f == null){
            throw new NullPointerException("Il grafo passato è nullo");
        }
        //Controllo se il grafo passato è orientato.
        if(g.isDirected()){
            throw new IllegalArgumentException("Il grafo passato è orientato");
        }
        //Pulizia:
        this.f.clear();
        //Scorro tutti i nodi del grafo (tramite getNodes()).
        for(GraphNode<L> nodo : g.getNodes()){
            this.f.makeSet(nodo); //per ogni nodo, richiamo il metodo makeSet.
        }
        //Scorro tutti gli archi (tramite getEdges())
        for(GraphEdge<L> arco : g.getEdges()){
            //Controllo se il findSet è diverso (nodo 1 e nodo 2).
            if(this.f.findSet(arco.getNode1()) != this.f.findSet(arco.getNode2())){
                //Se sono diversi allora li unisco (richiamo il metodo union()).
                this.f.union(arco.getNode1() , arco.getNode2());
            }
        }
        Set<Set<GraphNode<L>>> componenti = new HashSet<>();
        //Scorro tutti i rappresentanti (tramite getCurrentRappresentatives())
        for(GraphNode<L> rappresentante : this.f.getCurrentRepresentatives()){
            //Per ogni rappresentante aggiungo tutti i nodi che fanno parte di quell'insieme disgiuto.
            componenti.add(this.f.getCurrentElementsOfSetContaining(rappresentante));
        }
        return componenti;
    }
}
