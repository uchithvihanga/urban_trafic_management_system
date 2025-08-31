package com.uchith.utms.web;

import com.uchith.utms.analysis.remote.Analyze;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

@WebServlet(name = "GraphServlet",value = "/graph")
public class GraphServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            InitialContext context = new InitialContext();
            Analyze analyze = (Analyze) context.lookup("java:global/deployment-1.0/com.uchith.utms-businesslogic-1.0/TrafficAnalysis");
            HashMap<String, String> map = analyze.realTime();

            resp.getWriter().write(new Gson().toJson(map));

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
