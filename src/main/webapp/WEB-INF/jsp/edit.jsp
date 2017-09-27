<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head lang="en">
    <title>
        <c:choose>
            <c:when test="${forEdit == true}">
                <fmt:message key="title.edit"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="title.create"/>
            </c:otherwise>
        </c:choose>
    </title>

    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.teal-indigo.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/getmdl-select.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" type="text/css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/http.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/edit.js"></script>
    <script type="text/javascript" src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/getmdl-select.min.js"></script>
<body>
<div class="mdl-layout__container">
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
        <header class="mdl-layout__header">
            <div class="mdl-layout__header-row">
                <span class="mdl-layout__title">
                    <c:choose>
                        <c:when test="${forEdit == true}">
                            <fmt:message key="title.edit"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="title.create"/>
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
        </header>
        <main class="mdl-layout__content">
            <div class="page-content">
                <div class="mdl-grid">
                    <div class="mdl-cell mdl-cell--3-col"></div>
                    <div class="mdl-cell mdl-cell--6-col">
                        <div class="mdl-card mdl-shadow--2dp">
                            <!-- Contact info -->
                            <div class="card-contact-detail" id="contact1">
                                <div class="mdl-grid">
                                    <div class="mdl-cell mdl-cell--2-col" style="min-width: 120px;">
                                        <div class="avatar-container">
                                            <img src="${pageContext.request.contextPath}/resources/images/avatar-placeholder.png" alt="Contact avatar" class="img-responsive" id="contact-photo">
                                            <div class="avatar-overlay" id="avatar-overlay">
                                                <i class="material-icons">photo_camera</i>
                                            </div>
                                            <div class="mdl-tooltip" data-mdl-for="avatar-overlay"><fmt:message key="tooltip.set_photo"/></div>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--10-col">
                                        <div class="mdl-grid row">
                                            <div class="mdl-cell mdl-cell--4-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="fname" value="${contact.firstName}">
                                                    <label class="mdl-textfield__label" for="fname"><fmt:message key="field.f_name"/></label>
                                                    <span class="mdl-textfield__error"><fmt:message key="error.required"/></span>
                                                </div>
                                            </div>
                                            <div class="mdl-cell mdl-cell--4-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="lname" value="${contact.lastName}">
                                                    <label class="mdl-textfield__label" for="lname"><fmt:message key="field.l_name"/></label>
                                                    <span class="mdl-textfield__error"><fmt:message key="error.required"/></span>
                                                </div>
                                            </div>
                                            <div class="mdl-cell mdl-cell--4-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="mname" value="${contact.middleName}">
                                                    <label class="mdl-textfield__label" for="mname"><fmt:message key="field.m_name"/></label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mdl-grid row">
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="birthday" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${contact.birthday}"/>">
                                                    <label class="mdl-textfield__label" for="birthday"><fmt:message key="field.birthday"/></label>
                                                    <span class="mdl-textfield__error"><fmt:message key="error.date"/></span>
                                                </div>
                                            </div>
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-selector getmdl-select getmdl-select__fullwidth">
                                                    <input class="mdl-textfield__input" id="sex" name="sex" type="text" readonly tabIndex="-1" value="${contact.sex}"/>
                                                    <label class="mdl-textfield__label" for="sex"><fmt:message key="field.sex"/></label>
                                                    <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="sex">
                                                        <li class="mdl-menu__item"></li>
                                                        <li class="mdl-menu__item"><fmt:message key="field.sex.male"/></li>
                                                        <li class="mdl-menu__item"><fmt:message key="field.sex.female"/></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="mdl-grid row">
                                    <div class="mdl-cell mdl-cell--6-col">
                                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                            <input class="mdl-textfield__input" type="text" id="nationality" value="${contact.nationality}">
                                            <label class="mdl-textfield__label" for="nationality"><fmt:message key="field.nationality"/></label>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--6-col">
                                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                            <input class="mdl-textfield__input" type="text" id="marital_status" value="${contact.maritalStatus}">
                                            <label class="mdl-textfield__label" for="marital_status"><fmt:message key="field.marital_status"/></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="mdl-grid row">
                                    <div class="mdl-cell mdl-cell--4-col">
                                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                            <input class="mdl-textfield__input" type="text" id="site" value="${contact.site}">
                                            <label class="mdl-textfield__label" for="site"><fmt:message key="field.site"/></label>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--4-col">
                                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                            <input class="mdl-textfield__input" type="text" id="email" value="${contact.email}">
                                            <label class="mdl-textfield__label" for="email"><fmt:message key="field.email"/></label>
                                            <span class="mdl-textfield__error"><fmt:message key="error.email"/></span>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--4-col">
                                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                            <input class="mdl-textfield__input" type="text" id="job" value="${contact.currentJob}">
                                            <label class="mdl-textfield__label" for="job"><fmt:message key="field.job"/></label>
                                        </div>
                                    </div>
                                </div>
                                <!-- Address -->
                                <div class="mdl-grid row">
                                    <div class="mdl-cell mdl-cell--2-col" style="padding-right: 0;">
                                        <div>
                                            <i class="material-icons">home</i>
                                            <h6><fmt:message key="title.address"/></h6>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--10-col">
                                        <div class="mdl-grid row">
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="country" value="${contact.address.country}">
                                                    <label class="mdl-textfield__label" for="country"><fmt:message key="field.country"/></label>
                                                </div>
                                            </div>
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="city" value="${contact.address.city}">
                                                    <label class="mdl-textfield__label" for="city"><fmt:message key="field.city"/></label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mdl-grid row">
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input" type="text" id="detail_address" value="${contact.address.detailAddress}">
                                                    <label class="mdl-textfield__label" for="detail_address"><fmt:message key="field.detail_address"/></label>
                                                </div>
                                            </div>
                                            <div class="mdl-cell mdl-cell--6-col">
                                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                    <input class="mdl-textfield__input number-input" type="text" pattern="-?[0-9]*(\.[0-9]+)?" id="zip" onkeypress="return isNumber(event)" value="${contact.address.zip}">
                                                    <label class="mdl-textfield__label" for="zip"><fmt:message key="field.zip"/></label>
                                                    <span class="mdl-textfield__error"><fmt:message key="error.zip"/></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <!-- Phones -->
                            <div class="phones-container">
                                <div class="mdl-grid row">
                                    <div class="mdl-cell mdl-cell--2-col" style="padding-right: 0;">
                                        <div class="table-title">
                                            <i class="material-icons">phone</i>
                                            <h6><fmt:message key="title.phones"/></h6>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--10-col">
                                        <div class="table-control-panel">
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="add-phone-btn" id="add-phone-btn">
                                                <i class="material-icons">add_circle</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="add-phone-btn"><fmt:message key="tooltip.add"/></div>
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="edit-phone-btn" id="edit-phone-btn" disabled>
                                                <i class="material-icons">edit</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="edit-phone-btn"><fmt:message key="tooltip.edit"/></div>
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="delete-phone-btn" id="delete-phone-btn" disabled>
                                                <i class="material-icons">delete</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="delete-phone-btn"><fmt:message key="tooltip.delete"/></div>
                                        </div>
                                        <table class="mdl-data-table table-borderless phones-table">
                                            <thead>
                                            <th style="width: 2%">
                                                <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-phones-header">
                                                    <input type="checkbox" id="table-phones-header" class="mdl-checkbox__input" />
                                                </label>
                                            </th>
                                            <th style="width: 30%"><fmt:message key="title.table.phone_number"/></th>
                                            <th style="width: 14%"><fmt:message key="title.table.phone_type"/></th>
                                            <th><fmt:message key="title.table.comment"/></th>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${contact.phones}" var="phone">
                                                <tr>
                                                    <td>
                                                        <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="phone<c:out value="${phone.id}"/>">
                                                            <input type="checkbox" id="phone<c:out value="${phone.id}"/>" class="mdl-checkbox__input" />
                                                        </label>
                                                    </td>
                                                    <td><c:out value="${phone.fullPhoneNumber}"/></td>
                                                    <td><c:out value="${phone.type}"/></td>
                                                    <td><c:out value="${phone.comment}"/></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                        <div class="empty-table-msg" <c:if test="${emptyPhoneTable}">style="display: block;"</c:if>><fmt:message key="msg.table.empty_phones"/></div>
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <!-- Attachments -->
                            <div class="attachments-container">
                                <div class="mdl-grid row">
                                    <div class="mdl-cell mdl-cell--2-col" style="padding-right: 0;">
                                        <div class="table-title">
                                            <i class="material-icons">attachment</i>
                                            <h6><fmt:message key="title.attachments"/></h6>
                                        </div>
                                    </div>
                                    <div class="mdl-cell mdl-cell--10-col">
                                        <div class="table-control-panel">
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="add-attachment-btn" id="add-attachment-btn">
                                                <i class="material-icons">add_circle</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="add-attachment-btn"><fmt:message key="tooltip.add"/></div>
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="edit-attachment-btn" id="edit-attachment-btn" disabled>
                                                <i class="material-icons">edit</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="edit-attachment-btn"><fmt:message key="tooltip.edit"/></div>
                                            <button class="mdl-button mdl-js-button mdl-js-ripple-effect" name="delete-attachment-btn" id="delete-attachment-btn" disabled>
                                                <i class="material-icons">delete</i>
                                            </button>
                                            <div class="mdl-tooltip" data-mdl-for="delete-attachment-btn"><fmt:message key="tooltip.delete"/></div>
                                        </div>
                                        <table class="mdl-data-table table-borderless attachments-table">
                                            <thead>
                                                <th style="width: 2%">
                                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="table-attachments-header">
                                                        <input type="checkbox" id="table-attachments-header" class="mdl-checkbox__input" />
                                                    </label>
                                                </th>
                                                <th style="width: 30%"><fmt:message key="title.table.file_name"/></th>
                                                <th style="width: 20%"><fmt:message key="title.table.upload_date"/></th>
                                                <th><fmt:message key="title.table.comment"/></th>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${contact.attachments}" var="attachment">
                                                <tr>
                                                    <td>
                                                        <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select" for="attachment<c:out value="${attachment.id}"/>">
                                                            <input type="checkbox" id="attachment<c:out value="${attachment.id}"/>" class="mdl-checkbox__input" />
                                                        </label>
                                                    </td>
                                                    <td><c:out value="${attachment.fileName}"/></td>
                                                    <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.uploadDate}"/></td>
                                                    <td><c:out value="${attachment.comment}"/></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                        <div class="empty-table-msg" <c:if test="${emptyAttachmentTable}">style="display: block;"</c:if>><fmt:message key="msg.table.empty_attachments"/></div>
                                    </div>
                                </div>
                                <div class="footer-btns">
                                    <button class="mdl-button mdl-js-button mdl-button--primary" id="cancel-contact-btn">
                                        <fmt:message key="btn.cancel"/>
                                    </button>
                                    <button class="mdl-button mdl-js-button mdl-button--primary" id="save-contact-btn">
                                        <fmt:message key="btn.save"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<!-- Edit Phone modal -->
<div class="modal" id="phoneEditModal">
    <div class="modal-content mdl-card mdl-shadow--4dp">
        <div class="modal-header">
            <h4 class="add-title"><fmt:message key="title.modal.add_phone"/></h4>
            <h4 class="edit-title" style="display: none;"><fmt:message key="title.modal.edit_phone"/></h4>
            <hr>
        </div>
        <div class="modal-body">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="cCode" onkeypress="return isNumber(event)"/>
                <label class="mdl-textfield__label" for="cCode"><fmt:message key="field.county_code"/></label>
                <span class="mdl-textfield__error"><fmt:message key="error.country_code"/></span>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="oCode" onkeypress="return isNumber(event)"/>
                <label class="mdl-textfield__label" for="oCode"><fmt:message key="field.operator_code"/></label>
                <span class="mdl-textfield__error"><fmt:message key="error.operator_code"/></span>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="pNumber" onkeypress="return isNumber(event)"/>
                <label class="mdl-textfield__label" for="pNumber"><fmt:message key="field.phone_number"/></label>
                <span class="mdl-textfield__error"><fmt:message key="error.phone_number"/></span>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-selector getmdl-select getmdl-select__fullwidth">
                <input class="mdl-textfield__input" id="pType" name="pType" type="text" readonly tabIndex="-1"/>
                <label class="mdl-textfield__label" for="pType"><fmt:message key="field.phone_type"/></label>
                <ul class="mdl-menu mdl-menu--bottom-left mdl-js-menu" for="pType">
                    <li class="mdl-menu__item"><fmt:message key="field.phone_type.home"/></li>
                    <li class="mdl-menu__item"><fmt:message key="field.phone_type.mobile"/></li>
                </ul>
                <span class="mdl-textfield__error"><fmt:message key="error.required"/></span>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="pComment">
                <label class="mdl-textfield__label" for="pComment"><fmt:message key="field.comment"/></label>
            </div>
        </div>
        <div class="footer-btns">
            <button class="mdl-button mdl-js-button mdl-button--primary" onclick="document.querySelector('#phoneEditModal').style.display = 'none';">
                <fmt:message key="btn.cancel"/>
            </button>
            <button class="mdl-button mdl-js-button mdl-button--primary" id="save-phone-btn">
                <fmt:message key="btn.save"/>
            </button>
        </div>
    </div>
</div>

<!-- Edit Attachment modal -->
<div class="modal" id="attachmentEditModal">
    <div class="modal-content mdl-card mdl-shadow--4dp">
        <div class="modal-header">
            <h4 class="add-title"><fmt:message key="title.modal.add_attachment"/></h4>
            <h4 class="edit-title" style="display: none;"><fmt:message key="title.modal.edit_attachment"/></h4>
            <hr>
        </div>
        <div class="modal-body">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--file mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="upload-file" readonly/>
                <label class="mdl-textfield__label" for="upload-file"><fmt:message key="field.file_name"/></label>
                <div class="mdl-button mdl-button--primary mdl-button--icon mdl-button--file">
                    <i class="material-icons">attach_file</i><input type="file" id="upload-btn"/>
                </div>
                <span class="mdl-textfield__error"><fmt:message key="error.required"/></span>
            </div>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="aComment"/>
                <label class="mdl-textfield__label" for="aComment"><fmt:message key="field.comment"/></label>
            </div>
        </div>
        <div class="footer-btns">
            <button class="mdl-button mdl-js-button mdl-button--primary" onclick="document.querySelector('#attachmentEditModal').style.display = 'none';">
                <fmt:message key="btn.cancel"/>
            </button>
            <button class="mdl-button mdl-js-button mdl-button--primary" id="save-attachment-btn">
                <fmt:message key="btn.save"/>
            </button>
        </div>
    </div>
</div>

<!-- Edit Photo modal -->
<div class="modal" id="photoEditModal">
    <div class="modal-content mdl-card mdl-shadow--4dp">
        <div class="modal-header">
            <h4 class="add-title"><fmt:message key="title.modal.photo"/></h4>
        </div>
        <div class="modal-body">
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--file mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="text" id="upload-photo-file" readonly/>
                <label class="mdl-textfield__label" for="upload-photo-file"><fmt:message key="field.file_name"/></label>
                <div class="mdl-button mdl-button--primary mdl-button--icon mdl-button--file">
                    <i class="material-icons">attach_file</i><input type="file" accept="image/*" id="upload-photo-btn"/>
                </div>
                <span class="mdl-textfield__error"><fmt:message key="error.required"/></span>
            </div>
        </div>
        <div class="footer-btns">
            <button class="mdl-button mdl-js-button mdl-button--primary" onclick="document.querySelector('#photoEditModal').style.display = 'none';">
                <fmt:message key="btn.cancel"/>
            </button>
            <button class="mdl-button mdl-js-button mdl-button--primary" id="save-photo-btn">
                <fmt:message key="btn.save"/>
            </button>
        </div>
    </div>
</div>

<form class="hidden-attachments-container" method="POST" enctype="multipart/form-data" action="/contacts/save"></form>
</body>
</html>