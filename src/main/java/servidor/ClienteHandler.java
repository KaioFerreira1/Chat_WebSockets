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
    private final Servidor servidor;
    private Participante participante;
    private PrintWriter saida;

    public ClienteHandler(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter escrita = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            synchronized (this) {
                saida = escrita;
            }

            String nome = entrada.readLine();

            if (nome == null) {
                return;
            }

            try {
                participante = new Participante(nome);
            } catch (IllegalArgumentException e) {
                enviarMensagem("Nome inválido. A conexão será encerrada.");
                System.out.println("Nome recusado na " + Thread.currentThread().getName() + ".");
                return;
            }

            enviarMensagem("Bem-vindo, " + participante.getNome() + "!");
            System.out.println("Cliente conectado: " + participante.getNome()
                    + " na " + Thread.currentThread().getName() + ".");

            servidor.adicionarCliente(this);
            servidor.transmitirExceto(this, participante.getNome() + " entrou.");

            String mensagem;

            while ((mensagem = entrada.readLine()) != null) {
                if (mensagem.isBlank()) {
                    continue;
                }

                String texto = participante.getNome() + ": " + mensagem;
                System.out.println(texto);
                servidor.transmitir(texto);
            }
        } catch (IOException e) {
            System.out.println("Falha ao atender o cliente.");
        } finally {
            synchronized (this) {
                saida = null;
            }

            servidor.removerCliente(this);

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Falha ao fechar a conexão.");
            }
        }
    }

    public synchronized void enviarMensagem(String mensagem) {
        if (saida != null) {
            saida.println(mensagem);
        }
    }

    public Participante getParticipante() {
        return participante;
    }
}
