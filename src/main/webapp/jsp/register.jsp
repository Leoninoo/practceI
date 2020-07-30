<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div>
    <div>
        <h1>Регистрация</h1>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <label for="login">Логин
            <input class="input-field" type="text" id="login" name="login" required>
        </label>
        <label for="password">Пароль
            <input class="input-field" type="password" id="password" name="password" required>
        </label>
        <label for="email">E-mail
            <input class="input-field" type="email" id="email" name="email" required>
        </label>
        <input type="submit" value="Зарегистрироваться">
    </form>
</div>
</body>
</html>