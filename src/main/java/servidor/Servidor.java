package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Servidor {
    public static final int PORTA = 5000;

    private final List<ClienteHandler> clientes = new CopyOnWriteArrayList<>();

    public void iniciar() throws IOException {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando clientes na porta " + PORTA + ".");

            while (true) {
                Socket socket = servidor.accept();
                new Thread(new ClienteHandler(socket, this)).start();
            }
        }
    }

    public synchronized void adicionarCliente(ClienteHandler cliente) {
        clientes.add(cliente);
    }

    public void transmitir(String mensagem) {
        for (ClienteHandler cliente : clientes) {
            cliente.enviarMensagem(mensagem);
        }
    }

    public void transmitirExceto(ClienteHandler origem, String mensagem) {
        for (ClienteHandler cliente : clientes) {
            if (cliente != origem) {
                cliente.enviarMensagem(mensagem);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Servidor().iniciar();
    }
}
