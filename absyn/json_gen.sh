#!/bin/sh
mkdir json_out
for file in ./testcases/*

do
    ./main < ${file}
    name=`basename $file`
    cat output.json > ./json_out/"${name}.json"
	 cp ./json_out/"${name}.json" ../src/test/resources/json_out/


done
