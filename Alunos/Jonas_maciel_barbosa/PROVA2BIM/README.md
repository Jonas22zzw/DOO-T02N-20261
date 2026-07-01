# TV Tracker

Sistema desktop em **Java (Swing)** para acompanhar séries de TV usando a API
pública do [TVMaze](https://www.tvmaze.com/api).

## Como compilar e executar

Requer **JDK 11 ou superior** instalado (não apenas o JRE — precisa do `javac`).

### Linux / Mac
```bash
chmod +x compilar.sh executar.sh
./compilar.sh
./executar.sh
```

### Windows
```
compilar.bat
executar.bat
```

Não há dependências externas (sem Maven/Gradle necessários): todo o código
usa apenas a biblioteca padrão do Java (`java.net.http` para chamadas HTTP,
Swing para a interface, e um parser/escritor JSON próprio, implementado do
zero em `fag.json`, para não depender de bibliotecas como
Gson/Jackson).

Os dados são salvos automaticamente em `~/.tvtracker/dados.json`. Na primeira
execução esse arquivo é criado com **dados pré-carregados** (um usuário
"Convidado" e 5 séries populares já em cache, distribuídas entre as listas),
para que o sistema já possa ser demonstrado sem precisar buscar nada na API.

## Estrutura do projeto (pacotes)

```
fag
 ├── Main.java                 -> ponto de entrada, tratamento global de exceções
 ├── model/                    -> classes de domínio (POJOs)
 │    ├── Series.java
 │    ├── SeriesStatus.java
 │    ├── ListType.java
 │    ├── UserLibrary.java     -> as 3 listas de um usuário
 │    └── AppData.java         -> agrega usuários + cache de séries
 ├── json/                     -> parser/escritor JSON implementado do zero
 │    ├── JsonParser.java
 │    └── JsonWriter.java
 ├── exceptions/                -> exceções de negócio específicas
 │    ├── ApiException.java
 │    └── PersistenceException.java
 ├── service/                   -> regras de negócio / integração
 │    ├── TVMazeService.java    -> consome a API do TVMaze
 │    ├── PersistenceService.java -> lê/grava o JSON local + seed de dados
 │    └── SeriesComparators.java  -> comparadores para as 4 formas de ordenação
 └── ui/                         -> telas Swing
      ├── MainFrame.java
      ├── LoginDialog.java
      ├── SearchPanel.java
      ├── SeriesListPanel.java
      ├── SeriesDetailDialog.java
      └── SeriesTableModel.java
```

## Boas práticas de POO aplicadas

- **Responsabilidade única**: `TVMazeService` só fala com a API,
  `PersistenceService` só lida com arquivos, as classes de `ui` só cuidam de
  interface, `Series`/`UserLibrary`/`AppData` só representam dados.
- **Encapsulamento**: todos os atributos são privados, acessados por
  getters/setters; `UserLibrary` esconde os `Set<Integer>` internos atrás de
  métodos (`add`, `remove`, `toggle`, `contains`).
- **Enums** (`SeriesStatus`, `ListType`) em vez de strings soltas, evitando
  valores mágicos.
- **Reuso via composição**: `SeriesListPanel` é usado três vezes (favoritos,
  assistidas, quero assistir) parametrizado por `ListType`, evitando código
  duplicado.
- **Exceções de domínio** (`ApiException`, `PersistenceException`) em vez de
  deixar `IOException`/`RuntimeException` genéricas vazarem para a UI.

## Tratamento de exceções (não fecha inesperadamente)

- `Main` instala um `Thread.setDefaultUncaughtExceptionHandler` global: qualquer
  erro não previsto exibe um `JOptionPane` em vez de derrubar o programa.
- Chamadas de rede (`TVMazeService`) rodam em `SwingWorker`, com
  `try/catch` tanto no `doInBackground` quanto no `done()`, mostrando mensagens
  amigáveis em caso de falha de conexão, timeout ou resposta inválida.
- Toda leitura/escrita do arquivo JSON é protegida por `try/catch`, com
  fallback para dados vazios/pré-carregados se o arquivo estiver corrompido.
- Se salvar uma alteração falhar, a alteração em memória é revertida para
  manter a consistência entre tela e disco.

## Persistência

Formato **JSON**, escrito à mão pelo sistema (`fag.json`), salvo em
`~/.tvtracker/dados.json`, contendo:
- `seriesCache`: todas as séries já vistas (buscadas ou pré-carregadas), para
  que as listas funcionem mesmo sem internet.
- `users`: cada usuário local com seus três arrays de ids (`favorites`,
  `watched`, `wantToWatch`).

Os dados são salvos automaticamente a cada ação (adicionar/remover de lista,
trocar de usuário) e também ao fechar a janela.

## Funcionalidades

- Usuário informa nome/apelido local ao abrir o sistema (ou escolhe um já
  existente), podendo trocar de usuário a qualquer momento.
- Busca de séries por nome via API do TVMaze.
- Adicionar/remover séries de **Favoritos**, **Já Assistidas** e **Quero
  Assistir**, tanto pela tela de busca quanto pelas próprias telas de lista.
- As listas podem ser ordenadas por: nome (alfabética), nota geral, estado
  (em transmissão / concluída / cancelada / etc.) e data de estreia.
- Tela de detalhes exibindo: nome, idioma, gêneros, nota geral, estado, data
  de estreia, data de término e emissora — tanto a partir da busca quanto a
  partir de qualquer uma das listas.
