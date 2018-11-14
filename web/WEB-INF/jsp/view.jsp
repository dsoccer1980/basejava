<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static com.urise.webapp.util.DateUtil.NOW" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>

        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
    <H3><c:out value="${sectionEntry.key.title}"/></H3>
    <c:choose>
        <c:when test="${(sectionEntry.key.name() == 'PERSONAL') || (sectionEntry.key.name() == 'OBJECTIVE')}">
            <%=((TextSection) sectionEntry.getValue()).getContent() %><br/>
        </c:when>

        <c:when test="${(sectionEntry.key.name() == 'ACHIEVEMENT') || (sectionEntry.key.name() == 'QUALIFICATIONS')}">
            <c:set var="listSection" value="${sectionEntry.value}"/>
            <jsp:useBean id="listSection" type="com.urise.webapp.model.ListSection"/>
            <c:forEach var="item" items="${listSection.items}">
                <LI><c:out value="${item}"/></LI>
            </c:forEach>
        </c:when>

        <c:when test="${(sectionEntry.key.name() == 'EXPERIENCE') || (sectionEntry.key.name() == 'EDUCATION')}">
            <c:set var="organizationSection" value="${sectionEntry.value}"/>
            <jsp:useBean id="organizationSection" type="com.urise.webapp.model.OrganizationSection"/>
            <c:forEach var="organization" items="${organizationSection.section}">
                <a href="${organization.homePage.url}">${organization.homePage.name}</a>

                <c:forEach var="position" items="${organization.positions}">
                    <jsp:useBean id="position" type="com.urise.webapp.model.Organization.Position"/>
                    <BR>
                    <%= position.getDateBegin().format(DateTimeFormatter.ofPattern("MM/yyyy")) %> -
                    <%= position.getDateEnd().equals(NOW) ? "Сейчас" : position.getDateEnd().format(DateTimeFormatter.ofPattern("MM/yyyy"))%>
                    <c:out value="${position.title}"/><BR>
                    <c:out value="${position.text}"/><BR>
                </c:forEach>
                <BR>
            </c:forEach>
        </c:when>

    </c:choose>
    </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
