
# Aws Lambda Framework
![ALF](https://www.diariodejerez.es/2018/08/02/television/extraterrestre-Alf-regresara-television-version_1269183655_87631852_667x375.jpg)
---
Also know as ALF, is our little and very lightwight framework to handle lambdas on AWS. Allows you treat many Handlers as you want, inject interceptors (or filters) before the request to manage database connections,  do some business validations or whatever you want, even you can intercept responses to attach more data to them or do some transformations.

---
### Why ALF?
FaaS is a cool improvement on how we develop applications. We forget all our fights with Operation Systems, Application Server configurations, Hardware issues and so on, but in the other hand, we have a now sort of problems. With ALF we try to solve 3 of them:

1.  **Reduce cold starts** , especially in those that have a low execution rate, having one "big" lambda with many functions, where you can mix in this "big lambda" functions with a high rate of executions that subsidise those who are called less frequently, having always containers available to process requests.
2.  **Reduce number of lambdas** , we develop Febos completely in lambdas, at the beginning we had hundreds of lambdas (per country) and was very difficult to manage that. Now we can group them strategically and then manage and monitor a very few lambdas per country.
3.  **Interceptors**, we love to automate things, and Lambdas in native mode is struggle to do that. With interceptors we can automate almost all business validations without touching the function logic itself, it all occurs outside, in the interceptors.

### Managing database connections
To connect your lambdas to a database, you must set the following environment variables (this is when you use the default connection manager: **DefaultDatabaseConnection**)

* db_host  
* db_user 
* db_pass 
* db_driver // package and class of the driver

If you want to use your own class to manage database connections, which should extend from **io.febos.framework.lambda.interceptors.impl.db.DatabaseConnection** interface, you need to set up an additional environment variable:

* class_db_connector

If not set, the default value is `io.febos.framework.lambda.interceptors.impl.db.DefaultConnection`, which is the default class of the framework to handle database connections.

### Customizing Request and Response classes
Sometimes you want to add specific fields to all your request (like a token) and responses (like a message or code response), in that cases you must create your clases implementing the following interfaces:
