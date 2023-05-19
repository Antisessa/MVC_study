package ru.antisessa.mvctest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.antisessa.mvctest.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
        //Лямбда выражение осуществляет поиск соответствий, и либо выводит объект класса Person, либо если такого id нет - null,
        //метод findAny возвращает Optional<>, поэтому он подходит под возвращаемый тип метода

        //Второй аргумент передает id для внутреннего PreparedStatement который неявно использует JdbcTemplate,
        //этот id будет поставлен за место ? в SQL запрос
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, age, email) VALUES(?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatePerson){
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?",
                updatePerson.getName(), updatePerson.getAge(), updatePerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public void BatchUpdate(){
        List<Person> people = create1000();
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

    private List<Person> create1000(){
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "Name" + i + "@mail.ru"));
        }
        return people;
    }
}
