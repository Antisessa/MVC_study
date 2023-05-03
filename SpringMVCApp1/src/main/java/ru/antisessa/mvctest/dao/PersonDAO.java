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

        people.add(new Person(++PEOPLE_COUNT, "Mike"));
        people.add(new Person(++PEOPLE_COUNT, "Nick"));
        people.add(new Person(++PEOPLE_COUNT, "Andrey"));
        people.add(new Person(++PEOPLE_COUNT, "Maksim"));
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
    }
}
