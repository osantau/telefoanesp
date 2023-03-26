<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td>Telefon:</td>
    <td><input type="text" name="number" required="required"/></td>
</tr>
<tr>
    <td>&nbsp;</td>
    <td> <c:if test="${param.tip=='filiala' || param.tip=='birou'}">
            <label><input type="radio" name="tel" checked="true" value="interior"/>Interior&nbsp;</label>
            </c:if>
            <c:if test="${param.tip=='filiala' || param.tip=='birou'}">
            <label><input type="radio" name="tel" value="fix"/>Fix&nbsp;</label>
            </c:if>
            <c:if test="${param.tip=='persoana'}">
            <label><input type="radio" name="tel" value="fix" checked="true"/>Fix&nbsp;</label>
            </c:if>
        <label><input type="radio" name="tel"  value="mobil"/>Mobil</label>
            <c:if test="${param.tip=='persoana'}">
            <label><input type="radio" name="tel" value="serv"/>Serviciu</label>
            </c:if>
    </td>
</tr>