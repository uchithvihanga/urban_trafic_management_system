package com.uchith.utms.service.impl;

import com.uchith.utms.entity.TrafficData;
import com.uchith.utms.service.remote.DataService;
import com.uchith.utms.service.remote.Direction;
import com.uchith.utms.util.HibernateUtilBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class TrafficDataService implements DataService {

    @Inject
    private HibernateUtilBean hibernateUtilBean;

    @Override
    public void save(HashMap<String, Object> dataMap){
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        TrafficData trafficData = new TrafficData();
        trafficData.setDescription(String.valueOf(dataMap.get("description")));
        trafficData.setSpeed(Double.valueOf(String.valueOf(dataMap.get("speed"))));

        Transaction transaction = session.beginTransaction();

        session.persist(trafficData);
        transaction.commit();
        session.close();
    }

    @Override
    public ArrayList<HashMap<String, String>> getAll() {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();
        List<TrafficData> data = session.createQuery("select d from TrafficData d", TrafficData.class).getResultList();

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for(TrafficData trafficData : data){
            HashMap<String, String> map = new HashMap<>();
            map.put("description",trafficData.getDescription());
            map.put("speed",String.valueOf(trafficData.getSpeed()));
            map.put("time",String.valueOf(trafficData.getCreatedAt()));
            list.add(map);
        }
        session.close();

        return list;
    }

    @Override
    public ArrayList<HashMap<String, String>> getDateBetween(LocalDateTime from, LocalDateTime to) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();
        List<TrafficData> data = session.createQuery("select d from TrafficData d where d.createdAt between :fromDate and :toDate", TrafficData.class)
                .setParameter("fromDate",from)
                .setParameter("toDate",to)
                .getResultList();

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for(TrafficData trafficData : data){
            HashMap<String, String> map = new HashMap<>();
            map.put("description",trafficData.getDescription());
            map.put("speed",String.valueOf(trafficData.getSpeed()));
            map.put("time",String.valueOf(trafficData.getCreatedAt()));
            list.add(map);
        }
        session.close();

        return list;
    }

    @Override
    public ArrayList<HashMap<String, String>> getStartDirection(Direction direction) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        List<TrafficData> data = session.createQuery("select d from TrafficData d where d.description like :query", TrafficData.class)
                .setParameter("query","Started from "+direction.getDirection()+"%")
                .getResultList();

        System.out.println("Data :"+data);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for(TrafficData trafficData : data){
            HashMap<String, String> map = new HashMap<>();
            map.put("description",trafficData.getDescription());
            map.put("speed",String.valueOf(trafficData.getSpeed()));
            map.put("time",String.valueOf(trafficData.getCreatedAt()));
            list.add(map);
        }
        session.close();

        return list;
    }

    @Override
    public ArrayList<HashMap<String, String>> getEndDirection(Direction direction) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        List<TrafficData> data = session.createQuery("select d from TrafficData d where d.description like:query", TrafficData.class)
                .setParameter("query","% "+direction.getDirection())
                .getResultList();

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for(TrafficData trafficData : data){
            HashMap<String, String> map = new HashMap<>();
            map.put("description",trafficData.getDescription());
            map.put("speed",String.valueOf(trafficData.getSpeed()));
            map.put("time",String.valueOf(trafficData.getCreatedAt()));
            list.add(map);
        }
        session.close();

        return list;
    }

    @Override
    public ArrayList<HashMap<String, String>> getByPath(Direction direction) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        List<TrafficData> data = session.createQuery("select d from TrafficData d where d.description = :query", TrafficData.class)
                .setParameter("query",direction.getDirection())
                .getResultList();

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for(TrafficData trafficData : data){
            HashMap<String, String> map = new HashMap<>();
            map.put("description",trafficData.getDescription());
            map.put("speed",String.valueOf(trafficData.getSpeed()));
            map.put("time",String.valueOf(trafficData.getCreatedAt()));
            list.add(map);
        }
        session.close();

        return list;
    }
    @Override
    public long getCount(Direction direction){
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        Long count = session.createQuery("select count (c.id) from TrafficData c where c.description = :query",Long.class)
                .setParameter("query", direction.getDirection())
                .getSingleResult();

        session.close();
        return count;
    }

    @Override
    public long getCountByStart(Direction direction) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        long count = session.createQuery("select count (c.id) from TrafficData c where c.description like :query", Long.class)
                .setParameter("query", "Started from "+direction.getDirection()+"%")
                .getSingleResult();
        session.close();

        return count;
    }

    @Override
    public long getCountByEnd(Direction direction) {
        SessionFactory factory = hibernateUtilBean.getSessionFactory();
        Session session = factory.openSession();

        Long count = session.createQuery("select count(c.id) from TrafficData c where c.description like :query",Long.class)
                .setParameter("query","%"+direction.getDirection())
                .getSingleResult();
        session.close();

        return count;
    }

}
