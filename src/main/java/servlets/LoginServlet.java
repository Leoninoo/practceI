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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    UsersDao usersDao;

    //Подключаемся к базе данных
    @Override
    public void init() {
        //Данные для подключения к бд
        String url = "jdbc:mysql://localhost:3306/practice?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String userName = "root";
        String password = "524710kleo";

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
        //Передаем запрос на jsp страницу
        req.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Получаем данные из формы на jsp страницы
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        //Если одно из полей пустое - обновляем страницу
        if(login.isEmpty() || password.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        //Получаем список всех пользователей
        List<User> users = usersDao.findAll();

        //Создаем пустую переменную для хранения пользователя
        User user = null;

        //Ищем пользователя с таким же логином и паролем и кладем его в переменную 'user'
        for(User u : users) {
            if(login.equals(u.getLogin())) {
                if(password.equals(u.getPassword())) {
                    user = u;
                    break;
                }
            }
        }

        //Если пользователь с таким логином и паролем не был найден - обновляем страницу
        if(user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        //Создаем куку с id залогиненного пользователя
        Cookie cookie = new Cookie("id", user.getId() + "");

        //Добавляем куку и перенаправляем на главную страницу
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath() + "/main");
    }
}
