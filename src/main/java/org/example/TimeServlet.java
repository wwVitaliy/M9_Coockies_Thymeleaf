package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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
    public static final String TIME_ZONE_COOKIE_NAME = "lastTimezone";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write("<h1>Current time</h1>");

        ZoneId zone = ZoneId.of(getTimeZone(request, response));

        response.getWriter().write("<h2>Time zone: ${timeZone}</h2>".replace("${timeZone}", zone.toString()));
        response.getWriter().write("<br>");

        // Get current time
        ZonedDateTime currentTime = ZonedDateTime.now(zone);
        // Set time template
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        // Format time
        String formattedTime = dateTimeFormatter.format(currentTime);

        response.getWriter().write(formattedTime);
        response.getWriter().close();
    }

    /**
     * Reads timezone from HTTP request.
     * If there is timezone parameter then returns it and sets it as a cookie.
     * If there is no timezone parameter then try to read timezone from cookies.
     * If there is neither timezone parameter nor timezone cookie then returns default timezone.
     */
    private String getTimeZone(HttpServletRequest request, HttpServletResponse response) {
        //check timezone in request parameter
        if (request.getParameterMap().containsKey(TIME_ZONE_PARAM_NAME)) {

            //Replace is needed to read "+"-sign as a part of query parameter, because it is read as space (" ")
            String timezone = request.getParameter(TIME_ZONE_PARAM_NAME).replace(" ", "+");

            //add cookie to response
            response.addCookie(new Cookie(TIME_ZONE_COOKIE_NAME, timezone));

            return timezone;
        }

        //check timezone in cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(TIME_ZONE_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }

        //return default timezone
        return DEFAULT_TIME_ZONE;
    }
}