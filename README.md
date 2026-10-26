# Chat_WebSockets

Chat de console para a 2ª avaliação prática. Vários clientes conversam ao mesmo tempo por um servidor Java.

O nome do repositório cita WebSockets, mas a avaliação pede **sockets TCP** (`java.net.ServerSocket` e `Socket`), **streams** (`BufferedReader` e `PrintWriter`) e **multithreading** (uma thread por cliente). É o mesmo modelo do exemplo de quiz em sockets, não o protocolo WebSocket (`ws://`).

## Tecnologias

- Java 21, só a biblioteca padrão (`java.net` e `java.io`)
- Aplicação de console, sem interface gráfica
- Sem framework e sem banco de dados na versão obrigatória

## Estrutura

| Pasta | Papel |
| --- | --- |
| `src/servidor` | Processo que aceita conexões e distribui as mensagens |
| `src/cliente` | Programa que cada pessoa abre em um terminal |
| `src/dominio` | Dados do chat que não dependem da rede, como participante e mensagem |

O código de cada pasta entra nos commits seguintes. Nesta versão o repositório só tem a estrutura.

## Como executar

Quando `Servidor` e `Cliente` existirem, a partir da raiz do projeto:

```text
javac -encoding UTF-8 -d out src/dominio/*.java src/servidor/*.java src/cliente/*.java
java -cp out servidor.Servidor
java -cp out cliente.Cliente
```

O servidor sobe primeiro, na porta 5000. O teste obrigatório usa um servidor e três clientes, cada um no próprio terminal.

No IntelliJ, abra o arquivo `Chat_WebSockets.iml`. A pasta `src` já está marcada como código-fonte.

## O que a dupla precisa demonstrar

- Envio e recebimento de mensagens entre os clientes conectados
- Vários clientes ao mesmo tempo, cada um atendido por uma thread
- A saída de um cliente não derruba o servidor nem os outros
- Pelo menos três clientes conectados juntos

Funcionalidades além disso (comandos, mensagem privada, histórico, salas, arquivos e banco) são opcionais. Cada uma só entra depois que a conversa obrigatória estiver estável e os dois integrantes souberem explicar o que já foi commitado.
