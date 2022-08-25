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
 * index servlet that serves the index endpoint
 */
@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    private final ThreadSafeDataStorage data;

    public IndexServlet(ThreadSafeDataStorage data) {
        this.data = data;
    }

    /**
     * Get method for the index endpoint
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String word = req.getParameter("word");
        String num = req.getParameter("num");

        word = StringEscapeUtils.escapeHtml4(word);
        num = StringEscapeUtils.escapeHtml4(num);
        int numInt = 0;

        if (num != null){
            numInt = Integer.parseInt(num);
        }

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = resp.getWriter();

            out.println(data.getIndexJson(word, numInt));

    }
}
