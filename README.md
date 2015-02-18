# TSM MobOp App

## Introduction
During the spring semester 2015, a mobile application has to be developed which makes use of a cloud web service. Teams of three members each build such an app. The app and the server backend had to be built within seven weeks. This app represents the result of one of these groups.

## Added Value of Our App
The app's purpose is to provide environmental information to the customer related to his current location. Based on this information, he then can do better decision making on various things, such as where to move house or where to look for a new job.

There are a number of POIs (points of interest) which are interesting in such a case:

* Nuclear Power Plants
* Gas Stations
* Shopping Malls
* Airports
* Motorways
* Crime Statistics
* Chemical Production Facilities
* Recreational Parks and Areas
* Fitness Studios
* ...

## Technology Behind
The app is built on **Android** and the **Google Maps API** or **OpenStreetMap API**. It exchanges data with a web service which hosts all the data required to display environmental data inside the app.

The server backend is hosted on a Microsoft Azure cloud server running **Windows Server 2012** and **SQL Server 2014**. The web service is built using **ASP.NET MVC 5** and **C#**.

For data exchange we leverage the power and simplicity of JSON/HTTP.

## Security
Security is not a major concern during this project. Nevertheless, we secure our API using a simple **shared secret**. Certainly, there are much better ways to protect web services, but we had to cope with some time restrictions.

At this time, the app also does not require any kind of registration or authentication.
