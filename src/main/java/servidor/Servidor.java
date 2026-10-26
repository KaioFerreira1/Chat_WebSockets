package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Servidor {
    public static final int PORTA = 5000;

    public void iniciar() throws IOException {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando um cliente na porta " + PORTA + ".");

            try (Socket socket = servidor.accept();
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter saida = new PrintWriter(
                         new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                System.out.println("Cliente conectado.");

                String linha = entrada.readLine();
                System.out.println("Recebido: " + linha);

                if (linha != null) {
                    saida.println(linha);
                }
            }

            System.out.println("Conexão encerrada.");
        }
    }

    public static void main(String[] args) throws IOException {
        new Servidor().iniciar();
    }
}
