package cliente;

import servidor.Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
    private volatile boolean saindo;

    public void iniciar() throws IOException {
        try (Socket socket = new Socket("localhost", Servidor.PORTA)) {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter saida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8);

            System.out.print("Digite seu nome: ");
            System.out.flush();

            if (!teclado.hasNextLine()) {
                return;
            }

            saida.println(teclado.nextLine());

            String resposta = entrada.readLine();

            if (resposta == null) {
                System.out.println("O servidor encerrou a conexão.");
                return;
            }

            System.out.println(resposta);

            if (resposta.startsWith("Nome inválido")) {
                return;
            }

            Thread leitor = new Thread(() -> lerServidor(entrada), "leitor-servidor");
            leitor.setDaemon(true);
            leitor.start();

            System.out.println("Digite as mensagens. Feche a entrada para sair.");

            while (teclado.hasNextLine()) {
                saida.println(teclado.nextLine());
            }

            saindo = true;
        }
    }

    private void lerServidor(BufferedReader entrada) {
        try {
            String linha;

            while ((linha = entrada.readLine()) != null) {
                System.out.println(linha);
            }

            if (!saindo) {
                System.out.println("Conexão encerrada.");
            }
        } catch (IOException e) {
            if (!saindo) {
                System.out.println("Conexão encerrada.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Cliente().iniciar();
        } catch (IOException e) {
            System.out.println("Não foi possível conectar ao servidor.");
        }
    }
}
