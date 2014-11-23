#!/bin/sh

JS="../static/js"

CM="$JS/codemirror"

CMD="r.js -o name=main out=src/main/webapp/cms/main.min.js"\
" baseUrl=./src/main/webapp/cms"\
" excludeShallow=i18n,ckeditor,jquery-ui"\
" paths.i18n=libs/i18n"\
" paths.text=libs/text paths.order=libs/order"\
" paths.jquery.cookie=$JS/jquery.cookie"\
" paths.jquery-ui=$JS/jquery-ui"\
" paths.jquery.xmldom=$JS/jquery.xmldom"\
" paths.jquery.form=$JS/jquery.form"\
" paths.jquery.treeview=$JS/jquery.treeview"\
" paths.jquery.jquote2=libs/jquery.jquote2.min"\
" paths.jsonrpc=$JS/jsonrpc"\
" paths.vosao=$JS/vosao"\
" paths.cms=$JS/cms"\
" paths.back-services=$JS/back-services"\
" paths.cm=$CM/codemirror"\
" paths.cm-css=$CM/css"\
" paths.cm-html=$CM/htmlmixed"\
" paths.cm-js=$CM/javascript"\
" paths.cm-xml=$CM/xml"

node $CMD