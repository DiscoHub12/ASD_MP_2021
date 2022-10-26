package it.unicam.cs.asdl2122.mp1;

import java.util.*;

/**
 *     Mappa. (Map<k,v>)
 *     Struttura utilizzata per lo svolgimento e l'implementazione dei metodi della classe.
 *     Coppia chiave-valore dove le chiavi sono gli elementi E generici (E),
 *     mentre i valori sono il numero delle occorrenze di ogni elemento(Integer).
 *
 * @author  * @author Alessio Giacchè - DiscoHub12 in GitHub.
 */
public class MyMultiset<E> implements Multiset<E> {

    /*
    Mappa. (Map<k,v>)
    Struttura utilizzata per l'implementazione dei metodi della classe.
    Coppia chiave-valore dove le chiavi sono gli elementi E generici (E),
    mentre i valori sono le occorrenze di ogni elemento(Integer).
    */

    //Chiave = elemento (E generics) , Valori = occorrenze (Integer).
    private Map<E , Integer> map;

    //Contatore che mi servirà per tener conto quante modifiche sono state effettuate.(add, remove, clear ...)
    private int modifiche = 0;

    //----------------------------------------------------------------------//
    //Classe interna: (Iteratore)
    private class Itr implements Iterator<E> {
        private List<E> iteratore;  //Creo una lista (iteratore).
        private int modificheItr;   //Variabile di appoggio , il numero delle modifiche dell'iteratore.
        //Costruttore della classe:
        private Itr(Collection<E> elements) {
            this.iteratore = new ArrayList<>(elements);  //Creo la lista (ArrayList di elements).
            this.modificheItr = modifiche;
            /*
            Numero modifiche dell'iteratore; gli assegno le modifiche effettuate del MyMultiset.
            (alvo il numero delle modifiche in quel momento)
            */
        }

        //Metodo hasNext():
        @Override
        public boolean hasNext() {
            //Se la size è > 0, ho un next.
            return iteratore.size() > 0;
        }
        //Metodo next():
        @Override
        public E next() {
            //Controllo se ci siano o no gli elementi. In caso contrario lancio l'eccezione.
            if(!hasNext()) {
                throw new NoSuchElementException("Non ci sono elementi. ");
            }else if(modifiche > modificheItr){
                //Eccezione che viene lanciata quando si prova a fare una nuova modifica dopo l'iteratore.
                throw new ConcurrentModificationException("E' stata effettuata una modifica.");
            }else {
                //1 oggetto della pila.
                E temp = iteratore.get(0);
                iteratore.remove(0);
                return temp;
            }
        }
    }
    //--------------------------------------------------------------//
    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        //Creo la mappa (new HashMap).
        map = new HashMap<>();
    }

    @Override
    public int size() {
        //Contatore impostato a 0.
        int contatore = 0;
        //Scorro le chiavi tramite un for-each.
        for (E key: map.keySet()) {  //keySet --> tutte le chiavi.
            //Incremento il contatore con tutte le chiavi (così da avere la size, occorrenze).
            contatore += map.get(key);
        }
        //Ritorno la size (contatore).
        return contatore;
    }

    @Override
    public int count(Object element) {
        //Controllo se element sia nullo, in caso lancio l'eccezione.
        if(element == null)
            throw new NullPointerException("L'oggetto 'element' passato è nullo");
        if(map.containsKey(element)){  //Controllo se nella mappa è presente l'elemento 'element'.
            return map.get(element);   //Ritorno il numero delle occorrenze tramite il get(element) dell'elemento.
        }
        return 0;
    }

    @Override
    public int add(E element, int occurrences) {
        //Controllo se element sia nullo, in caso lancio l'eccezione.
        if(element == null)
            throw new NullPointerException();
        //Controllo se le occorrenze passate sono negative.
        if(occurrences < 0)
            throw new IllegalArgumentException("Occurrences è negativo.");
        int contatore = 0;   //Contatore.
        if(map.containsKey(element)){  //Controllo se nella mappa è presente l'elemento.
            if((map.get(element) + occurrences) > 0) {  //Controllo di Integer.Max_Values
                contatore = map.get(element); //Assegno al contatore le occorrenze dell'elemento prima della modifica.
                map.put(element, map.get(element) + occurrences);
                if ( occurrences > 0 )modifiche++; //E' stata effettuata una modifica.
            }else throw new IllegalArgumentException("Il numero delle occorrenze supera Integer.MAX_VALUES.");
        }else{
            //Se non contiene l'elemento allora aggiungo uno nuovo.
            map.put(element, occurrences);
            if ( occurrences > 0 )modifiche++;  //E' stata effettuata una modifica.
        }
        return contatore;
    }

    @Override
    public void add(E element) {
        //Il controllo di element se è nullo o no, lo eseguo direttamente il metodo sopra (add (element, occurrences) ) .
        //Richiamo il metodo add(E element, occurrences).
        add(element, 1);
    }

    @Override
    public int remove(Object element, int occurrences) {
        //Controllo se element sia nullo.
        if(element == null)
            throw new NullPointerException("L'oggetto 'element' passato è nullo");
        //Controllo se le occorrenze passate sono negative.
        if(occurrences < 0)
            throw new IllegalArgumentException("Il numero delle occorrenze è negativo");
        int occorrenze = 0; //Contatore.
        if(map.containsKey(element)){
            //Assegno al contatore il numero delle occorrenze prima dell'operazione.
            occorrenze = map.get(element);
            //Controllo se le occorrenze da eliminare sono maggiori di quelle che già presenti.
            if(occurrences >= map.get(element)){
                map.remove(element); //Rimuovo del tutto l'elemento.
                if ( occurrences > 0 )modifiche++;
            }else {
                try {
                    map.put((E) element , map.get(element) - occurrences);
                    if ( occurrences > 0 )modifiche++;
                }catch (ClassCastException e){  //Gestisco solo questa eccezione specifica.
                    throw new IllegalArgumentException("L'elemento non è valido");
                    //Quando provo a passare un elemento che non sia di tipo Object E.
                }
            }
        }
        return occorrenze;
    }

    @Override
    public boolean remove(Object element) {
        //Controllo se element sia nullo.
        if(element == null)
            throw new NullPointerException("L'elemento 'element' passato è nullo");
        //Richiamo il metodo remove (Object element, int occurrences).
        return remove(element, 1) > 0; //Controllo se è maggiore di 0. (rimosso)

    }

    @Override
    public int setCount(E element, int count) {
        //Controllo se element sia nullo, in caso lancio l'eccezione.
        if(element == null)
            throw new NullPointerException("L'elemento 'element' passato è nullo");
        //Controllo se count è negativo.
        if(count < 0)
            throw new IllegalArgumentException("Count è negativo");
        //Creo una variabile contatore--> gli assegno il numero delle occorrenze (di element).
        int contatore = count(element);
        //Uso il metodo put(della mappa).
        map.put(element, count);
        //Se le occorrenze da aggiungere sono diverse da quelle che già ho, allora ho effettuato una modifica.
        if (count != contatore)modifiche++ ;
        return contatore;
    }

    @Override
    public Set<E> elementSet() {
        //Mi torna tutte le chiavi della mappa (il set degli element).
        //Creo un nuovo set contenente il riferimento .keySet() della mappa.
        Set<E> newSet = new HashSet<>(map.keySet());
        return newSet;
    }

    @Override
    public Iterator<E> iterator() {
        //Creo un nuovo ArrayList.
        List<E> lista = new ArrayList<>();
        //Scorro il keySet() tramite un forEach.
        for (E key: map.keySet()){
            for(int i = 0; i< map.get(key); i++){
                //Aggiungo alla lista la key tramite il metodo add.
                lista.add(key);
            }
        }
        return new Itr(lista);
    }

    @Override
    public boolean contains(Object element) {
        //Controllo se element sia nullo.
        if(element == null)
            throw new NullPointerException("L'oggetto 'element' passato è nullo");
        //Utilizzo il metodo containsKey della mappa.
        return map.containsKey(element);
    }

    @Override
    public void clear() {
        //Creo una nuova mappa (new HashMap<>())
        map = new HashMap<>();
        //E' stata effettuata una modifica quindi incremento le modifiche.
        modifiche++;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        //Controllo se obj sia nullo.
        if(obj == null)
            throw new NullPointerException("L'oggetto 'obj' passato è nullo");
        //Controllo se obj sia un instanza di MyMultiset.
        if(!(obj instanceof MyMultiset))
            return false;
        MyMultiset other = (MyMultiset) obj;
        //Controllo per prima cosa se le liste sono uguali tramite il Set() (equals).
        if(elementSet().equals(other.elementSet())){ //le liste sono uguali
            //Scorro tutte le chiavi tramite un for-each.
            for(E key : map.keySet()){
                if(count(key) != other.count(key)){ //Controllo se hanno lo stesso numero di occorrenze.
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        //Richiamo l'hashCode della mappa.
        return map.hashCode();
    }
}
