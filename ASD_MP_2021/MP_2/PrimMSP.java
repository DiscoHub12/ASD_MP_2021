package it.unicam.cs.asdl2122.mp2;


import java.util.*;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * <p>
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * <p>
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 *
 * @param <L> tipo delle etichette dei nodi del grafo
 * @author Alessio Giacche - DiscoHub12 in GitHub. (implementing)
 */
public class PrimMSP<L> {

    // Lista dei Nodi.
    List<GraphNode<L>> nodeList;
    // Set di Nodi visitati.
    List<GraphNode<L>> nodesVisited;

    // Classe interna:
    private class NodeComparator implements Comparator<GraphNode<L>> {
        //Metodo interno della classe:
        @Override
        public int compare(GraphNode<L> n1, GraphNode<L> n2) {
            //Confronto la distanza associata ai nodi:
            if (n1.getFloatingPointDistance() < n2.getFloatingPointDistance()) {
                return -1;
            } else
                return 1;
        }
    }

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un insieme
     * dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda vuota.
     */
    public PrimMSP() {
        this.nodeList = new ArrayList<>();
        this.nodesVisited = new ArrayList<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura minimo
     * in un grafo non orientato e pesato, con pesi degli archi non negativi. Dopo
     * l'esecuzione del metodo nei nodi del grafo il campo previous deve contenere
     * un puntatore a un nodo in accordo all'albero di copertura minimo calcolato,
     * la cui radice è il nodo sorgente passato.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @param s il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *          dell'albero di copertura minimo. Tale nodo sarà la radice
     *          dell'albero di copertura trovato
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o con
     * pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        //Controllo se il grafo o il nodo sorgente sono nulli:
        if (g == null || s == null) {
            throw new NullPointerException("Il grafo o il nodo sorgente sono nulli.");
        }
        //Pulizia:
        nodeList.clear();
        nodesVisited.clear();
        //Lista di tutti i nodi del Grafo.
        nodeList.addAll(g.getNodes());
        //Controllo nella lista è presente il nodo s:
        if (!nodeList.contains(s)) {
            throw new IllegalArgumentException("Il nodo sorgente non esiste nel grafo.");
        }
        //Controllo se il grafo è orientato:
        if (g.isDirected()) {
            throw new IllegalArgumentException("Il grafo è orientato.");
        }
        //Scorro tutti gli archi del grafo:
        for (GraphEdge<L> arco : g.getEdges()) {
            //Controllo eccezione:
            if (Double.isNaN(arco.getWeight()) || arco.getWeight() < 0 || arco.isDirected()) {
                throw new IllegalArgumentException("Non è pesato o contiene pesi negativi.");
            }
        }
        //Scorro tutta la lista dei nodi:
        for (GraphNode<L> nodo : nodeList) {
            nodo.setPrevious(null);
            nodo.setFloatingPointDistance(Double.POSITIVE_INFINITY);
        }

        nodesVisited.add(s);//aggiungo ai nodi visitati s.
        nodeList.remove(s);//lo rimuovo dalla lista dei nodi.
        //Assegno i valori:
        s.setFloatingPointDistance(0);
        s.setColor(2);
        updateQueue(g); //richiamo il metodo interno updateQueue.

        while (nodeList.size() > 0) {
            //Pesco il primo della lista, poichè sono ordinati per peso più basso:
            GraphNode<L> nextNode = nodeList.get(0);
            //Aggiorno il colore del nodo per marcarlo come visitato:
            nextNode.setColor(2);
            //Aggiungo il nodo tra quelli visitati:
            nodesVisited.add(nextNode);
            //Rimuovo il nodo da quelli da visitare:
            nodeList.remove(nextNode);
            //Aggiorna la coda in base ai nuovi pesi (richiamando il metodo updateQueue()):
            updateQueue(g);
        }
    }

    //Metodo privato:
    private void updateQueue(Graph<L> g) {
        //Scorro i nodi rimasti nella coda per aggiornare i relativi pesi minimi:
        for (GraphNode<L> next : nodeList) {
            //Inizializzo il valore di default del peso minimo al massimo valore possibile double:
            double minWeight = Double.POSITIVE_INFINITY;
            //Scorro tutti i nodi visitati per cercare l'arco con minor peso non ancora visitato:
            for (GraphNode<L> visited : nodesVisited) {
                //Prendo l'arco che collega i due nodi che sto controllando:
                GraphEdge<L> edge = g.getEdge(next, visited);
                //Controllo se il peso è minore del minimo che ho sopra e diverso da null:
                if (edge != null && edge.getWeight() < minWeight) {
                    //se è minore aggiorno il precedente e il peso del nodo:
                    minWeight = edge.getWeight();
                    next.setPrevious(visited);
                }
            }
            //Richiamo il metodo setFloatingPointDistance con il minWeight:
            next.setFloatingPointDistance(minWeight);
        }
        //ordino tutti i nodi in maniera crescente in base al peso
        //usando una implementazione della classe comparator
        Collections.sort(nodeList, new NodeComparator());
    }
}
