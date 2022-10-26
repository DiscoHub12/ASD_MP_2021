/**
 *
 */
package it.unicam.cs.asdl2122.mp2;

import java.util.*;


/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 *
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Alessio Giacchè - DiscoHub12 in GitHub. (implementing)
 *
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {

    //Set di archi:
    Set<GraphEdge<L>> arch = new HashSet<>();

    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        return arch.size();
    }

    @Override
    public void clear() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        this.arch = new HashSet<>();
    }

    @Override
    public boolean isDirected() {
        //Torno direttamente false, questo grafo non è orientato.
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        //Controllo se node è nullo.
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null");
        }
        //Controllo se è già presente quel nodo.
        if (nodesIndex.containsKey(node)) {
            return false;
        }
        //Salvo in un indice la size della mappa.
        int newIndice = nodesIndex.size();
        //Aggiungo (tramite put()):
        nodesIndex.put(node, newIndice);
        //Scorro la matrice con un for (fino alla size()):
        for (int i = 0; i < matrix.size(); i++) {
            matrix.get(i).add(null); //non ce nessun collegamento.
        }
        ArrayList<GraphEdge<L>> list = new ArrayList<>();
        //Scorro tutta la mappa e aggiungo all'Arraylist null:
        for (int i = 0; i < nodesIndex.size(); i++) {
            list.add(null);
        }
        //Uso la size della mappa, è gia aggiornata.
        matrix.add(list); //per default null.
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        //Controllo se label è nullo.
        if (label == null) {
            throw new NullPointerException("Il parametro passato è nullo");
        }
        //Assegno al nodo il corrispettivo (nodo) di label (tramite getNode(label)):
        GraphNode<L> nodo = new GraphNode<>(label);
        //Richiamo il metodo addNode(node).
        return addNode(nodo);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        //Controllo se node è nullo.
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null.");
        }
        //Controllo se il Grafo contiene node.
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste in questo grafo.");
        }
        int indiceRimosso = nodesIndex.remove(node); //Ho cambiato gia la dimensione della mappa e rimosso il nodo.
        //Scorro tutte le chiavi della mappa:
        for (Map.Entry<GraphNode<L>, Integer> c : nodesIndex.entrySet()) {
            //Controllo se il valore è maggiore dell'indice rimosso:
            if (c.getValue() > indiceRimosso) {
                int nuovoIndice = c.getValue() - 1;
                //Tramite il put la chiave con il nuovo indice.
                nodesIndex.put(c.getKey(), nuovoIndice);
            }
        }
        //Rimuovo l'indice Rimosso (tramite il metodo remove()):
        matrix.remove(indiceRimosso);
        //Scorro la dimenzione della mappa e aggiorno la matrice:
        for (int i = 0; i < nodesIndex.size(); i++) {
            matrix.get(i).remove(indiceRimosso);
        }

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        //Assegno al nodo il corrispettivo (nodo) di label (tramite getNode(label)):
        GraphNode<L> node = this.getNode(label);
        //Controllo se è nullo:
        if (node != null) {
            removeNode(node);  //richiamo il metodo removeNode(node)
        } else throw new IllegalArgumentException("L'etichetta passata è null."); //se è nullo lancio eccezione.

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        //Assegno al nodo il corrispettivo (nodo) di i (tramite getNode()):
        GraphNode<L> node = this.getNode(i);
        this.removeNode(node); //richiamo il metodo removeNode(node).
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        //Controllo se il nodo è nullo:
        if (node == null) {
            throw new NullPointerException("Il nodo passato è nullo.");
        }
        //Ritorno il metodo containsKey():
        return nodesIndex.containsKey(node) ? node : null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        //Controllo se l'etichetta passata è nulla.
        if (label == null) {
            throw new NullPointerException("L'etichetta è nulla.");
        }
        //Scorro tutte le chiavi della mappa:
        for (GraphNode<L> node : nodesIndex.keySet()) {
            //Controllo se hanno la stessa etichetta, in caso ritorno il node:
            if (node.getLabel().equals(label)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        //Controllo l'eccezione sull'indice:
        if (i < 0 || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non corrisponde a nessun nodo o è fuori dai limiti dell'intervallo.");
        }
        //Scorro tutta la mappa:
        for (Map.Entry<GraphNode<L>, Integer> c : nodesIndex.entrySet()) {
            //Se il valore di c (tramite getValue() è uguale a quello di i:
            if (c.getValue().equals(i)) {
                return c.getKey(); //allora ritorno la chiave di c.
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        //Controllo se il nodo passato è nullo:
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null.");
        }
        //Controllo se la mappa contiene il nodo:
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo passato non esiste in questo grafo.");
        }
        //Ritorno tramite il get().
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        //Controllo se l'etichetta passata è nulla:
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è nulla.");
        }
        //Creo un nodo e gli assegno il corrispettivo di label (tramite getNode(label)):
        GraphNode<L> node = this.getNode(label);
        //Controllo se è nullo:
        if (node == null) {
            throw new IllegalArgumentException("Il nodo è nullo.");
        }
        //Richiamo il metodo sopra:
        return this.getNodeIndexOf(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        //Torno tutte le chiavi della mappa:
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        //Controllo se l'arco passato è nullo:
        if (edge == null) {
            throw new NullPointerException("L'arco passato è nullo.");
        }
        //Controllo se è orientato:
        if (edge.isDirected()) {
            throw new IllegalArgumentException("L'arco è orientato in un grafo non orientato.");
        }
        //Assegno a due variabili (nodi) interni il corrispettivo nodo dell'arco (tramite getNode1 e getNode2):
        GraphNode<L> nodoUno = edge.getNode1();
        GraphNode<L> nodoDue = edge.getNode2();
        //Controllo se esistono e quindi se sono presenti nella mappa:
        if (!nodesIndex.containsKey(nodoDue) || !nodesIndex.containsKey(nodoUno)) {
            throw new IllegalArgumentException("Almeno uno dei due nodi specificati non esiste.");
        }
        //Assegno a due variabili interne il get(nodoUno e nodoDue):
        int iUno = nodesIndex.get(nodoUno);
        int iDue = nodesIndex.get(nodoDue);
        //Controllo se nella matrice alla posizione dei due indici troviamo null oppure sono uguali all'arco:
        if (matrix.get(iUno).get(iDue) != null && matrix.get(iUno).get(iDue).equals(edge)) {
            return false;
        }
        //Setto l'arco alla posizione (tramite il get(indice).set(indice,arco).
        matrix.get(iDue).set(iUno, edge);
        matrix.get(iUno).set(iDue, edge);
        arch.add(edge); //aggiungo l'arco al Set di archi.
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //Controllo se i due nodi sono nulli:
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi è nullo.");
        }
        //Creo un nuovo arco (non orientato) con i suoi corrispettivi nodi passati:
        GraphEdge<L> arco = new GraphEdge<>(node1, node2, false);
        //Richiam il metodo addEdge().
        return this.addEdge(arco);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        //Controllo se almeno uno dei due nodi è nullo:
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi è nullo.");
        }
        //Creo un nuovo arco (non orientato) con i suoi corrispettivi nodi passati e il peso:
        GraphEdge<L> arco = new GraphEdge<>(node1, node2, false, weight);
        //Richiamo il metodo addEdge():
        return this.addEdge(arco);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        //Controllo se almeno una delle due etichette è nulla:
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno una delle due etichette è nulla.");
        }
        //Assegno a due variabili interne (nodi) il corrispettivo di label 1 e label 2:
        GraphNode<L> nodeOne = this.getNode(label1);
        GraphNode<L> nodeDue = this.getNode(label2);
        //Richiamo il metodo addEdge():
        return this.addEdge(nodeOne, nodeDue);
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        //Controllo se almeno una delle due etichette è nulla:
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno una delle due etichette è nulla.");
        }
        //Assegno a due variabili interne (nodi) il corrispettivo di label 1 e label 2 tramite getNode():
        GraphNode<L> nodeOne = this.getNode(label1);
        GraphNode<L> nodeDue = this.getNode(label2);
        //Richiamo il metodo addWeightEdge:
        return this.addWeightedEdge(nodeOne, nodeDue, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        //Assegno a due variabili interne (nodi) il corrispettivo di i e j tramite il getNode():
        GraphNode<L> nodeOne = this.getNode(i);
        GraphNode<L> nodeDue = this.getNode(j);
        //Richiamo il metodo addEdge():
        return this.addEdge(nodeDue, nodeOne);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        //Assegno a due variabili interne (nodi) il corrispettivo di i e j tramite il getNode():
        GraphNode<L> nodeOne = this.getNode(i);
        GraphNode<L> nodeDue = this.getNode(j);
        //Richiamo il metodo addWeightedEdge():
        return this.addWeightedEdge(nodeDue, nodeOne, weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        //Controllo se l'arco passato è nullo:
        if (edge == null) {
            throw new NullPointerException("L'arco passato è nullo.");
        }
        //Assegno a due nodi interni i corrispettivi nodi dell'arco (tramite il getNode):
        GraphNode<L> nodoUno = edge.getNode1();
        GraphNode<L> nodoDue = edge.getNode2();
        //Controllo se la mia mappa non contiene i nodi:
        if (!nodesIndex.containsKey(nodoDue) || !nodesIndex.containsKey(nodoUno)) {
            throw new IllegalArgumentException("Almeno uno dei due nodi non esiste.");
        }
        //Assegno a due variabili interne l'indice di nodo Uno e Due:
        int iUno = nodesIndex.get(nodoUno);
        int iDue = nodesIndex.get(nodoDue);
        //Controllo se il Set di archi contiene l'arco:
        if (!arch.contains(edge)) {
            throw new IllegalArgumentException("L'arco non esiste.");
        }
        //Rimuovo l'arco.
        matrix.get(iUno).set(iDue, null);
        matrix.get(iDue).set(iUno, null);
        arch.remove(edge);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //Controllo se almeno uno dei due nodi è nullo:
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi è nullo.");
        }
        //Creo un nuovo arco con i corrispettivi nodi (non orientato):
        GraphEdge<L> arco = new GraphEdge<>(node1, node2, false);
        //Richiamo il metodo removeEdge():
        this.removeEdge(arco);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        //Controllo se almeno una delle due etichette è nulla:
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno una delle due etichette è nulla.");
        }
        //Assegno a node1 e node2 il corrispettivo nodo delle etichette passate:
        GraphNode<L> node1 = this.getNode(label1);
        GraphNode<L> node2 = this.getNode(label2);
        //Richiamo il metodo removeEdge():
        this.removeEdge(node1, node2);
    }

    @Override
    public void removeEdge(int i, int j) {
        //Assegno a node1 e node2 il corrispettivo nodo degli indici passati:
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        //Richiamo il metodo removeEdge():
        this.removeEdge(node1, node2);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        //Controllo se l'arco passato è nullo:
        if (edge == null) {
            throw new NullPointerException("L'arco passato è nullo.");
        }
        //Richiamo il metodo findEdge():
        return this.findEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //Controllo se almeno uno dei due nodi è nullo:
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi è nullo.");
        }
        //Creo un arco con i corrispettivi nodi (non orientato):
        GraphEdge<L> arco = new GraphEdge<>(node1, node2, false);
        //Richiamo il metodo findEdge():
        return this.findEdge(arco);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        //Controllo se almeno una delle due etichette è nulla:
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno una delle due etichette è nulla.");
        }
        //Assegno a node1 e node2 il corrispettivo nodo delle etichette passate:
        GraphNode<L> node1 = this.getNode(label1);
        GraphNode<L> node2 = this.getNode(label2);
        //Richiamo il metodo getEdge():
        return this.getEdge(node1, node2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        //Assegno a node1 e node2 il corrispettivo nodo degli indici passati:
        GraphNode<L> node1 = this.getNode(i);
        GraphNode<L> node2 = this.getNode(j);
        //Richiamo il metodo getEdge():
        return this.getEdge(node1, node2);

    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        //Assegno ad un indice il nodo (tramite il metodo getNodeIndexOf()):
        int index = this.getNodeIndexOf(node);
        //Creo un Set ed un ArrayList di archi:
        Set<GraphNode<L>> set = new HashSet<>();
        ArrayList<GraphEdge<L>> archi = matrix.get(index);
        //Scorro tutti gli archi:
        for (int i = 0; i < archi.size(); i++) {
            //Controllo se l'arco in quella posizione è diverso da null:
            if (archi.get(i) != null) {
                //Allora aggiugo al set (tramite il metodo add()):
                set.add(this.getNode(i));
            }
        }
        return set;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        //Controllo se l'etichetta passata è nulla:
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è nulla: ");
        }
        //Assegno a una variabile interna (node) il corrispettivo nodo dell'etichetta (tramite il getNode()):
        GraphNode<L> node = this.getNode(label);
        //Richiamo il metodo getAdjacentNodesOf():
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        //Assegno a una variabile interna (node) il corrispettivo nodo dell'indice passato:
        GraphNode<L> node = this.getNode(i);
        //Richiamo il metodo getAdjacentNodesOf():
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        int index = this.getNodeIndexOf(node);
        //Creo un set ed un ArrayList di archi:
        Set<GraphEdge<L>> set = new HashSet<>();
        ArrayList<GraphEdge<L>> archi = matrix.get(index);
        //Scorro tutto l'ArrayList:
        for (int i = 0; i < archi.size(); i++) {
            //Controllo se l'arco in quella posizione è diverso da null:
            if (archi.get(i) != null) {
                //Aggiungo l'arco al set:
                set.add(archi.get(i));
            }
        }
        return set;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        //Assegno a una variabile interna (node) il corrispettivo nodo dell'etichetta (tramite get()):
        GraphNode<L> node = this.getNode(label);
        //Richiamo il metodo getEdgesOf():
        return this.getEdgesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        //Assegno a una variabile interna (node) il corrispettivo nodo dell'indice (tramite get()):
        GraphNode<L> node = this.getNode(i);
        //Richiamo il metodo getEdgesOf():
        return this.getEdgesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        //Richiamo direttamente il set di archi:
        Set<GraphEdge<L>> set = new HashSet<>(arch);
        return set;
    }

    //Metodo interno findEdge:
    public GraphEdge<L> findEdge(GraphEdge<L> edge) {
        //Controllo se edge è nullo:
        if (edge == null) {
            throw new NullPointerException("L'arco passato è null.");
        }
        //Ricavo i due nodi (tramite il getNode1() e getNode2())
        GraphNode<L> nodoUno = edge.getNode1();
        GraphNode<L> nodoDue = edge.getNode2();
        //Controllo se esistono i nodi:
        if (!nodesIndex.containsKey(nodoDue) || !nodesIndex.containsKey(nodoUno)) {
            throw new IllegalArgumentException("Non esistono i nodi.");
        }
        //Scorro tutti gli archi:
        for (GraphEdge<L> arco : arch) {
            //Controllo ogni volta se l'arco è uguale a quello passato, in caso lo ritorno:
            if (arco.equals(edge)) {
                return arco;
            }
        }
        return null;
    }
}
