<p>
  <img src="https://img.shields.io/badge/app--size-34%20MB-brightgreen">&emsp;
  <img src="https://img.shields.io/badge/database-firebase-orange">&emsp;
  <img src="https://img.shields.io/badge/platform-android-white">
</p>

# MyMargdarshaka

***Effective digital tutelage in 
the Indian context: Making 
the right connections***

The *My Margdarshaka* Android app based on the novel concept of connecting Indian school students who are unable to attend traditional school due to financial or logistic constraints with mentors who want to give back to society by teaching them. The idea is to mentor these students with the goal of taking the class 10 and 12 certification exams conducted by the National Institute of Open Schooling (NIOS) which is the Government of India’s official examination body. The app focuses on the UN SDG 4 - Quality Education. 
_________________________________________________________________
## Run the App

*Alternative 1:* Download the [APK]()  

*Alternative 2:* <details>
    <summary>Run it locally on Android Studio</summary>
    To run it locally,
1. Clone the repository to your local system using 
    ```
    git clone https://github.com/debesheedas/MyMargdarshaka
    ``` 
2. Then open the repository in Android Studio and Build it on your device of choice (either an emulator or phone connected through USB). [<sup>1</sup>](https://github.com/debesheedas/MyMargdarshaka#Note)
    </details>
________________________________________________________________

## User Guide and Features
On the homepage there is a clickable carousel with slides that explain the main idea behind the app. To login as a student who wants to connect with a mentor, click on the **I AM A STUDENT** button. Similarly for mentors, click on the **I AM A MENTOR** button. Login using mobile number and OTP authentication. 

![Screenshot - Mainpage and Login](Screenshot1.png)

For first time users, there are different sign up requirements for both students and mentors. Enter details such as name, email, class, subject, preferred language of instruction and preferred time slot.
For mentors, there is an additional test, to provide a basic check on the quality of the mentor.

![Screenshot - Sign up for Students and Mentors](Screenshot2.png)

After a successful sign up, students are matched with compatible mentors available and their details are provided to the student in the **My Mentors** page. Similarly, for mentors, the students are assigned based on the criteria such as subject, class, preferred language and time slot. The details of these students are presented in the **My Students** page. Separate Guidelines are provided for both mentors and students. The menu provides options to navigate, an option for providing Feedback, and to logout. Feedback is collected and stored in the database.

![Screenshot - My Mentors, My Students, Guidelines, Feedback](Screenshot3.png)
___________________________________________________________________

## Technologies Used
* Java and XML for structure, functionality and UI.
* Firebase for database and authentication.
* Material theme library for UI components.
___________________________________________________________________

## Note
If built locally, in order to see the authentication in action, please follow these steps on Android Studio:
* On the right panel, click on ```Gradle``` ➝ expand ```Tasks``` ➝ expand ```android``` ➝ double tap ```signiningReport```.
* In the console below, copy **SHA1** key.
* Navigate to [Firebase Project Console Settings](https://console.firebase.google.com/u/1/project/mymargdarshaka/settings/general/android:com.example.mymargdarshaka), scroll down and under 'Your apps', add your SHA1 figerprint.

*(Currently, this can only be performed by authorized users)*
