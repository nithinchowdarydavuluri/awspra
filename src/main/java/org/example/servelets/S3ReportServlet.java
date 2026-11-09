
package org.example.servelets;

import org.example.services.PythonService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/s3/report")
public class S3ReportServlet extends HttpServlet {

    private final PythonService pythonService =
            new PythonService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String bucketName = "my-awspra-bucket";
        String region = "us-east-1";

        try {

            String report = pythonService.generateS3Report(
                    bucketName,
                    region
            );

            request.setAttribute("report", report);

            request.getRequestDispatcher(
                    "/WEB-INF/s3-report.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to generate S3 report", e
            );
        }
    }
}
