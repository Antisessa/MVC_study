package ru.antisessa.mvctest.dao;

import org.springframework.stereotype.Component;
import ru.antisessa.mvctest.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    //Поле для адреса обращения к базе данных
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "23536293";

    private static Connection connection;
    //из пакета java.sql создаем статический экземпляр класса для соединения
    // и в каждом методе будем обращаться к нему

    static {
        try {
            Class.forName("org.postgresql.Driver");
            //проверяем наличие драйвера JDBC
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //станавливаем соединение, передавая в аргументах все данные для подключения
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        //Создаем динамический массив List с типом Person

        try {
            Statement statement = connection.createStatement();
            //также из пакета java.sql создаем объект который будет общаться с базой данных
            String SQL = "SELECT * FROM Person";
            //Запрос к базу данных на языке SQL
            ResultSet resultSet =  statement.executeQuery(SQL);
            //Объект ResultSet который хранит в себе тело ответа от БД на запрос
            //метод executeQuery() не изменяет данные в БД

            //цикл чтобы пройтись по всему ответу, каждый next() цикла будет перемещаться на следующую строчку ответа
            while(resultSet.next()) {
                Person person = new Person();
                //каждая ветка цикла создает Person и назначает ему через сеттеры значения одной из строчек ответа

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
                //и добавляет этого человека к массиву
            }

        } catch (SQLException e) {
            //Все ошибки вызванные java.sql помечаются как SQLException
            e.printStackTrace();
        }

        return people;
    }

    public Person show(int id){

        Person person = null;
        //Создание объекта Person в который мы поместим результат запроса к БД
        //Стоит учесть что объект создается вне блока try чтобы return мог вернуть его даже при SQLException

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id = ?");
            //Компилируем SQL запрос к БД который выбирает все поля в таблице Person,
            // удовлетворяющие условию id = id введенному в GET запросе

            preparedStatement.setInt(1, id);
            //В скомпилированный SQL запрос передается параметр id

            ResultSet resultSet = preparedStatement.executeQuery();
            //Выполняем get запрос к БД

            resultSet.next();
            //Смещаем указатель на следующую строку (первую)

            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    public void save(Person person) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");
            //Компилируем SQL запрос к БД

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            //Добавляем аргументы запроса за место ? по индексу где они должны быть

            preparedStatement.executeUpdate();
            //Метод executeUpdate() изменяет данные в БД, в этом случае добавляя нового человека
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person updatePerson){
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name =?,age =?,email=? WHERE id = ?");

            preparedStatement.setString(1, updatePerson.getName());
            preparedStatement.setInt(2, updatePerson.getAge());
            preparedStatement.setString(3, updatePerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Person WHERE id=?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
