# Building and running

    $ cd hadoop/batch-export
    $ mvn clean package appassembler:assemble

# Start the database

    $ sh ./target/appassembler/bin/start-database &

View the products in the PRODUCT table using the web UI, should be empty

To view the data in HDFS (as if it came out of a MR job)

    $ hadoop fs -ls /data/analysis/results

# Run the export job

    $ sh ./target/appassembler/bin/export



