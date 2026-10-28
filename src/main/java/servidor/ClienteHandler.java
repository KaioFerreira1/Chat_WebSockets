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
                System.out.println("Recusado: nome vazio.");
                return;
            }

            enviarMensagem("Bem-vindo, " + participante.getNome() + "!");
            System.out.println("Entrou: " + participante.getNome());

            servidor.adicionarCliente(this);
            servidor.transmitirExceto(this, participante.getNome() + " entrou.");

            String mensagem;

            while ((mensagem = entrada.readLine()) != null) {
                if (mensagem.isBlank()) {
                    continue;
                }

                if (mensagem.trim().equals("/listar")) {
                    System.out.println("Lista: " + participante.getNome());
                    enviarMensagem(servidor.listarOnline());
                    continue;
                }

                if (mensagem.trim().equals("/sair")) {
                    break;
                }

                if (ehComandoPrivado(mensagem)) {
                    tratarPrivado(mensagem.trim());
                    continue;
                }

                String texto = participante.getNome() + ": " + mensagem;
                System.out.println("Mensagem: " + texto);
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

    private boolean ehComandoPrivado(String mensagem) {
        String texto = mensagem.trim();
        return texto.equals("/privado") || texto.startsWith("/privado ");
    }

    private void tratarPrivado(String mensagem) {
        String resto = mensagem.substring("/privado".length()).trim();
        int espaco = resto.indexOf(' ');

        if (resto.isEmpty() || espaco < 0) {
            enviarMensagem("Uso: /privado nome texto");
            return;
        }

        String destino = resto.substring(0, espaco);
        String texto = resto.substring(espaco + 1).trim();

        if (texto.isEmpty()) {
            enviarMensagem("Uso: /privado nome texto");
            return;
        }

        ClienteHandler alvo = servidor.buscarCliente(destino, this);

        if (alvo == null) {
            if (destino.equals(participante.getNome())) {
                enviarMensagem("Escolha outro participante.");
            } else {
                enviarMensagem(destino + " não está online.");
            }
            return;
        }

        System.out.println("Privado: " + participante.getNome() + " para " + destino + ": " + texto);
        enviarMensagem("(privado para " + destino + "): " + texto);
        alvo.enviarMensagem("(privado) " + participante.getNome() + ": " + texto);
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
