# Projeto de Tarefas Java

Um pequeno aplicativo console em Java que permite:

- Criar contas de **Usuário** e **Administrador**.  
- Usuários podem:  
  - Adicionar tarefas com prioridade (`URGENTE`, `ALTA`, `MEDIA`, `BAIXA`) e data de prazo.  
  - Gerenciar tarefas (alterar nome/descrição, deletar, marcar como concluída/pendente).  
  - Visualizar tarefas ordenadas por prioridade e, em caso de empate, pela data de entrega.  
- Administradores podem:  
  - Listar todos os usuários comuns.  
  - Editar ou deletar um usuário selecionado.  

## Estrutura do Projeto

```
Projeto de Tarefas JAVA/
│
├── App.java                        # Ponto de entrada da aplicação (main) e lógica do menu console
│
├── entities/                       # Pacote com as entidades do domínio
│   ├── Person.java                 # Classe abstrata base para User e Admin (id, nome, email, senha)
│   ├── User.java                   # Usuário comum — estende Person, gerencia lista de tarefas
│   ├── Admin.java                  # Administrador — estende Person, pode editar/deletar usuários
│   ├── Task.java                   # Entidade de tarefa (nome, descrição, prioridade, prazo, status)
│   └── Priority.java               # Enum com os níveis de prioridade (URGENTE, ALTA, MEDIA, BAIXA)
│
└── services/                       # Pacote de serviços
    ├── AuthService.java            # Serviço de autenticação — criação de conta e login
    └── ValidationService.java      # Serviço de validação de dados de entrada
```


## Detalhes das Classes

### 1. Entidades (`entities`)

#### `Person.java`
- Classe abstrata que representa uma pessoa no sistema.
- Atributos:
  - `id`: Identificador único (int, gerado automaticamente).
  - `nome`: Nome da pessoa (String).
  - `email`: Email (String, deve ser único).
  - `senha`: Senha (String).
- Método abstrato:
  - `void displayOptions()`: Exibe as opções do menu dependendo do tipo (User ou Admin).
- Métodos:
  - Getters e Setters para os atributos.
  - `boolean isAdmin()`: Retorna true se for instância de Admin, false caso contrário.

#### `User.java`
- Estende `Person` representa um usuário comum.
- Atributos:
  - `taskList`: Lista de tarefas do usuário (`List<Task>`).
- Métodos:
  - `void addTask(Task task)`: Adiciona uma tarefa.
  - `void removeTask(int index)`: Remove uma tarefa pelo índice.
  - `void updateTaskName(int index, String newName)`: Atualiza o nome.
  - `void updateTaskDescription(int index, String newDescription)`: Atualiza a descrição.
  - `void toggleTaskStatus(int index)`: Alterna status (concluído/pendente).
  - `List<Task> getTaskList()`: Retorna a lista ordenada.
  - Sobrescrita de `displayOptions()`: Mostra "Adicionar Tarefa" e "Gerenciar Tarefas".

#### `Admin.java`
- Estende `Person` representa um administrador.
- Atributos:
  - `userList`: Lista de todos os usuários do sistema (`List<User>`).
- Métodos:
  - `void addUser(User user)`: Adiciona um novo usuário.
  - `void removeUser(int index)`: Remove um usuário.
  - `void updateUser(int index, String newName, String newEmail)`: Edita um usuário.
  - `User getUser(int index)`: Retorna um usuário pelo índice.
  - `List<User> getUserList()`: Retorna a lista de usuários.
  - Sobrescrita de `displayOptions()`: Mostra "Listar Usuários", "Editar Usuário" e "Deletar Usuário".

#### `Task.java`
- Representa uma tarefa.
- Atributos:
  - `id`: Identificador único.
  - `name`: Nome da tarefa.
  - `description`: Descrição.
  - `isCompleted`: Status (true/false).
  - `user`: Dono da tarefa.
  - `priority`: Prioridade (`Priority.URGENTE`, `Priority.ALTA`, `Priority.MEDIA`, `Priority.BAIXA`).
  - `dueDate`: Data limite (`LocalDate`).
- Implementa `Comparable<Task>`: Ordenação por prioridade e depois por data de entrega.
- Métodos:
  - Getters e Setters.
  - `String getPriorityString()`: Retorna descrição textual da prioridade.
  - `String getStatusString()`: Retorna status.
  - Sobrescrita de `toString()`: Representação formatada da tarefa.

#### `Priority.java`
- Enum com as prioridades:
  - `URGENTE`
  - `ALTA`
  - `MEDIA`
  - `BAIXA`

### 2. Serviços (`services`)

#### `AuthService.java`
- Gerencia autenticação.
- Armazena usuários em uma `List<Person>`.
- Métodos:
  - `void createAccount(String name, String email, String password, boolean isAdmin)`: Cria conta e valida email.
  - `Person login(String email, String password)`: Efetua login.
  - `boolean isEmailAvailable(String email)`: Verifica se email já existe.

#### `ValidationService.java`
- Valida entrada de dados.
- Métodos:
  - `boolean isValidEmail(String email)`: Validação básica de email.
  - `boolean isValidPassword(String password)`: Senha deve ter pelo menos 6 caracteres.
  - `boolean isValidName(String name)`: Nome não pode ser vazio.

### 3. Aplicação (`App.java`)
- Interface do usuário.
- Gerencia o menu principal e os loops.
- Interage com `AuthService`, `User`, `Admin` e `Task`.

## Fluxo de Uso

### 1. Tela Inicial
- Opção 1: Criar Conta
- Opção 2: Fazer Login
- Opção 0: Sair

### 2. Criar Conta
- Insere Nome, Email, Senha e tipo (Admin ou Usuário)
- Valida se email já existe

### 3. Fazer Login
- Insere Email e Senha
- Se correto → Acesso ao menu do perfil

### 4. Menu de Usuário
- Adicionar Tarefa
- Gerenciar Tarefas
- Deslogar
- Sair do programa

### 5. Gerenciar Tarefas
- Listar tarefas
- Alterar Nome
- Alterar Descrição
- Alterar Prioridade
- Alterar Data Limite
- Marcar/Desmarcar Concluído
- Deletar Tarefa

### 6. Menu de Administrador
- Listar Usuários
- Editar Usuário
- Deletar Usuário
- Deslogar
- Sair do programa

## Como Compilar e Executar

### Pré-requisitos
- Java Development Kit (JDK) 8 ou superior

### Compilação
```bash
javac -d out entities/*.java services/*.java App.java
```

### Execução
```bash
java -cp out App
```
