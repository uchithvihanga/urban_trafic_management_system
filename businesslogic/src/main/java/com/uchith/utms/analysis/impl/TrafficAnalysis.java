package com.uchith.utms.analysis.impl;

import com.uchith.utms.analysis.remote.Analyze;
import com.uchith.utms.service.remote.DataService;
import com.uchith.utms.service.remote.Direction;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

@Singleton
public class TrafficAnalysis implements Analyze {
    private LinkedList<HashMap<String, String>> list;

    @PostConstruct
    public void init(){
        this.list = new LinkedList<>();
    }

    @Override
    public HashMap analyze() {
        HashMap maps = new HashMap();
        maps.put("vehicleCount", getVehicleCount());
        maps.put("highTrafficRoute",getHighTrafficRoute());
        maps.put("countByRoute",getCountByRoute());

        return maps;
    }

    @Override
    public HashMap<String, String> realTime() {

        String time = list.getLast().get("time");
        String avgSpeed = String.valueOf(calculateAvgSpeed());

        HashMap<String, String> map = new HashMap<>();
        map.put("avgSpeed", avgSpeed);
        map.put("time", time);

        return map;
    }

    @Override
    public void addToAnalyze(HashMap<String, String> map) {
        list.add(map);
    }

    private double calculateAvgSpeed(){
        Iterator<HashMap<String, String>> iterator = list.iterator();

        double avgSpeed = 0;
        int count = 0;

        while (iterator.hasNext()){
            avgSpeed += Double.parseDouble(list.poll().get("speed"));
            count++;
        }
        return avgSpeed/count;
    }
    private HashMap<String, Long> getVehicleCount(){
        try {
            InitialContext context = new InitialContext();
            DataService dataService = (DataService) context.lookup("java:global/deployment-1.0/com.uchith.utms-dataaccess-1.0/TrafficDataService");
            long startLeft = dataService.getCountByStart(Direction.LEFT);
            long startRight = dataService.getCountByStart(Direction.RIGHT);
            long startNorth = dataService.getCountByStart(Direction.NORTH);
            long startSouth = dataService.getCountByStart(Direction.SOUTH);

            long endLeft = dataService.getCountByEnd(Direction.LEFT);
            long endRight = dataService.getCountByEnd(Direction.RIGHT);
            long endNorth = dataService.getCountByEnd(Direction.NORTH);
            long endSouth = dataService.getCountByEnd(Direction.SOUTH);


            HashMap<String, Long> map = new HashMap<>();
            map.put("startLeft",startLeft);
            map.put("startRight",startRight);
            map.put("startNorth",startNorth);
            map.put("startSouth",startSouth);
            map.put("endLeft",endLeft);
            map.put("endRight",endRight);
            map.put("endNorth",endNorth);
            map.put("endSouth",endSouth);
            map.put("allcount",Long.valueOf(String.valueOf(dataService.getAll().size())));

            return map;

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
    private String getHighTrafficRoute(){
        HashMap <String, Long> map = getCountByRoute();

        HashMap<Long, String> hashMap = new HashMap<>();
        for (Map.Entry<String, Long> set : map.entrySet()){
            hashMap.put(set.getValue(), set.getKey());
        }

        ArrayList<Long> counts = new ArrayList<>(hashMap.keySet());
        Long max = Collections.max(counts);

        String s = hashMap.get(max);
        return s;
    }
    private HashMap<String, Long> getCountByRoute(){
        try {
            InitialContext context = new InitialContext();
            DataService dataService = (DataService) context.lookup("java:global/deployment-1.0/com.uchith.utms-dataaccess-1.0/TrafficDataService");

            HashMap<String, Long> map = new HashMap<>();

            map.put(Direction.LEFT_TO_RIGHT.getDirection(),dataService.getCount(Direction.LEFT_TO_RIGHT));
            map.put(Direction.LEFT_TO_NORTH.getDirection(),dataService.getCount(Direction.LEFT_TO_NORTH));
            map.put(Direction.LEFT_TO_SOUTH.getDirection(),dataService.getCount(Direction.LEFT_TO_SOUTH));
            map.put(Direction.RIGHT_TO_LEFT.getDirection(),dataService.getCount(Direction.RIGHT_TO_LEFT));
            map.put(Direction.RIGHT_TO_NORTH.getDirection(),dataService.getCount(Direction.RIGHT_TO_NORTH));
            map.put(Direction.RIGHT_TO_SOUTH.getDirection(),dataService.getCount(Direction.RIGHT_TO_SOUTH));
            map.put(Direction.NORTH_TO_SOUTH.getDirection(),dataService.getCount(Direction.NORTH_TO_SOUTH));
            map.put(Direction.NORTH_TO_LEFT.getDirection(),dataService.getCount(Direction.NORTH_TO_LEFT));
            map.put(Direction.NORTH_TO_RIGHT.getDirection(),dataService.getCount(Direction.NORTH_TO_RIGHT));
            map.put(Direction.SOUTH_TO_NORTH.getDirection(),dataService.getCount(Direction.SOUTH_TO_NORTH));
            map.put(Direction.SOUTH_TO_LEFT.getDirection(),dataService.getCount(Direction.SOUTH_TO_LEFT));
            map.put(Direction.SOUTH_TO_RIGHT.getDirection(),dataService.getCount(Direction.SOUTH_TO_RIGHT));

            return map;

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
