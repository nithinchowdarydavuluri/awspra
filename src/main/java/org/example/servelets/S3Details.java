package org.example.servelets;

import org.example.services.s3service;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/s3/details")
public class S3Details extends HttpServlet {
    s3service ss = new s3service();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bucketName = req.getParameter("bucket");
        String objectKey = req.getParameter("key");

        HeadObjectResponse details =
                ss.getObjectInformation(
                        bucketName, objectKey);

        req.setAttribute("details", details);
        req.setAttribute("objectKey", objectKey);

        req.getRequestDispatcher("/WEB-INF/s3-details.jsp")
                .forward(req, resp);
    }


}
