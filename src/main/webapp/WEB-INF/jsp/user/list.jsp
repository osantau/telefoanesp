<%@include file="../../jspf/header.jspf"%>       
<c:set value="${usersList}" var="pageListHolder" scope="session"/>
<spring:url value="/utilizatori" var="pageurl" />
<div class="gbox">
    <p>
        <a href="<c:url value="utilizatori/adauga"/>" style="font-weight: bold;">Adauga Utilizator</a>
    </p>       
    <table cellspacing="5px" class="filtrare">
        <thead>    
        <th>Utilizator</th>
        <th>Parola</th>
        <th>Drepturi</th>
        <th>Activ</th>
        <th></th>
        </thead>
        <tbody>
            <c:forEach items="${pageListHolder.pageList}" var="ph">
                <tr>                 
                    <td>${ph.username}</td>
                    <td>${ph.password}</td>  
                    <td>
                        ${rolesInfo.get(ph.username).replaceAll('ROLE_','')}
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${ph.enabled==1}">Da</c:when>
                            <c:when test="${ph.enabled==0}">Nu</c:when>
                        </c:choose>
                    </td>                    
                    <td><a href="<c:url value="/utilizatori/edit/${ph.iduser}"/>"><img src="<c:url value="/resources/images/pencil.png"/>" border="0"/></a>
                        <a href="<c:url value="/utilizatori/delete/${ph.iduser}"/>"><img src="<c:url value="/resources/images/delete.png"/>" border="0"/></a>            
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br/>
    <p>
        <%@include file="../../jspf/pagination.jspf" %>
    </p>
</div>
<%@include file="../../jspf/footer.jspf"%>