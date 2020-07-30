<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div>
    <div>
        <h1>${user.login}</h1>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/change?id=${user.id}">
        <label for="login">Логин
            <input class="input-field" type="text" id="login" name="login" value="${user.login}" required>
        </label>
        <label for="password">Пароль
            <input class="input-field" type="password" id="password" name="password" value="${user.password}" required>
        </label>
        <label for="email">E-mail
            <input class="input-field" type="email" id="email" name="email" value="${user.email}" required>
        </label>
        <input type="submit" name="change" value="Изменить данные">
        <input type="submit" name="delete" value="Удалить аккаунт">
        <input type="submit" name="exit" value="Выйти из аккаунта">
    </form>
</div>
</body>
</html>