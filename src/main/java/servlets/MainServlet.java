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

@WebServlet("/main")
public class MainServlet extends HttpServlet {
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
        //Получаем список всех пользователей и кладем его в запрос
        List<User> users = usersDao.findAll();
        req.setAttribute("users", users);

        //Получаем список всех куки
        Cookie[] cookies = req.getCookies();

        //Ищем куку 'id'
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                //Если находим - получаем пользоавателя по id и передаем его в запрос
                if (cookie.getName().equals("id")) {
                    if (cookie.getValue().isEmpty())
                        break;
                    User user = usersDao.findById(Long.parseLong(cookie.getValue()));
                    req.setAttribute("user", user);
                    break;
                }
            }
        }

        //Передаем запрос на jsp страницу
        req.getServletContext().getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
    }
}
