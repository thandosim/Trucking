# Overview

As a software engineer, I want to develop my skills for developing mobile applications that use cloud databases to manage data, including creating, modifyng, and deleting data.

This app is for businesses that use external truking services to recieve or ship out goods. 
It is also for transport operators because it allows them to easily find cargo that needs to 
be transported. 
To use the app, one must log in (username: t@e.com, password: 123456) or create a new acount. After logging in the user 
must select find a truck if they are a business to looking to transport goods, or find goods 
if they are a transport company looking for customers.
They can update the location of their truck if it is available for a job or if cargo needs a truck. 

I created this app to develop my skills as a software engineer. It showcases my ability to 
learn new languages and adapt to new environments. It also showcases my abilty to take an idea 
and develop a functioning application to solve a real world problem. I use morden cloud technology to manage a database that users can access using their mobile devices.  In this case, the problem 
was given to me by a business person who was worried about the amount of time he has to spend
looking for transport for his goods. 

[Software Demo Video](https://youtu.be/RlfILEIjij0)

# Cloud Database

{Describe the cloud database you are using.}
I am using Google Firebase
{Describe the structure of the database that you created.}
The Database has two tables. one is the users table which store data for the users, such as usernames and passwords and optionally stores the Name of the user. 
The second table is the trucks table which stores the data about trucks including the name, location and geopoint data about the truck. 
The app can create, modify, delete and view any of this data. 


# Development Environment

The app was developed on Android Studio Ladybug Feature Drop | 2024.2.2 Patch 
I imntegrated Google maps API
I run my database on Google Firebase and manage it using the app's functions

I used Kotlin as my main language. 

# Useful Websites

* [Google Firebase](https://firebase.google.com/docs/auth/android/start)
* [Youtube](https://youtu.be/wm626abfMM8)

# Future Work

* find cargo button functionality.
* sorting functions in the listview so that the truck or cargo nearest to a specified location can be displayed. 
* communication protocols to connect trucks and customers once chosen.