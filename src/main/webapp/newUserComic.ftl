<head>
    <style>
        .hidden {display: none;};
    </style>
</head>
<div class="newUserComic">
    <div>
        <form name="newUserComic" action="/newUserComic/${userId}" method="post">
            <div class="nameField">
                <label class="fieldLabel">Name</label>
                <input type="text" name="name" value=""/>
            </div>
            <div class="homepageField">
                <label class="fieldLabel">Homepage</label>
                <input type="text" name="homepage" value=""/>
            </div>
            <input type="hidden" name="addToLists" value="${addToLists}"/>
            <input type="submit"/>
        </form>
    </div>
</div>
