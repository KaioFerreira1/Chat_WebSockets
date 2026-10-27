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
- O comando `/listar` responde só para quem digitou. Exemplo: `Online (3): Ana, Bruno, Caio`.
- O comando `/sair` encerra só quem digitou. Os outros recebem o aviso de saída.

Ainda não há o comando `/privado`, nem histórico, salas, envio de arquivo ou banco de dados.

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

Para sair de um cliente, digite `/sair`. Fechar o terminal também encerra. O servidor permanece no ar até o terminal dele ser fechado.

## Na apresentação

Abra um terminal com o servidor e três terminais com o cliente. Em cada cliente, digite um nome e depois as mensagens. Feche um dos clientes para mostrar que os outros continuam.
