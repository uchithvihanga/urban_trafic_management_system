package com.uchith.utms.analysis.remote;

import jakarta.ejb.Remote;

import java.util.ArrayList;
import java.util.HashMap;

@Remote
public interface Analyze {
    HashMap analyze();
    HashMap<String, String> realTime();
    void addToAnalyze(HashMap<String, String> map);
}
