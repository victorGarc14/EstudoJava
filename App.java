import java.util.Scanner;
import services.AuthService;
import entities.Person;

import entities.User;
import entities.Task;

import entities.User;
import entities.Admin;
import entities.Task;
import entities.Priority;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        Person loggedInUser = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {
            if (loggedInUser == null) {
                System.out.println("\n=== MENU DE AUTENTICAÇÃO ===");
                System.out.println("1. Criar Conta");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                int option = -1;
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                }
                scanner.nextLine(); // consumir a quebra de linha
                
                if (option == 0) {
                    System.out.println("Encerrando o programa...");
                    break;
                } else if (option == 1) {
                    System.out.println("\n--- CRIAÇÃO DE CONTA ---");
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();
                    System.out.print("A conta é de Administrador? (s/n): ");
                    String isAdminStr = scanner.nextLine();
                    boolean isAdmin = isAdminStr.equalsIgnoreCase("s");

                    authService.createAccount(nome, email, senha, isAdmin);
                } else if (option == 2) {
                    System.out.println("\n--- LOGIN ---");
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    loggedInUser = authService.login(email, senha);
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                System.out.println("\n=== MENU DO SISTEMA ===");
                String tipo = loggedInUser.isAdmin() ? "Administrador" : "Usuário Comum";
                System.out.println("Logado como: " + loggedInUser.getNome() + " [" + tipo + "]");
                
                if (!loggedInUser.isAdmin()) {
                    System.out.println("1. Adicionar Tarefa");
                    System.out.println("2. Gerenciar Tarefas");
                } else {
                    System.out.println("1. Gerenciar Usuários");
                }
                System.out.println("8. Deslogar da Conta");
                System.out.println("0. Sair do Programa");
                System.out.print("Escolha uma opção: ");

                int option = -1;
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                }
                scanner.nextLine(); // consumir a quebra de linha

                if (option == 0) {
                    System.out.println("Encerrando o programa...");
                    break;
                } else if (option == 8) {
                    loggedInUser = null;
                    System.out.println("Você foi deslogado com sucesso.");
                } else if (!loggedInUser.isAdmin() && option == 1) {
                    User user = (User) loggedInUser;
                    System.out.print("Nome da Tarefa: ");
                    String taskName = scanner.nextLine();
                    System.out.print("Descrição da Tarefa: ");
                    String taskDesc = scanner.nextLine();
                    
                    System.out.println("Prioridade:");
                    System.out.println("1 - Urgente | 2 - Alta | 3 - Média | 4 - Baixa");
                    System.out.print("Escolha: ");
                    int prioOpt = scanner.nextInt();
                    scanner.nextLine();
                    Priority priority;
                    switch(prioOpt) {
                        case 1: priority = Priority.URGENTE; break;
                        case 2: priority = Priority.ALTA; break;
                        case 3: priority = Priority.MEDIA; break;
                        default: priority = Priority.BAIXA; break;
                    }
                    
                    LocalDate deadline = null;
                    while (deadline == null) {
                        System.out.print("Data máxima (dd/MM/yyyy): ");
                        String dataStr = scanner.nextLine();
                        try {
                            deadline = LocalDate.parse(dataStr, formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Data inválida! Tente novamente no formato dd/MM/yyyy.");
                        }
                    }

                    Task newTask = new Task(taskName, taskDesc, false, user, priority, deadline);
                    user.addTask(newTask);
                    System.out.println("Tarefa adicionada com sucesso!");
                } else if (!loggedInUser.isAdmin() && option == 2) {
                    User user = (User) loggedInUser;
                    if (user.getTaskList().isEmpty()) {
                        System.out.println("Nenhuma tarefa encontrada.");
                    } else {
                        // Ordena as tarefas de acordo com Prioridade (Urgente > Alta...) e depois Data
                        Collections.sort(user.getTaskList());
                        
                        System.out.println("\n--- SUAS TAREFAS ---");
                        for (int i = 0; i < user.getTaskList().size(); i++) {
                            Task t = user.getTaskList().get(i);
                            String status = t.getIsComplete() ? "[Concluída]" : "[Pendente]";
                            String details = String.format(" | Prioridade: %s | Prazo: %s", t.getPriority(), t.getDeadline().format(formatter));
                            System.out.println(i + ". " + t.getName() + " - " + t.getDescription() + details + " " + status);
                        }
                        
                        System.out.print("\nDigite o número da tarefa para gerenciar (ou -1 para voltar): ");
                        if (scanner.hasNextInt()) {
                            int taskIndex = scanner.nextInt();
                            scanner.nextLine();
                            
                            if (taskIndex >= 0 && taskIndex < user.getTaskList().size()) {
                                Task t = user.getTaskList().get(taskIndex);
                                System.out.println("\n--- GERENCIAR TAREFA ---");
                                System.out.println("Tarefa: " + t.getName());
                                System.out.println("1. Alterar Nome/Descrição");
                                System.out.println("2. Deletar Tarefa");
                                String btnConcluir = t.getIsComplete() ? "3. Marcar como Pendente" : "3. Marcar como Concluída";
                                System.out.println(btnConcluir);
                                System.out.println("0. Voltar");
                                System.out.print("Escolha uma opção: ");
                                
                                int subOption = -1;
                                if (scanner.hasNextInt()) {
                                    subOption = scanner.nextInt();
                                }
                                scanner.nextLine();
                                
                                if (subOption == 1) {
                                    System.out.print("Novo nome (deixe em branco para não alterar): ");
                                    String novoNome = scanner.nextLine();
                                    if (!novoNome.trim().isEmpty()) t.setName(novoNome);
                                    
                                    System.out.print("Nova descrição (deixe em branco para não alterar): ");
                                    String novaDesc = scanner.nextLine();
                                    if (!novaDesc.trim().isEmpty()) t.setDescription(novaDesc);
                                    
                                    System.out.println("Tarefa alterada com sucesso!");
                                } else if (subOption == 2) {
                                    user.getTaskList().remove(taskIndex);
                                    System.out.println("Tarefa deletada com sucesso!");
                                } else if (subOption == 3) {
                                    t.setIsComplete(!t.getIsComplete());
                                    System.out.println("Status da tarefa atualizado com sucesso!");
                                } else if (subOption == 0) {
                                    System.out.println("Voltando...");
                                } else {
                                    System.out.println("Opção inválida.");
                                }
                            } else if (taskIndex != -1) {
                                System.out.println("Índice de tarefa inválido.");
                            }
                        } else {
                            scanner.nextLine();
                            System.out.println("Entrada inválida.");
                        }
                    }
                } else if (loggedInUser.isAdmin() && option == 1) {
                    Admin admin = (Admin) loggedInUser;
                    System.out.println("\n--- LISTA DE USUÁRIOS ---");
                    
                    List<Person> accounts = authService.getAccounts();
                    List<User> apenasUsers = new ArrayList<>();
                    
                    for (Person p : accounts) {
                        if (p instanceof User) {
                            apenasUsers.add((User) p);
                        }
                    }
                    
                    if (apenasUsers.isEmpty()) {
                        System.out.println("Nenhum usuário comum encontrado.");
                    } else {
                        for (int i = 0; i < apenasUsers.size(); i++) {
                            User u = apenasUsers.get(i);
                            System.out.println(i + ". Nome: " + u.getNome() + " | Email: " + u.getEmail());
                        }
                        
                        System.out.print("\nDigite o número do usuário para gerenciar (ou -1 para voltar): ");
                        if (scanner.hasNextInt()) {
                            int userIndex = scanner.nextInt();
                            scanner.nextLine();
                            
                            if (userIndex >= 0 && userIndex < apenasUsers.size()) {
                                User u = apenasUsers.get(userIndex);
                                System.out.println("\n--- GERENCIAR USUÁRIO ---");
                                System.out.println("Usuário: " + u.getNome());
                                System.out.println("1. Editar Dados");
                                System.out.println("2. Deletar Usuário");
                                System.out.println("0. Voltar");
                                System.out.print("Escolha uma opção: ");
                                
                                int subOption = -1;
                                if (scanner.hasNextInt()) {
                                    subOption = scanner.nextInt();
                                }
                                scanner.nextLine();
                                
                                if (subOption == 1) {
                                    System.out.print("Novo nome (deixe em branco para não alterar): ");
                                    String novoNome = scanner.nextLine();
                                    if (novoNome.trim().isEmpty()) novoNome = u.getNome();
                                    
                                    System.out.print("Novo email (deixe em branco para não alterar): ");
                                    String novoEmail = scanner.nextLine();
                                    if (novoEmail.trim().isEmpty()) novoEmail = u.getEmail();
                                    
                                    System.out.print("Nova senha (deixe em branco para não alterar): ");
                                    String novaSenha = scanner.nextLine();
                                    if (novaSenha.trim().isEmpty()) novaSenha = u.getSenha();
                                    
                                    admin.editUser(accounts, u, novoNome, novoEmail, novaSenha);
                                    System.out.println("Dados do usuário alterados com sucesso!");
                                } else if (subOption == 2) {
                                    admin.deleteUser(accounts, u);
                                    System.out.println("Usuário deletado com sucesso!");
                                } else if (subOption == 0) {
                                    System.out.println("Voltando...");
                                } else {
                                    System.out.println("Opção inválida.");
                                }
                            } else if (userIndex != -1) {
                                System.out.println("Índice de usuário inválido.");
                            }
                        } else {
                            scanner.nextLine();
                            System.out.println("Entrada inválida.");
                        }
                    }
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }

        scanner.close();
    }
}
