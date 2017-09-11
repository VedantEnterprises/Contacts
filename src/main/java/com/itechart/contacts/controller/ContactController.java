package com.itechart.contacts.controller;

import com.itechart.contacts.db.dao.ContactDao;
import com.itechart.contacts.db.dao.ContactDaoImpl;
import com.itechart.contacts.db.transaction.TransactionManager;
import com.itechart.contacts.db.model.ContactModel;
import com.itechart.contacts.db.transaction.TransactionManagerImpl;
import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ContactController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private static final String CREATE_OR_EDIT = "WEB-INF/jsp/edit.jsp";
    private static final String LIST = "WEB-INF/jsp/material-list.jsp";
    private static final String SEARCH = "WEB-INF/jsp/search.jsp";

    private TransactionManager txManger;
    private ContactDao contactDao;

    private int amountPerPage = 20;

    @Override
    public void init() throws ServletException {
        super.init();
        txManger = TransactionManagerImpl.getInstance();
        contactDao = ContactDaoImpl.getInstance();
        ((ContactDaoImpl) contactDao).setDataSource((TransactionManagerImpl) txManger);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        String action = req.getParameter("action");
        if (("create").equalsIgnoreCase(action)) {
            forward = CREATE_OR_EDIT;
        } else if (("edit").equalsIgnoreCase(action)) {
            forward = CREATE_OR_EDIT;
            editContact(req);
        } else if (("search").equalsIgnoreCase(action)) {
            forward = SEARCH;
        } else {
            forward = LIST;
            getContactList(req);
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    private void editContact(HttpServletRequest req) throws ServletException, IOException {
        String sId = req.getParameter("id");
        log.info("Editing a contact by id = '{}'...", sId);

        try {
            int id = Integer.parseInt(sId);
            ContactModel contactModel = txManger.executeTransaction(() -> contactDao.get(id));
            req.setAttribute("contact", contactModel);
        } catch (NumberFormatException e) {
            log.error("The id = '{}' is not an integer", sId);
            throw new ServletException(e);
        } catch (TransactionException e) {
            log.error("Failed getting a contact by id", e);
            throw new ServletException(e);
        }
    }

    private void getContactList(final HttpServletRequest req) throws ServletException, IOException {
        String sPage = req.getParameter("page");
        log.info("Getting contacts by page = '{}'...", sPage);

        try {
            List<ContactModel> contacts;
            if (sPage == null) {
                contacts = txManger.executeTransaction(() -> {
                    int size = contactDao.size();
                    if (size <= amountPerPage) {
                        req.setAttribute("lastPage", true);
                    }
                    return contactDao.getAll(1, amountPerPage);
                });
                req.setAttribute("page", 1);
            } else {
                final int page = Integer.parseInt(sPage);
                contacts = txManger.executeTransaction(() -> {
                    int size = contactDao.size();
                    if (page * amountPerPage < size) {
                        req.setAttribute("page", page);
                        return contactDao.getAll(page, amountPerPage);
                    } else if (page * amountPerPage == size){
                        req.setAttribute("lastPage", true);
                        req.setAttribute("page", page);
                        return contactDao.getAll(page, amountPerPage);
                    } else {
                        req.setAttribute("lastPage", true);
                        req.setAttribute("page", page == 1 ? page : page - 1);
                        return contactDao.getAll(page == 1 ? page : page - 1, amountPerPage);
                    }
                });
            }
            req.setAttribute("contacts", contacts);
        } catch (NumberFormatException e) {
            log.error("The page number = '{}' is not an integer", sPage);
            throw new ServletException(e);
        } catch (TransactionException e) {
            log.error("Failed getting a list of contacts", e);
            throw new ServletException(e);
        }
    }
}