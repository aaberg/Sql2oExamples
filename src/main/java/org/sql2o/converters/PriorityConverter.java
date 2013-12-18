package org.sql2o.converters;

import org.sql2o.DbHelper;
import org.sql2o.models.Priority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lars on 12/18/13.
 */
public class PriorityConverter implements Converter<Priority> {

    private Map<Integer, Priority> priorityMap = null;

    @Override
    public Priority convert(Object o) throws ConverterException {
        if (o == null) return null;

        if (!(o instanceof Number)) {
            throw new ConverterException("Don't know how to convert from type '" + o.getClass().getName() + "' to type '" + Priority.class.getName() + "'");
        }

        int priorityId = ((Number)o).intValue();
        return getPriorityMap().get(priorityId);
    }

    private Map<Integer, Priority> getPriorityMap() {
        if (priorityMap == null) {
            priorityMap = createPriorityMap();
        }
        return priorityMap;
    }

    public Map<Integer, Priority> createPriorityMap() {
        Map<Integer, Priority> map = new HashMap<Integer, Priority>();
        List<Priority> priorityList = DbHelper.getSql2o().createQuery("select id, text from priority").executeAndFetch(Priority.class);

        for (Priority p : priorityList) {
            map.put(p.getId(), p);
        }

        return map;
    }
}
