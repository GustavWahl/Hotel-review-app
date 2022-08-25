package servers.jettyServer;

import hotelapp.ThreadSafeDataStorage;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for the custom weather endpoint
 */
@SuppressWarnings("serial")
public class WeatherServlet extends HttpServlet {
    private final ThreadSafeDataStorage data;

    public WeatherServlet(ThreadSafeDataStorage data) {
        this.data = data;
    }

    /**
     * Get method for the weather endpoint, prints out weather json object
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String hotelId = req.getParameter("hotelId");

        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = resp.getWriter();

        out.println(data.getWeather(hotelId));
    }
}
