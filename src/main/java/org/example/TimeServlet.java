package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    public static final String DEFAULT_TIME_ZONE = "UTC";
    public static final String DATE_PATTERN = "yyy-MM-dd HH:mm:ss z";
    public static final String TIME_ZONE_PARAM_NAME = "timezone";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        resp.getWriter().write("<h1>Current time</h1>");

        ZoneId zone = ZoneId.of(getTimeZone(req));

        resp.getWriter().write("<h2>zone: ${timeZone}</h2>".replace("${timeZone}", zone.toString()));
        resp.getWriter().write("<br>");

        // Get current time
        ZonedDateTime currentTime = ZonedDateTime.now(zone);

        // Set time template
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        //Format time
        String formattedTime = dateTimeFormatter.format(currentTime);

        resp.getWriter().write(formattedTime);

        resp.getWriter().close();
    }

    private String getTimeZone(HttpServletRequest request){
        if (request.getParameterMap().containsKey(TIME_ZONE_PARAM_NAME)){
            //Replace is needed to read "+"-sign as a part of query parameter, because it is read as space (" ")
            return request.getParameter(TIME_ZONE_PARAM_NAME).replace(" ", "+");
        }else{
            return DEFAULT_TIME_ZONE;
        }
    }


}