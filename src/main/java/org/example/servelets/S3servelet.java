package org.example.servelets;

import org.example.services.s3service;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.plaf.synth.Region;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/upload")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class S3servelet extends HttpServlet{
    s3service ss = new s3service();
    @Override

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

//        String bucketName = request.getParameter("bucketName");

        Part filePart = request.getPart("file");

        String fileName = filePart.getSubmittedFileName();

        try (InputStream inputStream = filePart.getInputStream()) {

            ss.uploadAFile(
                    "my-awspra-bucket",
                    fileName,
                    inputStream,
                    filePart.getSize()
            );


            response.setContentType("text/html");
            response.getWriter().println("sucessfully pushed to s3 bucket");

        } catch (Exception e) {

            e.printStackTrace();


        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/Input_Page.jsp")
                .forward(req, resp);
    }
}
