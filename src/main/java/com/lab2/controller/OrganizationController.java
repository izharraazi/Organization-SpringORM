package com.lab2.controller;

import com.lab2.dao.OrganizationDao;
import com.lab2.dao.impl.HibernateOrganizationDao;
import com.lab2.domain.Address;
import com.lab2.domain.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/org*")
@Controller
public class OrganizationController {

    private OrganizationDao orgDAo;

    public OrganizationController() {
    	orgDAo = new HibernateOrganizationDao();
    }

    /* -------------------------------------------- Create a org -------------------------------------------- */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createOrganization(@RequestParam("name") String name,
                                                @RequestParam Map<String, String> params) {
        Organization org = new Organization(name);
        org.setDescription(params.get("description"));
        if (params.containsKey("street") && params.containsKey("city") &&
                params.containsKey("state") && params.containsKey("zip")) {
            Address address = new Address(params.get("street"), params.get("city"),
            				  params.get("state"), params.get("zip"));
            org.setAddress(address);
        }
        orgDAo.add(org);
 System.out.println("Adding Organization ");
        return new ResponseEntity<>(org, HttpStatus.OK);
    }

    /* ---------------------------------------------- Get a org ---------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getOrganizationJSON(@PathVariable("id") long orgId) {
        try {
            Organization org = orgDAo.findById(orgId);
            return new ResponseEntity<>(org, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, params = "format=xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<?> getOrganizationXML(@PathVariable("id") long orgId) {
        try {
            Organization org = orgDAo.findById(orgId);
            return new ResponseEntity<>(org, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, params = "format=html")
    public String getOrganizationHTML(@PathVariable("id") long orgId, Model model) {
        try {
            Organization org = orgDAo.findById(orgId);
            model.addAttribute("name", org.getName());
            model.addAttribute("description", org.getDescription());
            model.addAttribute("address", org.getAddressString());
            return "organization";
        } catch (Exception e) {
            model.addAttribute("errorCode", 404);
            model.addAttribute("errorMessage", e.toString());
            return "error";
        }
    }

    /* -------------------------------------------- Update a org -------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateOrganization(@PathVariable("id") long orgId,
                                                @RequestParam("name") String name,
                                                @RequestParam Map<String, String> params) {
        Organization org = new Organization(name);
        org.setId(orgId);
        org.setDescription(params.get("description"));
        if (params.containsKey("street") && params.containsKey("city") &&
                params.containsKey("state") && params.containsKey("zip")) {
            Address address = new Address(params.get("street"), params.get("city"),
                    params.get("state"), params.get("zip"));
            org.setAddress(address);
        }

        Organization updatedOrg = orgDAo.update(org);

        return new ResponseEntity<>(updatedOrg, HttpStatus.OK);
    }

    /* ---------------------------------------------- Delete a org ---------------------------------------------- */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteOrganization(@PathVariable("id") long orgId) {
        try {
        	orgDAo.delete(orgId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            if (e.getMessage().equals("ORG_NOT_EMPTY")) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}