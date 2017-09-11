var countChecked = 0;

function countCheckes(checkbox) {
    if (checkbox.checked) {
        countChecked++;
    } else {
        if (countChecked > 0) {
            countChecked--;
        }
    }

    var deleteBtn = document.getElementsByName("deleteBtn")[0];
    var emailBtn = document.getElementsByName("emailBtn")[0];
    var editBtn = document.getElementsByName("editBtn")[0];
    if (countChecked === 0) {
        deleteBtn.disabled = true;
        emailBtn.disabled = true;
        editBtn.disabled = true;
    } else {
        deleteBtn.disabled = false;
        emailBtn.disabled = false;
        if (countChecked === 1) {
            editBtn.disabled = false;
        } else {
            editBtn.disabled = true;
        }
    }
}

function toggle(source) {
    var checkboxes = document.getElementsByName("checkContact");
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked !== source.checked) {
            checkboxes[i].checked = source.checked;
            countCheckes(checkboxes[i]);
        }
    }
}

function editContact() {
    var checkboxes = document.getElementsByName("checkContact");
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            var contactId = checkboxes[i].id.replace("contact", "");
            sendRequest(document.location.href, {action: "edit", id: contactId}, "GET")
        }
    }
}

function deleteContact() {
}

function sendEmail() {
}