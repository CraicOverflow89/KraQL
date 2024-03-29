KraQL Project
=============

[![Known Vulnerabilities](https://snyk.io//test/github/CraicOverflow89/KoXML/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/CraicOverflow89/KraQL?targetFile=build.gradle)

Lightweight data storage library.

### Tasks

 - implement exceptions for reserved words
 - query execution time
 - params for all queries to be passed separately to query string
 - tests for database operations (eg: create, account stuff, table stuff, etc...)
 - docs

### Issues

 - the timestamp parser is just returning `Date()` for now
 - there are no default values for fields
 - there are no constraints for fields
 - indexes have yet be used (wait until objects are in separate files)
 - the current table record parser does not handle escaped pipes
 - some result descriptions are saying `x` instead of count
 - how to handle select/update/delete where id (rather than fields) match condition?

### File Types

 - everything in a database is stored in a **kqld** archive file (use ZIP library)
 - queries to be performed can be read from **kqlq** files or passed as strings

### Archive Structure

The database files are structured as shown below, in the archive;
 
 ```
ARCHIVE
 ├ ACCOUNTS
 └ TABLES
    ├ DATA
    └ INDEXES
```

### Notes

 - see https://www.javaworld.com/article/2074249/create-your-own-type-3-jdbc-driver--part-1.html