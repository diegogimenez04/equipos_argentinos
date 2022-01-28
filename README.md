# Equipos Argentinos

Equipos Argentinos es una aplicacion que permite ver los equipos Argentinos actuales, ver su pagina web en caso de tener y ademas añadirlo como favorito (guardandolo localmente).
Consiste en un login basico donde al entrar, se figuran los equipos Argentinos actuales y pueden abrirse, buscar por nombre del equipo y hasta incluso añadirse a una lista de equipos favoritos

## Packages

The project has the following dependencies:
Firebase, mostly for login and crashlytics purpose.
Room, for storing data of teams mostly.
Retrofit with Moshi, for fetching data.

## What I have done?

First off, the app starts with a SplashActivity, just for checking if user is logged in and manage which Activity comes after.
If the app decides that no user is logged in then it launches the log in activity.
In case the app decides an user is already logged then it redirects to de MainActivity.
The MainActivity just shows the list fragment, which is used for displaying the teams.
We have a menu bar in which you can search a team by its name, view your favorite team list or logout.
When a team is clicked we can see a detailed view of it, it is simply a fragment that shows the name, the badge and the stadium if it has one.
When a heart is clicked, the team corresponding to that heart is added to a favorite list, that will show once the user clicks the heart button.

The project uses an MVVM architecture. We have the activities/fragments that have an instance of a ViewModel. The ViewModel has a repository from which manages the database and the data fetching.
The repository has the following behaviour, fetches the data from remote sources, then saves the data inside the database and what we receive to the ViewModel is the database data. This prevents us from having inconsistencies (because we have only one source of information).


