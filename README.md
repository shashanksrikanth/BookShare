# BookShare

Welcome to BookShare! This is an app that enables the sharing of books between donors and receivers. 

## About and Features
A lot of us have a multitude of books that we have read and are currently sitting idle. BookShare facilitates an easy system of donation for used and read books. 
1. A user registers himself/herself/themself in the application. 
2. As a donor, the user can put up books for donation. The books are categorized as lists. For instance, if a user has Christmas books, the user will be able to add a Christmas book list, and then add books to that list. There is no limit to the number of lists of books that can be added.
3. When a user clicks a list, the user is presented with the books in that list. When the user clicks on a book, the user is presented with information regarding the book (i.e., name of the book, author, a picture of the book, description, genre, and average rating). The user does not need to add this information- this is derived from the internet.
4. As a receiver, the user is presented all of the lists up for donation (excluding the lists of the user). These lists can be filtered by the book name or genre.
5. When a user clicks a list in the receiver list, the user is presented with the books (and when they click the book, they are given the information about it as well). The user can multi-select the books they want to receive and click the e-mail icon. This will draft an email to the owner of the list in whichever e-mail application is on the device (up to user's discretion). The user can edit the email if needed and send it. 
6. The application will also check for network connectivity for the device- if it is not connected to the network, the application will let the user know upon startup.
7. The authentication features are as follows: sign-up for the application (one-time), sign in, and sign out.
8. All of the information regarding the users, the lists, and the books is stored in a cloud database (more on that below).

## Technologies used
1. Firebase for database purposes (storing users, storing lists, and storing books), user authentication. Check [Cloud Firestore](https://firebase.google.com/docs/firestore), which is the database used. Check [Firebase Authentication](https://firebase.google.com/docs/auth) for the authentication documentation.
2. Google Books API for deriving information about the books. Check the documentation [here](https://developers.google.com/books).
3. Picasso for getting the images of the books from the Google Books API's response. Check the documentation [here](https://square.github.io/picasso/).
4. Android Studio and related Android application programming concepts (drawer layouts, network connectivity, multithreading, and more). Click [here](https://developer.android.com/studio) for Android Studio and [here](https://developer.android.com) for Android application programming concepts and documentation.
5. Jira for keeping track of user stories.

## App on the Google Playstore
Currently working on this.

### User Stories

In the branches section and in the commits, you will see tags such as *BOOK-2* or *BOOK-3*. This refers to a specific user-story in the Jira board for this project. For reference,  those user-stories are included below. If you see a user story missing, it might have been a design story or such that did not have any coding involved, or a sub task for one of these user stories. 

1. *BOOK-2*: Create layouts for MainActivity, UserSignIn, UserSignUp
2. *BOOK-3*: Set up authentication using FireBase
3. *BOOK-5*: Adding a registeration form after signing up, adding a landing home page 
4. *BOOK-6*: Adding functionality that enables the user to add lists of donations
5. *BOOK-7*: Showing all the books the donor has for donation, adding a book for donation, deleting a book that is up for donation (i.e, all these books are list-specific)
6. *BOOK-9*: Adding the BookDetail functionality
7. *BOOK-10*: Adding drawer menus to UserHomePage, DonorHomePage, and ReceiverHomePage (i.e., the home pages)
8. *BOOK-15*: Functionality for updating the isbnList database everytime a book is added/deleted and a list is added/deleted
9. *BOOK-16*: ReceiverHomePage and basic ReceiverBookPage functionality added
10. *BOOK-17*: Adding filtration functionality- the user can filter the lists by the genre of book or the name of the book
11. *BOOK-18*: Adding request functionality: a user can select books and send an email to the donor for requesting the books
12. *BOOK-19*: Adding sign-out functionality
13. *BOOK-20*: Check if the device is connected to the network
14. *BOOK-21*: Add custom launcher icon for the application
15. *BOOK-22*: Adding documentation for the application