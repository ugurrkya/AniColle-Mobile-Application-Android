# AniColle Mobile Application
# Proposal
The project is about anime listing system. People who watch animes a lot at same time can forget easily how many episodes did they watch, or people can forget which anime they watched. The way to recognize the animes is take a note of them, but in this digital world, audiences want easy way. Moreover, choosing new animes to watch is a little bit troublesome, because nobody wants to watch bad quality animes, but there is no certain details about anime. Only thing that audience do is to read comments but audiences want to see the certain thing that understand easily which animes are good, which are bad. In this reason people want to score about the series that choose anime easily. However, problems didn’t end, some people who is maniac for anime can get difficult to choose the animes, because details of anime such producers, studios, vocal actor/actress, animators, designers are important criteria for them (like me). These audiences have to search a lot to get these information from different resources.  
I thougt that I can come up this problem with the solution with using the system called AniColle. To keep more than one information on one page is significant by categories, I can keep these important information on one page by using the system. Users can see the current anime, or aired animes all and click the anime names, or pic, it directs the page of the specific animes and see the scores of it which are given by the users, all information of animes. Users can add animes from the list and if they add it, they can list those which added by themselves in profile page, and they can update episodes how many they watched. 
There will be 2 entities in the system as users and an administrator. A membership system for users are used for the registration system. Users can create a profile, if they registered the system. Administrator has unique ID such as users but if admin signed by the ID, it leads to  admin page. Administrator can add new animes with its detail and, update anime information. The administrator page is directly connected to database. Users can add animes to their own list by choosing from “Anime” list. “Anime” list will be categorized by seasons, studios, genres… There are “add” buttons and it adds animes, if users watched it they can update the episodes by click the button. Moreover, they can score it. A profile page lists animes categorized by watching, completed, plan to watch, dropped. Users arrange the list by conditions of anime. Side bar will be designed and it keeps categories of anime condition and if they click, the list shows. Moreover, there will statistical data of time such how many time the user spends to watching animes. By the way, there will “search” bar and users can search animes easily. Additionally, there is a calendar which shows days of animes in the application.
The system will be created such a mobile application. To create it, Android programming language with Java will be used. I will use Android Studio for coding and designing. Cloud Firestore of Firebase will be used for database actions and management. 
# Development Details
- Android Studio(platform)
- Java(programming language)
- Firebase Authentication(to manage users)
- Firebase Realtime Database and Storage(to implement database transactions)
# Libraries
- Retrofit2
- Picasso
- Glide
- CircleImageView
- MaterialEditText
- ImageCropper
- General Firebase
# Functions
- Authentication
- Register/Login
- View all animes categorized by seasons
- View anime details
- Search animes and users
- Add animes to personalized lists
- Rate animes
- View others' profile and their lists
- Update episode of animes which are watched
- Update profile information
- Notification

# Screenshots
<div>
<img src= "https://user-images.githubusercontent.com/60930674/91186308-91769600-e6f7-11ea-9b45-d5e23abed842.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186312-93405980-e6f7-11ea-98c7-2832113b7b04.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186314-94718680-e6f7-11ea-88ce-86f430729028.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186317-95a2b380-e6f7-11ea-8a90-62487d2c580a.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186323-96d3e080-e6f7-11ea-86f2-2dd922fdd432.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186328-98050d80-e6f7-11ea-8132-1dfb20563deb.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186334-99ced100-e6f7-11ea-96f2-c23a77219e89.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186344-9afffe00-e6f7-11ea-937c-0212f47037fc.png" width="240" height="350">
<img src= "https://user-images.githubusercontent.com/60930674/91186351-9c312b00-e6f7-11ea-8466-276ccc67076c.png" width="240" height="350">
</div>

