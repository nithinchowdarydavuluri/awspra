<%@ page import="java.util.List" %>
<%@ page import="software.amazon.awssdk.services.s3.model.S3Object" %>

<h2>S3 Bucket Objects</h2>

<table >
    <tr>
        <th>Object Name</th>
        <th>Size</th>
        <th>Action</th>
    </tr>

    <%
        List<S3Object> objects =
                (List<S3Object>) request.getAttribute("objects");

        String bucketName =
                (String) request.getAttribute("bucketName");

        for (S3Object object : objects) {
    %>

    <tr>
        <td><%= object.key() %></td>
        <td><%= object.size() %></td>

        <td>
            <form action="details" method="get">
                <input type="hidden"
                       name="bucket"
                       value="<%= bucketName %>">

                <input type="hidden"
                       name="key"
                       value="<%= object.key() %>">

                <button type="submit">View Details</button>
            </form>
        </td>
    </tr>

    <% } %>

</table>