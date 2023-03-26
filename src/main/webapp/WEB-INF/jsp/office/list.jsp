<%@include file="../../jspf/header.jspf"%>       
<c:set value="${officeList}" var="pageListHolder" scope="session"/>
<spring:url value="/offices" var="pageurl" />
<div class="gbox">
<p>
    <a href="<c:url value="offices/add"/>" style="font-weight: bold;">Adauga Birou</a>
</p>       
<table cellspacing="5px" class="filtrare">
<thead>    
    <th>Birou</th>        
    <th>Filiala</th>       
    <th></th>
</thead>
<tbody>
    <c:forEach items="${pageListHolder.pageList}" var="ph">
        <tr>                 
            <td>${ph.name}</td>
            <td>${ph.branch.name}</td>
            <td><a href="<c:url value="/offices/edit/${ph.idoffice}"/>"><img src="<c:url value="/resources/images/pencil.png"/>" border="0"/></a></td>
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