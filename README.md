# Equipos Argentinos

Equipos Argentinos is an app that allows to see the current Argentinian teams, we can go to the teams webpage, see where the stadium is in case it has one and it can be added to favorites (locally).
It consists on a basic login that when it is accessed the teams are displayed, so that they can be added as favorites, searched by name or even enter a detailed view of each one.

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


