#!/bin/bash

me="$(basename "$0")"
host=$(hostname)

echo "Running [$me on $host]"

if true
then
  pushd ..
   mvn package
   if [[ "$?" -ne 0 ]] ; then
     echo "Build Error"; exit 1
   fi
  popd
fi


###########
# PARAMS
##########
CLASS="example.test"
TARGET="uber-test-1.0-SNAPSHOT.jar"
DB="example"

# cluster config
if false
then
echo "[$me on $host]: * executing example spark2 job"
spark-submit  --class $CLASS \
 --master yarn \
 --num-executors 4 \
 --executor-memory 8G \
 --executor-cores 4 \
 --driver-memory 8G \
 --driver-cores 1 \
../target/$TARGET $DB
echo $rc
fi


# dev config
if true
then
echo "[$me on $host]: * executing example spark2 job"
spark-submit --class $CLASS \
 --master local[*] \
 --executor-memory 8G \
 --driver-memory 8G \
../target/$TARGET $DB
fi


echo "[$me on $host]: * finished!"
