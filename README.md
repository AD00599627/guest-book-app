# guest-book-app
Guest book Application Documentation
Using the Guest Book App users can register and add comments or image and Admin can review and either can edit or approve or delete the user comments
Prerequisites: 
1.	Java 8
2.	MySQL server 8.0
3. Before download and execute of application we should run below commands in MySQL server.

Steps need to be executed before application start-up:
1. Need to create Database schema use below commands to create database Schema.
DROP DATABASE IF EXISTS techmdb;
CREATE DATABASE techmdb;
2. Need to create user root1 and give access to the techmdb database using below commands
CREATE USER 'root1'@'localhost' IDENTIFIED BY 'admin1';
GRANT ALL PRIVILEGES ON techmdb.* TO 'root1'@'localhost';
3. Commit the above changes.
commit;
4. Once above steps are executed successfully make sure that ‘techmdb’ schema has created and user root1 has access to it by executing below command
select * from mysql.db where db='techmdb';
it will list the user root1
GitHub URL and Setup:
Url: https://github.com/AD00599627/guest-book-app.git
1.	Pull the Application form above repository
2.	Import as maven project into either Eclipse or Intellij and execute maven clean and install (mvn clean, mvn install) for loading of required jars.

Run the Application:
1.	Clean the project 
2.	Run as Spring Boot App through eclipse/intellij tool
3.	This is a spring boot Application no need of server deployment (serverport:8081).
4.	Application while startup it will load the table with sample data from db scripts /db/sql/create-db.sql, /db/sql/insert-data.sql

If you are getting below error while running the application 
java.security.InvalidKeyException: No installed provider supports this key: (null)
to resolve this, try to clean the project once again and run the application as spring boot app
Still you are getting same error while running the application Go to troubleshoot and follow steps.

troubleshoot:
1. If you are getting below error even after running cleaning the application multiple times 
java.security.InvalidKeyException: No installed provider supports this key: (null)
2.Update the use.encrypt.pwd as false and clean the application and run as spring boot App.

Database details:
	 Url: jdbc:mysql://localhost:3306/techmdb
	 userName:  root1
	 Password: admin1
   
Walkthrough the Application:

When the application is up it will automatically build database table with sample data of given table below
User Type	Login Email	Password
Admin	admin@gmail.com	Test@123
User	joel@gmail.com	Test@123
user	abc@gmail.com	Test@123

1.	Login Page:
From this page, admin and user can log in with their email and password based on login type admin or user next page will render.
You can access this page by below Url:
http://localhost:8081/guest-book-app/
http://localhost:8081/guest-book-app/login
You also access this page by clicking Guest Book Login a button from the top left corner.

2.	Admin User Login:
Admin user can view all the user comments entered and he can edit, approve or delete the users from the list.
Once the user's comments are approved then the approve button changes into the blur and cannot be clickable after that.
Based on parameter ISADMIN system will identify the user is admin or not it can only be updated from the backed table.
 
3.	Admin Edit User Data
Admin can edit user data by clicking the edit button of above image it will take you to a new page, there Admin can do view user uploaded image, edit user comments, either approve or delete user data..
 
4.	User login:
Before login user has to register himself from the register a button from the top left corner 
Once the user has logged in successfully he can add, update and delete comments or upload image.

5.	Upload Image
Users can upload an image by clicking the upload image button, The system will only accept an image size of less than 1 MB.
 
6.	User Registration:

Users or Admin can register from this page can access this page from the register button from the top left corner or can use below URL
http://localhost:8081/guest-book-app/register

Name, Email, and Password are mandatory parameters for this page.    
The system won’t allow the user to register with already existed email id in the system.  
 


Thank You
