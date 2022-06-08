package lk.ijse.dep8.tasks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Task implements SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String details;
    private int position;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JoinColumn(name = "task_list_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TaskList taskListId;

    public enum Status{
        completed, needsAction
    }
}
