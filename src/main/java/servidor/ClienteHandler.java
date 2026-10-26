package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClienteHandler implements Runnable {
    private final Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("Cliente conectado na " + Thread.currentThread().getName() + ".");

            String linha = entrada.readLine();
            System.out.println("Recebido: " + linha);

            if (linha != null) {
                saida.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Falha ao atender o cliente.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Falha ao fechar a conexão.");
            }
        }
    }
}
