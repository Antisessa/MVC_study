package ru.antisessa.mvctest.dao;

import org.springframework.stereotype.Component;
import ru.antisessa.mvctest.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT, "Mike", 25, "Mike@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Nick", 28, "Nick@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Andrey", 20, "Andrey@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT, "Maksim", 18, "Maksim@mail.ru"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id){
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatePerson){
        Person personToBeUpdated = show(id);

        personToBeUpdated.setName(updatePerson.getName());
        personToBeUpdated.setAge(updatePerson.getAge());
        personToBeUpdated.setEmail(updatePerson.getEmail());
    }

    public void delete(int id) {
        people.removeIf(p -> p.getId() == id);
    }
}
