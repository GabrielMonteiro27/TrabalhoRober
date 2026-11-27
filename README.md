# Sistema de Gerenciamento de Eventos

Projeto de Laboratório de Programação II: aplicação Java de console, orientada a objetos, para cadastrar eventos presenciais e online, inscrever participantes respeitando vagas e prazos, registrar presença, emitir relatórios e exportá-los em CSV. Os dados ficam salvos em arquivo e são recuperados quando o sistema é aberto de novo.

## Estrutura

```
src/                  código do sistema
├── app/              Main
├── visao/            Menu, LeitorEntrada
├── servico/          GerenciadorEventos
├── modelo/           Pessoa, Participante, Organizador,
│                     Evento, EventoPresencial, EventoOnline,
│                     Inscricao, StatusEvento, StatusInscricao
├── persistencia/     Repositorio (interface), RepositorioArquivo, BaseDeDados, ExportadorCsv
└── excecao/          SistemaEventosException e as 5 exceções de regra de negócio

test/                 testes automatizados (JUnit 5)
├── modelo/           PessoaTest, EventoTest, InscricaoTest
├── servico/          GerenciadorEventosTest
└── persistencia/     RepositorioArquivoTest, ExportadorCsvTest, RepositorioMemoria
```

O sistema tem 23 arquivos em 6 pacotes e compila com Java 8 ou superior. Os testes ficam separados em `test/`: o sistema roda sem o JUnit, que só é necessário para rodar os testes.

## Como rodar o sistema

### Pela IDE (NetBeans, IntelliJ, Eclipse ou VS Code)

1. Crie um projeto Java vazio (sem classe principal gerada).
2. Copie as 6 pastas de `src/` para dentro da pasta `src` do projeto.
3. Execute a classe `app.Main`.

Se os acentos aparecerem estranhos, configure a codificação do projeto como UTF-8.

### Pelo terminal

Linux ou Mac:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

Windows (Prompt de Comando):

```bat
chcp 65001
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
java -cp out app.Main
```

O `chcp 65001` faz o Prompt de Comando mostrar os acentos corretamente.

### Dados e arquivos gerados

- `dados.dat`: todos os dados do sistema, na pasta de onde o programa é executado. O sistema salva depois de cada alteração e também ao sair. Para começar do zero, apague este arquivo.
- `eventos.csv` e `inscritos-EV00X.csv`: criados pelo menu Relatórios → 3. Exportar em CSV, na mesma pasta. Abrem direto no Excel, no LibreOffice ou no Google Planilhas.

## Como rodar os testes

Os 51 testes usam o **JUnit 5**. Ele não vem com o Java: é preciso adicioná-lo ao projeto uma vez.

### Pela IDE

- **NetBeans** (projeto Java with Ant): copie as pastas de `test/` para a pasta `test` do projeto ("Test Packages"). Clique com o botão direito no projeto → Properties → Libraries → aba Compile Tests → adicione a biblioteca **JUnit 5**. Depois, botão direito no projeto → **Test**.
- **IntelliJ IDEA**: copie a pasta `test` para a raiz do projeto, clique com o botão direito nela → Mark Directory as → **Test Sources Root**. Abra um arquivo de teste, coloque o cursor em `@Test` e use Alt+Enter → **Add 'JUnit 5' to classpath**. Depois, botão direito na pasta `test` → **Run 'All Tests'**.
- **Eclipse**: copie a pasta `test` para a raiz do projeto e adicione-a como pasta de código (botão direito → Build Path → Use as Source Folder). Depois, botão direito no projeto → Build Path → Add Libraries → **JUnit** → JUnit 5. Para rodar: botão direito na pasta `test` → Run As → **JUnit Test**.
- **VS Code**: com o Extension Pack for Java instalado, abra a aba Testing e clique em **Enable Java Tests** → **JUnit Jupiter**.

### Pelo terminal

Baixe o arquivo `junit-platform-console-standalone` (versão 1.10 ou mais nova) no Maven Central e salve-o na pasta do projeto com o nome `junit-platform-console-standalone.jar`.

Linux ou Mac:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
javac -encoding UTF-8 -cp out:junit-platform-console-standalone.jar -d out-test $(find test -name "*.java")
java -jar junit-platform-console-standalone.jar --class-path out:out-test --scan-class-path
```

No Windows, troque `:` por `;` no `--class-path` e no `-cp`, e use `dir /s /b test\*.java > testes.txt` com `@testes.txt` para listar os arquivos, como no exemplo do sistema.

### O que os testes verificam

| Classe de teste | O que verifica | Testes |
| --- | --- | --- |
| `PessoaTest` | Validação de CPF, e-mail e campos obrigatórios; polimorfismo; `equals` pelo CPF | 7 |
| `EventoTest` | Vagas, data-limite, capacidade, cancelamento, status REALIZADO, ordem por data, polimorfismo e taxa de presença | 11 |
| `InscricaoTest` | Status, presença, reativação e `equals` por participante + evento | 5 |
| `GerenciadorEventosTest` | RN01 a RN09, reinscrição, check-in, códigos em sequência e salvar e carregar | 19 |
| `RepositorioArquivoTest` | Gravação e leitura do arquivo, primeira execução e arquivo corrompido | 4 |
| `ExportadorCsvTest` | Formato do CSV, conteúdo dos dois relatórios e proteção contra fórmulas | 5 |

Os testes do `GerenciadorEventos` usam o `RepositorioMemoria`, que implementa a interface `Repositorio` guardando tudo na memória, sem criar arquivos. As datas dos testes são calculadas a partir do dia em que rodam, então eles continuam passando depois das datas dos eventos de exemplo.

## Javadoc

```bash
javadoc -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 -d doc -sourcepath src -subpackages app:visao:servico:modelo:persistencia:excecao
```

Abra `doc/index.html` no navegador.

## Arquivos por cartão do Trello

| Arquivo | Cartão | Responsável |
| --- | --- | --- |
| `modelo/Pessoa`, `Participante`, `Organizador` | 2 | Arthur |
| `modelo/Evento`, `EventoPresencial`, `EventoOnline`, `StatusEvento` | 3 | Arthur |
| `modelo/Inscricao`, `StatusInscricao` | 4 | Heitor |
| `excecao/*` | 5 | Uriel |
| `persistencia/Repositorio`, `RepositorioArquivo`, `BaseDeDados` | 6 | Guilherme |
| `servico/GerenciadorEventos`: seção Pessoas | 7 | Guilherme |
| `servico/GerenciadorEventos`: seção Eventos | 8 | Arthur |
| `servico/GerenciadorEventos`: seção Inscrições e `registrarPresenca` | 9 e 14 | Heitor |
| `visao/LeitorEntrada` | 10 | Anderson |
| `app/Main`, `visao/Menu`: menu principal e Pessoas | 11 | Anderson |
| `visao/Menu`: Eventos e Inscrições | 12 | Uriel |
| `visao/Menu`: Check-in | 14 | Heitor |
| `visao/Menu`: Relatórios | 15 | Uriel |
| `persistencia/ExportadorCsv`, `visao/Menu`: `exportarCsv()` e pasta `test/` | 18 | Guilherme |

`Menu` e `GerenciadorEventos` são divididos em seções com comentários. Como várias pessoas mexem nesses dois arquivos, combinem quem edita cada seção para evitar conflitos no Git.

## Regras de negócio

| Regra | Onde fica | Exceção |
| --- | --- | --- |
| RN01: CPF único | `GerenciadorEventos.cadastrarPessoa` | `CadastroDuplicadoException` |
| RN02: só inscreve se houver vaga | `GerenciadorEventos.inscrever` | `VagasEsgotadasException` |
| RN03: uma inscrição confirmada por participante e evento | `GerenciadorEventos.inscrever` | `InscricaoDuplicadaException` |
| RN04: inscrição só até a data-limite e com evento ABERTO | `Evento.aceitaInscricao` | `InscricoesEncerradasException` |
| RN05: CPF, código ou inscrição inexistente | `GerenciadorEventos.buscar...` | `EntidadeNaoEncontradaException` |
| RN06: cancelar inscrição libera a vaga | `Inscricao.cancelar` | — |
| RN07: cancelar evento cancela as inscrições | `Evento.cancelar` | — |
| RN08: validação de toda entrada | `LeitorEntrada` e setters do modelo | `IllegalArgumentException` |
| RN09: não remover quem organiza evento ou tem inscrição confirmada | `GerenciadorEventos.removerPessoa` | `SistemaEventosException` |

## Conceitos de OO usados

- **Herança:** `Pessoa` → `Participante` e `Organizador`; `Evento` → `EventoPresencial` e `EventoOnline`; `SistemaEventosException` → 5 exceções.
- **Classes abstratas e polimorfismo:** `getTipo()`, `getDetalhe()` e `getInformacoesAcesso()` são abstratos. O menu lista pessoas e eventos sem saber o tipo de cada um.
- **Interfaces:** `Repositorio`, implementada por `RepositorioArquivo` (no sistema) e por `RepositorioMemoria` (nos testes); `Comparable<Evento>`, para ordenar os eventos por data.
- **Coleções:** `Map<String, Pessoa>` (busca por CPF), `List<Evento>` e a `List<Inscricao>` de cada evento.
- **Composição:** cada `Evento` guarda as próprias inscrições.
- **Exceções próprias:** todas herdam de `SistemaEventosException` e são tratadas no menu com um único `catch`.
- **Persistência:** `BaseDeDados` é gravada inteira com um único `writeObject`.
- **`toString`, `equals` e `hashCode`:** em todas as classes. Nos enums, `equals` não pode ser sobrescrito, porque é `final` na classe `Enum`.
- **Recursos além da sala (item 14):** testes automatizados com JUnit 5, exportação em CSV, `java.time` (`LocalDate`, `LocalDateTime`, `DateTimeFormatter`), `enum` com atributos, `Optional`, `Comparator.comparing` e try-with-resources.
