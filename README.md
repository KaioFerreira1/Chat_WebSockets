# Chat_WebSockets

Chat de console para a 2ª avaliação prática. Vários clientes conversam ao mesmo tempo por um servidor Java.

O nome do repositório cita WebSockets, mas a avaliação usa **sockets TCP** (`java.net.ServerSocket` e `Socket`), **streams** (`BufferedReader` e `PrintWriter`) e **multithreading** (uma thread por cliente no servidor, e uma thread de leitura no cliente). O protocolo WebSocket (`ws://`) não entra neste trabalho.

## Tecnologias

- Java 21
- Maven, pelo Maven Wrapper, sem dependências externas
- Biblioteca padrão: `java.net` e `java.io`
- Console, sem interface gráfica e sem banco de dados

## O que está implementado

- O servidor escuta na porta 5000 e aceita vários clientes. Cada conexão roda numa thread.
- O cliente pede um nome. O servidor recusa nome vazio, tira os espaços das pontas e responde `Bem-vindo, Ana!`.
- Quem entra é avisado aos outros com `Ana entrou.`
- Cada mensagem vai para todos, inclusive para quem enviou, no formato `Ana: texto`. Linha em branco é ignorada.
- Quando alguém sai, o servidor tira essa conexão da lista e avisa os outros com `Ana saiu.` O servidor e os clientes restantes continuam.
- Cliente e servidor trocam texto em UTF-8.

Ainda não há comandos (`/listar`, `/sair`, `/privado`), histórico, salas, envio de arquivo nem banco de dados.

## Estrutura

| Classe | Papel |
| --- | --- |
| `servidor.Servidor` | Abre a porta, aceita conexões e distribui as mensagens |
| `servidor.ClienteHandler` | Atende um cliente: nome, leitura e saída |
| `cliente.Cliente` | Pede o nome, imprime o que chega e envia o que for digitado |
| `dominio.Participante` | Guarda o nome de quem entrou |

## Como executar

Na raiz do projeto. Na primeira vez o Maven Wrapper baixa o Maven.

Terminal do servidor:

```text
mvnw.cmd exec:java "-Dexec.mainClass=servidor.Servidor"
```

Cada cliente, no próprio terminal, depois que o servidor avisar que está aguardando:

```text
mvnw.cmd exec:java "-Dexec.mainClass=cliente.Cliente"
```

No PowerShell as aspas em `"-Dexec.mainClass=..."` são necessárias. No Linux ou no macOS, troque `mvnw.cmd` por `./mvnw`.

No IntelliJ, abra a pasta do projeto para ele importar o `pom.xml`. Execute `servidor.Servidor` e, em seguida, `cliente.Cliente` uma vez por pessoa.

Para sair de um cliente, feche o terminal ou use Ctrl+C. O servidor permanece no ar até o terminal dele ser fechado.

## Demo com três clientes

1. Suba o servidor. A saída esperada é `Servidor aguardando clientes na porta 5000.`
2. Abra três clientes. Use os nomes `Ana`, `Bruno` e `Caio`.
3. Cada um vê a própria boas-vindas. Quem já estava conectado vê `Bruno entrou.` e `Caio entrou.`
4. Na `Ana`, digite `oi`. Os três terminais mostram `Ana: oi`.
5. Feche o terminal da `Ana`. `Bruno` e `Caio` mostram `Ana saiu.`
6. No `Bruno`, digite `ainda aqui`. O `Caio` mostra `Bruno: ainda aqui`.

O servidor continua aceitando conexão depois que a `Ana` sai. Um quarto cliente pode entrar com outro nome e conversar com quem ficou.
