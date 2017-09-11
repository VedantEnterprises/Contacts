var countChecked = 0;

function countCheckes(checkbox) {
	if (checkbox.checked) {
		countChecked++;
	} else {
		if (countChecked > 0) {
			countChecked--;
		}
	}

	var deleteBtn = document.getElementsByName('delete-btn')[0];
	var emailBtn = document.getElementsByName('email-btn')[0];
	var editBtn = document.getElementsByName('edit-btn')[0];
	if (countChecked === 0) {
		deleteBtn.setAttribute('disabled', 'disabled');
		emailBtn.setAttribute('disabled', 'disabled');
		editBtn.setAttribute('disabled', 'disabled');
	} else {
		deleteBtn.removeAttribute('disabled');
		emailBtn.removeAttribute('disabled');
		if (countChecked === 1) {
			editBtn.removeAttribute('disabled');
		} else {
			editBtn.setAttribute('disabled', 'disabled');
		}
	}
}

function editContact() {
	console.log("EDIT!");
	var boxes = document.querySelectorAll('table tbody .mdl-checkbox__input');
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked) {
			var contactId = boxes[i].id.replace("contact", "");
			sendRequest(document.location.href, {action: "edit", id: contactId}, "GET")
		}
	}
}

function deleteContact() {
}

function sendEmail() {
}

function addCheckListener() {
	var table = document.querySelector('table');
	var headerCheckbox = table.querySelector('thead .mdl-data-table__select input');
	var boxes = table.querySelectorAll('tbody .mdl-data-table__select');

	var headerCheckHandler = function(event) {
		if (event.target.checked) {
			for (var i = 0; i < boxes.length; i++) {
				boxes[i].MaterialCheckbox.check();
				countCheckes(event.target);
			}
		} else {
			for (var i = 0; i < boxes.length; i++) {
				boxes[i].MaterialCheckbox.uncheck();
				countCheckes(event.target);
			}
		}
	};
	headerCheckbox.addEventListener('change', headerCheckHandler);

	var contactCheckHandler = function(event) {
		countCheckes(event.target);
	}
	for (var i = 0; i < boxes.length; i++) {
		boxes[i].addEventListener('change', contactCheckHandler);
	}
}