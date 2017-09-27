(function () {
    'use strict';

    var dateRegex = /^(0?[1-9]|1[0-2])\/(0?[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/;
    var emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    var phoneIdPrefix = 'phone';
    var newPhoneIdPrefix = 'newPhone';
    var attachmentIdPrefix = 'attachment';
    var newAttachmentIdPrefix = 'newAttachment';

    var newPhonesCount = -1;
    var newAttachmentCount = -1;

    var phoneMdlCheckbox;
    var attachmentMdlCheckbox;

    var defaultPhoto;

    function whenLoaded() {
        sessionStorage.setItem('checkedAttachmentCount', '0');
        sessionStorage.setItem('checkedPhoneCount', '0');

        phoneMdlCheckbox = new MdlCheckbox('checkedPhoneCount', '.phones-table', '#edit-phone-btn', '#delete-phone-btn');
        phoneMdlCheckbox.init();
        phoneMdlCheckbox.addHeaderCheckboxListener();

        attachmentMdlCheckbox = new MdlCheckbox('checkedAttachmentCount', '.attachments-table', '#edit-attachment-btn', '#delete-attachment-btn');
        attachmentMdlCheckbox.init();
        attachmentMdlCheckbox.addHeaderCheckboxListener();

        window.addEventListener('click', closeModal);

        var avatarContainer = document.querySelector('.avatar-container');
        avatarContainer.addEventListener('mouseover', showUpAvatarOverlay);
        avatarContainer.addEventListener('mouseout', hideAvatarOverlay);
        avatarContainer.addEventListener('click', openPhotoModal);

        document.querySelector('#save-contact-btn').addEventListener('click', saveContact);
        document.querySelector('#cancel-contact-btn').addEventListener('click', function() {location.href='/contacts';});

        document.querySelector('#upload-btn').addEventListener('change', changeUploadFileValue);
        document.querySelector('#upload-photo-btn').addEventListener('change', changeUploadFileValue);

        document.querySelector('#add-phone-btn').addEventListener('click', addPhone);
        document.querySelector('#add-attachment-btn').addEventListener('click', addAttachment);

        document.querySelector('#edit-phone-btn').addEventListener('click', editPhone);
        document.querySelector('#edit-attachment-btn').addEventListener('click', editAttachment);

        document.querySelector('#save-phone-btn').addEventListener('click', savePhone);
        document.querySelector('#save-attachment-btn').addEventListener('click', saveAttachment);
        document.querySelector('#save-photo-btn').addEventListener('click', savePhoto);

        document.querySelector('#delete-phone-btn').addEventListener('click', deletePhones);
        document.querySelector('#delete-attachment-btn').addEventListener('click', deleteAttachments);

        defaultPhoto = document.querySelector('img#contact-photo').getAttribute('src');
    }
    window.addEventListener ?
        window.addEventListener("load", whenLoaded, false) :
        window.attachEvent && window.attachEvent("onload", whenLoaded);

    function MdlCheckbox(storageName, tableName, editBtnId, deleteBtnId) {
        this.storageName = storageName;
        this.tableName = tableName;
        this.editBtnId = editBtnId;
        this.deleteBtnId = deleteBtnId;

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
            var editBtn = document.querySelector(editBtnId);
            if (checkedCount === 0) {
                deleteBtn.setAttribute('disabled', 'disabled');
                clearActiveTooltip(deleteBtn);
                editBtn.setAttribute('disabled', 'disabled');
                clearActiveTooltip(editBtn);
                if (this.headerCheckbox.checked) {
                    this.headerCheckbox.parentNode.MaterialCheckbox.uncheck();
                }
            } else if (checkedCount === 1) {
                editBtn.removeAttribute('disabled');
                deleteBtn.removeAttribute('disabled');
            } else {
                deleteBtn.removeAttribute('disabled');
                editBtn.setAttribute('disabled', 'disabled');
                clearActiveTooltip(editBtn);
            }

            sessionStorage.setItem(this.storageName, checkedCount);
        };

        var checkHandler = function(event) {
            this.countCheck(event.target);
            if (sessionStorage.getItem(this.storageName)) {
                this.headerCheckbox.parentNode.MaterialCheckbox.uncheck();
            }
        }.bind(this);

        this.addCheckboxListener = function(box) {
            box.addEventListener('change', checkHandler);
        };

        this.removeCheckboxListener = function(box) {
            box.removeEventListener('change', checkHandler);
        };

        this.addHeaderCheckboxListener = function() {
            var headerCheckHandler = function(event) {
                if (event.target.checked) {
                    for (var i = 0; i < this.boxes.length; i++) {
                        if (!this.boxes[i].querySelector('input.mdl-checkbox__input').checked && this.boxes[i].parentNode.parentNode.getAttribute('data-mark') !== 'is-deleted') {
                            this.boxes[i].MaterialCheckbox.check();
                            this.countCheck(event.target);
                        }
                    }
                } else {
                    for (var i = 0; i < this.boxes.length; i++) {
                        if (this.boxes[i].querySelector('input.mdl-checkbox__input').checked && this.boxes[i].parentNode.parentNode.getAttribute('data-mark') !== 'is-deleted') {
                            this.boxes[i].MaterialCheckbox.uncheck();
                            this.countCheck(event.target);
                        }
                    }
                }
            };
            this.headerCheckbox.addEventListener('change', headerCheckHandler.bind(this));

            for (var i = 0; i < this.boxes.length; i++) {
                this.addCheckboxListener(this.boxes[i]);
            }
        }
    }

    function addPhone() {
        showClearModal('#phoneEditModal')
    }

    function addAttachment() {
        showClearModal('#attachmentEditModal')
    }

    function showClearModal(modalSelector) {
        var modal = document.querySelector(modalSelector);
        modal.querySelector('.modal-header h4.add-title').style.display = 'block';
        modal.querySelector('.modal-header h4.edit-title').style.display = 'none';
        var inputs = modal.querySelectorAll('input');
        clearInputFields(inputs);
        modal.querySelector('.modal-body').setAttribute('id', '');
        modal.style.display = 'block';
    }

    function clearInputFields(inputFields) {
        for (var i = 0; i < inputFields.length; i++) {
            inputFields[i].value = '';
            inputFields[i].parentNode.classList.remove('is-dirty');
            inputFields[i].parentNode.classList.remove('is-invalid');
        }
    }

    function clearActiveTooltip(button) {
        var btnId = button.getAttribute('id');
        button.parentNode.querySelector('.mdl-tooltip[data-mdl-for=' + btnId + ']').classList.remove('is-active');
    }

    function setInputValue(inputField, value) {
        inputField.value = value;
        inputField.parentNode.classList.add('is-dirty');
    }

    function changeUploadFileValue(event) {
        var inputFile = event.target.parentNode.parentNode.querySelector('input');
        setInputValue(inputFile, this.files[0] ? this.files[0].name : '');
        inputFile.parentNode.classList.remove('is-invalid');
    }

    function validContactFields() {
        var errorCount = 0;

        // Check for empty First name
        var fName = document.querySelector('#fname');
        if (!fName.value) {
            showInputError(fName);
            errorCount++;
        }

        // Check for empty Last name
        var lName = document.querySelector('#lname');
        if (!lName.value) {
            showInputError(lName);
            errorCount++;
        }

        // Check Birthday format
        var birthday = document.querySelector('#birthday');
        if (birthday.value && !dateRegex.test(birthday.value)) {
            showInputError(birthday);
            errorCount++;
        }

        // Check E-mail format
        var email = document.querySelector('#email');
        if (email.value && !emailRegex.test(email.value)) {
            showInputError(email);
            errorCount++;
        }

        return !errorCount;
    }

    function validatePhoneModal(phoneModal) {
        var errorCount = 0;

        var countryCodeInput = phoneModal.querySelector('input#cCode');
        if (countryCodeInput.value.length !== 3) {
            showInputError(countryCodeInput);
            errorCount++;
        }

        var operatorCodeInput = phoneModal.querySelector('input#oCode');
        if (operatorCodeInput.value.length !== 2) {
            showInputError(operatorCodeInput);
            errorCount++;
        }

        var phoneNumberInput = phoneModal.querySelector('input#pNumber');
        if (phoneNumberInput.value.length !== 7) {
            showInputError(phoneNumberInput);
            errorCount++;
        }

        var phoneTypeInput = phoneModal.querySelector('input#pType');
        if (!phoneTypeInput.value) {
            showInputError(phoneTypeInput);
            errorCount++;
        }

        return !errorCount;
    }

    function showInputError(inputField) {
        inputField.parentNode.classList.add('is-invalid');
        inputField.parentNode.classList.add('is-dirty');
    }

    function editPhone() {
        var phoneRows = document.querySelectorAll('table.phones-table tbody tr');
        var index = getSelectedIndex(phoneRows);

        var editablePhoneItems = document.querySelectorAll('table.phones-table tbody tr')[index].querySelectorAll('td');
        var phoneId = editablePhoneItems[0].querySelector('input').getAttribute('id');
        var phoneNumParts = editablePhoneItems[1].innerText.split('-');
        var phoneType = editablePhoneItems[2].innerText;
        var comment = editablePhoneItems[3].innerText;

        var phoneModal = document.querySelector('#phoneEditModal');
        var inputs = phoneModal.querySelectorAll('input');
        clearInputFields(inputs);
        phoneModal.querySelector('.modal-header h4.add-title').style.display = 'none';
        phoneModal.querySelector('.modal-header h4.edit-title').style.display = 'block';

        phoneModal.querySelector('.modal-body').setAttribute('id', phoneId);
        setInputValue(phoneModal.querySelector('input#cCode'), phoneNumParts[0]);
        setInputValue(phoneModal.querySelector('input#oCode'), phoneNumParts[1]);
        setInputValue(phoneModal.querySelector('input#pNumber'), phoneNumParts[2]);
        setInputValue(phoneModal.querySelector('input#pType'), phoneType);
        setInputValue(phoneModal.querySelector('input#pComment'), comment)

        phoneModal.style.display = 'block';
    }

    function editAttachment() {
        var attachmentRows = document.querySelectorAll('table.attachments-table tbody tr');
        var index = getSelectedIndex(attachmentRows);

        var editableAttachmentItems = document.querySelectorAll('table.attachments-table tbody tr')[index].querySelectorAll('td');
        var attachmentId = editableAttachmentItems[0].querySelector('input').getAttribute('id');
        var fileName = editableAttachmentItems[1].innerText;
        var comment = editableAttachmentItems[3].innerText;

        var attachmentModal = document.querySelector('#attachmentEditModal');
        var inputs = attachmentModal.querySelectorAll('input');
        clearInputFields(inputs);
        attachmentModal.querySelector('.modal-header h4.add-title').style.display = 'none';
        attachmentModal.querySelector('.modal-header h4.edit-title').style.display = 'block';

        attachmentModal.querySelector('.modal-body').setAttribute('id', attachmentId);
        setInputValue(attachmentModal.querySelector('input#upload-file'), fileName);
        setInputValue(attachmentModal.querySelector('input#aComment'), comment)

        var attachmentModal = document.querySelector('#attachmentEditModal');
        attachmentModal.style.display = 'block';
    }

    function savePhone() {
        var phoneModal = document.querySelector('#phoneEditModal');
        if (!validatePhoneModal(phoneModal)) {
            return;
        }

        var phoneModalId = phoneModal.querySelector('.modal-body').getAttribute('id');
        var countryCode = phoneModal.querySelector('input#cCode').value;
        var operatorCode = phoneModal.querySelector('input#oCode').value;
        var phoneNumber = phoneModal.querySelector('input#pNumber').value;
        var fullPhoneNum = countryCode + '-' + operatorCode + '-' + phoneNumber;
        var phoneType = phoneModal.querySelector('input#pType').value;
        var comment = phoneModal.querySelector('input#pComment').value;

        var phoneTable = document.querySelector('table.phones-table tbody');
        var phoneTableRows = phoneTable.querySelectorAll('tr');
        for (var i = 0; i < phoneTableRows.length; i++) {
            if (phoneTableRows[i].querySelector('input').getAttribute('id') === phoneModalId) {
                if (!phoneTableRows[i].getAttribute('data-mark')) {
                    phoneTableRows[i].setAttribute('data-mark', 'is-edited');
                }
                var editablePhoneItems = phoneTableRows[i].querySelectorAll('td');
                editablePhoneItems[1].innerText = fullPhoneNum;
                editablePhoneItems[2].innerText = phoneType;
                editablePhoneItems[3].innerText = comment;

                phoneModal.style.display = 'none';
                return;
            }
        }

        var row = phoneTable.insertRow(phoneTableRows.length);
        var checkboxCell = row.insertCell(0);
        var phoneNumCell = row.insertCell(1);
        var phoneTypeCell = row.insertCell(2);
        var commentCell = row.insertCell(3);

        row.setAttribute('data-mark', 'is-new');
        checkboxCell.innerHTML = '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select"><input type="checkbox" class="mdl-checkbox__input" /></label>';
        checkboxCell.querySelector('label.mdl-checkbox').setAttribute('for', newPhoneIdPrefix + newPhonesCount);
        checkboxCell.querySelector('input.mdl-checkbox__input').setAttribute('id', newPhoneIdPrefix + newPhonesCount);
        newPhonesCount--;

        phoneNumCell.innerHTML = fullPhoneNum;
        phoneTypeCell.innerHTML = phoneType;
        commentCell.innerHTML = comment;

        componentHandler.upgradeDom();
        phoneMdlCheckbox.init();
        phoneMdlCheckbox.addCheckboxListener(checkboxCell.querySelector('input'));

        phoneModal.style.display = 'none';
    }

    function saveAttachment() {
        var attachmentModal = document.querySelector('#attachmentEditModal');
        var fileNameInput = attachmentModal.querySelector('input#upload-file');
        if (!fileNameInput.value) {
            showInputError(fileNameInput);
            return;
        }

        var attachmentModalId = attachmentModal.querySelector('.modal-body').getAttribute('id');
        var attachmentName = attachmentModal.querySelector('input#upload-file').value;
        var comment = attachmentModal.querySelector('input#aComment').value;

        var fileInput = attachmentModal.querySelector('input#upload-btn');

        var attachmentTable = document.querySelector('table.attachments-table tbody');
        var attachmentTableRows = attachmentTable.querySelectorAll('tr');
        for (var i = 0; i < attachmentTableRows.length; i++) {
            if (attachmentTableRows[i].querySelector('input').getAttribute('id') === attachmentModalId) {
                if (!attachmentTableRows[i].getAttribute('data-mark')) {
                    attachmentTableRows[i].setAttribute('data-mark', 'is-edited');
                }
                var editableAttachmentItems = attachmentTableRows[i].querySelectorAll('td');
                if (fileInput.files.length) {
                    editableAttachmentItems[1].innerText = attachmentName;
                    var today = new Date(Date.now()).toLocaleString('en-US');
                    editableAttachmentItems[2].innerText = today.split(',')[0];

                    replaceFileInput(attachmentModal, fileInput, attachmentModalId);
                }
                editableAttachmentItems[3].innerText = comment;

                attachmentModal.style.display = 'none';
                return;
            }
        }

        var row = attachmentTable.insertRow(attachmentTableRows.length);
        var checkboxCell = row.insertCell(0);
        var fileNameCell = row.insertCell(1);
        var uploadDateCell = row.insertCell(2);
        var commentCell = row.insertCell(3);

        row.setAttribute('data-mark', 'is-new');
        checkboxCell.innerHTML = '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select"><input type="checkbox" class="mdl-checkbox__input" /></label>';
        checkboxCell.querySelector('label.mdl-checkbox').setAttribute('for', newAttachmentIdPrefix + newAttachmentCount);
        checkboxCell.querySelector('input.mdl-checkbox__input').setAttribute('id', newAttachmentIdPrefix + newAttachmentCount);

        fileNameCell.innerHTML = attachmentName;
        var today = new Date(Date.now()).toLocaleString('en-US');
        uploadDateCell.innerHTML = today.split(',')[0];
        commentCell.innerHTML = comment;

        componentHandler.upgradeDom();
        attachmentMdlCheckbox.init();
        attachmentMdlCheckbox.addCheckboxListener(checkboxCell.querySelector('input'));

        replaceFileInput(attachmentModal, fileInput, 'newAttachment' + newAttachmentCount);
        newAttachmentCount--;

        attachmentModal.style.display = 'none';
    }

    function replaceFileInput(modal, fileInput, attachmentId) {
        var hiddenAttachmentsContainer = document.querySelector('form.hidden-attachments-container');
        var existingFileInput = hiddenAttachmentsContainer.querySelector('input[id=' + attachmentId + ']');
        if (existingFileInput) {
            // Delete a field with the same id if exist
            hiddenAttachmentsContainer.removeChild(existingFileInput);
        }
        // Move the current file input field to the form
        fileInput.setAttribute('id', attachmentId);
        fileInput.setAttribute('name', attachmentId);
        hiddenAttachmentsContainer.appendChild(fileInput);

        // Create new file input field instead the previous one
        var newFileInput = document.createElement("input");
        newFileInput.setAttribute('type', 'file');
        newFileInput.setAttribute('id', 'upload-btn');
        newFileInput.addEventListener('change', changeUploadFileValue);
        var fileInputBtn = modal.querySelector('.mdl-button--file');
        fileInputBtn.appendChild(newFileInput);
    }

    function deletePhones() {
        var phoneTable = document.querySelector('.phones-table tbody');
        var phoneTableRows = phoneTable.querySelectorAll('tr');
        for (var i = phoneTableRows.length - 1; i >= 0; i--) {
            var box = phoneTableRows[i].querySelector('input');
            if (box.checked) {
                phoneTableRows[i].style.display = 'none';
                box.parentNode.MaterialCheckbox.uncheck();
                phoneMdlCheckbox.removeCheckboxListener(box.parentNode);
                phoneTableRows[i].setAttribute('data-mark', 'is-deleted');
                phoneMdlCheckbox.countCheck(box);
            }
        }
        checkForEmptyTable(phoneTable);
    }

    function deleteAttachments() {
        var attachmentTable = document.querySelector('.attachments-table tbody');
        var attachmentTableRows = attachmentTable.querySelectorAll('tr');
        for (var i = attachmentTableRows.length - 1; i >= 0; i--) {
            var box = attachmentTableRows[i].querySelector('input');

            if (box.checked) {
                attachmentTableRows[i].style.display = 'none';
                box.parentNode.MaterialCheckbox.uncheck();
                attachmentMdlCheckbox.removeCheckboxListener(box.parentNode);
                attachmentTableRows[i].setAttribute('data-mark', 'is-deleted');
                attachmentMdlCheckbox.countCheck(box);

                var hiddenAttachmentsContainer = document.querySelector('form.hidden-attachments-container');
                var existingFileInput = hiddenAttachmentsContainer.querySelector('input[id=' + box.getAttribute('id') + ']');
                if (existingFileInput) {
                    // Delete a field with the same id if exist
                    hiddenAttachmentsContainer.removeChild(existingFileInput);
                }
            }
        }
        checkForEmptyTable(attachmentTable);
    }

    function checkForEmptyTable(table) {
        var msgBlock = table.parentNode.parentNode.querySelector('.empty-table-msg');
        var count = 0;
        var tableRows = table.querySelectorAll('tr');
        for (var i = 0; i < tableRows.length; i++) {
            if (tableRows[i].getAttribute('data-mark') === 'is-deleted') {
                count++;
            }
        }

        if (table.querySelectorAll('tr').length === count) {
            msgBlock.style.display = 'block';
        } else {
            msgBlock.style.display = 'none';
        }
    }

    function getSelectedIndex(tableRows) {
        for (var i = 0; i < tableRows.length; i++) {
            if (tableRows[i].querySelector('label.mdl-data-table__select input').checked) {
                return i;
            }
        }
    }

    function closeModal(event) {
        var phoneModal = document.querySelector('#phoneEditModal');
        var attachmentModal = document.querySelector('#attachmentEditModal');
        var photoModal = document.querySelector('#photoEditModal');
        if (event.target == phoneModal) {
            phoneModal.style.display = 'none';
        } else if (event.target == attachmentModal) {
            attachmentModal.style.display = 'none';
        } else if (event.target == photoModal) {
            photoModal.style.display = 'none';
        }
    }

    function saveContact() {
        if (!validContactFields()) {
            return;
        }

        var contact = {
            first_name: document.querySelector('input#fname').value,
            last_name: document.querySelector('input#lname').value,
            address: {},
            phones: [],
            attachments: []
        };

        var contactId = document.querySelector('.card-contact-detail').getAttribute('id');
        if (contactId.substr('contact'.length, contactId.length)) {
            contact.contact_id = contactId.substr('contact'.length, contactId.length);
        }
        if (document.querySelector('input#mname').value) {
            contact.middle_name = document.querySelector('input#mname').value;
        }
        if (document.querySelector('input#birthday').value) {
            contact.birthday = document.querySelector('input#birthday').value;
        }
        if (document.querySelector('input#sex').value.toLowerCase()) {
            contact.sex = document.querySelector('input#sex').value.toLowerCase();
        }
        if (document.querySelector('input#nationality').value) {
            contact.nationality = document.querySelector('input#nationality').value;
        }
        if (document.querySelector('input#marital_status').value) {
            contact.marital_status = document.querySelector('input#marital_status').value;
        }
        if (document.querySelector('input#site').value) {
            contact.site = document.querySelector('input#site').value;
        }
        if (document.querySelector('input#email').value) {
            contact.email = document.querySelector('input#email').value;
        }
        if (document.querySelector('input#job').value) {
            contact.jov = document.querySelector('input#job').value;
        }
        if (document.querySelector('input#country').value) {
            contact.address.country = document.querySelector('input#country').value;
        }
        if (document.querySelector('input#city').value) {
            contact.address.city = document.querySelector('input#city').value;
        }
        if (document.querySelector('input#detail_address').value) {
            contact.address.detail_address = document.querySelector('input#detail_address').value;
        }
        if (document.querySelector('input#zip').value) {
            contact.address.zip = document.querySelector('input#zip').value;
        }

        var phoneTableRows = document.querySelectorAll('.phones-table tbody tr');
        for (var i = 0; i < phoneTableRows.length; i++) {
            var phoneItems = phoneTableRows[i].querySelectorAll('td');
            var phoneId = phoneItems[0].querySelector('input').getAttribute('id');
            var fullPhoneNum = phoneItems[1].innerText.split('-');
            contact.phones.push({
                phone_id: phoneId.indexOf('new') === -1 ? phoneId.substr(phoneIdPrefix.length, phoneId.length) : phoneId.substr(newPhoneIdPrefix.length, phoneId.length),
                country_code: fullPhoneNum[0],
                operator_code: fullPhoneNum[1],
                phone_num: fullPhoneNum[2],
                phone_type: phoneItems[2].innerText.toLowerCase(),
                comment: phoneItems[3].innerText,
                mark: phoneTableRows[i].getAttribute('data-mark')
            });
        }

        var attachmentTableRows = document.querySelectorAll('.attachments-table tbody tr');
        for (var i = 0; i < attachmentTableRows.length; i++) {
            var attachmentItems = attachmentTableRows[i].querySelectorAll('td');
            var attachmentId = attachmentItems[0].querySelector('input').getAttribute('id');
            contact.attachments.push({
                attachment_id: attachmentId.indexOf('new') === -1 ? attachmentId.substr(attachmentIdPrefix.length, attachmentId.length) : attachmentId.substr(newAttachmentIdPrefix.length, attachmentId.length),
                // file_name: attachmentItems[1].innerText,
                // upload_date: attachmentItems[2].innerText,
                comment: attachmentItems[3].innerText,
                mark: attachmentTableRows[i].getAttribute('data-mark')
            });
        }

        var contactJson = JSON.stringify(contact);
        console.log(contactJson);

        var hiddenAttachmentsContainer = document.querySelector('form.hidden-attachments-container');

        var hiddenField = document.createElement('input');
        hiddenField.setAttribute('type', 'hidden');
        hiddenField.setAttribute('name', 'contact_json');
        hiddenField.setAttribute('value', contactJson);
        hiddenAttachmentsContainer.appendChild(hiddenField);

        var photoModal = document.querySelector('#photoEditModal');
        var inputField = photoModal.querySelector('input#upload-photo-btn')
        if (inputField.files && inputField.files[0]) {
            replaceFileInput(photoModal, inputField, 'photo');
        }

        hiddenAttachmentsContainer.submit();
    }

    function showUpAvatarOverlay() {
        document.querySelector('.avatar-overlay').style.display = 'block';
    }

    function hideAvatarOverlay() {
        document.querySelector('.avatar-overlay').style.display = 'none';
    }

    function openPhotoModal() {
        var photoModal = document.querySelector('#photoEditModal');
        var inputField = photoModal.querySelector('input#upload-photo-file')
        if (inputField.value) {
            inputField.parentNode.classList.add('is-dirty');
        } else {
            inputField.parentNode.classList.remove('is-dirty');
            inputField.parentNode.classList.remove('is-invalid');
        }
        photoModal.style.display = 'block';
    }

    function savePhoto() {
        var photoModal = document.querySelector('#photoEditModal');
        /*var photoNameInput = photoModal.querySelector('input#upload-photo-file');
        if (!photoNameInput.value) {
            showInputError(photoNameInput);
            return;
        }*/

        var inputField = photoModal.querySelector('input#upload-photo-btn')
        var image = document.querySelector('img#contact-photo');
        if (inputField.files && inputField.files[0]) {
            var reader = new FileReader();
            reader.onload = function(event) {
                image.setAttribute('src', event.target.result);
            }
            reader.readAsDataURL(inputField.files[0]);
        } else {
            image.setAttribute('src', defaultPhoto);
        }

        photoModal.style.display = 'none';
    }

}());

function isNumber(event) {
    event = (event) ? event : window.event;
    var charCode = (event.which) ? event.which : event.keyCode;
    if ((charCode > 31 && charCode < 48) || charCode > 57) {
        return false;
    }
    return true;
}