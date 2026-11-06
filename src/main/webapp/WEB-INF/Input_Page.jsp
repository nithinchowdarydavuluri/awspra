<!DOCTYPE html>
<html>
<head>
    <title>Upload to AWS S3</title>
</head>
<body>

<h2>Upload File to AWS S3</h2>

<form action="${pageContext.request.contextPath}/upload"
      method="POST"
      enctype="multipart/form-data">


    <label>Select File:</label>
    <input type="file" name="file" required>

    <br><br>

    <button type="submit">Upload to S3</button>

</form>

<p>${message}</p>

</body>
</html>