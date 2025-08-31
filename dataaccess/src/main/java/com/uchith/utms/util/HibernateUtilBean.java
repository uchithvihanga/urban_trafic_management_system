package com.uchith.utms.util;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Singleton
public class HibernateUtilBean {
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init(){
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Lock(LockType.READ)
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
