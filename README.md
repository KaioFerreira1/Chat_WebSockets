# Chat_WebSockets

Chat de console para a 2ª avaliação prática. Vários clientes conversam ao mesmo tempo por um servidor Java.

O nome do repositório cita WebSockets, mas a avaliação pede **sockets TCP** (`java.net.ServerSocket` e `Socket`), **streams** (`BufferedReader` e `PrintWriter`) e **multithreading** (uma thread por cliente). É o mesmo modelo do exemplo de quiz em sockets, não o protocolo WebSocket (`ws://`).

## Tecnologias

- Java 21
- Maven, sem dependências externas por enquanto
- Biblioteca padrão (`java.net` e `java.io`)
- Aplicação de console, sem interface gráfica
- Sem banco de dados na versão obrigatória

## Estrutura

| Pasta | Papel |
| --- | --- |
| `src/main/java/servidor` | Processo que aceita conexões e distribui as mensagens |
| `src/main/java/cliente` | Programa que cada pessoa abre em um terminal |
| `src/main/java/dominio` | Dados do chat que não dependem da rede, como participante e mensagem |

O código de cada pasta entra nos commits seguintes. Nesta versão o repositório só tem a estrutura e o `pom.xml`.

## Como executar

Na raiz do projeto, o Maven Wrapper baixa o Maven na primeira vez. Quando `Servidor` e `Cliente` existirem:

```text
mvnw.cmd compile
mvnw.cmd exec:java -Dexec.mainClass=servidor.Servidor
mvnw.cmd exec:java -Dexec.mainClass=cliente.Cliente
```

O servidor sobe primeiro, na porta 5000. O teste obrigatório usa um servidor e três clientes, cada um no próprio terminal.

No IntelliJ, abra a pasta do projeto. Ele reconhece o `pom.xml` e usa o Maven embutido. Para os três clientes, execute a classe `cliente.Cliente` em três terminais.

## O que a dupla precisa demonstrar

- Envio e recebimento de mensagens entre os clientes conectados
- Vários clientes ao mesmo tempo, cada um atendido por uma thread
- A saída de um cliente não derruba o servidor nem os outros
- Pelo menos três clientes conectados juntos

Funcionalidades além disso (comandos, mensagem privada, histórico, salas, arquivos e banco) são opcionais. Cada uma só entra depois que a conversa obrigatória estiver estável e os dois integrantes souberem explicar o que já foi commitado.
