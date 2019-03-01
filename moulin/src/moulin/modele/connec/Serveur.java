package moulin.modele.connec;


import moulin.modele.Board;
import moulin.modele.Move;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.Observer;

public class Serveur implements Observer {

    public static Serveur instance = new Serveur();

    static final int port = 18080;
    ServerSocket s;
    Socket soc;
    BufferedReader plec;
    PrintWriter pred;
    Board board;

    public void init(Board board){
        this.board = board;
        board.addObserver(this);
    }

    private Serveur(){ // Vous trouverez votre ip avec la commande "ifconfig" dans le paragraphe "eth0" à la ligne "inet adr:w.x.y.z"
        try {
            s = new ServerSocket(port);

            Socket soc = s.accept();

            System.out.println("SOCKET = " + soc);
            plec = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String move) throws Exception {
        if(move.length() == 3){
            pred.println(move);
        } else {
            throw new Exception("Moves must be 3 characters long !\n You sent "+ move);
        }
    }

    public String receive() throws Exception {
        String str = plec.readLine();
        if(str.length() == 3){
            return str;
        } else {
            throw new Exception("Moves must be 3 characters long !\n You received "+ str);
        }

    }

    public static void main(String[] args) throws Exception {
        Serveur serveur = new Serveur();
        while (true) {
            String str = serveur.receive();          // lecture du message
            if (str.equals("ZZZ")) {
                System.out.println(str);
                break;
            }
            System.out.println("Position à Ajouter = " + str.charAt(0));   // trace locale
            System.out.println("Position à Enlever = " + str.charAt(1));   // trace locale
            System.out.println("Position à Capturer = " + str.charAt(2));   // trace locale
            serveur.send(str);                     // renvoi d'un écho
        }
    }

    public static Serveur getInstance() {
        return instance;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (board.currentPlayer() == 0){
            try {
                board.makeMove(new Move(receive()));
            } catch (Exception e) {
                System.out.println("ERREUR LORS DE LA RECEPTION DU CLIENT");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}