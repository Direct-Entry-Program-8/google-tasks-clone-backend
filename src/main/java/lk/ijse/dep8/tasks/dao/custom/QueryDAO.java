package lk.ijse.dep8.tasks.dao.custom;

import lk.ijse.dep8.tasks.dao.SuperDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;

public interface QueryDAO extends SuperDAO {

    CustomEntity getTaskListInformation(int taskListId);

}
