Capstone, Stage 2: Movie Pocket

### Project Overview
In Stage 1, you designed and planned your Capstone app. Now, it's time to build it!

### Why this Project?
In this project, you will demonstrate the skills you've learned in your Nanodegree journey, and apply them to creating a unique app experience of your own. By the end of this project, you will have an app that you can submit to the Google Play Store for distribution.

### What Will I Learn? 
The Capstone project will give you the experience you need to own the full development cycle of an app.

## Rubric

### Meets Specifications
#### Reviewer Comments
Congratulations! You made it! :fireworks::fireworks::fireworks:

It's been a long way, and after all the efforts you have put in to learn new concepts and pass the projects, you can proudly say that you are an Android Developer!

I hope you enjoyed the course and our support to help you improve your skills! Bear in mind that you'll always be part of the Udacity community, so take your time to share your success with your peers, and keep catching up with them on the Slack channel!

I look forward to seeing your App on the Play Store!
Congratulations again, and recall staying :udacious:!

### Common Project Requirements
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines
- [x] App is written solely in the Java Programming Language
- [x] App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### Core Platform Development
- [x] App integrates a third-party library.
- [x] App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash.
- [x] App includes support for accessibility. That includes content descriptions, navigation using a D-pad, and, if applicable, non-audio versions of audio cues.
- [x] App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
- [x] App provides a widget to provide relevant information to the user on the home screen.

### Google Play Services
- [x] App integrates two or more Google services. Google service integrations can be a part of Google Play Services or Firebase.
- [x] Each service imported in the build.gradle is used in the app.
- [x] If Location is used, the app customizes the user’s experience by using the device's location.
- [x] If Admob is used, the app displays test ads. If Admob was not used, student meets specifications.
- [x] If Analytics is used, the app creates only one analytics instance. If Analytics was not used, student meets specifications.
- [x] If Maps is used, the map provides relevant information to the user. If Maps was not used, student meets specifications.
- [x] If Identity is used, the user’s identity influences some portion of the app. If Identity was not used, student meets specifications.

### Material Design
- [x] App theme extends AppCompat.
- [x] App uses an app bar and associated toolbars.
- [x] App uses standard and simple transitions between activities.

### Building
- [x] App builds from a clean repository checkout with no additional configuration.
- [x] App builds and deploys using the installRelease Gradle task.
- [x] App is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path.
- [x] All app dependencies are managed by Gradle.

### Data Persistence
- [x] App stores data locally either by implementing a ContentProvider OR using Firebase Realtime Database OR using Room. No third party frameworks nor Persistence Libraries may be used.
- [x] If Content provider is used, the app uses a Loader to move its data to its views.
- [x] If Room is used then LiveData and ViewModel are used when required and no unnecessary calls to the database are made.
- [x] Must implement at least one of the three:
If it regularly pulls or sends data to/from a web service or API, app updates data in its cache at regular intervals using a SyncAdapter or JobDispatcher.
OR
If it needs to pull or send data to/from a web service or API only once, or on a per request basis (such as a search application), app uses an IntentService to do so.
OR
It it performs short duration, on-demand requests(such as search), app uses an AsyncTask.
