package com.uchith.utms.service.remote;

import com.uchith.utms.entity.TrafficData;
import jakarta.ejb.Remote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Remote
public interface DataService {
    void save(HashMap<String, Object> dataMap);
    ArrayList<HashMap<String,String>> getAll();
    ArrayList<HashMap<String,String>> getDateBetween(LocalDateTime from, LocalDateTime to);
    ArrayList<HashMap<String,String>> getStartDirection(Direction direction);
    ArrayList<HashMap<String,String>> getEndDirection(Direction direction);
    ArrayList<HashMap<String,String>> getByPath(Direction direction);
    long getCount(Direction direction);
    long getCountByStart(Direction direction);
    long getCountByEnd(Direction direction);

}
