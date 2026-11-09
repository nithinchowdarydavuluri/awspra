<%@ page import="software.amazon.awssdk.services.s3.model.HeadObjectResponse" %>

<%
    HeadObjectResponse details =
            (HeadObjectResponse) request.getAttribute("details");

    String objectKey =
            (String) request.getAttribute("objectKey");
%>

<h2>Selected Object Information</h2>

<table border="1">
    <tr>
        <td>Object Key</td>
        <td><%= objectKey %></td>
    </tr>

    <tr>
        <td>File Size</td>
        <td><%= details.contentLength() %> bytes</td>
    </tr>

    <tr>
        <td>Content Type</td>
        <td><%= details.contentType() %></td>
    </tr>

    <tr>
        <td>ETag</td>
        <td><%= details.eTag() %></td>
    </tr>

    <tr>
        <td>Last Modified</td>
        <td><%= details.lastModified() %></td>
    </tr>

    <tr>
        <td>Storage Class</td>
        <td><%= details.storageClass() %></td>
    </tr>
</table>