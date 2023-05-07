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
//        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
        return null;
    }

    public void save(Person person) {
//        person.setId(++PEOPLE_COUNT);
//        people.add(person);

        try {
            Statement statement = connection.createStatement();
            //создаем объект который общается с БД

            String SQL = "INSERT INTO Person VALUES(" + 1 + ",'"
                    + person.getName() + ","
                    + "'," + person.getAge() + ",'"
                    + person.getEmail() + "')";
            // ТАК ДЕЛАТЬ НЕ НАДО...
            // Запрос вида: INSERT INTO Person VALUES(1, 'Tom', 18, 'asge@mail.ru')

            statement.executeUpdate(SQL);
            //Метод executeUpdate() изменяет данные в БД, в этом случае добавляя нового человека
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person updatePerson){
//        Person personToBeUpdated = show(id);
//
//        personToBeUpdated.setName(updatePerson.getName());
//        personToBeUpdated.setAge(updatePerson.getAge());
//        personToBeUpdated.setEmail(updatePerson.getEmail());
    }

    public void delete(int id) {
//        people.removeIf(p -> p.getId() == id);
    }
}
