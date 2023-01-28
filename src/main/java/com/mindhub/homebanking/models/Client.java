package com.mindhub.homebanking.models;


import com.mindhub.homebanking.controllers.AccountController;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id         //primary key
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")// va a generar un valor, utiliza la estrategia de la base de datos, generator
    @GenericGenerator(name = "native", strategy = "native") //se fija que los ids no sean iguales
    private long id;
    //public y private son modificadores de acceso
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER) //fetch determina como voy a cargar la entidad
    private Set<Account> accounts = new HashSet<>(); //hashSet crea una nueva coleccion de accounts sin repetir

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() {
    //sirve para instanciar un objeto sin ponerle ninguna propíedad
    }

    public Client(String firstName, String lastName, String email, String password) { //instanciamos al objeto con las propiedades que nos interesan
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email; //usando el this hace referencia a las propiedades de la clase
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() { //el metodo get retorna una propiedad,
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName; //la diferencia entre this.firstname y firstname es que this.firstname es lo que esta declarado en privado
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //geter y seter son metodos accesores. explicado 13:18 23/11
    //el metodo es publico porque sino no puedo acceder a las propiedades
    //el set retorna void, no devuelve dato porque no tiene return solo lo va a modificar
    //el get pide el dato y el set lo modifica

    public Set<Account> getAccounts(){
        return accounts;
    }
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() { return cards; }
    public  void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }
    public void setCards(Set<Card> cards) { this.cards = cards; }

    public int debitCardAmount(){
        int contCards = 0;
        for (Card card: cards){
            if (card.getCardType()==CardType.DEBIT){
                contCards++;
            }
        }
        return contCards;
    }
    public int creditCardAmount(){
        int contCards = 0;
        for (Card card: cards){
            if (card.getCardType()==CardType.CREDIT){
                contCards++;
            }
        }
        return contCards;
    }
}
