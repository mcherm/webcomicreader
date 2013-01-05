<div class="userHomepage">
<#list comicLists as comicList>
    <div class="comicList">
        <div class="comicListName">${comicList.tagName}</div>
        <ol class="listOfComics">
<#list comicListComics[comicList.tagName] as userComic>
            <li><a href="viewComic/${userComic.id}">${userComic.name}</a></li>
</#list>
        </ol>
    </div>
</#list>
</div>
