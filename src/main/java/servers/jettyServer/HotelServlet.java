package servers.jettyServer;

import hotelapp.ThreadSafeDataStorage;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HotelServlet that servers the hotel endpoint
 */
@SuppressWarnings("serial")
public class HotelServlet extends HttpServlet {
    private final ThreadSafeDataStorage data;

    public HotelServlet(ThreadSafeDataStorage data) {
        this.data = data;
    }

    /**
     * Get method for the hotel endpoint
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String hotelId = req.getParameter("hotelId");

        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        if (hotelId.equals("") || hotelId == null){

        }

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = resp.getWriter();

        out.println(data.getHotelJsonObject(hotelId));
    }
}
