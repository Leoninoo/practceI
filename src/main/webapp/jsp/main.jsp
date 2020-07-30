<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<body>
<div>
    <div>
        <c:choose>
            <c:when test="${user == null}">
                <a href="${pageContext.request.contextPath}/login"><button>Вход</button></a>
                <a href="${pageContext.request.contextPath}/register"><button>Регистрация</button></a>
            </c:when>
            <c:otherwise>
                <h3>Привет, ${user.login}</h3>
                <a href="${pageContext.request.contextPath}/change?id=${user.id}"><button>Изменить данные</button></a>
            </c:otherwise>
        </c:choose>

    </div>

    <div>
        Другие пользователи:
    </div>
    <table>
        <tr>
            <th style="border: 1px solid black">Логин</th>
            <th style="border: 1px solid black">E-mail</th>
        </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td style="border: 1px solid black">${user.login}</td>
                <td style="border: 1px solid black">${user.email}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>