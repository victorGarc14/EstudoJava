package entities;

import java.util.List;

public class Admin extends Person {
    public Admin(String nome, String email, String senha) {
        super(nome, email, senha, true);
    }

    public void editUser(List<Person> users, User user, String name, String email, String senha){
        if(users.contains(user)){
            user.setNome(name);
            user.setEmail(email);
            user.setSenha(senha);
        }
        else{
            System.out.println("Usuário não encontrado.");
        }
    }

    public void deleteUser(List<Person> users, User user){
        if(users.contains(user)){
            users.remove(user);
        }
        else{
            System.out.println("Usuário não encontrado.");
        }
    }
}
