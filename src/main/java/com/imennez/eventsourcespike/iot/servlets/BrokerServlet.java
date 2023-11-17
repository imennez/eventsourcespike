package com.imennez.eventsourcespike.iot.servlets;

import com.imennez.eventsourcespike.iot.AmzIotInitException;
import com.imennez.eventsourcespike.iot.AmzIoTMqClient;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "IoTBrokerServlet",
        description = "Servlet that serves as a IoT broker",
        urlPatterns = {"/iot-broker"}
)
public class BrokerServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            AmzIoTMqClient client = new AmzIoTMqClient(
                    "amqps://<uri>",
                    "<user>>",
                    "<password>");

            resp.setContentType("text/event-stream");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setHeader("Connection", "keep-alive");

            final PrintWriter out = resp.getWriter();
            client.bind((msg) -> {
                System.out.println("Message from MQ: " + msg);
                out.write("data: " + msg +"\n\n");
                out.flush();
            });

            do {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while(!out.checkError());

            System.out.println("DONE!!!");
            client.close();
            out.close();

        } catch (AmzIotInitException e) {
            // If client could not be initialized, return the error message without establishing an event stream connection
            System.out.println("Error: " + e.getMessage());
            resp.setContentType("text/plain");
            final PrintWriter out = resp.getWriter();
            out.print(e.getMessage());
            out.close();
        }
    }
}