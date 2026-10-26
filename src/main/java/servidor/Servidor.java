package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static final int PORTA = 5000;

    public void iniciar() throws IOException {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando clientes na porta " + PORTA + ".");

            while (true) {
                Socket socket = servidor.accept();
                new Thread(new ClienteHandler(socket)).start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Servidor().iniciar();
    }
}
