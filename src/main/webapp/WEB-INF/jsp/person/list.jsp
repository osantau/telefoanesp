<%@include file="../../jspf/header.jspf"%>       
<c:set value="${personList}" var="pageListHolder" scope="session"/>
<spring:url value="/persons" var="pageurl" />
<div class="gbox">
    <p>
        <a href="<c:url value="persons/add"/>" style="font-weight: bold;">Adauga Persoana</a>
    </p>       
    <table cellspacing="5px" class="filtrare">
        <thead>    
        <th>Nume</th>
        <th>Prenume</th>
        <th>Birou</th>
        <th>Filiala</th>       
        <th></th>
        </thead>
        <tbody>
            <c:forEach items="${pageListHolder.pageList}" var="ph">
                <tr>                 
                    <td>${ph.fname}</td>
                    <td>${ph.lname}</td>
                    <td>${ph.birou}</td>
                    <td>${ph.filiala}</td>
                    <td><a href="<c:url value="/persons/edit/${ph.idperson}"/>"><img src="<c:url value="/resources/images/pencil.png"/>" border="0"/></a>
                        <a href="<c:url value="/persons/delete/${ph.idperson}"/>"><img src="<c:url value="/resources/images/delete.png"/>" border="0"/></a>            
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