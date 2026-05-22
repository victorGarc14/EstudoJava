package entities;

import java.util.ArrayList;
import java.util.List;

public class User extends Person {
    
    private List<Task> taskList;
    
    public User(String nome, String email, String senha) {
        super(nome, email, senha, false);
        this.taskList = new ArrayList<>();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task){
        this.taskList.add(task);
    }

    public void taskCompletion(Task task){
        if(this.taskList.contains(task)){
            task.setIsComplete(true);
        }
    }

    @Override
    public String toString() {
        return "User [name=" + getNome() + ", email=" + getEmail() + ", senha=" + getSenha() + ", isAdmin=" + isAdmin() + "]";
    }    
}
