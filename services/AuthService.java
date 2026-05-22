package services;

import entities.Person;
import entities.User;
import entities.Admin;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<Person> accounts;

    public AuthService() {
        this.accounts = new ArrayList<>();
    }

    public Person createAccount(String nome, String email, String senha, boolean isAdmin) {
        if (getAccountByEmail(email) != null) {
            System.out.println("Erro: Email já cadastrado!");
            return null;
        }

        Person newPerson;
        if (isAdmin) {
            newPerson = new Admin(nome, email, senha);
        } else {
            newPerson = new User(nome, email, senha);
        }
        
        accounts.add(newPerson);
        System.out.println("Conta criada com sucesso! ID gerado: " + newPerson.getId());
        return newPerson;
    }

    public Person login(String email, String senha) {
        Person account = getAccountByEmail(email);
        if (account == null) {
            System.out.println("Erro: Conta não encontrada!");
            return null;
        }
        
        if (account.getSenha().equals(senha)) {
            System.out.println("Login realizado com sucesso! Bem-vindo(a), " + account.getNome() + ".");
            if (account.isAdmin()) {
                System.out.println(">> Autenticado como Administrador.");
            } else {
                System.out.println(">> Autenticado como Usuário.");
            }
            return account;
        } else {
            System.out.println("Erro: Senha incorreta!");
            return null;
        }
    }

    private Person getAccountByEmail(String email) {
        for (Person p : accounts) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    public List<Person> getAccounts() {
        return accounts;
    }
}
