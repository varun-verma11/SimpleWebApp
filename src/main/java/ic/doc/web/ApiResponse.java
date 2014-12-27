package ic.doc.web;

import ic.doc.QueryProcessor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ApiResponse {

    private final String query;

    public ApiResponse(String query) {
        this.query = query;
    }

    public void writeTo(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        writer.println(new QueryProcessor().process(query));
    }
}
