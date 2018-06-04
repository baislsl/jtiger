#!/bin/sh
mkdir json_out
for file in ./testcases/*

do
    ./main < ${file}
    name=`basename $file`
    cat output.json > ./json_out/"${name}.json"


done