package lk.ijse.dep8.tasks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomEntity implements SuperEntity {
    private int taskListId;
    private String taskListName;
    private String userName;
}
