<head>
    <script type="text/javascript">
        $(function() {
            $("input.currentPositionInput").change(function() {
                $.ajax({
                    type: "POST",
                    url: "http://localhost:8080/ajax/currentPosition/${userId}/${comicId}",
                    data: {
                        'currentPosition': $(this).val()
                    }
                });
            });
        });
    </script>
</head>
<div>
    <div class="comicDetails">
        <div class="comicID"><span class="fieldLabel">Id:</span> ${comic.id}</div>
        <div class="comicName"><span class="fieldLabel">Name:</span> ${comic.name!}</div>
        <div class="homepage"><a href="${comic.homepageURL}">Homepage</a></div>
        <div class="currentPosition">
            <a href="${comic.currentPositionURL}">Current Position</a>
            <div class="currentPositionEdit">
                <label class="fieldLabel">Current Position</label>
                <input class="currentPositionInput" type="text" name="currentPosition" value="${comic.currentPositionURL}">
            </div>
        </div>
    </div>
    <div class="comicLists">
        <div class="title">Lists</div>
    <#if listsThisComicIsIn?size = 0>
        <span>Not in any lists.</span>
    <#else>
        <ul>
            <#list listsThisComicIsIn as tagname>
                <li class="tagname">${tagname}</li>
            </#list>
        </ul>
    </#if>
    </div>
    <div><a href="/app/userHomepage/${userId}">Back to Homepage</a></div>
    <div><a href="/editComic/${comic.id}">raw edit</a></div>
</div>
