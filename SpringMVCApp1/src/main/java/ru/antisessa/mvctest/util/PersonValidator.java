package ru.antisessa.mvctest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.antisessa.mvctest.dao.PersonDAO;
import ru.antisessa.mvctest.models.Person;

@Component
public class PersonValidator implements Validator {

    public final PersonDAO personDAO;
    //Добавление personDAO для обращения к его методам

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
        //Указываем spring к какому классу относится этот валидатор,
        //проверка выполняется при создании объекта с валидатором внутри,
        //вместо aClass spring подставляет значение класса который его вызывает
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (personDAO.show(person.getEmail()).isPresent())
            errors.rejectValue("email", "", "This email is already taken");
        //Первый аргумент - поле которое вызвало ошибку, второй - код ошибки, третий - сообщение ошибки

    }
}
