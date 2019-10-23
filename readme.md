KraQL Project
=============

[![Known Vulnerabilities](https://snyk.io//test/github/CraicOverflow89/KoXML/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/CraicOverflow89/KraQL?targetFile=build.gradle)

Lightweight data storage library.

### Tasks

 - database loader
 - params for all queries to be passed separately to query string
 - shell interface
 - docs

### Issues

 - the timestamp parser is just returning `Date()` for now

### File Types

 - everything in a database is stored in a **kqld** archive file (use ZIP library)
 - queries to be performed can be read from **kqlq** files or passed as strings