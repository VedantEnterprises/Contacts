(function () {
    'use strict';

    var contactIdPrefix = 'contact';

    var contactMdlCheckbox;

    function whenLoaded() {
        sessionStorage.setItem('checkedContactCount', '0');

        contactMdlCheckbox = new MdlCheckbox('checkedContactCount', '.contacts-table', '#edit-btn', '#delete-btn', '#email-btn');
        contactMdlCheckbox.init();
        contactMdlCheckbox.addHeaderCheckboxListener();

        document.querySelector('#edit-btn').addEventListener('click', editContact);
        document.querySelector('#delete-btn').addEventListener('click', deleteContacts);
        document.querySelector('#email-btn').addEventListener('click', sendEmails);
        document.querySelector('#create-btn').addEventListener('click', function() {location.href='/contacts/create';});
        document.querySelector('#prev-btn').addEventListener('click', prevPage);
        document.querySelector('#next-btn').addEventListener('click', nextPage);
    }

    window.addEventListener ?
        window.addEventListener("load", whenLoaded, false) :
        window.attachEvent && window.attachEvent("onload", whenLoaded);

    function MdlCheckbox(storageName, tableName, editBtnId, deleteBtnId, emailBtnId) {
        this.storageName = storageName;
        this.tableName = tableName;
        this.editBtnId = editBtnId;
        this.deleteBtnId = deleteBtnId;
        this.emailBtnId = emailBtnId;

        this.init = function() {
            this.table = document.querySelector(this.tableName);
            this.headerCheckbox = this.table.querySelector('thead .mdl-data-table__select input');
            this.boxes = this.table.querySelectorAll('tbody .mdl-data-table__select');
        };

        this.countCheck = function(checkbox) {
            var checkedCount = sessionStorage.getItem(this.storageName);
            if (checkbox.checked) {
                checkedCount++;
            } else {
                checkedCount > 0 ? checkedCount-- : checkedCount;
            }

            var deleteBtn = document.querySelector(deleteBtnId);
            var emailBtn = document.querySelector(emailBtnId);
            var editBtn = document.querySelector(editBtnId);
            if (checkedCount === 0) {
                deleteBtn.setAttribute('disabled', 'disabled');
                emailBtn.setAttribute('disabled', 'disabled');
                editBtn.setAttribute('disabled', 'disabled');
                if (this.headerCheckbox.checked) {
                    this.headerCheckbox.parentNode.MaterialCheckbox.uncheck();
                }
            } else if (checkedCount === 1) {
                editBtn.removeAttribute('disabled');
                deleteBtn.removeAttribute('disabled');
                emailBtn.removeAttribute('disabled');
            } else {
                deleteBtn.removeAttribute('disabled');
                emailBtn.removeAttribute('disabled');
                editBtn.setAttribute('disabled', 'disabled');
            }

            sessionStorage.setItem(this.storageName, checkedCount);
        };

        this.addCheckboxListener = function(box) {
            var checkHandler = function(event) {
                this.countCheck(event.target);
                if (sessionStorage.getItem(this.storageName)) {
                    this.headerCheckbox.parentNode.MaterialCheckbox.uncheck();
                }
            };
            box.addEventListener('change', checkHandler.bind(this));
        };

        this.addHeaderCheckboxListener = function() {
            var headerCheckHandler = function(event) {
                if (event.target.checked) {
                    for (var i = 0; i < this.boxes.length; i++) {
                        if (!this.boxes[i].querySelector('input.mdl-checkbox__input').checked) {
                            this.boxes[i].MaterialCheckbox.check();
                            this.countCheck(event.target);
                        }
                    }
                } else {
                    for (var i = 0; i < this.boxes.length; i++) {
                        if (this.boxes[i].querySelector('input.mdl-checkbox__input').checked) {
                            this.boxes[i].MaterialCheckbox.uncheck();
                            this.countCheck(event.target);
                        }
                    }
                }
            };
            this.headerCheckbox.addEventListener('change', headerCheckHandler.bind(this));

            for (var i = 0; i < this.boxes.length; i++) {
                this.addCheckboxListener(this.boxes[i]);
                this.boxes[i].parentNode.parentNode.querySelector('td:nth-child(2)').addEventListener('click', _editContact);
            }
        }
    }

    function editContact() {
        var contactRows = document.querySelectorAll('table.contacts-table tbody tr');
        var index = getSelectedIndex(contactRows);
        var editableContactItems = document.querySelectorAll('table.contacts-table tbody tr')[index].querySelectorAll('td');
        var contactId = editableContactItems[0].querySelector('input').getAttribute('id');
        var subId = contactId.substr(contactIdPrefix.length, contactId.length);
        sendRequest('/contacts/edit', {id: subId}, 'GET');
    }

    function _editContact(event) {
        var contactId = event.target.parentNode.querySelector('input').getAttribute('id');
        var subId = contactId.substr(contactIdPrefix.length, contactId.length);
        sendRequest('/contacts/edit', {id: subId}, 'GET');
    }

    function deleteContacts() {
        doContactsAction('/contacts/delete');
    }

    function sendEmails() {
        doContactsAction('/contacts/email');
    }

    function doContactsAction(action) {
        var contactIds = [];
        var contactTable = document.querySelector('.contacts-table tbody');
        var contactTableRows = contactTable.querySelectorAll('tr');
        for (var i = contactTableRows.length - 1; i >= 0; i--) {
            var box = contactTableRows[i].querySelector('input');
            if (box.checked) {
                var contactId = box.getAttribute('id');
                contactIds.push(contactId.substr(7, contactId.length));
            }
        }
        var jsonIds = JSON.stringify(contactIds);
        sendRequest(action, {ids: jsonIds});
    }

    function getSelectedIndex(tableRows) {
        for (var i = 0; i < tableRows.length; i++) {
            if (tableRows[i].querySelector('label.mdl-data-table__select input').checked) {
                return i;
            }
        }
    }

    function prevPage() {
        var pageNum = document.querySelector('button#page-indicator').innerText;
        var numPattern = /^\d+$/;
        if (numPattern.test(pageNum)) {
            sendRequest('/contacts', {page: --pageNum}, 'GET');
        }
    }

    function nextPage() {
        var pageNum = document.querySelector('button#page-indicator').innerText;
        var numPattern = /^\d+$/;
        if (numPattern.test(pageNum)) {
            sendRequest('/contacts', {page: ++pageNum}, 'GET');
        }
    }
}());