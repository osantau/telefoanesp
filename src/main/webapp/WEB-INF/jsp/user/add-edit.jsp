<%@include file="../../jspf/header.jspf"%>
<div class="gbox">
    <h3>Creare / Actualizare</h3>  

    <form:form servletRelativeAction="/utilizatori/adauga" method="post" commandName="userForm">    
        <c:if test="${not empty isEdit}">
        <input type="hidden" name="isEdit" value="${isEdit}"/>
        <input type="hidden" name="iduser" value="${iduser}"/>
        </c:if>
        <form:errors path="*" element="div" cssClass = "errorblock"/>       
        <table class="filtrare">
            <tr>
                <td>Nume utilizator: </td>
                <td>
                    <form:input path="username" size="30" maxlength="30" required="required"/>
                </td>

            </tr>
            <tr>
                <td>Parola: </td>
                <td>
                    <form:input path="password" size="30" maxlength="30" required="required"/>
                </td>
            </tr>
            <tr>
                <td>Rol: </td>
                <td>
                    <select name="role">
                        <c:forEach items="${roles}" var="role">
                            <option value="${role}">${role.replaceAll('ROLE_','')}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="submit" value="Trimite" />
                </td>
            </tr>
        </table>
    </form:form>
</div>
<%@include file="../../jspf/footer.jspf" %>