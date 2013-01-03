<div class="comicList">
    <div class="listTitle">Your Comics:</div>
<#list comics as comic>
  <div class="comicName"><a href="viewComic/${comic.id}">${comic.name}</a></div>
</#list>
</div>
