# clane-cars 

RESTful application for managing cars catalogue for companies that sell cars.
 
## Description 
The application allows the addition of new cars to the catalogue. Each car can belong to, at
least, one category and can have one or more tags and images associated with it.
To make the user experience simpler, adding new cars by supplying just
the name alone are allowed. The application is smart enough to fill in other REQUIRED properties
of the car.

On the other hand, it is also possible to add a new entry by supplying all of the
required properties(name, description, engine number (it must be sequential and 5 digits long) and category)
as well as optional fields(tags, images, and timestamp).
Categories include sedan, truck, tanker, bus etc.
Tags include electric, v4, v6, v8, petrol, luxury, sports, off-road etc.

### Application Features
1. Ability to add a new car entry to the catalogue
2. Ability to see all the cars in the catalogue
3. Ability to view a single car in the catalogue
4. Ability to update any of the properties of the cars in the catalogue
5. Ability to search for cars in the catalogue using a single search term that can either
match the name, description, category or tags of a car
6. No authentication nor staff management as service is purely for managing the cars and its properties.

### Technical Features
1. Accepts and returns JSON using SpringBoot 2.3.0.RELEASE and Maven
2. Developed in Java version 8
3. Configurable with application properties folder in the config directory
4. Backend database is PostgreSQL

### Building and Testing
1. **Database** 
   If you don't aready have PostgreSQL database installed, please download from https://sbp.enterprisedb.com/getfile.jsp?fileid=12539&_ga=2.255713544.1882901634.1592917000-255392400.1592917000
   Install the binaries and note the password for postgres user during installation.
   
   After installation search for psql command and execute it. Accept default parameters on the screen.
   Create **cars** database by executing the command:
   ```bash
     postgres=# create database cars;
   ```
   Switch to the new cars database by executing the command:
   ```bash
     postgres=# \c cars
   ```
   You should see a message _You are now connected to database "cars" as user "postgres"._
   
2. **Building the application**
   a. Ensure you have JDK 1.8 installed on your syetem environment and **JAVA_HOME** in environment variable to point to the root of your JDK installation.
   Also add _%JAVA_HOME%/bin to system path on windows or _$JAVA_HOME/bin to system path on linux.
   b. Ensure Apache maven version 3.6.1 and above is installed on your system. Read here https://howtodoinjava.com/maven/how-to-install-maven-on-windows/
   c. Ensure Git is installed on your environment. Clone the source code by running command:
   ```bash
     git clone https://github.com/ocularminds/clane-cars.git
   ```
   d. Change directory to the _clane-car_ folder created.
   e. Running this command to build the application:
   ```bash
      mvn clean package
   ```
   f. Upon succesful build, the binary file will be generated in the *tagert* folder.
3. **Running the application**
   a. Edit the config/application.properties file and replace the _app.db.password_ with your postgres user password.
   b. On the bash/command prompt in the root directory of the application, run this command.
      ```bash
      java -jar target\clane-car.jar
      ```
      If all go well, you should see _Undertow started on port(s) 5030 (http)_
      Meaning the app is started and running on PORT 5030
      Point your browser to http://127.0.0.1:5030 This should return:
      ```javascript
        {"app":"Car Service API","status":"Running. Healthy.","uptime":"145secs"}
      ```
   