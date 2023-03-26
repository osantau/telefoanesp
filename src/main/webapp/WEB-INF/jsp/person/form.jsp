<%@include file="../../jspf/header.jspf" %>
<div class="gbox">
    <h3>Adaugare / Editare Persoana</h3>
    <c:if test="${action=='edit'}">
        <form:form servletRelativeAction="/persons/${personForm.idperson}/add-office" method="post">            
            <div style="float:right; margin-right:150px;">
                <table >
                    <tr><td><b>Birouri:</b></td>
                        <td>     <select  name="idoffice">       
                                <c:forEach items="${officeCombo}" var="b" >
                                    <option value="10" disabled="disabled" style="font-weight: bold;">${fn:toUpperCase(b.key.name)}</option>
                                    <c:forEach items="${officeCombo[b.key]}" var="o">        	
                                        <option value="${o.idoffice }">&nbsp;&nbsp;${o.name}</option> 
                                    </c:forEach>        
                                </c:forEach>
                            </select></td>
                        <td style="text-align: right;"><input type="submit" value="Adauga"/> </td>
                    </tr>
                    <c:forEach items="${offices}" var="o">
                        <tr>   	
                            <td></td>
                            <td style="font-weight: bold;">${o.name}</td>
                            <td style="font-weight: bold;">${o.branch.name }</td>
                            <td><a href="<c:url value="/persons/remove-office/${personForm.idperson}/${o.idoffice}"/>">Deasociaza</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </form:form>
    </c:if>
    <form:form method="post" commandName="personForm" servletRelativeAction="/persons/${action}/${personForm.idperson}"> 
        <form:errors path="*" element="div" cssClass = "errorblock"/>            
        <fieldset style="width:300px;"><legend><b>Adauga/Editeaza Persoana</b></legend>
            <table cellpadding="10px">
                <tr>
                    <td><b>Nume:</b> </td>
                    <td>
                        <form:input path="fname" style="background-color: #A9F5A9; font-weight: bold;"/>
                    </td>
                </tr>
                <tr>
                    <td><b>Prenume:</b> </td>
                    <td>
                        <form:input path="lname" style="background-color: #A9F5A9;font-weight: bold;"/>
                    </td>
                </tr>
                <tr>
                    <td title="Pseudonim"><b>Pseudonim:</b></td>
                    <td>
                        <form:input path="nickname" style="background-color: #A9F5A9;font-weight: bold;"/>
                    </td>
                </tr>
                <tr>
                    <td></td><td align="right"><input type="submit" value="Adauga / Actualizeaza"/>&nbsp;
                        <a href="<c:url value="/persons"/>" style="text-decoration: none;">
                            <input type="button" value="Cancel"/> 
                        </a> </td>
                </tr>
            </table>
        </fieldset> 
    </form:form>        
        <form action="<c:url value="/persons/addphone/${personForm.idperson}"/>" method="post">
            <fieldset style="width:250px;   ">
                <legend><b>Adauga numar personal:</b></legend>
                <table>
                    <jsp:include page="../../jspf/telefontip.jsp">
                        <jsp:param name="tip" value="persoana"/>
                    </jsp:include>
                    <tr>
                        <td></td><td align="right"><input type="submit" value="Adauga"/>&nbsp;
                            <a href="<c:url value="/persons"/>" style="text-decoration: none;">
                                <input type="button" value="Cancel"/> 
                            </a> </td>
                    </tr>
                </table>    
            </fieldset>
        </form>
        <c:if test="${not empty phones}">
            <p><b>Numere de telefon personale:</b></p>
            <c:forEach items="${phones}" var="phone">
                <p>${phone.number}&nbsp;-&nbsp;${phone.getTypeLabel()}&nbsp;&nbsp;
                    <a href="<c:url value="/persons/deletephone/${phone.idphone}/${personForm.idperson}" />">Sterge</a>
                </p>   
            </c:forEach>
        </c:if>    
</div>
<%@include file="../../jspf/footer.jspf" %>                    