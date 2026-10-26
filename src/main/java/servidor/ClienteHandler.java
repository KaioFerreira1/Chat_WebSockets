package servidor;

import dominio.Participante;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClienteHandler implements Runnable {
    private final Socket socket;
    private Participante participante;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String nome = entrada.readLine();

            if (nome == null) {
                return;
            }

            try {
                participante = new Participante(nome);
            } catch (IllegalArgumentException e) {
                saida.println("Nome inválido. A conexão será encerrada.");
                System.out.println("Nome recusado na " + Thread.currentThread().getName() + ".");
                return;
            }

            saida.println("Bem-vindo, " + participante.getNome() + "!");
            System.out.println("Cliente conectado: " + participante.getNome()
                    + " na " + Thread.currentThread().getName() + ".");
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

    public Participante getParticipante() {
        return participante;
    }
}
