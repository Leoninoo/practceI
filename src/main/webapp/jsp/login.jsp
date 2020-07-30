<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div>
    <div>
        <h1>Вход</h1>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <label for="login">Логин
            <input class="input-field" type="text" id="login" name="login" required>
        </label>
        <label for="password">Пароль
            <input class="input-field" type="password" id="password" name="password" required>
        </label>
        <input type="submit" value="Войти">
    </form>
</div>
</body>
</html>