### LLAMALAND

This project requires at least Java 9 JDK and Maven installed.

To build, test and package the project, use the following commands:
```
mvn clean package
```

To run the project, start with the following:
```
java -jar target/llamaland-1.0-SNAPSHOT.jar
java -jar target/llamaland-1.0-SNAPSHOT.jar data/mock_birthdays.csv
java -jar target/llamaland-1.0-SNAPSHOT.jar data/mock_birthdays.csv --today=04-10-2021
java -jar target/llamaland-1.0-SNAPSHOT.jar data/mock_birthdays.csv --today=04-10-2021 --blacklist=data/blacklist.csv
java -jar target/llamaland-1.0-SNAPSHOT.jar data/mock_birthdays.csv --today=27-09-2021 --blacklist=data/blacklist.csv
```

### Assumptions

- The format of the rows of the birthday file is `lastName, firstName, bithDay, email`
- The blacklist file contains one email address per row
- We assume that the birthday file and the blacklist file fits into memory.

### Notes

The [data/mock_birthdays.csv](data/mock_birthdays.csv) file was generated using https://www.mockaroo.com, 
then manually edited.
