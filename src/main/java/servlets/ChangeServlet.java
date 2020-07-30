package servlets;

import crud.UsersDao;
import crud.UsersDaoImpl;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/change")
public class ChangeServlet extends HttpServlet {
    UsersDao usersDao;

    //Подключаемся к базе данных
    @Override
    public void init() {
        //Данные для подключения к бд
        String url = "jdbc:mysql://localhost:3306/practice?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String userName = "root";
        String password = "root";

        try {
            //подключаем драйвер для mySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Подключаемся к бд и передаем подключение нашему crud
            Connection connection = DriverManager.getConnection(url, userName, password);
            usersDao = new UsersDaoImpl(connection);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Получаем id из url
        long id = Long.parseLong(req.getParameter("id"));

        //Находим пользователя по id и передаем его в запрос
        req.setAttribute("user", usersDao.findById(id));

        //Передаем запрос на jsp страницу
        req.getServletContext().getRequestDispatcher("/jsp/change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Получаем id из url
        long id = Long.parseLong(req.getParameter("id"));

        //Если была нажата кнопка "Удалить аккаунт" - удаляем пользователя из бд
        if(req.getParameter("delete") != null) {
            usersDao.delete(id);

            //Убираем id пользователя из куки
            Cookie cookie = new Cookie("id", "");
            resp.addCookie(cookie);

            //Перенаправляем запрос на главную страницу
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }

        //Если была нажата кнопка "Выйти из аккаунта" - удаляем id куки
        if(req.getParameter("exit") != null) {
            //Убираем id пользователя из куки
            Cookie cookie = new Cookie("id", "");
            resp.addCookie(cookie);

            //Перенаправляем запрос на главную страницу
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }

        //Получаем данные из формы на jsp страницы
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        //Если одно из полей пустое - обновляем страницу
        if(login.isEmpty() || password.isEmpty() || email.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/change?id=" + id);
            return;
        }

        //Получаем список всех пользователей
        List<User> users = usersDao.findAll();

        //Если логин или email одного из пользователей совпадают с новым пользователем - обновляем страницу
        for(User u : users) {
            if(u.getId() == id)
                continue;

            if(login.equals(u.getLogin()) || email.equals(u.getEmail())) {
                resp.sendRedirect(req.getContextPath() + "/change?id=" + id);
                return;
            }
        }

        //Создаем нового пользователя и присваеваем ему id старого пользователя
        User user = new User(login, password, email);
        user.setId(id);

        //Обновляем пользователя в бд
        usersDao.update(user);

        //Перенаправляем запрос на главную страницу
        resp.sendRedirect(req.getContextPath() + "/main");
    }
}
