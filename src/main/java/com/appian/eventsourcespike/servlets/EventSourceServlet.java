package com.appian.eventsourcespike.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "EventSourceServlet",
        description = "Example Servlet Using Annotations",
        urlPatterns = {"/eventsource"}
)
public class EventSourceServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        PrintWriter writer = resp.getWriter();
        do {
            String msg = "data: Timestamp=" + System.currentTimeMillis() +"\n\n";
            System.out.println(msg);
            writer.write(msg);
            writer.flush();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while(!writer.checkError());

        System.out.println("DONE!!!");
        writer.close();
    }
}
