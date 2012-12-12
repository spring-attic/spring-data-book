# Building and running

    $ cd hadoop/batch-import
    $ mvn clean package appassembler:assemble

# Start the database

    $ sh ./target/appassembler/bin/start-database

View the products in the PRODUCT table using the web UI

# Run the import job

    $ sh ./target/appassembler/bin/import

To view the imported product database

    $ hadoop fs -ls /import/data/products


