<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head lang="en">
    <title><fmt:message key="title"/></title>

    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.teal-indigo.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/material-style.css">

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/http.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/js/material-list.js"></script>
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</head>
<body onload="addCheckListener()">
<div class="mdl-layout__container">
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
        <header class="mdl-layout__header">
            <div class="mdl-layout__header-row">
                <span class="mdl-layout__title">Contacts</span>
                <div class="mdl-layout-spacer"></div>
                <button id="search-button" class="mdl-button mdl-js-button mdl-button--icon">
                    <i class="material-icons">search</i>
                </button>
                <button id="more-button" class="mdl-button mdl-js-button mdl-button--icon">
                    <i class="material-icons">more_vert</i>
                </button>
                <div class="mdl-menu__container" style="right: 0; top: 0;">
                    <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="more-button">
                        <li class="mdl-menu__item" name="create-btn" onclick="location.href='?action=create';">
                            <fmt:message key="btn.create"/></li>
                        <li disabled class="mdl-menu__item" name="edit-btn" onclick="editContact()"><fmt:message
                                key="btn.edit"/></li>
                        <li disabled class="mdl-menu__item" name="delete-btn"><fmt:message key="btn.delete"/></li>
                        <li disabled class="mdl-menu__item" name="email-btn"><fmt:message key="btn.email"/></li>
                    </ul>
                </div>
            </div>
        </header>
        <main class="mdl-layout__content">
            <div class="page-content">
                <table class="mdl-data-table mdl-shadow--2dp">
                    <thead>
                    <tr>
                        <th style="width: 2%">
                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select"
                                   for="table-header">
                                <input type="checkbox" id="table-header" class="mdl-checkbox__input"/>
                            </label>
                        </th>
                        <th style="width: 22%" class="mdl-data-table__cell--non-numeric"><fmt:message
                                key="col.name"/></th>
                        <th style="width: 10%"><fmt:message key="col.bd"/></th>
                        <th style="width: 36%"><fmt:message key="col.address"/></th>
                        <th style="width: 30%"><fmt:message key="col.job"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${contacts}" var="contact">
                        <tr>
                            <td>
                                <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect mdl-data-table__select"
                                       for="contact<c:out value="${contact.id}"/>">
                                    <input type="checkbox" id="contact<c:out value="${contact.id}"/>"
                                           class="mdl-checkbox__input"/>
                                </label>
                            </td>
                            <td onclick="location.href='?action=edit&id=<c:out value="${contact.id}"/>';"
                                class="mdl-data-table__cell--non-numeric"><c:out value="${contact.fullName}"/></td>
                            <td><fmt:formatDate pattern="dd-MM-yyyy" value="${contact.birthday}"/></td>
                            <td><c:out value="${contact.address.fullAddress}"/></td>
                            <td><c:out value="${contact.currentJob}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <footer class="mdl-mini-footer" style="justify-content: center;">
                    <button
                            <c:if test="${page == 1}">disabled</c:if>
                            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored">
                        <fmt:message key="btn.prev"/></button>
                    <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored page-btn">
                        ${page}
                    </button>
                    <button
                            <c:if test="${lastPage}">disabled</c:if>
                            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored">
                        <fmt:message key="btn.next"/></button>
                </footer>
            </div>
        </main>
    </div>
</div>
</body>
</html>