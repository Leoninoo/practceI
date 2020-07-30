package servlets;

import crud.UsersDao;
import crud.UsersDaoImpl;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
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
        //Передаем запрос на jsp страницу
        req.getServletContext().getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Получаем данные из формы на jsp страницы
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        //Если одно из полей пустое - обновляем страницу
        if(login.isEmpty() || password.isEmpty() || email.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        //Получаем список всех пользователей
        List<User> users = usersDao.findAll();

        //Если логин или email одного из пользователей совпадают с новым пользователем - обновляем страницу
        for(User u : users) {
            if(login.equals(u.getLogin()) || email.equals(u.getEmail())) {
                resp.sendRedirect(req.getContextPath() + "/register");
                return;
            }
        }

        //Создаем нового пользователя и созраняем его в бд
        usersDao.save(new User(login, password, email));

        //Перенаправляем запрос на главную страницу
        resp.sendRedirect(req.getContextPath() + "/main");
    }
}
