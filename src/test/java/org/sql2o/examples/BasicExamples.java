package org.sql2o.examples;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.*;
import org.sql2o.data.Row;
import org.sql2o.data.Table;
import org.sql2o.models.Task;

import java.util.List;

/**
 * Created by lars on 12/18/13.
 */
public class BasicExamples {

    static Logger logger = LoggerFactory.getLogger(BasicExamples.class);

    @Test
    public void selectExample() {

        String sql = "select id, due_date dueDate, description, priority from task";

        List<Task> tasks = DbHelper.getSql2o().createQuery(sql).executeAndFetch(Task.class);

    }

    @Test
    public void selectOneExample() {
        String sql = "select id, due_date dueDate, description, priority from task where id = :idparam";

        Task task = DbHelper.getSql2o().createQuery(sql).addParameter("idparam", 1).executeAndFetchFirst(Task.class);
    }

    @Test
    public void transactioExample() {

        Connection connection = null;

        try {
            connection = DbHelper.getSql2o().beginTransaction();

            int createdId = connection.createQuery("insert into task(due_date, description, priority) values (:duedate, :description, :priority)", true)
                    .addParameter("duedate", DateTime.now().plusDays(4))
                    .addParameter("description", "do the foo")
                    .addParameter("priority", 3)
                    .executeUpdate().getKey(Integer.class);

            connection.createQuery("update task set priority = :newPri where id = :id")
                    .addParameter("newPri", 1)
                    .addParameter("id", createdId)
                    .executeUpdate();

            connection.commit();
        } catch(Throwable t) {
            if (connection != null) {
                connection.rollback();
            }
            throw new RuntimeException("Something went wrong, rolling back transaction", t);
        }
    }

    @Test
    public void transactionExample2() {
        DbHelper.getSql2o().runInTransaction(new StatementRunnable() {
            @Override
            public void run(Connection connection, Object o) throws Throwable {

                int createdId = connection.createQuery("insert into task(due_date, description, priority) values (:duedate, :description, :priority)", true)
                        .addParameter("duedate", DateTime.now().plusDays(4))
                        .addParameter("description", "do the foo")
                        .addParameter("priority", 3)
                        .executeUpdate().getKey(Integer.class);

                connection.createQuery("update task set priority = :newPri where id = :id")
                        .addParameter("newPri", 1)
                        .addParameter("id", createdId)
                        .executeUpdate();
            }
        });
    }

    @Test
    public void scalarExamples() {

        int cnt = DbHelper.getSql2o().createQuery("select count(*) from task").executeScalar(Integer.class);

        List<Integer> allIds = DbHelper.getSql2o().createQuery("select id from task").executeScalarList();
    }

    @Test
    public void tableExample() {

        Table t = DbHelper.getSql2o().createQuery("select * from task").executeAndFetchTable();

        for (Row r : t.rows()) {
            int id = r.getInteger("id");
            DateTime dueDate = r.getDateTime("due_date");
            String description = r.getString("description");
            int priority = r.getInteger("priority");
        }
    }

    @Test
    public void aLotOfInsertsExample() {

        final String sql = "insert into task(due_date, description, priority) values (:duedate, :desc, :pri)";

        DateTime start = DateTime.now();

        DbHelper.getSql2o().runInTransaction(new StatementRunnable() {
            @Override
            public void run(Connection connection, Object o) throws Throwable {

                for (int idx = 0; idx < 10000; idx++){

                connection.createQuery(sql)
                        .addParameter("duedate", DateTime.now().plusDays(idx))
                        .addParameter("desc", "foo " + idx)
                        .addParameter("pri", (idx % 3) + 1)
                        .executeUpdate();
                }
            }
        });

        DateTime end = DateTime.now();
        Period dur = new Period(start, end, PeriodType.millis());
        logger.info("Total execution time: " + dur.getMillis());
    }

    @Test
    public void batchExample() {

        final String sql = "insert into task(due_date, description, priority) values (:duedate, :desc, :pri)";

        DbHelper.getSql2o().runInTransaction(new StatementRunnable() {
            @Override
            public void run(Connection connection, Object o) throws Throwable {

                Query query = connection.createQuery(sql);
                for (int idx = 0; idx < 10000; idx++){

                    query.addParameter("duedate", DateTime.now().plusDays(idx));
                    query.addParameter("desc", "foo " + idx);
                    query.addParameter("pri", (idx % 3) + 1);

                    query.addToBatch();
                }

                query.executeBatch();
            }
        });
    }

}
