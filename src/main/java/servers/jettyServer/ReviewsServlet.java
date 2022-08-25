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
 * Servlet that handles the review endpoint
 */
@SuppressWarnings("serial")
public class ReviewsServlet extends HttpServlet {
    private final ThreadSafeDataStorage data;

    public ReviewsServlet(ThreadSafeDataStorage data) {
        this.data = data;
    }

    /**
     * Get method for the review endpoint, prints out json object
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String hotelId = req.getParameter("hotelId");
        String num = req.getParameter("num");

        num = StringEscapeUtils.escapeHtml4(num);
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);

        int numInt = 0;

        if (num != null){
            numInt = Integer.parseInt(num);
        }

        resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = resp.getWriter();

            out.println(data.getReviewJsonObject(hotelId, numInt));

    }
}
