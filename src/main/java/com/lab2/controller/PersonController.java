package com.lab2.controller;

import com.lab2.dao.PersonDao;
import com.lab2.dao.impl.HibernatePersonDao;
import com.lab2.domain.Address;
import com.lab2.domain.Organization;
import com.lab2.domain.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/person*")
@Controller
public class PersonController {

    private PersonDao personDao;

    public PersonController() {
        personDao = new HibernatePersonDao();
    }


    /* -------------------------------------------- Create a person -------------------------------------------- */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createPerson(@RequestParam("firstname") String firstName,
                                          @RequestParam("lastname") String lastName,
                                          @RequestParam("email") String email,
                                          @RequestParam Map<String, String> params) {
        Person person = new Person(firstName, lastName, email);
        person.setDescription(params.get("description"));
        if (params.containsKey("friends")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (params.containsKey("street") && params.containsKey("city") &&
                params.containsKey("state") && params.containsKey("zip")) {
            Address address = new Address(params.get("street"), params.get("city"),
                    params.get("state"), params.get("zip"));
            person.setAddress(address);
        }
        if (params.containsKey("organization")) {
            Organization org = new Organization();
            org.setId(Long.parseLong(params.get("organization")));
            person.setOrganization(org);
        }

        try {
            personDao.store(person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /* ---------------------------------------------- Get a person ---------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getPersonJSON(@PathVariable("id") long userId) {
        try {
            Person person = personDao.findById(userId);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, params = "format=xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<?> getPersonXML(@PathVariable("id") long userId) {
        try {
            Person person = personDao.findById(userId);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, params = "format=html")
    public String getPersonHTML(@PathVariable("id") long userId, Model model) {
        try {
            Person person = personDao.findById(userId);
            model.addAttribute("firstName", person.getFirstname());
            model.addAttribute("lastName", person.getLastname());
            model.addAttribute("email", person.getEmail());
            model.addAttribute("description", person.getDescription());
                    model.addAttribute("address", person.getAddressString());
            if (person.getOrganization() != null) {
                model.addAttribute("orgName", person.getOrganization().getName());
                model.addAttribute("orgDescription", person.getOrganization().getDescription());
                model.addAttribute("orgAddress", person.getOrganization().getAddressString());
            }
            model.addAttribute("friends", person.getFriendsString());
            return "person";
        } catch (Exception e) {
            model.addAttribute("errorCode", 404);
            model.addAttribute("errorMessage", e.toString());
            return "error";
        }
    }

    /* -------------------------------------------- Update a person -------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updatePerson(@PathVariable("id") long userId,
                                          @RequestParam("email") String email,
                                          @RequestParam Map<String, String> params) {
        Person person = new Person();
        person.setEmail(email);
        person.setId(userId);
        person.setDescription(params.get("description"));
        if (params.containsKey("friends")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (params.containsKey("street") && params.containsKey("city") &&
                params.containsKey("state") && params.containsKey("zip")) {
            Address address = new Address(params.get("street"), params.get("city"),
                    params.get("state"), params.get("zip"));
            person.setAddress(address);
        }

        if (params.containsKey("organization")) {
            Organization org = new Organization();
            org.setId(Long.parseLong(params.get("organization")));
            person.setOrganization(org);
        }

        try {
            Person updatedPerson = personDao.update(person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    /* ---------------------------------------------- Delete a person ---------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deletePerson(@PathVariable("id") long userId) {
        try {
            personDao.delete(userId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}