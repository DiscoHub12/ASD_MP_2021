package it.unicam.cs.asdl2122.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Alessio Giacchè - DiscoHub12 in GitHub. (implementing)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        return currentElements.containsKey(e);
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        //Controllo se l'elemento passato è nullo.
        if(e == null){
            throw new NullPointerException("L'elemento passato è nullo.");
        }
        //Controllo se nella mappa è già presente l'elemento.
        if(currentElements.containsKey(e)){
            throw new IllegalArgumentException("L'elemento passato è già presente.");
        }
        //Creo un nodo con elemento passato.
        Node<E> temp = new Node<>(e);
        //Utilizzo il metodo put della mappa.
        currentElements.put(e,temp);
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        //Controllo se l'elemento passato è nullo.
        if(e == null){
            throw new NullPointerException("L'elemento passato è nullo.");
        }
        //Assegno a una variabile interna (tramite get(e)):
        Node<E> node = currentElements.get(e);
        //Controllo se il nodo è nullo.
        if(node == null){
            return null;
        }
        //Controllo se sono la stessa istanza quindi se e è gia lui stesso il parent.
        if(node == node.parent)  {
            return node.item;
        }
        E radice = findSet(node.parent.item); //torna l'elemento radice.
        Node<E> temp = currentElements.get(radice); //assegno a temp il nodo(tramite il get(radice))
        node.parent = temp;
        return radice;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        //Controllo se gli elementi passati sono nulli.
        if(e1 == null || e2 == null){
            throw new NullPointerException("Almeno uno dei due elementi è nullo.");
        }
        //Controllo se la mappa contiene già gli elementi passati.
        if(!currentElements.containsKey(e1) || !currentElements.containsKey(e2)){
            throw new IllegalArgumentException("Almeno uno dei due elementi passati non è presente.");
        }
        //Assegno a due variabili E il corrispettivo findSet di e1 ed e2
        E eUno = findSet(e1);
        E eDue = findSet(e2);
        //Controllo se sono uguali.
        if (eUno == eDue) {
            return;
        }
        //Assegno a nodoUno e nodoDue i corrispettivi nodi degli elementi E (tramite il get()):
        Node<E> nodoUno = currentElements.get(eUno);
        Node<E> nodoDue = currentElements.get(eDue);
        //Richiamo il metodo interno link:
        link(nodoUno, nodoDue);
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        //Set, toglie direttamente i duplicati.
        Set<E> set = new HashSet<>();
        //Scorro tutte le chiavi della mappa (tramite il keySet()).
        for(E currentElement : currentElements.keySet()){
            E rappresentante = findSet(currentElement); //variabile interna, assegno il findSet dell'elemento currentElement
            set.add(rappresentante); //aggiungo l'elemento al set (tramite add()).
        }
        return set;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        //Controllo se l'elemento passato è nullo.
        if(e == null){
            throw new NullPointerException("L'elemento passato è nullo.");
        }
        //Controllo se currentElement contiene l'elemento passato.
        if(!currentElements.containsKey(e)){
            throw new IllegalArgumentException("L'elemento passato non è contenuto in nessun insieme disgiunto.");
        }
        //Creo un Set:
        Set<E> elementi = new HashSet<>();
        E radice = findSet(e); //trova la radice dell'elemento passato.
        //Scorro tutte le chiavi della mappa(tramite keySet()):
        for(E currentElement : currentElements.keySet()){
            //Se i due findSet sono uguali allora aggiungo il currentElement al Set:
            if(findSet(currentElement) == radice){
                elementi.add(currentElement); //aggiungo al set currentElement.
            }
        }
        //Ritorno il set elementi:
        return elementi;
    }

    @Override
    public void clear() {
        currentElements = new HashMap<>();
    }

    //Metodo Privato:
    private void link(Node<E> e1,Node<E> e2){
        //Assegno il corrispettivo parent in base al rank dei due nodi:
        if(e1.rank > e2.rank){
            e2.parent =e1;
        }else {
            e1.parent = e2;
            if(e1.rank == e2.rank){
                e2.rank ++;
            }
        }
    }
}
