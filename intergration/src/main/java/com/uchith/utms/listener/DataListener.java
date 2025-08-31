package com.uchith.utms.listener;

import com.uchith.utms.analysis.remote.Analyze;
import com.uchith.utms.service.remote.DataService;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDateTime;
import java.util.HashMap;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup",propertyValue = "trafficQueue")
        }
)
public class DataListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            HashMap<String, Object> data = message.getBody(HashMap.class);

            InitialContext context = new InitialContext();
            DataService dataService = (DataService) context.lookup("java:global/deployment-1.0/com.uchith.utms-dataaccess-1.0/TrafficDataService");
            Analyze analyze = (Analyze) context.lookup("java:global/deployment-1.0/com.uchith.utms-businesslogic-1.0/TrafficAnalysis");

            HashMap<String, String> map = new HashMap<>();
            map.put("speed",String.valueOf(data.get("speed")));
            map.put("time",String.valueOf(LocalDateTime.now().toLocalTime()));

            analyze.addToAnalyze(map);
            dataService.save(data);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
