# Build It Bigger

### Project Overview
In this project, you will create an app with multiple flavors that uses multiple libraries and Google Cloud Endpoints. The finished app will consist of four modules:

* A Java library that provides jokes
* A Google Cloud Endpoints (GCE) project that serves those jokes
* An Android Library containing an activity for displaying jokes
* An Android app that fetches jokes from the GCE module and passes them to the Android Library for display

### Why this Project?
As Android projects grow in complexity, it becomes necessary to customize the behavior of the Gradle build tool, allowing automation of repetitive tasks. Particularly, factoring functionality into libraries and creating product flavors allow for much bigger projects with minimal added complexity.

### What Will I Learn?
You will learn the role of Gradle in building Android Apps and how to use Gradle to manage apps of increasing complexity. You'll learn to:

* Add free and paid flavors to an app, and set up your build to share code between them
* Factor reusable functionality into a Java library
* Factor reusable Android functionality into an Android library
* Configure a multi-project build to compile your libraries and app
* Use the Gradle App Engine plugin to deploy a backend
* Configure an integration test suite that runs against the local App Engine development server

### Project Rubric
Your project will be evaluated by a Udacity Code Reviewer according to this rubric.

#### A summary of the rubric is provided below.

##### Required Components
* Project contains a Java library for supplying jokes
* Project contains an Android library with an activity that displays jokes passed to it as intent extras.
* Project contains a Google Cloud Endpoints module that supplies jokes from the Java library. Project loads jokes from GCE module via an async task. Note that this GCE module doesn't need to be deployed to a server. Local testing works fine.
* Project contains connected tests to verify that the async task is indeed loading jokes.
* Project contains paid/free flavors. The paid flavor has no ads, and no unnecessary dependencies.

## Rubric

### Meets Specifications
#### Reviewer Comments
Hurray! I am glad your project meet specifications. You did great demonstrating knowledge of the required rubric items. I am sure you are all set up for success now - all the best as you build better apps.

Generally, I would strongly advise that you be reading Medium posts related to Android. Lots of professional Android engineers share weekly tips and tutorials on new concepts that I promise you, will add alot of value to your development. For example Android Medium Posts . Also do take a look at Advanced Android Development Course- Practicals

### Common Project Requirements
- [x] App is written solely in the Java Programming Language
- [x] App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### Required Components
- [x] Project contains a Java library for supplying jokes.
- [x] Project contains an Android library with an activity that displays jokes passed to it as intent extras.
- [x] Project contains a Google Cloud Endpoints module that supplies jokes from the Java library. Project loads jokes from GCE module via an AsyncTask.
- [x] Project contains connected tests to verify that the AsyncTask is indeed loading jokes.
- [x] Project contains paid/free flavors. The paid flavor has no ads and no unnecessary dependencies. Ads are required in the free version.

### Required Behavior
- [x] App retrieves jokes from Google Cloud Endpoints module and displays them via an Activity from the Android Library. Note that the GCE module need only be deployed locally.
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines.
