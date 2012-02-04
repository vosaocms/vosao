#!/bin/bash

APPCFG=/opt/gae/bin/appcfg.sh

$APPCFG rollback target/vosaocms
$APPCFG update target/vosaocms
$APPCFG backends target/vosaocms rollback
$APPCFG backends target/vosaocms update pub

