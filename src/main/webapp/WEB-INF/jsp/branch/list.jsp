<%@include file="../../jspf/header.jspf"%>       
<c:set value="${branchList}" var="pageListHolder" scope="session"/>
<spring:url value="/branches" var="pageurl" />
<div class="gbox">
<p>
    <a href="<c:url value="branches/add"/>" style="font-weight: bold;">Adauga Filiala</a>
</p>       
<table cellspacing="5px" class="filtrare">
<thead>    
    <th>Nume</th>        
</thead>
<tbody>
    <c:forEach items="${pageListHolder.pageList}" var="ph">
        <tr>                 
            <td>${ph.name}</td>
            <td><a href="<c:url value="/branches/edit/${ph.idoffice}"/>"><img src="<c:url value="/resources/images/pencil.png"/>" border="0"/></a></td>
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