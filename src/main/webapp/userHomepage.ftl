<head>
    <style>
        .hidden {display: none;};
        hr {height: 4px; width: 4px; };
        ul.comicList-mini { margin: 0; padding: 0; width: 60%; }
        ul.comicList-mini li {
            list-style-type: none;
            margin: 0 3px 3px 3px;
            padding: 0.4em;
            padding-left: 1.5em;
            font-size: 1.4em;
            height: 18px;
        }
        ul.comicList-mini li span.ui-icon {
            position: absolute;
            margin-left: -1.3em;
        }
    </style>
    <script type="text/javascript">
        $(function() {
            $( ".sortable" ).sortable({
                handle: ".sort-handle",
                update: function(event, ui) {
                    var comicListId = $(this).find("span.comicListId").text();
                    var tagname = $(this).find('span.tagname').text();
                    var newSortStr = '';
                    $(this).children().find('span.comicId').each(function() {
                        newSortStr = newSortStr + $(this).text() + ' ';
                    });
                    // FIXME: Do not hard-code the start of the URL
                    $.ajax({
                        type: "POST",
                        url: "http://localhost:8080/ajax/comicList/" + comicListId,
                        data: {
                            'tagname': tagname,
                            'itemsInOrder' : newSortStr
                        }
                    });
                }
            });
        });
    </script>
</head>
<div class="userHomepage">
    <#list comicLists as comicList>
    <div class="comicList">
        <div class="comicListName">${comicList.tagname}</div>
        <ul class="sortable comicList-mini">
            <span class="hidden comicListId">${comicList.id}</span>
            <span class="hidden tagname">${comicList.tagname}</span>
            <#list comicListComics[comicList.id] as userComic>
            <li class="ui-state-default">
                <span class="ui-icon ui-icon-arrowthick-2-n-s sort-handle"></span>
                <a href="/viewComic/${userId}/${userComic.comicId}">${userComic.name}</a>
                <span class="hidden comicId">${userComic.comicId}</span>
            </li>
            </#list>
        </ul>
        <ul class="comicList-mini">
            <li>
                <span class="ui-icon ui-icon-plusthick"></span>
                <a class="new-comic" href="/newUserComic/${userId}?addToLists=${comicList.tagname?url('UTF-8')}">add new comic</a>
            </li>
        </ul>
    </div>
    </#list>
</div>
