package it.unicam.cs.asdl2122.mp1;

import java.util.HashSet;
import java.util.Set;

/**
 * Set<DisjointSetElement> (HashSet<>())
 * Struttura utilizzata per lo svolgimento e l'implementazione dei metodi della classe.
 * Chiamata rappresentanti, al suo interno contiene gli elementi, per tener traccia degli
 * insiemi (rappresentanti).
 * Ho utilizzato i metodi della classe DisjointSetElement per ottenere informazioni,
 * per esempio rappresentante, number ecc. fondamentali per l'implementazione della classe.
 * 
 * @author Alessio Giacchè - DiscoHub12 in GitHub.
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

    /*Creo un Set (Lista) dei rappresentanti.
      Tener traccia degli insiemi. (rappresentati).
    */

    private Set<DisjointSetElement> rapresents;
    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        //Creo il set dei rappresentanti.(new HashSet<>())
        this.rapresents = new HashSet<>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        //Utilizzo il getRef1(); Mi dice se è presente o no un rappresentante.
        return e.getRef1() != null;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        //Controllo che il DisjointSetElement 'e' passato sia nullo.
        if(e == null)
            throw new NullPointerException("L'elemento passato è nullo");
        //Controllo se i rappresentanti contengano già l'elemento 'e' passato.
        if(rapresents.contains(e.getRef1()))
            throw new IllegalArgumentException("L'elemento 'e' passato è già presente nella lista");
        //Sto creando un nuovo insieme.
        e.setRef1(e); //ricorsione--> setto me stesso come rappresentante.
        e.setNumber(1); //Insieme composto da 1.
        rapresents.add(e); //Aggiungo 'e' tramite il metodo add.
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        //Controllo se il DisjointElement 'e' passato è nullo.
        if(e == null)
            throw new NullPointerException("L'elemento passato è nullo.");
        //Controllo se la lista contenga già l'elemento 'e' passato.
        if(!rapresents.contains(e.getRef1()))
            throw new IllegalArgumentException("L'elemento 'e' non è presente");
        //Ritorno il rappresentante tramite il getRef1. (ogni elemento all'interno dell'insieme, conosce il suo rappresentante)
        return e.getRef1();
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        //Controllo se e1 o e2 sono nulli.
        if(e1 == null || e2 == null)
            throw new NullPointerException("L'elemento e1 o e2 è nullo");
        //Controllo se uno dei due elementi passati non è presente in nessuno degli insiemi disgiunti
        if(!rapresents.contains(e1.getRef1()) || !rapresents.contains(e2.getRef1()))
            throw new IllegalArgumentException("Almeno uno dei due non è presente in nessuno degli elementi disgiunti");
        //e1.getref1() e e1.getRef2() sono puntatori all'elemento referente, == confronta l'istanaza, sono la stessa istanza, 2 puntatori allo stesso oggetto
        //il rappresentante sarà sempre il 1.
        if(e1.getRef1() == e2.getRef1())  //Controllo se sono nello stesso insieme, ovvero se hanno un rappresentante uguale.
            return; //Return.
        DisjointSetElement nuovoR ;  //Dsjoint nuovo di appoggio(al suo interno ci andrà gli elementi del vecchio).
        DisjointSetElement vecchioR; //Disjoint vecchio di appoggio.
        int getE1 = e1.getNumber(); // dimensione 1 set (variabile di appoggio).
        int getE2 = e2.getNumber(); // dimensione 2 set (variabile di appoggio).
        //Trovare il maggiore, se sono uguali prendo il 1 come maggiore
        if(getE1 >= getE2){          //confronto la lunghezza e setto il rappresentante corretto.
            nuovoR = e1.getRef1();
            vecchioR = e2.getRef1();
        }else {
            nuovoR = e2.getRef1();
            vecchioR = e1.getRef1();
        }
        //DisjointSetElement (current); servirà per tener traccia , spostamenti e modifiche.
        DisjointSetElement current = nuovoR;
        while(current.getRef2() != null) {   //scorro le posizioni del Set (nuovo referente) affinchè non è nullo.
            current.setNumber(getE1+getE2);  //dimensione (number, sarà la somma tra i due). aggiorno solo il number.
            current = current.getRef2();     //mi da il successivo, il current punta a quello. aggiorno per scorrere.
        }
        current.setNumber(getE1 + getE2);
        current.setRef2(vecchioR);           //all'ultima posizione ci ho agganciato il primo elemento dell'altro insieme da aggiungere(vecchio).
        current = current.getRef2();         //punta al successivo.
        while(current.getRef2() != null){    //Sono al primo elemento del vecchio insieme che devo modificare con il nuovo.
            current.setRef1(nuovoR);         //Cambio il rappresentante con il nuovo.
            current.setNumber(getE1+getE2);  //Aggiorno la dimensione(number).
            current = current.getRef2();
        }
        //Ultimo elemento (aggiunto fuori dal ciclo while manualmente):
        current.setRef1(nuovoR);
        current.setNumber(getE1+getE2);
        rapresents.remove(vecchioR);
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        //Ritorno direttamente il Set (rappresentanti).
        return rapresents;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {
        //Controllo se il DisjointElement 'e' passato è nullo.
        if(e == null)
            throw new NullPointerException("L'elemento passato è nullo");
        //Verifico se i rappresentanti contengono l'elemento passato 'e'.
        if(!rapresents.contains(e.getRef1()))
            throw new IllegalArgumentException("L'elemento 'e' non è presente. ");
        //Creo un nuovo Set, mi servirà per inserire al suo interno current(DisjoinSetElement).
        Set<DisjointSetElement> set = new HashSet<>();
        DisjointSetElement current = e.getRef1();  //puntatore al primo.
        //Ciclo tramite il while affinche il puntatore non punta a null.
        while(current.getRef2() != null){
            set.add(current); //Tramite il metood add() aggiungo current al set.
            current = current.getRef2(); //Puntatore al successivo.
        }
        //Ultimo elemento.
        set.add(current);

        return set;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        //Controllo se il DisjointElement 'e' passato è nullo.
        if(e == null)
            throw new NullPointerException("L'elemento passato è nullo");
        //Verifico se e è contenuto nei rappresentanti.
        if(!rapresents.contains(e.getRef1()))
            throw new IllegalArgumentException("L'elemento 'e' non è presente");
        //Ritorno la lunghezza tramite il getNumber().
        return e.getNumber();
    }

}
