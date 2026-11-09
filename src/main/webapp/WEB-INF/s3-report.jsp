
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>S3 CloudWatch Report</title>
</head>

<body>

<h2>S3 Bucket CloudWatch Report</h2>

<pre>
    ${report}
</pre>

<a href="${pageContext.request.contextPath}/s3/report">
    Refresh Report
</a>

</body>
</html>
