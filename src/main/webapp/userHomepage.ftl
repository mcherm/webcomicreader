<head>
    <style>
        hr {height: 4px; width: 4px; };
        ul.sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
        ul.sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 1.4em; height: 18px; }
        ul.sortable li span { position: absolute; margin-left: -1.3em; }
    </style>
    <script type="text/javascript">
        $(function() {
            $( ".sortable" ).sortable({ handle: ".sort-handle" });
        });
    </script>
</head>
<div class="userHomepage">
    <#list comicLists as comicList>
    <div class="comicList">
        <div class="comicListName">${comicList.tagName}</div>
        <ul class="sortable">
            <#list comicListComics[comicList.tagName] as userComic>
            <li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s sort-handle">X</span><a href="/viewComic/${userComic.id}">${userComic.name}</a></li>
            </#list>
        </ul>
    </div>
    </#list>
</div>
