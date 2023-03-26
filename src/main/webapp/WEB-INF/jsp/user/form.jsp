<%@include file="../../jspf/header.jspf"%>
<div class="gbox">
    <h3>Autentificare</h3>

    <c:if test="${not empty errors}">
        <div class="errorblock">
            <c:forEach items="${errors}" var="err">
                <p>${err.value}</p>
            </c:forEach>
        </div>
    </c:if>

    <form action="<c:url value="/process-login"/>" method="post">            
        <table align="center">
            <tr>
                <td>Nume utilizator: </td>
                <td>
                    <input type="text" name="username" size="30" maxlength="30" required="required"/>
                </td>

            </tr>
            <tr>
                <td>Parola: </td>
                <td>
                    <input type="password" name="password" size="30" maxlength="30" required="required"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="submit" value="Login" />
                </td>
            </tr>
        </table>
    </form>
</div>
<%@include file="../../jspf/footer.jspf" %>