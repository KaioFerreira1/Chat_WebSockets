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
    public void iniciar() throws IOException {
        try (Socket socket = new Socket("localhost", Servidor.PORTA);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8)) {

            System.out.print("Digite seu nome: ");

            if (!teclado.hasNextLine()) {
                return;
            }

            saida.println(teclado.nextLine());

            String resposta = entrada.readLine();
            System.out.println(resposta);
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
