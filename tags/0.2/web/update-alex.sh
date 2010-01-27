#!/bin/bash
cat ~/secret/pass.txt | /opt/gae/bin/appcfg.sh --email=kinyelo@gmail.com --passin update target/vosaocms
