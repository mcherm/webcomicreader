<div class="comicDetails">
    <div class="comicID"><span class="fieldLabel">Id:</span> ${comic.id}</div>
    <div class="comicName"><span class="fieldLabel">Name:</span> ${comic.name!}</div>
    <#if comic.homepageURL??><div class="homepage"><a href="${comic.homepageURL}">Homepage</a></div></#if>
    <#if comic.currentPositionURL??><div class="currentPosition"><a href="${comic.currentPositionURL}">Current Position</a></div></#if>
</div>
<div><a href="/comicList">Back to List</a></div>
<div><a href="/editComic/${comic.id}">raw edit</a></div>
