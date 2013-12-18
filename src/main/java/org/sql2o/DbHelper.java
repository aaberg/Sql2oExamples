package org.sql2o;

import org.sql2o.converters.Convert;
import org.sql2o.converters.PriorityConverter;
import org.sql2o.models.Priority;

/**
 * Created by lars on 12/18/13.
 */
public class DbHelper {

    private static Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost/Sql2oTestDb", "test", "testtest", QuirksMode.PostgreSQL);

    public static Sql2o getSql2o() {
        return sql2o;
    }



//    static{
//        Convert.registerConverter(Priority.class, new PriorityConverter());
//    }
}
