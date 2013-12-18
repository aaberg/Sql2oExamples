package org.sql2o.examples;

import org.joda.time.DateTime;
import org.junit.Test;
import org.sql2o.DbHelper;
import org.sql2o.converters.Convert;
import org.sql2o.models.Priority;
import org.sql2o.models.Task;
import org.sql2o.models.Task2;

import java.util.List;

/**
 * Created by lars on 12/18/13.
 */
public class AdvancedExample {

    @Test
    public void selectExample(){

        String sql = "select id, due_date duedate, description, priority from task";

        List<Task2> tasks = DbHelper.getSql2o().createQuery(sql).executeAndFetch(Task2.class);
    }


}
