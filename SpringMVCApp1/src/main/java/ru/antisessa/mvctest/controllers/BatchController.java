package ru.antisessa.mvctest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.antisessa.mvctest.dao.PersonDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {

    private final PersonDAO personDAO;
    //Внедряем PersonDAO в класс для взаимодействия с БД через его методы

    @Autowired
    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }
    //PersonDAO является компонентом, то есть создается спрингом автоматически,
    //а значит и внедрение должно быть через @autowired

    @GetMapping()
    public String index() {
        return "batch/index";
    }

    @GetMapping("/batchUpdate")
    public String withBatch(){
        personDAO.BatchUpdate();
        return "redirect:/people";
    }
}
