package entities;

import java.time.LocalDate;

public class Task implements Comparable<Task> {
    private String name;
    private String description;
    private Boolean isComplete;
    private User owner;
    private Priority priority;
    private LocalDate deadline;
    
    public Task(String name, String description, Boolean isComplete, User owner, Priority priority, LocalDate deadline){
        this.name = name;
        this.description = description;
        this.isComplete = isComplete;
        this.owner = owner;
        this.priority = priority;
        this.deadline = deadline;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Boolean getIsComplete() { return isComplete; }
    public User getOwner() { return owner; }
    public Priority getPriority() { return priority; }
    public LocalDate getDeadline() { return deadline; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setIsComplete(Boolean isComplete) { this.isComplete = isComplete; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    @Override
    public int compareTo(Task other) {
        int priorityComparison = this.priority.compareTo(other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return this.deadline.compareTo(other.deadline);
    }

    @Override
    public String toString() {
        return "Task [name=" + name + ", description=" + description + ", priority=" + priority + ", deadline=" + deadline + ", isComplete=" + isComplete + "]";
    }    
}