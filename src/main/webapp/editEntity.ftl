<div>
    <div>Edit Comic Raw</div>
    <form name="editEntity" action="/editComic/${id}" method="post">
<#list fields as field>
        <div><label class="fieldLabel">${field.key}</label> <input type="text" name="${field.key}" value="${field.value}"/></div>
</#list>
        <input type="submit"/>
    </form>

</div>
