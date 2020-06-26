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
5. HickariCP for fast Connection pooling
6. Undertow as embedded server

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
   1. Ensure you have JDK 1.8 installed on your syetem environment and **JAVA_HOME** in environment variable to point to the root of your JDK installation.
   Also add _%JAVA_HOME%/bin_ to system path on windows or _$JAVA_HOME/bin_ to system path on linux.
   2. Ensure Apache maven version 3.6.1 and above is installed on your system. Read here https://howtodoinjava.com/maven/how-to-install-maven-on-windows/
   3. Ensure Git is installed on your environment. Clone the source code by running command:
   ```bash
     git clone https://github.com/ocularminds/clane-cars.git
   ```
   4. Change directory to the _clane-car_ folder created.
   5. Running this command to build the application:
   ```bash
      mvn clean package
   ```
   6. Upon succesful build, the binary file will be generated in the *tagert* folder.
3. **Running the application**
   1. Edit the __config/application.properties__ file and replace the __app.db.password__ with your postgres user password.
   2. On the bash/command prompt in the root directory of the application, run this command.
      ```bash
      java -jar target\clane-car.jar
      ```
      If all go well, you should see _Undertow started on port(s) 5030 (http)_
      Meaning the app is started and running on PORT 5030
      Point your browser to http://127.0.0.1:5030 This should return:
      ```javascript
        {"app":"Car Service API","status":"Running. Healthy.","uptime":"145secs"}
      ```
4. **Testing the API endpoints**

   I use httpie to test as well as Postman. You can copy each endpoints to post man and test.
   Supported endpoints include:
   
   *Categories*
   http://localhost:5030/api/categories GET, POST --
   http://localhost:5030/api/categories/00001 GET, PUT, DELETE --
   
   *Tags*
   http://localhost:5030/api/tags GET, POST --
   http://localhost:5030/api/tags/00001 GET, PUT, DELETE --
   
   *Cars*   
   http://localhost:5030/api/cars GET, POST --
   http://localhost:5030/api/cars/00000001 GET, PUT, DELETE --
   
   Sample input:
   ```javascript
   {
       "name": "German Machine",
       "description": "New arrival from Germany",
       "category": {"id": "37641001"},
       "tags": [
           {"id":"041001","name":"v6"},
           {"id":"041002","name":"petrol"},
           {"id":"041003","name":"luxury"}
       ],
       "images":[{"id":"0012222", "link":"file/front.jpg"},{"id":"0012224", "link":"files/back.jpg"},{"id":"0012223", "link":"files/rear.jpg"}]
   }
   ```
   
   Sample response:
   ```javascript
     {"error":"201","fault":"Completed Successfully","data":"46384102170000000001","success":false,"failed":true}
    ```
    The data contains the system id of the newly created resource.
   
   *Car Tags*
   http://localhost:5030/api/cars/00000001/tags GET, POST
   http://localhost:5030/api/cars/00000001/tags/0001 GET, PUT, DELETE
   
   *Car Images*
   http://localhost:5030/api/cars/00000001/images POST for multi-part form upload
   http://localhost:5030/api/cars/00000001/images/0001 GET, PUT, DELETE
   
   