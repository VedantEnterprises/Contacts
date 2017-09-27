package com.itechart.contacts.controller;

import com.itechart.contacts.service.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactsController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ContactsController.class);

    private ContactsService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ContactsService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathParts = req.getRequestURI().split("/");
        if (pathParts.length == 2 && pathParts[1].equalsIgnoreCase("contacts")) {
            service.loadContactsList(req, resp);
        } else if (pathParts.length == 3) {
            if (pathParts[2].equalsIgnoreCase("create")) {
                service.createContact(req, resp);
            } else if (pathParts[2].equalsIgnoreCase("delete")) {
                service.deleteContact(req, resp);
            } else if (pathParts[2].equalsIgnoreCase("edit")) {
                service.loadContact(req, resp);
            } else if (pathParts[2].equalsIgnoreCase("save")) {
                service.saveContact(req, resp);
            } else {
                resp.sendError(404, "Page not found!");
            }
        } else {
            resp.sendError(404, "Page not found!");
        }
    }
}