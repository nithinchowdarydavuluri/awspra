package org.example.servelets;

import org.example.services.s3service;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import  java.util.*;

@WebServlet("/s3/list")
public class S3GetInfoServelet extends HttpServlet {
    s3service ss = new s3service();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bucketName = "my-awspra-bucket";

        List<S3Object> objects =
                ss.getAllObjects(bucketName);

        req.setAttribute("objects", objects);
        req.setAttribute("bucketName", bucketName);
        req.getRequestDispatcher("/WEB-INF/ListPage.jsp")
                .forward(req, resp);
    }
}
