<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>async processing</title>
</head>

<body>
<h4>Performance Tuning with Async Servlets</h4>

<input type="button" id="regular" value="Regular Servlet" onclick="regular()"/>
<div id="respRegular">Response:</div><br/>

<input type="button" id="async" value="Async Servlet" onclick="async()"/>
<div id="respAsync">Response:</div>

<script>
    function regular() {
        invoke("respRegular", "regular");
    }

    function async() {
        invoke("respAsync", "async");
    }

    function invoke(elementId, servletUrn) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var contentElement = document.getElementById(elementId);
                contentElement.innerHTML = contentElement.innerHTML + " " + xmlhttp.responseText;
            }
        };
        xmlhttp.open("GET", servletUrn, true);
        xmlhttp.send();
    }
</script>

</body>
</html>