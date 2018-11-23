# AwsLambdaFramework

Connector to the default database used is **DefaultConnector**
which requires having the following environment variables:
* conector_db_host  //Host 
* conector_db_user  //Usuario
* conector_db_pass  //PASS
* conector_db_driver //Driver utilzado

Modifying the default connector requires assigning the environment variable
**class_conector_db** EJ: **io.febos.framework.lambda.interceptors.impl.db.DefaultConector**
which should extend from **io.febos.framework.lambda.interceptors.impl.db.DbConector** inteface


para modificar las clases de respuesta se usa mismo truco que en coneccion