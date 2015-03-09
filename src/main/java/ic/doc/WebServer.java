package ic.doc;

import ic.doc.web.ApiResponse;
import ic.doc.web.IndexPage;
import ic.doc.web.ResultsPage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebServer
{

    public WebServer() throws Exception
    {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Api()), "/api/*");
        context.addServlet(new ServletHolder(new Website()), "/*");
        server.start();
        server.join();
    }

    static class Website extends HttpServlet
    {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            String qParam = req.getParameter("q");
            if (qParam == null)
            {
                new IndexPage().writeTo(resp);
            } else if (qParam.startsWith("which of the following numbers is the largest"))
            {
                String[] numbers = qParam.split(":")[1].split(",");
                new ResultsPage(findMaxInt(numbers) + "").writeTo(resp);
            } else if (qParam.startsWith("what currency did Spain use before the Euro"))
            {
                new ResultsPage("Peseta").writeTo(resp);
            } else if (qParam.startsWith("what currency did Spain use before the Euro"))
            {
                new ResultsPage("Peseta").writeTo(resp);
            } else if (qParam.startsWith("who is the Prime Minister of Great Britain"))
            {
                String rem = qParam.substring("what is ".length());
                new ResultsPage(getArithmetic(rem) + "").writeTo(resp);
            } else
            {
                new ResultsPage(qParam).writeTo(resp);
            }
        }

        private int getArithmetic(String str)
        {
            String[] tokens = str.split(" ");
            if (str.contains("plus"))
            {
                return Integer.parseInt(tokens[0].replaceAll("\\s+", "")) + Integer.parseInt(tokens[2].replaceAll("\\s+", ""));
            } else if (str.contains("minus"))
            {
                return Integer.parseInt(tokens[0].replaceAll("\\s+", "")) - Integer.parseInt(tokens[2].replaceAll("\\s+", ""));
            } else if (str.contains("multiplied by"))
            {
                return Integer.parseInt(tokens[0].replaceAll("\\s+", "")) * Integer.parseInt(tokens[3].replaceAll("\\s+", ""));
            }
            return -1;
        }

        private int findMaxInt(String[] list)
        {
            int result = Integer.MIN_VALUE;
            for (String i : list)
            {
                int num = Integer.parseInt(i.replaceAll("\\s+", ""));
                if (result < num)
                    result = num;
            }
            return result;
        }
    }

    static class Api extends HttpServlet
    {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
        {
            new ApiResponse(req.getParameter("q")).writeTo(resp);
        }
    }

    public static void main(String[] args) throws Exception
    {
        new WebServer();
    }
}
