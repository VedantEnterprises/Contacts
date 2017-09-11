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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-theme.min.css"/>

    <link href="https://fonts.googleapis.com/css?family=Open+Sans&amp;subset=cyrillic" rel="stylesheet"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/http.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/list.js"></script>
</head>
<body>
<div class="header">
    <button class="simple-btn" type="submit" name="createBtn" onclick="location.href='?action=create';">
        <fmt:message key="btn.create"/></button>
    <button class="simple-btn" type="submit" name="editBtn" onclick="editContact()" disabled="disabled"><fmt:message
            key="btn.edit"/></button>
    <button class="simple-btn" type="submit" name="deleteBtn" onclick="deleteContact()" disabled="disabled"><fmt:message
            key="btn.delete"/></button>
    <div class="btn-wrapper-right">
        <button class="simple-btn" type="submit" name="searchBtn"><fmt:message key="btn.search"/></button>
        <button class="simple-btn" type="submit" name="emailBtn" onclick="sendEmail()" disabled="disabled"><fmt:message
                key="btn.email"/></button>
    </div>
</div>
<div class="table-container">
    <table class="table table-hover">
        <thead>
        <tr>
            <th style="width: 2%">
                <label>
                    <input type="checkbox" onClick="toggle(this)" name="main.check"/>
                </label>
            </th>
            <th style="width: 22%"><fmt:message key="col.name"/></th>
            <th style="width: 10%"><fmt:message key="col.bd"/></th>
            <th style="width: 36%"><fmt:message key="col.address"/></th>
            <th style="width: 30%"><fmt:message key="col.job"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${contacts}" var="contact">
            <tr>
                <td><input title="c" type="checkbox" name="checkContact" onchange="countCheckes(this)"
                           id="contact<c:out value="${contact.id}"/>"/></td>
                <td><a href="${pageContext.request.contextPath}?action=edit&id=<c:out value="${contact.id}"/>"><c:out
                        value="${contact.fullName}"/></a></td>
                <td><fmt:formatDate pattern="dd-MM-yyyy" value="${contact.birthday}"/></td>
                <td><c:out value="${contact.address.fullAddress}"/></td>
                <td><c:out value="${contact.currentJob}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="footer">
    <div class="pagination">
        <button class="simple-btn left-btn <c:if test="${page == 1}">hidden-btn</c:if>">&lt;</button>
        <div class="page">${page}</div>
        <button class="simple-btn right-btn <c:if test="${lastPage}">hidden-btn</c:if>">&gt;</button>
    </div>
</div>
</body>
</html>