package com.itechart.contacts.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.itechart.contacts.db.dao.AddressDao;
import com.itechart.contacts.db.dao.AttachmentDao;
import com.itechart.contacts.db.dao.ContactDao;
import com.itechart.contacts.db.dao.PhoneDao;
import com.itechart.contacts.db.dao.implimentation.AddressDaoImpl;
import com.itechart.contacts.db.dao.implimentation.AttachmentDaoImpl;
import com.itechart.contacts.db.dao.implimentation.ContactDaoImpl;
import com.itechart.contacts.db.dao.implimentation.PhoneDaoImpl;
import com.itechart.contacts.db.model.AttachmentModel;
import com.itechart.contacts.db.model.ContactModel;
import com.itechart.contacts.db.model.PhoneModel;
import com.itechart.contacts.db.transaction.TransactionManager;
import com.itechart.contacts.db.transaction.TransactionManagerImpl;
import com.itechart.contacts.exception.StorageException;
import com.itechart.contacts.exception.TransactionException;
import com.itechart.contacts.storage.FileStorage;
import com.itechart.contacts.storage.LocalFileStorage;
import com.itechart.contacts.util.Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContactsService {

    private static final Logger log = LoggerFactory.getLogger(ContactsService.class);

    private static final int CONTACTS_AMOUNT_PER_PAGE = 20;
    private int currentPage = 1;

    private static final String LIST = "/WEB-INF/jsp/list.jsp";
    private static final String EDIT = "/WEB-INF/jsp/edit.jsp";

    private ServletContext context;

    private TransactionManager txManger;
    private ContactDao contactDao;
    private AddressDao addressDao;
    private AttachmentDao attachmentDao;
    private PhoneDao phoneDao;

    private ThreadLocal<ContactModel> contactHolder = new ThreadLocal<>();

    public ContactsService(ServletContext context) {
        this.context = context;
        txManger = new TransactionManagerImpl();
        contactDao = new ContactDaoImpl((TransactionManagerImpl) txManger);
        addressDao = new AddressDaoImpl((TransactionManagerImpl) txManger);
        phoneDao = new PhoneDaoImpl((TransactionManagerImpl) txManger);
        attachmentDao = new AttachmentDaoImpl((TransactionManagerImpl) txManger);
    }

    public void loadContactsList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sPage = req.getParameter("page");

        log.info("Get contacts by page = '{}'...", sPage);

        try {
            List<ContactModel> contacts;
            if (sPage == null) {
                contacts = txManger.executeTransaction(() -> {
                    int size = contactDao.size();
                    if (size <= CONTACTS_AMOUNT_PER_PAGE) {
                        req.setAttribute("lastPage", true);
                    }
                    return contactDao.getContactsByPage(currentPage, CONTACTS_AMOUNT_PER_PAGE);
                });
                req.setAttribute("page", currentPage);
            } else {
                final int page = Integer.parseInt(sPage);
                if (page < 0) {
                    throw new NumberFormatException("Page number is less than zero!");
                }

                contacts = txManger.executeTransaction(() -> {
                    int size = contactDao.size();
                    if (page * CONTACTS_AMOUNT_PER_PAGE < size) {
                        currentPage = page;
                        req.setAttribute("page", page);
                        return contactDao.getContactsByPage(page, CONTACTS_AMOUNT_PER_PAGE);
                    } else if (page * CONTACTS_AMOUNT_PER_PAGE == size){
                        currentPage = page;
                        req.setAttribute("lastPage", true);
                        req.setAttribute("page", page);
                        return contactDao.getContactsByPage(page, CONTACTS_AMOUNT_PER_PAGE);
                    } else {
                        req.setAttribute("lastPage", true);
                        req.setAttribute("page", currentPage);
                        return contactDao.getContactsByPage(currentPage, CONTACTS_AMOUNT_PER_PAGE);
                    }
                });
            }
            log.info("Loaded {} contacts", contacts.size());

            req.setAttribute("contacts", contacts);
            req.getRequestDispatcher(LIST).forward(req, resp);
        } catch (NumberFormatException e) {
            log.error("Failed getting a list of contacts for page: '{}'", sPage);
            resp.sendError(400);
        } catch (TransactionException e) {
            log.error("Failed getting a list of contacts for page: '{}'", sPage);
            log.error(e.getMessage(), e);
            resp.sendError(400);
        }
    }

    public void createContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Create new contact..");

        contactHolder.remove();
        req.setAttribute("emptyPhoneTable", true);
        req.setAttribute("emptyAttachmentTable", true);
        req.setAttribute("forEdit", false);
        req.getRequestDispatcher(EDIT).forward(req, resp);
    }

    public void loadContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        contactHolder.remove();
        String sId = req.getParameter("id");

        log.info("Get a contact by id = '{}'...", sId);

        try {
            if (sId == null) {
                throw new TransactionException("The contact id cannot be NULL!");
            }

            int id = Integer.parseInt(sId);
            ContactModel contact = txManger.executeTransaction(() -> {
                ContactModel contactModel = contactDao.get(id);
                contactModel.setPhones(phoneDao.get(id));
                contactModel.setAttachments(attachmentDao.get(id));
                return contactModel;
            });
            contactHolder.remove();
            contactHolder.set(contact);
            if (contact.getPhones().isEmpty()) {
                req.setAttribute("emptyPhoneTable", true);
            }
            if (contact.getAttachments().isEmpty()) {
                req.setAttribute("emptyAttachmentTable", true);
            }

            req.setAttribute("forEdit", true);
            req.setAttribute("contact", contact);
            req.getRequestDispatcher(EDIT).forward(req, resp);
        } catch (NumberFormatException e) {
            log.error("The contact id = '{}' is not an INT", sId);
            resp.sendError(404, "Contact not found!");
        } catch (TransactionException e) {
            log.error("Failed getting a contact by id: '{}'", sId);
            log.error(e.getMessage(), e);
            resp.sendError(404, "Contact not found!");
        }
    }

    public void saveContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        File repository = (File) context.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            ContactModel savableContact = null;
            List<FileItem> items = upload.parseRequest(req);
            Map<String, FileItem> uploadedFiles = new HashMap<>();
            for (FileItem item : items) {
                if (item.isFormField() && item.getFieldName().equalsIgnoreCase("contact_json")) {
                    String contactJson = item.getString();
                    Gson gson = Utils.buildGsonInstance();
                    savableContact = gson.fromJson(contactJson, ContactModel.class);
                    break;
                } else {
                    uploadedFiles.put(item.getFieldName(), item);
                }
            }

            if (savableContact != null) {
                if (savableContact.getFirstName() == null || savableContact.getLastName() == null) {
                    throw new IllegalArgumentException("First Name or/and Last Name are Empty!");
                }

                ContactModel currentContact = contactHolder.get();
                if (currentContact != null && currentContact.getId() != savableContact.getId()) {
                    throw new IllegalArgumentException("The id of a savable contact is not correct!");
                }

                if (currentContact != null) {
                    processUpdateContact(savableContact, uploadedFiles);
                } else {
                    processCreateContact(savableContact, uploadedFiles);
                }
            }

            resp.sendRedirect("/contacts");
        } catch (FileUploadException | JsonParseException | NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (TransactionException e) {
            log.error("Cannot save the contact", e);
        }
        resp.sendError(400, "Cannot save the contact!");
    }

    private void processUpdateContact(final ContactModel contact, Map<String, FileItem> uploadedFiles)
            throws TransactionException {

        txManger.executeTransaction(() -> {
            try {
                int contactId = contact.getId();
                FileStorage storage = new LocalFileStorage();

                FileItem photo = uploadedFiles.get("photo");
                if (photo != null) {
                    String photoUuid = storage.saveFile(contactId, photo.getInputStream());
                    contact.setAvatar(photoUuid);
                }

                int addressId = addressDao.add(contact.getAddress());
                contact.getAddress().setId(addressId);

                contactDao.update(contact);

                List<PhoneModel> phones = contact.getPhones();
                for (PhoneModel phone : phones) {
                    if (validatePhone(phone)) {
                        String mark = phone.getMark();
                        if ("is-new".equals(mark)) {
                            phone.setContactId(contactId);
                            phoneDao.add(phone);
                        } else if ("is-edited".equals(mark)) {
                            phoneDao.update(phone);
                        } else if ("is-deleted".equals(mark)) {
                            phoneDao.delete(phone.getId());
                        }
                    }
                }

                List<AttachmentModel> attachments = contact.getAttachments();
                for (AttachmentModel attachment : attachments) {
                    String mark = attachment.getMark();
                    if ("is-new".equals(mark)) {
                        String attachmentId = "newAttachment" + attachment.getId();
                        FileItem item = uploadedFiles.get(attachmentId);
                        if (item != null) {
                            String uuid = storage.saveFile(contactId, item.getInputStream());
                            attachment.setFileUuid(uuid);
                            attachment.setFileName(item.getName());
                            attachment.setContactId(contactId);
                            attachmentDao.add(attachment);
                        }
                    } else if ("is-edited".equals(mark)) {
                        FileItem item = uploadedFiles.get(String.valueOf(attachment.getId()));
                        Optional<AttachmentModel> optionalAttachment = contactHolder.get().getAttachments().stream()
                                .filter(attachmentModel -> attachmentModel.getId() == attachment.getId())
                                .findFirst();
                        if (item != null && optionalAttachment.isPresent()) {
                            String currentUuid = optionalAttachment.get().getFileUuid();
                            storage.deleteFile(contactId, currentUuid);
                            String newUuid = storage.saveFile(contactId, item.getInputStream());
                            attachment.setContactId(contactId);
                            attachment.setFileUuid(newUuid);
                            attachment.setFileName(item.getName());
                            attachment.setUploadDate(Timestamp.from(Instant.now()));
                            attachmentDao.update(attachment);
                        } else if (optionalAttachment.isPresent()) {
                            optionalAttachment.get().setComment(attachment.getComment());
                            attachmentDao.update(optionalAttachment.get());
                        }
                    } else if ("is-deleted".equals(mark)) {
                        Optional<AttachmentModel> optionalAttachment = contactHolder.get().getAttachments().stream()
                                .filter(attachmentModel -> attachmentModel.getId() == attachment.getId())
                                .findFirst();
                        if (optionalAttachment.isPresent()) {
                            storage.deleteFile(contactId, optionalAttachment.get().getFileUuid());
                            attachmentDao.delete(optionalAttachment.get().getId());
                        }
                    }
                }

                return true;
            } catch (StorageException e) {
                throw new TransactionException("Failed to update an attachment", e);
            } catch (IOException e) {
                throw new TransactionException("Failed to retrieve an attachment from an input field", e);
            }
        });
    }

    private void processCreateContact(final ContactModel contact, Map<String, FileItem> uploadedFiles)
            throws TransactionException {

        txManger.executeTransaction(() -> {
            try {
                int contactId = contactDao.add(contact);
                FileStorage storage = new LocalFileStorage();

                FileItem photo = uploadedFiles.get("photo");
                if (photo != null) {
                    String photoUuid = storage.saveFile(contactId, photo.getInputStream());
                    contact.setAvatar(photoUuid);
                    contact.setId(contactId);
                }

                addressDao.update(contact.getAddress());
                contactDao.update(contact);

                List<PhoneModel> phones = contact.getPhones();
                for (PhoneModel phone : phones) {
                    if (validatePhone(phone)) {
                        phone.setContactId(contactId);
                        phoneDao.add(phone);
                    }
                }

                List<AttachmentModel> attachments = contact.getAttachments();
                for (AttachmentModel attachment : attachments) {
                    String attachmentId = "newAttachment" + attachment.getId();
                    FileItem item = uploadedFiles.get(attachmentId);
                    if (item != null) {
                        String uuid = storage.saveFile(contactId, item.getInputStream());
                        attachment.setFileUuid(uuid);
                        attachment.setFileName(item.getName());
                        attachment.setContactId(contactId);
                        attachmentDao.add(attachment);
                    }
                }

                return true;
            } catch (StorageException e) {
                throw new TransactionException("Failed to save an attachment", e);
            } catch (IOException e) {
                throw new TransactionException("Failed to retrieve an attachment from an input field", e);
            }
        });
    }

    private boolean validatePhone(PhoneModel phone) {
        if (String.valueOf(phone.getCountryCode()).length() != 3) {
            return false;
        }
        if (String.valueOf(phone.getOperatorCode()).length() != 2) {
            return false;
        }
        if (String.valueOf(phone.getPhoneNumber()).length() != 7) {
            return false;
        }
        if (phone.getType() == null || phone.getType().isEmpty()) {
            return false;
        }
        return true;
    }

    public void deleteContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}
}