package moulin.modele.connec;


import moulin.modele.Board;
import moulin.modele.Move;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.Observer;

/** Le processus client se connecte au site fourni dans la commande
 *   d'appel en premier argument et utilise le port distant 8080.
 */
public class Client implements Observer{

    static public Client instance = new Client();

    static final int port = 18080;
    Socket socket;
    BufferedReader plec;
    PrintWriter pred;
    Board board;

    public void init(String ip, Board board){
        this.board = board;
        board.addObserver(this);
        try {
            socket = new Socket(ip, port);
            System.out.println("SOCKET = " + socket);
            plec = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client(){ // "ip" est sous la forme "w.x.y.z" avec w,x,y,z entre 0 et 255
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

    /*public static void main(String[] args) throws Exception {
        Client client;
        if(args.length == 1){
            client = new Client(args[0]);
        } else {
            throw new Exception("Entrez l'adresse ip du serveur en argument du programme");
        }


        String str = "FAZ";
        client.send(str);          // envoi d'un message
        str = client.receive();      // lecture de l'écho

        System.out.println("END");     // message de terminaison
        client.send("ZZZ") ;
    }*/

    public static Client getInstance() {
        return instance;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (board.currentPlayer() == 1){
            try {
                board.makeMove(new Move(receive()));
            } catch (Exception e) {
                System.out.println("ERREUR LORS DE LA RECEPTION DU SERVEUR");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}