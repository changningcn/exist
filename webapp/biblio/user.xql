xquery version "1.0";

declare namespace user="http://exist-db.org/xquery/biblio/user";

declare function user:add-to-personal-list() {
    let $cached := session:get-attribute("cached")
    let $pos := xs:integer(request:get-parameter("pos", 1))
    let $oldList0 := session:get-attribute("personal-list")
    let $oldList :=
        if ($oldList0) then $oldList0 else <mylist/>
    let $id := concat(document-uri(root($cached[$pos])), '#', util:node-id($cached[$pos]))
    let $newList :=
        <myList>
            <listitem id="{$id}">{ $cached[$pos] }</listitem>
            { $oldList/listitem }
        </myList>
    let $stored :=
        session:set-attribute("personal-list", $newList)
    return
        ()
};

declare function user:remove-from-personal-list() {
    let $id := request:get-parameter("id", ())
    let $oldList := session:get-attribute("personal-list")
    let $newList :=
        <myList>
            { $oldList/listitem[not(@id = $id)] }
        </myList>
    let $stored :=
        session:set-attribute("personal-list", $newList)
    return
        ()
};

declare function user:personal-list($list as xs:string) {
    if ($list eq 'add') then
        user:add-to-personal-list()
    else
        user:remove-from-personal-list()
};

declare function user:personal-list-size() {
    let $list := session:get-attribute("personal-list")
    return
        <span>{count($list/listitem)}</span>
};

let $list := request:get-parameter("list", ())
return
    if ($list) then
        user:personal-list($list)
    else
        user:personal-list-size()