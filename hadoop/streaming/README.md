# Building and running

    $ cd hadoop/wordcount-hdfs-copy
    $ mvn clean package appassembler:assemble
    $ sh ./target/appassembler/bin/streaming

To send a message to syslog

    $ logger -p local3.info -t TESTING "Test Syslog Message"

Look at the data inside hadoop

    $ hadoop fs -ls /data

