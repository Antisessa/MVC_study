package ru.antisessa.mvctest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.antisessa.mvctest.models.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id){
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
        //Лямбда выражение осуществляет поиск соответствий, и либо выводит объект класса Person, либо если такого id нет - null,
        //метод findAny возвращает Optional<>, поэтому он подходит под возвращаемый тип метода

        //Второй аргумент передает id для внутреннего PreparedStatement который неявно использует JdbcTemplate,
        //этот id будет поставлен вместо ? в SQL запрос
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
        //Метод отображения человека по почте, в качестве аргумента принимающий значение email, и подставляющий
        //его в PreparedStatement вместо ?, лямбда выражение осуществляет поиск
        //и возвращает Optional<> - подходящего человека или null
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, age, email, address) VALUES(?, ?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatePerson){
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=? WHERE id=?",
                updatePerson.getName(), updatePerson.getAge(), updatePerson.getEmail(), updatePerson.getAddress(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public void BatchUpdate(){
        List<Person> people = create100();
        jdbcTemplate.batchUpdate("INSERT INTO Person(name, age, email) VALUES(?, ?, ?)",
                new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, people.get(i).getName());
                preparedStatement.setInt(2, people.get(i).getAge());
                preparedStatement.setString(3, people.get(i).getEmail());
            }
            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
    }

    //блок метода для заполнения БД ста случайными записями
    private List<Person> create100(){
        List<Person> people = new ArrayList<>();
        File nameFile = new File("D:\\Java Prod\\actual\\MVC_study\\SpringMVCApp1\\src\\main\\resources\\NamesWithNumber.txt");
        File mailFile = new File("D:\\Java Prod\\actual\\MVC_study\\SpringMVCApp1\\src\\main\\resources\\MailWithNumbers.txt");
        try (InputStream inputNameStream = new FileInputStream(nameFile);
             InputStream inputMailStream = new FileInputStream(mailFile)) {

            Properties nameProperties = new Properties();
            nameProperties.load(inputNameStream);

            Properties mailProperties = new Properties();
            mailProperties.load(inputMailStream);

            Random random = new Random();
            for (int i = 1; i < 101; i++) {
                people.add(new Person(i, nameProperties.getProperty("" + i), random.nextInt(85) + 1, nameProperties.getProperty("" + i) + mailProperties.getProperty(""+(random.nextInt(11))), "SameAddress"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return people;
    }
}
