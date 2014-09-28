#!/bin/bash

## compile script


build_dir=build
mkdir -p $build_dir
rm -rf $build_dir/*

#classpath="$(hadoop classpath):$(hbase classpath)"
#classpath="$(hadoop classpath)"
classpath="$(hadoop classpath):/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/pig/pig.jar:/home/ubuntu/richa/kafka_2.8.0-0.8.1.1/libs/*:/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/hbase/lib/*"
echo $classpath

javac -d $build_dir  -sourcepath src -classpath "$classpath"  $(find src -name "*.java")

rm  -f a.jar
jar cf a.jar -C $build_dir .

rm -rf $build_dir
