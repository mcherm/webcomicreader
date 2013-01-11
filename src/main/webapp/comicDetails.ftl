<div class="comicDetails">
    <div class="comicID"><span class="fieldLabel">Id:</span> ${comic.id}</div>
    <div class="comicName"><span class="fieldLabel">Name:</span> ${comic.name!}</div>
    <#if comic.homepageURL??><div class="homepage"><a href="${comic.homepageURL}">Homepage</a></div></#if>
    <#if comic.currentPositionURL??><div class="currentPosition"><a href="${comic.currentPositionURL}">Current Position</a></div></#if>
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
