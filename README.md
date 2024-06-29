# SQLConnect by Alexys Veloz!

## This is a two-tier Client-Server Java-Based Application interacting with MySQL databases using JDBC for connectivity!

### This application uses Java to create a front-end GUI (Client Side) application to connect to MySQL servers.

Hello and Welcome to SQLConnect! This uses JDBC to connect to MySQL Servers, which you can use to enter SQL functions and see the results.

This application comes with two Data base URL Properties, three User properties, and a way to enter and see results of SQL functions!

Note: TO add or customize  users or Database URL's, you must make their own .properties file and put it in the "in" folder.

## Using SQLConnect


### Logging into a Database
Once you run the application, choose both The DB URL and User Properties, as well as the username and password. Once you do so, click the "Connect to Database" button! If it was all correct, You'll see "Connected To: " and then the url of your respective Database.

If any of that info is wrong, then instead you'll see a "Not Connected" line, as well as an error message of "User Credentials do not match properties file!"

### Submitting MySQL Functions
On the right side, you can see where you can enter an SQL Function. From there you can write your function. It's case insensitive, so whether you write "Select" or "SELECT" you'll be okay!

Once you wrote your SQL function, click the "Execute SQL Command" button to have it processed. Additiionally, if you would rather delete your entire function, you can press the "Clear SQL command" Button instead.

If for any reason though, you either wrote an incorrect syntax message, or your user doesn't have the correct permissions to execute your previous statement, A message will pop up in your screen, explaining the database error.

### Displaying Results
Once your table or results show up in the SQL Execution window, you can press the "Clear Results Window" to clear it up! Otherwise, your results will stay there until the next applicable SQL function / query takes precedence.


## You may use this if you wish for any project on two conditions:
1) It must __NEVER__ be a commercial product or in a product people have to pay for.
2) You give credit to me, Alexys Octavio Veloz.
