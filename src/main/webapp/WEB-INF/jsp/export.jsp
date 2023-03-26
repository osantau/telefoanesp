<%@include file="../jspf/header.jspf"%>
<div class="gbox">
    <h3>Liste numere de telefon:</h3>
    <ul style="list-style-type: disc; margin-left: 50px; padding: 10px;">
        <li>
            <a href="<c:url value="/reports/birouri" />">Interioare Birouri</a>      
        </li>
        <li style="padding: 7px 0px;">
            <a href="<c:url value="/reports/personal" />">Interioare Personal</a>
        </li>
        <li>
            <a href="<c:url value="/reports/filiale" />">Filiale</a>
        </li>
    </ul>          
</div>
<%@include file="../jspf/footer.jspf" %>