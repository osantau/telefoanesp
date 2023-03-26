<%@include file="../../jspf/header.jspf" %>
<div class="gbox">
    <h3>Adaugare / Editare Birou</h3> 
    <form:form method="post" commandName="officeForm" servletRelativeAction="/offices/add"> 
        <form:errors path="*" element="div" cssClass = "errorblock"/>
        <input type="hidden" name="idoffice"
               value="${officeForm.idoffice}" />       
        <table cellpadding="10px">
            <tr>
                <td><b>Filiala: </b></td>
                <td style="font-weight: bold;">
                    <form:select path="parent">                        
                        <form:options items="${branches}" itemValue="idoffice" itemLabel="name"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td><form:label path="name"><b>Denumire: </b></form:label></td>
                    <td style="font-weight: bold;">
                    <form:input path="name"/>                                
                </td>                
            </tr>
            <tr>
                <td></td>
                <td align="right"><input type="submit" value="Adauga / Actualizeaza" />&nbsp;
                    <a href="<c:url value="/offices"/>" style="text-decoration: none;">
                        <input type="button" value="Cancel" /> 
                    </a> </td>
            </tr>
        </table>        
    </form:form>
    <c:if test="${action=='edit'}">
        <form action="<c:url value="/offices/addphone/${officeForm.idoffice}"/>" method="post">
            <fieldset style="width:250px;   ">
                <legend><b>Adauga numar filiala:</b></legend>
                <table>
                    <jsp:include page="../../jspf/telefontip.jsp">
                        <jsp:param name="tip" value="birou"/>
                    </jsp:include>
                    <tr>
                        <td></td><td align="right"><input type="submit" value="Adauga"/>&nbsp;
                            <a href="<c:url value="/offices"/>" style="text-decoration: none;">
                                <input type="button" value="Cancel"/> 
                            </a> </td>
                    </tr>
                </table>    
            </fieldset>
        </form>
        <c:if test="${not empty phones}">
            <p><b>Numere de telefon birou:</b></p>
            <c:forEach items="${phones}" var="phone">
                <p>${phone.number}&nbsp;-&nbsp;${phone.getTypeLabel()}&nbsp;&nbsp;
                    <a href="<c:url value="/offices/deletephone/${phone.idphone}/${officeForm.idoffice}" />">Sterge</a>
                </p>   
            </c:forEach>
        </c:if>
    </c:if>
</div>
<%@include file="../../jspf/footer.jspf" %>                    