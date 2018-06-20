<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@include file="validausuario.jsp"%>
<%
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>HGC</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.js"></script>
        <style>
            #logo {
                display: block;
                margin-left: auto;
                margin-right: auto;
                opacity: 0.8;
                -webkit-transition: all 0.5s ease;
                -moz-transition: all 0.5s ease;
                -o-transition: all 0.5s ease;
                -webkit-box-reflect: below 0px -webkit-gradient(linear, left top, left bottom, from(transparent), color-stop(.7, transparent), to(rgba(0,0,0,0.1)));
            }

            .indicador {
                font-size:large;
                font-weight: bold;
            }
            
            .caja {
                background: #FCF9F9;
            }
        </style>
    </head>
    <body>
        <%@ include file="header.jsp"%>
        <div class="container">
            <%  if (perfil.equals("1") || perfil.equals("4") ) { %>
            <jsp:include page="indicadorMensajeria.jsp" />
            <% } else {  %>
            <img src="images/electricaribe.jpg" id="logo" class="img-rounded">
            <% }%>
        </div>
    </body>
    <script src="js/bootstrap.min.js"></script>
</html>