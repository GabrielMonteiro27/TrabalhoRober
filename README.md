# Sistema de Gerenciamento de Eventos

Projeto de Laboratório de Programação II: aplicação Java de console, orientada a objetos, para cadastrar eventos presenciais e online, inscrever participantes respeitando vagas e prazos, registrar presença e emitir relatórios. Os dados ficam salvos em arquivo e são recuperados quando o sistema é aberto de novo.

## Estrutura

```
src/
├── app/            Main
├── visao/          Menu, LeitorEntrada
├── servico/        GerenciadorEventos
├── modelo/         Pessoa, Participante, Organizador,
│                   Evento, EventoPresencial, EventoOnline,
│                   Inscricao, StatusEvento, StatusInscricao
├── persistencia/   Repositorio (interface), RepositorioArquivo, BaseDeDados
└── excecao/        SistemaEventosException e as 5 exceções de regra de negócio
```

São 22 arquivos em 6 pacotes. Compila com Java 8 ou superior.

## Como rodar

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

### Dados

Os dados ficam no arquivo `dados.dat`, na pasta de onde o programa é executado. O sistema salva depois de cada alteração e também ao sair. Para começar do zero, apague o `dados.dat`.

### Javadoc

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
| `persistencia/*` | 6 | Guilherme |
| `servico/GerenciadorEventos`: seção Pessoas | 7 | Guilherme |
| `servico/GerenciadorEventos`: seção Eventos | 8 | Arthur |
| `servico/GerenciadorEventos`: seção Inscrições e `registrarPresenca` | 9 e 14 | Heitor |
| `visao/LeitorEntrada` | 10 | Anderson |
| `app/Main`, `visao/Menu`: menu principal e Pessoas | 11 | Anderson |
| `visao/Menu`: Eventos e Inscrições | 12 | Uriel |
| `visao/Menu`: Check-in | 14 | Heitor |
| `visao/Menu`: Relatórios | 15 | Uriel |

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

## Conceitos de OO usados

- **Herança:** `Pessoa` → `Participante` e `Organizador`; `Evento` → `EventoPresencial` e `EventoOnline`; `SistemaEventosException` → 5 exceções.
- **Classes abstratas e polimorfismo:** `getTipo()`, `getDetalhe()` e `getInformacoesAcesso()` são abstratos. O menu lista pessoas e eventos sem saber o tipo de cada um.
- **Interfaces:** `Repositorio`, implementada por `RepositorioArquivo`; `Comparable<Evento>`, para ordenar os eventos por data.
- **Coleções:** `Map<String, Pessoa>` (busca por CPF), `List<Evento>` e a `List<Inscricao>` de cada evento.
- **Composição:** cada `Evento` guarda as próprias inscrições.
- **Exceções próprias:** todas herdam de `SistemaEventosException` e são tratadas no menu com um único `catch`.
- **Persistência:** `BaseDeDados` é gravada inteira com um único `writeObject`.
- **`toString`, `equals` e `hashCode`:** em todas as classes. Nos enums, `equals` não pode ser sobrescrito, porque é `final` na classe `Enum`.
- **Recursos além da sala:** `java.time` (`LocalDate`, `LocalDateTime`, `DateTimeFormatter`), `enum` com atributos, `Optional`, `Comparator.comparing` e try-with-resources.
